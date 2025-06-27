package com.aseubel.weave.pojo.refer;

import com.aseubel.weave.pojo.entity.Badge;
import com.aseubel.weave.pojo.entity.BaseEntity;
import com.aseubel.weave.pojo.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:52
 */
@Getter
@Setter
@Entity
@Table(name = "user_badge")
public class UserBadge extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;
}
