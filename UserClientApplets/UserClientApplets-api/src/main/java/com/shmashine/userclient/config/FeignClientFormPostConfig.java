package com.shmashine.userclient.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.FormEncoder;

/**
 * Feign 配置
 */
@Configuration
public class FeignClientFormPostConfig {
    // 这里会由容器自动注入HttpMessageConverters的对象工厂
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    // new一个form编码器，实现支持form表单提交
    // 注意这里方法名称，也就是bean的名称是什么不重要，
    // 重要的是返回类型要是 Encoder 并且实现类必须是 FormEncoder 或者其子类
    @Bean
    public Encoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(messageConverters));
    }

    // 开启Feign的日志
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}