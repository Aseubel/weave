package com.aseubel.weave.common.util;

/**
 * @author Aseubel
 * @date 2025/7/1 下午2:39
 * @description 脱敏工具类
 */
public class SensitiveDataUtil {
    /**
     * 手机号脱敏处理
     */
    public static String maskMobile(String mobile) {
        if (mobile == null || mobile.length() != 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }
}
