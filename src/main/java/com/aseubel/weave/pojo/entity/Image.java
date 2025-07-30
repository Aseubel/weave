package com.aseubel.weave.pojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.aseubel.weave.common.Constant.*;

/**
 * @author Aseubel
 * @date 2025/7/27 下午8:17
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

    @Column(name = "uploader_id", nullable = false)
    private Long uploader;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Transient
    private MultipartFile image;

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Image(Long id) {
        super.setId(id);
    }

    /**
     * 获取在OSS中的文件名称（在类型文件夹下）
     */
    public String imageObjectName() {
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/")
                .append(super.getId())
                .append(Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".")));
        return objectName.toString();
    }

    /**
     * 获取oss的url
     */
    public String ossUrl() {
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(BUCKET_NAME)
                .append(".")
                .append(ENDPOINT)
                .append("/")
                .append(imageObjectName());
        this.imageUrl = stringBuilder.toString();
        return this.imageUrl;
    }
}
