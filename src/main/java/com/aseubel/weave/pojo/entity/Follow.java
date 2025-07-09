package com.aseubel.weave.pojo.entity;

import com.aseubel.weave.pojo.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 用户关注实体
 * @author Aseubel
 * @date 2025/6/29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 关注者

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following; // 被关注者

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FollowStatus status = FollowStatus.ACTIVE; // 关注状态

    // 关注状态枚举
    public enum FollowStatus {
        ACTIVE,   // 正常关注
        BLOCKED   // 已屏蔽
    }
}