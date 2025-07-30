package com.aseubel.weave.service.impl.common;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.weave.common.util.AliOSSUtil;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.image.UploadImageRequest;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.repository.ImageRepository;
import com.aseubel.weave.service.CommonService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author Aseubel
 * @date 2025/7/30 下午1:38
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final AliOSSUtil aliOSSUtil;
    private final ImageRepository imageRepository;
    private final ThreadPoolTaskExecutor threadPoolExecutor;

    @Override
    public Image uploadImage(UploadImageRequest request) throws ClientException {
        User currentUser = UserContext.getCurrentUser();
        final Image image = request.toImage(currentUser);
        // 先存储拿到id
        image.setId(imageRepository.save(image).getId());
        aliOSSUtil.upload(image);
        // 更新url，之前没id
        image.ossUrl();
        // 再存一次
        threadPoolExecutor.execute(() -> imageRepository.save(image));
        return image;
    }
}
