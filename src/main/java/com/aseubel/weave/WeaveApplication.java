package com.aseubel.weave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeaveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeaveApplication.class, args);
    }

}
