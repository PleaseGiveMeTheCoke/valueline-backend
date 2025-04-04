package com.valueline.api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableDubbo
public class ValueApiApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(ValueApiApplicaton.class, args);
    }
}
