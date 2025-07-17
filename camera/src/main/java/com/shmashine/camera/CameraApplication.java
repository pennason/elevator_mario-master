package com.shmashine.camera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author chenx
 */
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.shmashine.common", "com.shmashine.camera.service", "com.shmashine.camera.client"})
// , "com.shmashine.hkCameraByYS.client", "com.shmashine.hikYunMou.client"
@MapperScan("com.shmashine.camera.dao")
@EnableScheduling
public class CameraApplication {

    public static void main(String[] args) {
        SpringApplication.run(CameraApplication.class, args);
    }

}
