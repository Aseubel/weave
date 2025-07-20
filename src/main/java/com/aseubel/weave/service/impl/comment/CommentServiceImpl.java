package com.aseubel.weave.service.impl.comment;

import cn.hutool.core.util.BooleanUtil;
import com.aseubel.weave.common.disruptor.DisruptorProducer;
import com.aseubel.weave.common.disruptor.EventType;
import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.comment.CommentRequest;
import com.aseubel.weave.pojo.dto.comment.CommentResponse;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.entity.comment.Comment;
import com.aseubel.weave.pojo.entity.comment.CommentLike;
import com.aseubel.weave.pojo.entity.ich.IchResource;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.post.PostLike;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.*;
import com.aseubel.weave.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 * @author Aseubel
 * @date 2025/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final IchResourceRepository ichResourceRepository;
    private final StringRedisTemplate redisTemplate;
    private final CommentLikeRepository commentLikeRepository;
    private final DisruptorProducer disruptorProducer;

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        User currentUser = UserContext.getCurrentUser();
        
        Comment.CommentBuilder commentBuilder = Comment.builder()
                .user(currentUser)
                .content(request.getContent());

        // 设置评论目标（帖子或资源）
        if (request.getPostId() != null) {
            Post post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new BusinessException("帖子不存在"));
            commentBuilder.post(post);
            
            // 增加帖子评论数
            postRepository.updateCommentCount(request.getPostId(), 1L);
        }

        // 设置父评论（如果是回复）
        if (request.getParentId() != null) {
            Comment parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException("父评论不存在"));
            commentBuilder.parent(parent);
        }

        Comment comment = commentRepository.save(commentBuilder.build());
        return convertToCommentResponse(comment, currentUser);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        User currentUser = UserContext.getCurrentUser();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException("评论不存在"));
        
        // 检查权限
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("无权限删除此评论");
        }

        comment.setStatus(Comment.CommentStatus.DELETED);
        commentRepository.save(comment);
        
        // 减少帖子评论数
        if (comment.getPost() != null) {
            postRepository.updateCommentCount(comment.getPost().getId(), -1L);
        }
    }

    @Override
    public PageResponse<CommentResponse> getPostComments(Long postId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
        
        Page<Comment> comments = commentRepository.findByPostAndParentIsNullAndStatusOrderByCreatedAtDesc(
                post, Comment.CommentStatus.PUBLISHED, pageable);
        
        return convertToPageResponse(comments, currentUser);
    }

    @Override
    public PageResponse<CommentResponse> getResourceComments(Long resourceId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        IchResource resource = ichResourceRepository.findById(resourceId)
                .orElseThrow(() -> new BusinessException("资源不存在"));

        Page<Comment> comments = commentRepository.findByResourceAndParentIsNullAndStatusOrderByCreatedAtDesc(
                resource, Comment.CommentStatus.PUBLISHED, pageable);

        return convertToPageResponse(comments, currentUser);
    }

    @Override
    public PageResponse<CommentResponse> getCommentReplies(Long commentId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException("评论不存在"));
        
        List<Comment> replies = commentRepository.findByParentAndStatusOrderByCreatedAtAsc(
                parent, Comment.CommentStatus.PUBLISHED);
        
        List<CommentResponse> responses = replies.stream()
                .map(reply -> convertToCommentResponse(reply, currentUser))
                .collect(Collectors.toList());
        
        return PageResponse.<CommentResponse>builder()
                .content(responses)
                .total((long) responses.size())
                .totalPages(1)
                .size(responses.size())
                .page(1)
                .isFirst(true)
                .isLast(true)
                .hasNext(false)
                .hasPrevious(false)
                .build();
    }

    @Override
    public PageResponse<CommentResponse> getUserComments(Long userId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        
        Page<Comment> comments = commentRepository.findByUserAndStatusOrderByCreatedAtDesc(
                user, Comment.CommentStatus.PUBLISHED, pageable);
        
        return convertToPageResponse(comments, currentUser);
    }

    @Override
    @Transactional
    public void toggleCommentLike(Long commentId) {
        User currentUser = UserContext.getCurrentUser();
        Comment comment = getCommentEntity(commentId);

        Long userId = currentUser.getId();
        Boolean isLiked;

        redisTemplate.opsForSet().add(KeyBuilder.postLikeRecentKey(), String.valueOf(userId));
        if ((isLiked = isLiked(commentId, userId)) == null) {
            isLiked = commentLikeRepository.findByUserAndComment(currentUser, comment).isPresent();
            redisTemplate.opsForHash().put(KeyBuilder.postLikeStatusKey(commentId), userId, isLiked);
        }
        if (isLiked) {
            // 取消点赞
            redisTemplate.opsForHash().put(KeyBuilder.commentLikeStatusKey(commentId), userId, false);
            redisTemplate.opsForHash().increment(KeyBuilder.commentLikeCountKey(), commentId, -1);

            CommentLike commentLike = CommentLike.builder()
                    .user(currentUser)
                    .comment(comment)
                    .type(CommentLike.LikeType.DISLIKE)
                    .build();
            disruptorProducer.publish(commentLike, EventType.COMMENT_UNLIKE);
        } else {
            // 点赞
            redisTemplate.opsForHash().put(KeyBuilder.commentLikeStatusKey(commentId), userId, true);
            redisTemplate.opsForHash().increment(KeyBuilder.commentLikeCountKey(), commentId, 1);

            CommentLike commentLike = CommentLike.builder()
                    .user(currentUser)
                    .comment(comment)
                    .type(CommentLike.LikeType.DISLIKE)
                    .build();
            disruptorProducer.publish(commentLike, EventType.COMMENT_UNLIKE);
        }
    }

    @Override
    public PageResponse<CommentResponse> getHotComments(Long postId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
        
        Page<Comment> comments = commentRepository.findHotCommentsByPost(
                post, Comment.CommentStatus.PUBLISHED, pageable);
        
        return convertToPageResponse(comments, currentUser);
    }

    @Override
    public PageResponse<CommentResponse> getLatestComments(Long postId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
        
        Page<Comment> comments = commentRepository.findLatestCommentsByPost(
                post, Comment.CommentStatus.PUBLISHED, pageable);
        
        return convertToPageResponse(comments, currentUser);
    }

    private Comment getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException("评论不存在"));
    }

    private Boolean isLiked(Long commentId, Long userId) {
        return redisTemplate.opsForSet().isMember(KeyBuilder.commentLikeStatusKey(commentId), userId);
    }

    private CommentResponse convertToCommentResponse(Comment comment, User currentUser) {
        // 当前用户是否点赞
        Boolean isLiked = isLiked(comment.getId(), currentUser.getId());

        // 获取回复列表
        List<Comment> replies = commentRepository.findByParentAndStatusOrderByCreatedAtAsc(
                comment, Comment.CommentStatus.PUBLISHED);
        
        List<CommentResponse> replyResponses = replies.stream()
                .map(reply -> convertToCommentResponse(reply, currentUser))
                .collect(Collectors.toList());

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likeCount(comment.getLikeCount())
                .status(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .user(convertToUserInfo(comment.getUser()))
                .parent(comment.getParent() != null ? convertToParentCommentInfo(comment.getParent()) : null)
                .replies(replyResponses)
                .replyCount(replies.size())
                .isLiked(BooleanUtil.isTrue(isLiked))
                .build();
    }

    private CommentResponse.UserInfo convertToUserInfo(User user) {
        return CommentResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .level(user.getLevel())
                .build();
    }

    private CommentResponse.ParentCommentInfo convertToParentCommentInfo(Comment parent) {
        return CommentResponse.ParentCommentInfo.builder()
                .id(parent.getId())
                .content(parent.getContent())
                .user(convertToUserInfo(parent.getUser()))
                .build();
    }

    private PageResponse<CommentResponse> convertToPageResponse(Page<Comment> comments, User currentUser) {
        List<CommentResponse> responses = comments.getContent().stream()
                .map(comment -> convertToCommentResponse(comment, currentUser))
                .collect(Collectors.toList());

        return PageResponse.<CommentResponse>builder()
                .content(responses)
                .total(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .size(comments.getSize())
                .page(comments.getNumber() + 1)
                .isFirst(comments.isFirst())
                .isLast(comments.isLast())
                .hasNext(comments.hasNext())
                .hasPrevious(comments.hasPrevious())
                .build();
    }
}