package com.valueline.web.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.valueline.client.dao.domain.Strategy;
import com.valueline.client.dao.domain.StrategyRunResult;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DruidDataSource druidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public Dao nutDao(@Autowired DruidDataSource druidDataSource) {
        Dao dao = new NutDao(druidDataSource);
        dao.create(Strategy.class, false);
        dao.create(StrategyRunResult.class, false);

        return dao;
    }
}
