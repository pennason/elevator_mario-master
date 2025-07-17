package com.shmashine.fault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 故障处理模块
 *
 * @author little.li
 */
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.shmashine.common", "com.shmashine.fault.feign"})
@ComponentScan({"com.shmashine.common", "com.shmashine.fault"})
public class FaultApplication {


    public static void main(String[] args) {
        SpringApplication.run(FaultApplication.class, args);
    }


}
