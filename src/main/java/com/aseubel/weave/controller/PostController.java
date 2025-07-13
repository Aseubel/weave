package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.post.PostRequest;
import com.aseubel.weave.pojo.dto.post.PostResponse;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子控制器
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 创建帖子
     */
    @PostMapping
    @RequireLogin
    public ApiResponse<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        PostResponse response = postService.createPost(request);
        return ApiResponse.success(response);
    }

    /**
     * 更新帖子
     */
    @PutMapping("/{postId}")
    @RequireLogin
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId,
            @Valid @RequestBody PostRequest request) {
        PostResponse response = postService.updatePost(postId, request);
        return ApiResponse.success(response);
    }

    /**
     * 删除帖子
     */
    @DeleteMapping("/{postId}")
    @RequireLogin
    public ApiResponse<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.success();
    }

    /**
     * 获取帖子详情
     */
    @GetMapping("/{postId}")
    @RequireLogin
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPostById(postId);
        return ApiResponse.success(response);
    }

    /**
     * 分页查询帖子列表
     */
    @GetMapping
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.getPosts(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 根据类型查询帖子
     */
    @GetMapping("/type/{type}")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getPostsByType(
            @PathVariable Post.PostType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.getPostsByType(type, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 查询用户的帖子
     */
    @GetMapping("/user/{userId}")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.getUserPosts(userId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 查询关注用户的帖子
     */
    @GetMapping("/following")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getFollowingPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.getFollowingPosts(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 查询热门帖子
     */
    @GetMapping("/hot")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getHotPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.getHotPosts(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 搜索帖子
     */
    @GetMapping("/search")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<PostResponse> response = postService.searchPosts(keyword, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 点赞/取消点赞帖子
     */
    @PostMapping("/{postId}/like")
    @RequireLogin
    public ApiResponse<Void> toggleLike(@PathVariable Long postId) {
        postService.toggleLike(postId);
        return ApiResponse.success();
    }

    /**
     * 获取置顶帖子列表
     */
    @GetMapping("/top")
    @RequireLogin
    public ApiResponse<PageResponse<PostResponse>> getTopPosts() {
        PageResponse<PostResponse> response = postService.getTopPosts();
        return ApiResponse.success(response);
    }

    /**
     * 置顶/取消置顶帖子（管理员功能）
     */
    @PostMapping("/{postId}/top")
    @RequireLogin
    // @RequirePermission("ADMIN") // 需要管理员权限
    public ApiResponse<Void> toggleTop(@PathVariable Long postId) {
        postService.toggleTop(postId);
        return ApiResponse.success();
    }

    /**
     * 设置/取消热门帖子（管理员功能）
     */
    @PostMapping("/{postId}/hot")
    @RequireLogin
    // @RequirePermission("ADMIN") // 需要管理员权限
    public ApiResponse<Void> toggleHot(@PathVariable Long postId) {
        postService.toggleHot(postId);
        return ApiResponse.success();
    }
}