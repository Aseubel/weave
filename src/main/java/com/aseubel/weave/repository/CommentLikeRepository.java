package com.aseubel.weave.repository;

import com.aseubel.weave.pojo.entity.comment.Comment;
import com.aseubel.weave.pojo.entity.comment.CommentLike;
import com.aseubel.weave.pojo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 帖子点赞Repository
 * @author Aseubel
 * @date 2025/7/13
 */
@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    /**
     * 查询用户对帖子的点赞记录
     */
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    /**
     * 检查用户是否点赞了帖子
     */
    boolean existsByUserAndComment(User user, Comment comment);

    /**
     * 检查用户是否点赞了帖子（指定类型）
     */
    boolean existsByUserAndCommentAndType(User user, Comment comment, CommentLike.LikeType type);

    /**
     * 统计帖子的点赞数
     */
    long countByCommentAndType(Comment comment, CommentLike.LikeType type);

    /**
     * 统计用户的点赞数
     */
    long countByUserAndType(User user, CommentLike.LikeType type);

    /**
     * 删除用户对帖子的点赞
     */
    void deleteByUserAndComment(User user, Comment comment);
}