package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.user.UserResponse;

import org.springframework.data.domain.Pageable;

/**
 * 关注服务接口
 * @author Aseubel
 * @date 2025/6/29
 */
public interface FollowService {

    /**
     * 关注用户
     */
    void followUser(Long userId);

    /**
     * 取消关注用户
     */
    void unfollowUser(Long userId);

    /**
     * 检查是否关注某用户
     */
    boolean isFollowing(Long userId);

    /**
     * 获取关注列表
     */
    PageResponse<UserResponse> getFollowing(Pageable pageable);

    /**
     * 获取指定用户的关注列表
     */
    PageResponse<UserResponse> getUserFollowing(Long userId, Pageable pageable);

    /**
     * 获取粉丝列表
     */
    PageResponse<UserResponse> getFollowers(Pageable pageable);

    /**
     * 获取指定用户的粉丝列表
     */
    PageResponse<UserResponse> getUserFollowers(Long userId, Pageable pageable);

    /**
     * 获取互相关注的用户列表
     */
    PageResponse<UserResponse> getMutualFollows(Pageable pageable);

    /**
     * 获取关注数量
     */
    long getFollowingCount();

    /**
     * 获取指定用户的关注数量
     */
    long getUserFollowingCount(Long userId);

    /**
     * 获取粉丝数量
     */
    long getFollowersCount();

    /**
     * 获取指定用户的粉丝数量
     */
    long getUserFollowersCount(Long userId);



    /**
     * 屏蔽用户
     */
    void blockUser(Long userId);

    /**
     * 取消屏蔽用户
     */
    void unblockUser(Long userId);
}