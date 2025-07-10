package com.aseubel.weave.pojo.entity.ich;

import com.aseubel.weave.pojo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * 非遗分类实体类 - 支持层级分类
 * 根据非遗十大类分类体系设计：
 * 1. 民间文学
 * 2. 传统音乐
 * 3. 传统舞蹈
 * 4. 传统戏剧
 * 5. 曲艺
 * 6. 传统体育、游艺与杂技
 * 7. 传统美术
 * 8. 传统技艺
 * 9. 传统医药
 * 10. 民俗
 * 
 * @author Aseubel
 * @date 2025/6/27 下午7:29
 */
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name; // 分类名称

    @Column(length = 500)
    private String description; // 分类描述

    @Column(name = "parent_id")
    private Long parentId; // 父分类ID，null表示顶级分类

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parent; // 父分类

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children; // 子分类列表

    @Column(nullable = false)
    private Integer level; // 分类级别：1-顶级分类(十大类)，2-二级分类，3-三级分类等

    @Column(length = 20)
    private String code; // 分类编码，便于管理和查询

    @Column(name = "sort_order")
    private Integer sortOrder; // 排序字段

    /**
     * 判断是否为顶级分类（十大类）
     */
    public boolean isTopLevel() {
        return level != null && level == 1;
    }

    /**
     * 判断是否为叶子节点（没有子分类）
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    /**
     * 获取完整路径名称（父分类 > 子分类）
     */
    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }
}
