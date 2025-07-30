package com.aseubel.weave.pojo.dto.common;

import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/30 下午12:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse implements Serializable {
    private Long imageId;

    private String imageUrl;

    private byte[] image;

    public ImageResponse(Image image) throws IOException {
        this.imageId = image.getId();
        this.imageUrl = image.getImageUrl();
//        this.image = image.getImage().getBytes();
    }
}
