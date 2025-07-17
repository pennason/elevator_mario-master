package com.shmashine.pm.api;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author little.li
 */
@EnableCaching
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.shmashine.common", "com.shmashine.pm.api", "com.shmashine.socketClients"})
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.shmashine.pm.api.dao")
@ComponentScan({"com.shmashine.common", "com.shmashine.pm.api"})
public class ProjectManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManageApplication.class, args);
    }
}

