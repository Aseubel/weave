package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.comment.Comment;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.pojo.entity.ich.IchResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论Repository
 * @author Aseubel
 * @date 2025/6/29
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 查询帖子的评论（不包括回复）
     */
    Page<Comment> findByPostAndParentIsNullAndStatusOrderByCreatedAtDesc(
            Post post, Comment.CommentStatus status, Pageable pageable);

    /**
     * 查询资源的评论（不包括回复）
     */
    Page<Comment> findByResourceAndParentIsNullAndStatusOrderByCreatedAtDesc(
            IchResource resource, Comment.CommentStatus status, Pageable pageable);

    /**
     * 查询评论的回复
     */
    List<Comment> findByParentAndStatusOrderByCreatedAtAsc(
            Comment parent, Comment.CommentStatus status);

    /**
     * 查询用户的评论
     */
    Page<Comment> findByUserAndStatusOrderByCreatedAtDesc(
            User user, Comment.CommentStatus status, Pageable pageable);

    /**
     * 统计帖子的评论数
     */
    long countByPostAndStatus(Post post, Comment.CommentStatus status);

    /**
     * 统计资源的评论数
     */
    long countByResourceAndStatus(IchResource resource, Comment.CommentStatus status);

    /**
     * 统计评论的回复数
     */
    long countByParentAndStatus(Comment parent, Comment.CommentStatus status);

    /**
     * 统计用户的评论数
     */
    long countByUserAndStatus(User user, Comment.CommentStatus status);

    /**
     * 增加评论点赞数
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + :increment WHERE c.id = :commentId")
    void updateLikeCount(@Param("commentId") Long commentId, @Param("increment") long increment);

    /**
     * 查询热门评论（按点赞数排序）
     */
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL AND c.status = :status ORDER BY c.likeCount DESC, c.createdAt DESC")
    Page<Comment> findHotCommentsByPost(@Param("post") Post post, @Param("status") Comment.CommentStatus status, Pageable pageable);

    /**
     * 查询最新评论
     */
    @Query("SELECT c FROM Comment c WHERE c.post = :post AND c.parent IS NULL AND c.status = :status ORDER BY c.createdAt DESC")
    Page<Comment> findLatestCommentsByPost(@Param("post") Post post, @Param("status") Comment.CommentStatus status, Pageable pageable);

    /**
     * 删除帖子的所有评论
     */
    @Modifying
    @Query("UPDATE Comment c SET c.status = :status WHERE c.post = :post")
    void updateStatusByPost(@Param("post") Post post, @Param("status") Comment.CommentStatus status);
}