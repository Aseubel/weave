package com.aseubel.weave.service.impl.post;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.aseubel.weave.common.disruptor.DisruptorProducer;
import com.aseubel.weave.common.disruptor.EventType;
import com.aseubel.weave.common.exception.BusinessException;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.post.PostRequest;
import com.aseubel.weave.pojo.dto.post.PostResponse;
import com.aseubel.weave.pojo.entity.Follow;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.post.PostLike;
import com.aseubel.weave.pojo.entity.user.InterestTag;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.redis.IRedisService;
import com.aseubel.weave.redis.KeyBuilder;
import com.aseubel.weave.repository.*;
import com.aseubel.weave.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子服务实现类
 *
 * @author Aseubel
 * @date 2025/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;
    private final InterestTagRepository interestTagRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final IRedisService redisService;
    private final DisruptorProducer disruptorProducer;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest request) {
        User currentUser = UserContext.getCurrentUser();

        // 构建帖子实体
        Post post = Post.builder()
                .author(currentUser)
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .status(request.getStatus())
                .images(convertListToJson(request.getImages()))
                .tags(convertTagsToString(request.getTags()))
                .isTop(false)
                .build();

        // 设置兴趣标签
        if (request.getInterestTagIds() != null && !request.getInterestTagIds().isEmpty()) {
            Set<InterestTag> interestTags = new HashSet<>(
                    interestTagRepository.findAllById(request.getInterestTagIds()));
            post.setInterestTags(interestTags);
        }

        post = postRepository.save(post);
        return convertToPostResponse(post, currentUser);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        User currentUser = UserContext.getCurrentUser();
        Post post = getPostEntity(postId);

        // 检查权限
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new BusinessException("无权限修改此帖子");
        }

        // 更新帖子信息
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setType(request.getType());
        post.setImages(convertListToJson(request.getImages()));
        post.setTags(convertTagsToString(request.getTags()));

        // 更新兴趣标签
        if (request.getInterestTagIds() != null) {
            Set<InterestTag> interestTags = new HashSet<>(
                    interestTagRepository.findAllById(request.getInterestTagIds()));
            post.setInterestTags(interestTags);
        }

        post = postRepository.save(post);
        return convertToPostResponse(post, currentUser);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        User currentUser = UserContext.getCurrentUser();
        Post post = getPostEntity(postId);

        // 检查权限
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new BusinessException("无权限删除此帖子");
        }

        post.setStatus(Post.PostStatus.DELETED);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public PostResponse getPostById(Long postId) {
        User currentUser = UserContext.getCurrentUser();
        Post post = getPostEntity(postId);

        // 增加浏览次数
        incrementViewCount(postId);

        return convertToPostResponse(post, currentUser);
    }

    @Override
    public PageResponse<PostResponse> getPosts(Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Page<Post> posts = postRepository.findByStatusOrderByCreatedAtDesc(Post.PostStatus.PUBLISHED, pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    public PageResponse<PostResponse> getPostsByType(Post.PostType type, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Page<Post> posts = postRepository.findByTypeAndStatusOrderByCreatedAtDesc(type, Post.PostStatus.PUBLISHED,
                pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    public PageResponse<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        Page<Post> posts = postRepository.findByAuthorAndStatusOrderByCreatedAtDesc(user, Post.PostStatus.PUBLISHED,
                pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    public PageResponse<PostResponse> getFollowingPosts(Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Page<Post> posts = postRepository.findFollowingPosts(currentUser, Post.PostStatus.PUBLISHED, pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    public PageResponse<PostResponse> getHotPosts(Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        LocalDateTime startTime = LocalDateTime.now().minusDays(7); // 最近7天的热门帖子
        Page<Post> posts = postRepository.findHotPostsByTimeRange(startTime, Post.PostStatus.PUBLISHED, pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    public PageResponse<PostResponse> searchPosts(String keyword, Pageable pageable) {
        User currentUser = UserContext.getCurrentUser();
        Page<Post> posts = postRepository.searchByContent(keyword, Post.PostStatus.PUBLISHED, pageable);
        return convertToPageResponse(posts, currentUser);
    }

    @Override
    @Transactional
    public void toggleLike(Long postId) {
        User currentUser = UserContext.getCurrentUser();
        String userId = String.valueOf(currentUser.getId());
        Boolean isLiked;
        Post post = getPostEntity(postId);

        redisService.addToSet(KeyBuilder.postLikeRecentKey(), userId);
        if ((isLiked = isLiked(postId, userId)) == null) {
            isLiked = postLikeRepository.findByUserAndPost(currentUser, post).isPresent();
            redisService.addToMap(KeyBuilder.postLikeStatusKey(postId), userId, isLiked);
        }
        if (isLiked) {
            // 取消点赞
            redisService.addToMap(KeyBuilder.postLikeStatusKey(postId), userId, false);
            redisService.incrMap(KeyBuilder.postLikeCountKey(), String.valueOf(postId), -1);

            PostLike postLike = PostLike.builder()
                    .user(currentUser)
                    .post(post)
                    .type(PostLike.LikeType.DISLIKE)
                    .build();
            disruptorProducer.publish(postLike, EventType.POST_UNLIKE);
        } else {
            // 点赞
            redisService.addToMap(KeyBuilder.postLikeStatusKey(postId), userId, true);
            redisService.incrMap(KeyBuilder.postLikeCountKey(), String.valueOf(postId), 1);

            PostLike postLike = PostLike.builder()
                    .user(currentUser)
                    .post(post)
                    .type(PostLike.LikeType.LIKE)
                    .build();
            disruptorProducer.publish(postLike, EventType.POST_LIKE);
        }
    }

    @Override
    @Transactional
    public void incrementViewCount(Long postId) {
        postRepository.incrementViewCount(postId);
    }

    @Override
    @Transactional
    public void toggleTop(Long postId) {
        Post post = getPostEntity(postId);
        post.setIsTop(!post.getIsTop());
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void toggleHot(Long postId) {
        Post post = getPostEntity(postId);
        post.setIsHot(!post.getIsHot());
        postRepository.save(post);
    }

    @Override
    public PageResponse<PostResponse> getTopPosts() {
        User currentUser = UserContext.getCurrentUser();
        List<Post> topPosts = postRepository.findByIsTopTrueAndStatusOrderByCreatedAtDesc(Post.PostStatus.PUBLISHED);
        List<PostResponse> responses = topPosts.stream()
                .map(post -> convertToPostResponse(post, currentUser))
                .collect(Collectors.toList());

        return PageResponse.<PostResponse>builder()
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

    private Boolean isLiked(Long postId, String userId) {
        return redisService.getFromMap(KeyBuilder.postLikeStatusKey(postId), userId);
    }

    private Post getPostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException("帖子不存在"));
    }

    private PostResponse convertToPostResponse(Post post, User currentUser) {
        // 检查当前用户是否点赞
        boolean isLiked = ObjectUtil.isNotEmpty(currentUser) && postLikeRepository.existsByUserAndPost(currentUser, post);

        // 检查是否关注作者
        boolean isFollowingAuthor = false;
        if (ObjectUtil.isNotEmpty(currentUser) && !post.getAuthor().getId().equals(currentUser.getId())) {
            isFollowingAuthor = followRepository.existsByFollowerAndFollowingAndStatus(
                    currentUser, post.getAuthor(), Follow.FollowStatus.ACTIVE);
        }

        return PostResponse.builder()
                .id(String.valueOf(post.getId()))
                .title(post.getTitle())
                .content(post.getContent())
                .type(post.getType())
                .status(post.getStatus())
                .images(convertJsonToList(post.getImages()))
                .tags(convertStringToTags(post.getTags()))
                .viewCount(post.getViewCount())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .shareCount(post.getShareCount())
                .isTop(post.getIsTop())
                .isHot(post.getIsHot())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .author(convertToAuthorInfo(post.getAuthor()))
                .interestTags(convertToInterestTagInfos(post.getInterestTags()))
                .isLiked(isLiked)
                .isFollowingAuthor(isFollowingAuthor)
                .build();
    }

    private PostResponse.AuthorInfo convertToAuthorInfo(User user) {
        return PostResponse.AuthorInfo.builder()
                .id(String.valueOf(user.getId()))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(Optional.ofNullable(user.getAvatar()).map(Image::getImageUrl).orElse(null))
                .level(user.getLevel())
                .isActive(user.getIsActive())
                .build();
    }

    private Set<PostResponse.InterestTagInfo> convertToInterestTagInfos(Set<InterestTag> interestTags) {
        if (interestTags == null) {
            return new HashSet<>();
        }
        return interestTags.stream()
                .map(tag -> PostResponse.InterestTagInfo.builder()
                        .id(String.valueOf(tag.getId()))
                        .name(tag.getName())
                        .color(tag.getColor())
                        .build())
                .collect(Collectors.toSet());
    }

    private PageResponse<PostResponse> convertToPageResponse(Page<Post> posts, User currentUser) {
        List<PostResponse> responses = posts.getContent().stream()
                .map(post -> convertToPostResponse(post, currentUser))
                .collect(Collectors.toList());

        return PageResponse.<PostResponse>builder()
                .content(responses)
                .total(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .size(posts.getSize())
                .page(posts.getNumber() + 1)
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .hasNext(posts.hasNext())
                .hasPrevious(posts.hasPrevious())
                .build();
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return JSONUtil.toJsonStr(list);
        } catch (Exception e) {
            log.error("转换列表为JSON失败", e);
            return null;
        }
    }

    private List<String> convertJsonToList(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return JSONUtil.toList(json, String.class);
        } catch (Exception e) {
            log.error("转换JSON为列表失败", e);
            return new ArrayList<>();
        }
    }

    private String convertTagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return String.join(",", tags);
    }

    private List<String> convertStringToTags(String tagsString) {
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(tagsString.split(","));
    }
}