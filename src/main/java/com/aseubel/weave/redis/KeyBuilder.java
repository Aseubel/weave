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

    /**
     * map 记录点赞数的增量
     */
    public static String postLikeCountKey() {
        return APP + POST_LIKE_COUNT_KEY;
    }

    /**
     * set 记录点赞的用户
     */
    public static String postLikeStatusKey(Long postId) {
        return APP + POST_LIKE_STATUS_KEY + postId;
    }
}
