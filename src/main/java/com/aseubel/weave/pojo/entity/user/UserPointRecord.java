package com.aseubel.weave.pojo.entity.user;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:53
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_points_record")
public class UserPointRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(nullable = false)
    private Integer points; // 本次变动的积分（可正可负）

    @Column(nullable = false, length = 200)
    private String description; // 积分来源，如 "每日签到", "兑换商品"
}
