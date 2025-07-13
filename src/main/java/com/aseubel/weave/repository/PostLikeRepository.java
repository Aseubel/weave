package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.post.PostLike;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 帖子点赞Repository
 * @author Aseubel
 * @date 2025/6/29
 */
@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    /**
     * 查询用户对帖子的点赞记录
     */
    Optional<PostLike> findByUserAndPost(User user, Post post);

    /**
     * 检查用户是否点赞了帖子
     */
    boolean existsByUserAndPost(User user, Post post);

    /**
     * 检查用户是否点赞了帖子（指定类型）
     */
    boolean existsByUserAndPostAndType(User user, Post post, PostLike.LikeType type);

    /**
     * 统计帖子的点赞数
     */
    long countByPostAndType(Post post, PostLike.LikeType type);

    /**
     * 统计用户的点赞数
     */
    long countByUserAndType(User user, PostLike.LikeType type);

    /**
     * 删除用户对帖子的点赞
     */
    void deleteByUserAndPost(User user, Post post);
}