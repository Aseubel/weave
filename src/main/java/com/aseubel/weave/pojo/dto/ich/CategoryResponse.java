package com.aseubel.weave.pojo.dto.ich;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类响应DTO
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
    
    private Long resourceCount; // 该分类下的资源数量
}