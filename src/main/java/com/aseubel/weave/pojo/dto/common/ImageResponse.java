package com.aseubel.weave.pojo.dto.common;

import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/7/30 下午12:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse implements Serializable {

    private String imageName;
    private String imageId;
    private String imageUrl;
    private LocalDateTime uploadTime;
    private byte[] image;

    public ImageResponse(Image image) throws IOException {
        this.imageId = image.getId();
        this.imageUrl = image.getImageUrl();
//        this.image = image.getImage().getBytes();
        this.imageName = image.getName();
        this.uploadTime = image.getUploadTime();
    }
}
