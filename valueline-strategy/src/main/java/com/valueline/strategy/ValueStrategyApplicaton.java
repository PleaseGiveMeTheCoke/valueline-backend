package com.valueline.strategy;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableDubbo
public class ValueStrategyApplicaton {
    public static void main(String[] args) {
        SpringApplication.run(ValueStrategyApplicaton.class);
        new Thread(() -> {
            synchronized (ValueStrategyApplicaton.class) {
                try {
                    ValueStrategyApplicaton.class.wait();
                } catch (Throwable e) {

                }
            }
        }).start();
    }
}
