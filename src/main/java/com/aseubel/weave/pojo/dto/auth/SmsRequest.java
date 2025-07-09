package com.aseubel.weave.pojo.dto.auth;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/6/30 下午8:27
 */
@Getter
public class SmsRequest implements Serializable {
    private String mobile;
}
