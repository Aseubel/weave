package com.aseubel.weave.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.weave.pojo.dto.image.UploadImageRequest;
import com.aseubel.weave.pojo.entity.Image;

/**
 * @author Aseubel
 * @date 2025/7/30 下午1:38
 */
public interface CommonService {

    Image uploadImage(UploadImageRequest request) throws ClientException;

}
