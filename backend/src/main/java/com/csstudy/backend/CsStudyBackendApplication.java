package com.csstudy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsStudyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsStudyBackendApplication.class, args);
    }
}
