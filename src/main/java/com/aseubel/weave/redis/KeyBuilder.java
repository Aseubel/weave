package com.aseubel.weave.redis;

import static com.aseubel.weave.redis.RedisKey.*;

/**
 * @author Aseubel
 * @date 2025/6/28 下午9:22
 */
public class KeyBuilder {
    public static String userTokenKey(Long userId) {
        return APP + REDIS_TOKEN_KEY_PREFIX + userId;
    }

    public static String smsCodeKey(String mobile) {
        return APP + REDIS_SMS_CODE_PREFIX + mobile;
    }

    /**
     * set
     */
    public static String postLikeRecentKey() {
        return APP + POST_LIKE_RECENT_KEY;
    }

    public static String postLikeCountKey(Long postId) {
        return APP + POST_LIKE_COUNT_KEY + postId;
    }
}
