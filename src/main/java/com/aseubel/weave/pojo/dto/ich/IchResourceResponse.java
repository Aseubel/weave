package com.aseubel.weave.pojo.dto.ich;

import com.aseubel.weave.common.annotation.FieldDesc;
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

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "描述")
    private String description;

    @FieldDesc(name = "类型")
    private ResourceType type;

    @FieldDesc(name = "内容URL")
    private String contentUrl;

    @FieldDesc(name = "版权信息")
    private String copyrightInfo;

    @FieldDesc(name = "分类")
    private CategoryResponse category;

    @FieldDesc(name = "上传者")
    private UploaderInfo uploader;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createdAt;

    @FieldDesc(name = "更新时间")
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