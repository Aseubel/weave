package com.aseubel.weave.pojo.dto.ich;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类请求DTO
 * @author Aseubel
 * @date 2025/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50个字符")
    private String name;
    
    @Size(max = 200, message = "分类描述长度不能超过200个字符")
    private String description;
}