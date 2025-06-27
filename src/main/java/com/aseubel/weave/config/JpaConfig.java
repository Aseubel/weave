package com.aseubel.weave.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * @author Aseubel
 * @date 2025/6/27 下午7:08
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.aseubel.weave.repository")
@EntityScan(basePackages = "com.aseubel.weave.pojo.entity")
@EnableJpaAuditing
public class JpaConfig {
    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.empty();
    }
}
