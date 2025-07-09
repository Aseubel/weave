package com.aseubel.weave.pojo.dto.auth;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改密码请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class ChangePasswordRequest {

    @NotBlank(message = "原密码不能为空")
    private String oldPassword; // 原密码

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码长度必须在6-20位之间")
    private String newPassword; // 新密码

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword; // 确认密码
}