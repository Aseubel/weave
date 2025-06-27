package com.aseubel.weave.pojo.entity.ich;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:29
 */
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name; // 分类名称，如 "传统戏剧"

    @Column
    private String description;
}
