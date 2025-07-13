package com.aseubel.weave.redis;

import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Map;
import java.util.Set;

/**
 * @author Aseubel
 * @date 2025/7/11 下午10:51
 */
public class LuaScript {

    public static final DefaultRedisScript<?> SET_GET_REMOVE_SCRIPT = new DefaultRedisScript<>("local key = KEYS[1] " +
            "local members = redis.call('SMEMBERS', key) " +
            "redis.call('DEL', key) " +
            "return members", Set.class);

    public static final DefaultRedisScript<?> HASH_GET_REMOVE_SCRIPT = new DefaultRedisScript<>("local key = KEYS[1] " +
            "local members = redis.call('HGETALL', key) " +
            "redis.call('DEL', key) " +
            "return members", Map.class);
}
