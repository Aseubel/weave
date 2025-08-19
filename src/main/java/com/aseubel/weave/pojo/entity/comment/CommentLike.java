package com.aseubel.weave.pojo.entity.comment;

import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.post.Post;
import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 评论点赞实体
 * @author Aseubel
 * @date 2025/7/13
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_like",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "comment_id"}))
public class CommentLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user; // 点赞用户

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment comment; // 被点赞的评论

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private LikeType type = LikeType.LIKE; // 点赞类型

    // 点赞类型枚举
    public enum LikeType {
        LIKE,    // 点赞
        DISLIKE  // 踩
    }
}