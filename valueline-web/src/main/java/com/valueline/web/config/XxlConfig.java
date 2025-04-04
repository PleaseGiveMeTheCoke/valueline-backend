package com.valueline.web.config;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.client.ExecutorBizClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlConfig {

    @Value("${xxl.job.executor.addresses}")
    private String executorAddresses;

    @Value("${xxl.job.admin.accessToken}")
    private String accessToken;

    @Value("${xxl.job.admin.timeout}")
    private Integer timeout;

    @Bean
    public ExecutorBiz executorBiz() {
        return new ExecutorBizClient(executorAddresses, accessToken, timeout);
    }
}
