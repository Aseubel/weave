package com.aseubel.weave.pojo.dto.auth;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/1 上午11:10
 */
@Getter
public class RefreshTokenRequest implements Serializable {

    private String refreshToken;
}
