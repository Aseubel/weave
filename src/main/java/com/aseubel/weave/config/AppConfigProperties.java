package com.aseubel.weave.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @date 2025/7/31 下午6:59
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {
    private String filePath;
    private String password = "Weave123456";
    private Integer competitionVoteLimit = 3;
    private String getFileApi;
}
