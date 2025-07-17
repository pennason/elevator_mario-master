package com.shmashine.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author little.li
 */
@EnableCaching
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.shmashine.common", "com.shmashine.api", "com.shmashine.socketClients",
        "com.shmashine.haierCamera.client", "com.shmashine.userclient.client", "com.shmashine.satoken.client"})
@SpringBootApplication()
@MapperScan("com.shmashine.api.dao")
@ComponentScan({"com.shmashine.common", "com.shmashine.api"})
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
