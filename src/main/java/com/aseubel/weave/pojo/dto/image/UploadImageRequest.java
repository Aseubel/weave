package com.aseubel.weave.pojo.dto.image;

import com.aseubel.weave.common.annotation.FieldDesc;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/27 下午8:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequest implements Serializable {

    @FieldDesc(name = "图片本体")
    private MultipartFile file;

    public Image toImage(User uploader) {
        Image image = new Image();
        image.setUploader(uploader.getId());
        image.setImage(file);
        image.ossUrl();
        return image;
    }
}
