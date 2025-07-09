package com.aseubel.weave.pojo.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 第三方登录请求DTO
 * 
 * @author Aseubel
 * @date 2025/6/29
 */
@Data
public class ThirdPartyLoginRequest {

    @NotNull(message = "登录类型不能为空")
    private LoginType type; // 登录类型

    @NotBlank(message = "授权码不能为空")
    private String code; // 授权码

    private String state; // 状态参数（可选）

    /**
     * 第三方登录类型
     */
    public enum LoginType {
        WECHAT, // 微信登录
        QQ, // QQ登录
        WEIBO // 微博登录
    }
}