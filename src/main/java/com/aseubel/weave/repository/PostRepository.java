package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.Post;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 帖子Repository
 * @author Aseubel
 * @date 2025/6/29
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 根据状态分页查询帖子
     */
    Page<Post> findByStatusOrderByCreatedAtDesc(Post.PostStatus status, Pageable pageable);

    /**
     * 根据作者查询帖子
     */
    Page<Post> findByAuthorAndStatusOrderByCreatedAtDesc(User author, Post.PostStatus status, Pageable pageable);

    /**
     * 根据帖子类型查询
     */
    Page<Post> findByTypeAndStatusOrderByCreatedAtDesc(Post.PostType type, Post.PostStatus status, Pageable pageable);

    /**
     * 查询热门帖子
     */
    Page<Post> findByIsHotTrueAndStatusOrderByCreatedAtDesc(Post.PostStatus status, Pageable pageable);

    /**
     * 查询置顶帖子
     */
    List<Post> findByIsTopTrueAndStatusOrderByCreatedAtDesc(Post.PostStatus status);

    /**
     * 根据标题搜索帖子
     */
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% AND p.status = :status ORDER BY p.createdAt DESC")
    Page<Post> searchByTitle(@Param("keyword") String keyword, @Param("status") Post.PostStatus status, Pageable pageable);

    /**
     * 根据内容搜索帖子
     */
    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) AND p.status = :status ORDER BY p.createdAt DESC")
    Page<Post> searchByContent(@Param("keyword") String keyword, @Param("status") Post.PostStatus status, Pageable pageable);

    /**
     * 增加浏览次数
     */
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    /**
     * 增加点赞数
     */
    @Modifying
    @Query("UPDATE Post p SET p.likeCount = p.likeCount + :increment WHERE p.id = :postId")
    void updateLikeCount(@Param("postId") Long postId, @Param("increment") int increment);

    /**
     * 增加评论数
     */
    @Modifying
    @Query("UPDATE Post p SET p.commentCount = p.commentCount + :increment WHERE p.id = :postId")
    void updateCommentCount(@Param("postId") Long postId, @Param("increment") int increment);

    /**
     * 查询用户关注的人的帖子
     */
    @Query("SELECT p FROM Post p JOIN Follow f ON p.author = f.following WHERE f.follower = :user AND p.status = :status ORDER BY p.createdAt DESC")
    Page<Post> findFollowingPosts(@Param("user") User user, @Param("status") Post.PostStatus status, Pageable pageable);

    /**
     * 根据时间范围查询热门帖子
     */
    @Query("SELECT p FROM Post p WHERE p.createdAt >= :startTime AND p.status = :status ORDER BY (p.likeCount * 2 + p.commentCount * 3 + p.viewCount * 0.1) DESC")
    Page<Post> findHotPostsByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("status") Post.PostStatus status, Pageable pageable);

    /**
     * 统计用户发帖数量
     */
    long countByAuthorAndStatus(User author, Post.PostStatus status);
}