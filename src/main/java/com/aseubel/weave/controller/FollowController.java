package com.aseubel.weave.controller;

import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.user.UserResponse;
import com.aseubel.weave.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 关注控制器
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 关注用户
     */
    @PostMapping("/{userId}")
    @RequireLogin
    public ApiResponse<Void> followUser(@PathVariable Long userId) {
        followService.followUser(userId);
        return ApiResponse.success();
    }

    /**
     * 取消关注用户
     */
    @DeleteMapping("/{userId}")
    @RequireLogin
    public ApiResponse<Void> unfollowUser(@PathVariable Long userId) {
        followService.unfollowUser(userId);
        return ApiResponse.success();
    }

    /**
     * 检查是否关注某用户
     */
    @GetMapping("/{userId}/status")
    @RequireLogin
    public ApiResponse<Boolean> isFollowing(@PathVariable Long userId) {
        boolean isFollowing = followService.isFollowing(userId);
        return ApiResponse.success(isFollowing);
    }

    /**
     * 获取关注列表
     */
    @GetMapping("/following")
    @RequireLogin
    public ApiResponse<PageResponse<UserResponse>> getFollowing(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = followService.getFollowing(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取指定用户的关注列表
     */
    @GetMapping("/user/{userId}/following")
    @RequireLogin
    public ApiResponse<PageResponse<UserResponse>> getUserFollowing(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = followService.getUserFollowing(userId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取粉丝列表
     */
    @GetMapping("/followers")
    @RequireLogin
    public ApiResponse<PageResponse<UserResponse>> getFollowers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = followService.getFollowers(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取指定用户的粉丝列表
     */
    @GetMapping("/user/{userId}/followers")
    @RequireLogin
    public ApiResponse<PageResponse<UserResponse>> getUserFollowers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = followService.getUserFollowers(userId, pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取互相关注列表
     */
    @GetMapping("/mutual")
    @RequireLogin
    public ApiResponse<PageResponse<UserResponse>> getMutualFollows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserResponse> response = followService.getMutualFollows(pageable);
        return ApiResponse.success(response);
    }

    /**
     * 获取关注数量
     */
    @GetMapping("/following/count")
    @RequireLogin
    public ApiResponse<Long> getFollowingCount() {
        long count = followService.getFollowingCount();
        return ApiResponse.success(count);
    }

    /**
     * 获取指定用户的关注数量
     */
    @GetMapping("/user/{userId}/following/count")
    @RequireLogin
    public ApiResponse<Long> getUserFollowingCount(@PathVariable Long userId) {
        long count = followService.getUserFollowingCount(userId);
        return ApiResponse.success(count);
    }

    /**
     * 获取粉丝数量
     */
    @GetMapping("/followers/count")
    @RequireLogin
    public ApiResponse<Long> getFollowersCount() {
        long count = followService.getFollowersCount();
        return ApiResponse.success(count);
    }

    /**
     * 获取指定用户的粉丝数量
     */
    @GetMapping("/user/{userId}/followers/count")
    @RequireLogin
    public ApiResponse<Long> getUserFollowersCount(@PathVariable Long userId) {
        long count = followService.getUserFollowersCount(userId);
        return ApiResponse.success(count);
    }

    /**
     * 拉黑用户
     */
    @PostMapping("/{userId}/block")
    @RequireLogin
    public ApiResponse<Void> blockUser(@PathVariable Long userId) {
        followService.blockUser(userId);
        return ApiResponse.success();
    }

    /**
     * 取消拉黑用户
     */
    @DeleteMapping("/{userId}/block")
    @RequireLogin
    public ApiResponse<Void> unblockUser(@PathVariable Long userId) {
        followService.unblockUser(userId);
        return ApiResponse.success();
    }
}