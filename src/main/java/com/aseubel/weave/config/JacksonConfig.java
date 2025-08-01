package com.aseubel.weave.config;


import com.aseubel.weave.common.desensitize.DesensitizationSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aseubel
 * @date 2025/8/1 下午10:39
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new DesensitizationSerializer());
        mapper.registerModule(module);
        return mapper;
    }
}

