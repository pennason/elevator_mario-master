package com.shmashine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.shmashine.config.MessageListener;

import lombok.extern.slf4j.Slf4j;

@EnableCaching
@SpringBootApplication
@MapperScan("com.shmashine.mapper")
@EnableEurekaClient
@Slf4j
public class ApiAuthServerApplication {

    public static void main(String[] args) {
        log.debug("======== SpringApplication 启动... ======== ");
        SpringApplication application = new SpringApplication(ApiAuthServerApplication.class);
        // 注册监听器，添加properties配置
        application.addListeners(new MessageListener("config/message.properties"));
        application.run(args);
    }
}
