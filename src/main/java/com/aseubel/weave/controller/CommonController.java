package com.aseubel.weave.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.config.AppConfigProperties;
import com.aseubel.weave.pojo.dto.common.ImageResponse;
import com.aseubel.weave.pojo.dto.image.UploadImageRequest;
import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    private final AppConfigProperties appConfigProperties;

    /**
     * 上传图片
     *
     * @param request 上传图片请求
     * @param response 响应
     * @return ApiResponse
     * @throws ClientException OSS上传异常
     */
    @PostMapping("/uploadImage")
    @RequireLogin
    @Operation(summary = "上传图片", description = "上传图片，返回图片信息")
    public ApiResponse<ImageResponse> uploadImage(@ModelAttribute UploadImageRequest request,
                                                  HttpServletResponse response) throws ClientException, IOException {
        if (ObjectUtil.isEmpty(request.getFile()) || StrUtil.isEmpty(request.getFile().getOriginalFilename())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        Image image = commonService.uploadImage(request);
        return ApiResponse.success(new ImageResponse(image));
    }

    /**
     * 获取头像
     *
     * @param imgName 图片名称
     * @return 图片文件流
     */
    @GetMapping(value = "/file/{imgName}")
    @Operation(summary = "获取图片", description = "获取图片，返回图片文件流")
    public ResponseEntity<byte[]> getFile(@PathVariable String imgName) {
        String filePath = appConfigProperties.getFilePath();
        try {
            File file = new File(filePath + imgName);
            HttpStatus status = HttpStatus.OK;
            MediaType mediaType = MediaTypeFactory.getMediaType(imgName).orElse(MediaType.IMAGE_JPEG);
            // 判断图片是否存在
            if (!file.exists()) {
                file = new File(filePath + "default.jpg");
                status = HttpStatus.NOT_FOUND;
                mediaType = MediaType.IMAGE_JPEG;
            }
            // 读取图片文件
            InputStream is = FileUtil.getInputStream(file);
            byte[] avatarBytes = StreamUtils.copyToByteArray(is);
            // 构建响应头，设置Content-Type
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(mediaType);
            return new ResponseEntity<>(avatarBytes, httpHeaders, status);
        } catch (IOException e) {
            // 使用日志框架记录错误信息
            System.err.println("Error reading avatar file: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
