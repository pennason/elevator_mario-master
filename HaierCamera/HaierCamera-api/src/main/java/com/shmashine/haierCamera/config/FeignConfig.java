package com.shmashine.haierCamera.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shmashine.haierCamera.client.HaierCameraClientFallback;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/19 15:14
 */
@EnableFeignClients(basePackages = "com.shmashine.haierCamera.client")
@Configuration
public class FeignConfig {

    @Bean
    public HaierCameraClientFallback haierCameraClientFallback() {
        return new HaierCameraClientFallback();
    }

}
