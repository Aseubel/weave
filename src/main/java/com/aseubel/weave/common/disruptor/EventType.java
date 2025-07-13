package com.aseubel.weave.common.disruptor;

import lombok.Getter;

/**
 * @author Aseubel
 * @date 2025/5/7 下午2:02
 */
@Getter
public enum EventType {
    TEST,

    REGISTER,
    LOGIN,
    LOGOUT,

    POST_LIKE,
    POST_UNLIKE,
    POST_COMMENT,
    POST_TOP
}
