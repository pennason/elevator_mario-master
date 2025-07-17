package com.shmashine.socket.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DataSource 多数据源注入配置
 *
 * @author little.li
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "oreo")
    @ConfigurationProperties(prefix = "spring.datasource.oreo")
    public DataSource oreoDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "nezha")
    @ConfigurationProperties(prefix = "spring.datasource.nezha")
    public DataSource nezhaDataSource() {
        return DataSourceBuilder.create().build();
    }
}
