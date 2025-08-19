package com.aseubel.weave.pojo.entity.post;

import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 帖子点赞实体
 * @author Aseubel
 * @date 2025/6/29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post_like", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class PostLike extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user; // 点赞用户

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Post post; // 被点赞的帖子

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