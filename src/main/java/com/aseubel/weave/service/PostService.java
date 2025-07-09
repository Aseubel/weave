package com.aseubel.weave.service;

import com.aseubel.weave.pojo.dto.common.PageResponse;
import com.aseubel.weave.pojo.dto.post.PostRequest;
import com.aseubel.weave.pojo.dto.post.PostResponse;
import com.aseubel.weave.pojo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 帖子服务接口
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
public interface PostService {

    /**
     * 创建帖子
     */
    PostResponse createPost(PostRequest request);

    /**
     * 更新帖子
     */
    PostResponse updatePost(Long postId, PostRequest request);

    /**
     * 删除帖子
     */
    void deletePost(Long postId);

    /**
     * 获取帖子详情
     */
    PostResponse getPostById(Long postId);

    /**
     * 分页查询帖子列表
     */
    PageResponse<PostResponse> getPosts(Pageable pageable);

    /**
     * 根据类型查询帖子
     */
    PageResponse<PostResponse> getPostsByType(Post.PostType type, Pageable pageable);

    /**
     * 查询用户的帖子
     */
    PageResponse<PostResponse> getUserPosts(Long userId, Pageable pageable);

    /**
     * 查询关注用户的帖子
     */
    PageResponse<PostResponse> getFollowingPosts(Pageable pageable);

    /**
     * 查询热门帖子
     */
    PageResponse<PostResponse> getHotPosts(Pageable pageable);

    /**
     * 搜索帖子
     */
    PageResponse<PostResponse> searchPosts(String keyword, Pageable pageable);

    /**
     * 点赞/取消点赞帖子
     */
    void toggleLike(Long postId);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long postId);

    /**
     * 置顶/取消置顶帖子（管理员功能）
     */
    void toggleTop(Long postId);

    /**
     * 设置/取消热门帖子（管理员功能）
     */
    void toggleHot(Long postId);

    /**
     * 获取置顶帖子列表
     */
    PageResponse<PostResponse> getTopPosts();
}