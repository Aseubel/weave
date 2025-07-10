package com.aseubel.weave.pojo.dto.ich;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类请求DTO - 支持层级分类
 * @author Aseubel
 * @date 2025/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;
    
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
    
    private Long parentId; // 父分类ID，null表示创建顶级分类
    
    @NotNull(message = "分类级别不能为空")
    @Min(value = 1, message = "分类级别最小为1")
    @Max(value = 5, message = "分类级别最大为5")
    private Integer level; // 分类级别
    
    @Size(max = 20, message = "分类编码长度不能超过20个字符")
    private String code; // 分类编码
    
    @Min(value = 0, message = "排序值不能小于0")
    private Integer sortOrder; // 排序字段
}