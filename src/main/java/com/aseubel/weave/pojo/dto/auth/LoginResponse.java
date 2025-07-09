package com.aseubel.weave.pojo.dto.auth;

import com.aseubel.weave.pojo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 * 
 * @author Aseubel
 * @date 2025/6/27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 访问令牌过期时间（毫秒）
     */
    private Long accessTokenExpiration;

    /**
     * 刷新令牌过期时间（毫秒）
     */
    private Long refreshTokenExpiration;

    /**
     * 从用户实体创建登录响应
     */
    public static LoginResponse fromUser(User user, String accessToken, String refreshToken,
            Long accessTokenExpiration, Long refreshTokenExpiration) {
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatar())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpiration)
                .refreshTokenExpiration(refreshTokenExpiration)
                .build();
    }
}