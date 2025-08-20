package com.aseubel.weave.pojo.dto.auth;

import com.aseubel.weave.pojo.entity.Image;
import com.aseubel.weave.pojo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

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
    private String userId;

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
    private String accessTokenExpiration;

    /**
     * 刷新令牌过期时间（毫秒）
     */
    private String refreshTokenExpiration;

    /**
     * 从用户实体创建登录响应
     */
    public static LoginResponse fromUser(User user, String accessToken, String refreshToken,
            String accessTokenExpiration, String refreshTokenExpiration) {
        return LoginResponse.builder()
                .userId(String.valueOf(user.getId()))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(Optional.ofNullable(user.getAvatar()).map(Image::getImageUrl).orElse(null))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpiration)
                .refreshTokenExpiration(refreshTokenExpiration)
                .build();
    }
}