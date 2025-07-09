package com.aseubel.weave.pojo.entity.user;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:51
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "badge")
public class Badge extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name; // "匠人", "端午节龙舟勋章"

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String iconUrl; // 勋章图标URL

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private BadgeType type; // 等级勋章/限定勋章

    public enum BadgeType {
        LEVEL,
        LIMITED
    }
}