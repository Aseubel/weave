package com.aseubel.weave.pojo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@Data
public class LoginRequest {

    /**
     * 登录标识（用户名或手机号）
     */
    @NotBlank(message = "登录标识不能为空")
    private String loginId;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 登录类型：username, mobile
     */
    private String loginType = "username";
}