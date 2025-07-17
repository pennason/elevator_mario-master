package com.shmashine.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EnableScheduling 定时器
 *
 * @author little.li
 */
@EnableScheduling
@EnableEurekaClient
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
@EnableFeignClients(basePackages = {"com.shmashine.common", "com.shmashine.socket.feign"})
@EnableCaching
public class SocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketApplication.class, args);
    }

}
