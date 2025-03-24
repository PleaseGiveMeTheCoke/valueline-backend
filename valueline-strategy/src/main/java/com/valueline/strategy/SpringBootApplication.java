package com.valueline.strategy;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableDubbo
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class);
        new Thread(() -> {
            synchronized (SpringBootApplication.class) {
                try {
                    SpringBootApplication.class.wait();
                } catch (Throwable e) {

                }
            }
        }).start();
    }
}
