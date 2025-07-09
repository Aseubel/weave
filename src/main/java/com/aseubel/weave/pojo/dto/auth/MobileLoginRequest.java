package com.aseubel.weave.pojo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 手机验证码登录请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@Data
public class MobileLoginRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    /**
     * 短信验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String smsCode;
}