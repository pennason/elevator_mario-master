package com.shmashine.cameratysl.config;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Rest配置
 *
 * @author chenx
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        // 长链接保持时间长度20秒
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(3, TimeUnit.SECONDS);
        // 设置最大链接数
        poolingHttpClientConnectionManager.setMaxTotal(20 * getMaxCpuCore() + 3);
        // 单路由的并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(20 * getMaxCpuCore());
        return poolingHttpClientConnectionManager;
    }

    @Bean
    public HttpClientBuilder gethttpClientBuilder(
            @Autowired PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        // 重试次数3次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        // 保持长链接配置，keep-alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        return httpClientBuilder;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory(
            @Autowired HttpClientBuilder httpClientBuilder) {
        HttpClient httpClient = httpClientBuilder.build();
        var httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 链接超时配置 5秒
        httpComponentsClientHttpRequestFactory.setConnectTimeout(60000);
        // 连接读取超时配置
        //httpComponentsClientHttpRequestFactory.setReadTimeout(10000);
        // 连接池不够用时候等待时间长度设置，分词那边 500毫秒 ，我们这边设置成1秒
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(1000);
        // 缓冲请求数据，POST大量数据，可以设定为true 我们这边机器比较内存较大
        httpComponentsClientHttpRequestFactory.setBufferRequestBody(true);
        return httpComponentsClientHttpRequestFactory;
    }

    @Bean
    public RestTemplate getRestTemplate(
            @Autowired HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    private int getMaxCpuCore() {
        int cpuCore = Runtime.getRuntime().availableProcessors();
        return cpuCore;
    }
}
