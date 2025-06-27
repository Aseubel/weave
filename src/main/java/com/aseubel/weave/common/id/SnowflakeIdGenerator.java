package com.aseubel.weave.common.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @author Aseubel
 * @date 2025/6/27 下午6:50
 */
public class SnowflakeIdGenerator {

    private final static Snowflake snowflake = IdUtil.getSnowflake(1, 1);

    public static Long nextId() {
        return snowflake.nextId();
    }
}
