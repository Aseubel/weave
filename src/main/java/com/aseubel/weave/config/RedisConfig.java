package com.aseubel.weave.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author Aseubel
 * @date 2025/8/25 下午5:56
 */
@Configuration
@EnableRedisRepositories(basePackages = "non.existent.package")
public class RedisConfig {
}
