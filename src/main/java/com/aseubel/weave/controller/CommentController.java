package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.pojo.dto.comment.CommentRequest;
import com.aseubel.weave.pojo.dto.comment.CommentResponse;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 创建评论
     */
    @PostMapping
    @RequireLogin
    public ApiResponse<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(request);
        return ApiResponse.success(response);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    @RequireLogin
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.success();
    }

    /**
     * 获取帖子的评论列表
     */
    @GetMapping("/post/{postId}")
    public ApiResponse<PageResponse<CommentResponse>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getPostComments(postId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取资源的评论列表
     */
    @GetMapping("/resource/{resourceId}")
    public ApiResponse<PageResponse<CommentResponse>> getResourceComments(
            @PathVariable Long resourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getResourceComments(resourceId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取用户的评论列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResponse<CommentResponse>> getUserComments(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getUserComments(userId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取评论的回复列表
     */
    @GetMapping("/{commentId}/replies")
    public ApiResponse<PageResponse<CommentResponse>> getCommentReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getCommentReplies(commentId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 点赞/取消点赞评论
     */
    @PostMapping("/{commentId}/like")
    @RequireLogin
    public ApiResponse<Void> toggleLike(@PathVariable Long commentId) {
        commentService.toggleCommentLike(commentId);
        return ApiResponse.success();
    }

    /**
     * 获取热门评论
     */
    @GetMapping("/hot")
    public ApiResponse<PageResponse<CommentResponse>> getHotComments(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getHotComments(postId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取最新评论
     */
    @GetMapping("/latest")
    public ApiResponse<PageResponse<CommentResponse>> getLatestComments(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<CommentResponse> response = commentService.getLatestComments(postId, pageable);
        return ApiResponse.success(response);
    }
}