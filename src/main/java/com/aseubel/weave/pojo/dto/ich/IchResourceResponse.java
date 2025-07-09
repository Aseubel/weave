package com.aseubel.weave.pojo.dto.ich;

import com.aseubel.weave.pojo.entity.ich.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 非遗资源响应DTO
 * @author Aseubel
 * @date 2025/7/8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IchResourceResponse {

    private Long id;
    
    private String title;
    
    private String description;
    
    private ResourceType type;
    
    private String contentUrl;
    
    private String copyrightInfo;
    
    private CategoryResponse category;
    
    private UploaderInfo uploader;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploaderInfo {
        private Long id;
        private String username;
        private String nickname;
        private String avatar;
    }
}