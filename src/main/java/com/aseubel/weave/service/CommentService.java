package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.comment.CommentRequest;
import com.aseubel.weave.pojo.dto.comment.CommentResponse;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * 评论服务接口
 * @author Aseubel
 * @date 2025/6/29
 */
public interface CommentService {

    /**
     * 创建评论
     */
    CommentResponse createComment(CommentRequest request);

    /**
     * 删除评论
     */
    void deleteComment(Long commentId);

    /**
     * 获取帖子的评论列表
     */
    PageResponse<CommentResponse> getPostComments(Long postId, Pageable pageable);

    /**
     * 获取资源的评论列表
     */
    PageResponse<CommentResponse> getResourceComments(Long resourceId, Pageable pageable);

    /**
     * 获取评论的回复列表
     */
    PageResponse<CommentResponse> getCommentReplies(Long commentId, Pageable pageable);

    /**
     * 获取用户的评论列表
     */
    PageResponse<CommentResponse> getUserComments(Long userId, Pageable pageable);

    /**
     * 点赞/取消点赞评论
     */
    void toggleCommentLike(Long commentId);

    /**
     * 获取热门评论
     */
    PageResponse<CommentResponse> getHotComments(Long postId, Pageable pageable);

    /**
     * 获取最新评论
     */
    PageResponse<CommentResponse> getLatestComments(Long postId, Pageable pageable);
}