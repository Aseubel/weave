package com.aseubel.weave.pojo.dto.ich;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类响应DTO - 支持层级分类
 * @author Aseubel
 * @date 2025/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private Long parentId; // 父分类ID
    
    private String parentName; // 父分类名称
    
    private Integer level; // 分类级别
    
    private String code; // 分类编码
    
    private Integer sortOrder; // 排序字段
    
    private Boolean isActive; // 是否启用
    
    private String fullPath; // 完整路径名称
    
    private Boolean isTopLevel; // 是否为顶级分类
    
    private Boolean isLeaf; // 是否为叶子节点
    
    private List<CategoryResponse> children; // 子分类列表（用于树形结构）
    
    private Long resourceCount; // 该分类下的资源数量
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}