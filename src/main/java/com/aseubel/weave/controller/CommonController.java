package com.aseubel.weave.controller;

import cn.hutool.core.util.StrUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.common.util.AliOSSUtil;
import com.aseubel.weave.context.UserContext;
import com.aseubel.weave.pojo.dto.common.ImageResponse;
import com.aseubel.weave.pojo.dto.image.UploadImageRequest;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import com.aseubel.weave.repository.ImageRepository;
import com.aseubel.weave.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 通用方法控制器
 *
 * @author Aseubel
 * @date 2025/7/27 下午8:28
 */
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
@Tag(name = "通用方法")
public class CommonController {

    private final CommonService commonService;

    /**
     * 上传图片
     *
     * @param request
     * @param response
     * @return
     * @throws ClientException
     */
    @PostMapping("/uploadImage")
    @RequireLogin
    @Operation(summary = "上传图片", description = "上传图片，返回图片信息")
    public ApiResponse<ImageResponse> uploadImage(@ModelAttribute UploadImageRequest request,
                                                  HttpServletResponse response) throws ClientException, IOException {
        if (StrUtil.isEmpty(request.getFile().getOriginalFilename())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        Image image = commonService.uploadImage(request);
        return ApiResponse.success(new ImageResponse(image));
    }
}
