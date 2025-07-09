package com.aseubel.weave.pojo.dto.ich;

import com.aseubel.weave.pojo.entity.ich.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 非遗资源请求DTO
 * @author Aseubel
 * @date 2025/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IchResourceRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;
    
    @NotBlank(message = "描述不能为空")
    private String description;
    
    @NotNull(message = "资源类型不能为空")
    private ResourceType type;
    
    @NotBlank(message = "资源链接不能为空")
    private String contentUrl;
    
    @Size(max = 100, message = "版权信息长度不能超过100个字符")
    private String copyrightInfo;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
}