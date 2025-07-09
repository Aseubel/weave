package com.aseubel.weave.pojo.entity.user;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aseubel
 * @date 2025/6/27 下午6:38
 */
@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name; // e.g., ROLE_USER, ROLE_ADMIN
}
