package com.shmashine.common.utils;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 发送请求工具类
 *
 * @author little.li
 */
public class RequestUtil {


    /**
     * 发送post请求
     *
     * @param builder 拼接后的URL
     * @return 响应
     */
    public static ResponseEntity<String> sendPost(UriComponentsBuilder builder, Map queryMap) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(
                builder.build().encode().toUri(),
                queryMap,
                String.class);

    }


    /**
     * 发送get请求
     *
     * @param builder 拼接后的URL
     * @return 响应
     */
    public static ResponseEntity<String> sendGet(UriComponentsBuilder builder) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);

    }

}
