package com.shmashine.pm.api.config.restTemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory) {
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    @Bean
    public RestTemplate iotCardRestTemplate(ClientHttpRequestFactory iotCardClientHttpRequestFactory) {
        return new RestTemplate(iotCardClientHttpRequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(5000);//单位为ms
        simpleClientHttpRequestFactory.setConnectTimeout(5000);//单位为ms
        return simpleClientHttpRequestFactory;
    }

    @Bean
    public ClientHttpRequestFactory iotCardClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setReadTimeout(25000);//单位为ms
        simpleClientHttpRequestFactory.setConnectTimeout(5000);//单位为ms
        return simpleClientHttpRequestFactory;
    }
}
