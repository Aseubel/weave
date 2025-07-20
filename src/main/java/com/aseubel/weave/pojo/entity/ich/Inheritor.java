package com.aseubel.weave.pojo.entity.ich;

import com.aseubel.weave.common.annotation.FieldDesc;
import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/20 下午3:32
 * @description 传承者
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inheritor")
public class Inheritor extends BaseEntity {

    @Column(nullable = false, length = 100)
    @FieldDesc(name = "名字")
    private String name;

    @Column(length = 5000)
    @FieldDesc(name = "描述")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "inheritor_category",
            joinColumns = @JoinColumn(name = "inheritor_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
