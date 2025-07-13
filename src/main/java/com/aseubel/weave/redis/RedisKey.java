package com.aseubel.weave.redis;

/**
 * @author Aseubel
 * @date 2025/6/28 下午9:22
 */
public class RedisKey {
    public static final String APP = "weave:";

    public static final String REDIS_TOKEN_KEY_PREFIX = "auth:token:";
    public static final String REDIS_SMS_CODE_PREFIX = "sms:code:";

    public static final String POST_LIKE_RECENT_KEY = "post:like:recent";
    public static final String POST_LIKE_COUNT_KEY = "post:like:count";
    public static final String POST_VIEW_COUNT_KEY = "post:view:count:";
    public static final String POST_COMMENT_COUNT_KEY = "post:comment:count:";
    // 是否点赞
    public static final String POST_LIKE_STATUS_KEY = "post:like:status:";
}
