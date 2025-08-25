package com.aseubel.weave;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configurable
@EnableScheduling
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class WeaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeaveApplication.class, args);
    }

}
