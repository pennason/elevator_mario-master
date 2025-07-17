package com.shmashine.pm.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shmashine.common.interceptor.CryptHandler;
import com.shmashine.common.interceptor.PrivacyInterceptor;


/**
 * bean装配
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/5/20 17:32
 * @Since: 1.0.0
 */
@Configuration
public class PrivacyAutoConfiguration {

    @Bean
    public PrivacyInterceptor privacyInterceptor(CryptHandler cryptHandler) {
        return new PrivacyInterceptor(cryptHandler);
    }

    @Bean
    public CryptHandler cryptHandler() {
        return new CryptHandler();
    }

}
