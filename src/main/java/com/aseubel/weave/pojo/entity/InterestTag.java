package com.aseubel.weave.pojo.entity;

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
@Table(name = "interest_tag")
public class InterestTag extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name; // e.g., "手工艺爱好者", "戏曲学习者"
}
