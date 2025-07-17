package com.shmashine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shmashine.socketClients.SocketClientFallback;

/**
 * feign 配置
 *
 * @author jiangheng
 * @version V1.0.0 2020/12/22
 */
@Configuration
public class FeignConfig {

    @Bean
    public SocketClientFallback socketClientFallback() {
        return new SocketClientFallback();
    }
}
