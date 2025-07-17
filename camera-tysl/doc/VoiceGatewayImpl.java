// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.cameratysl.gateway.VoiceGateway;
import com.shmashine.cameratysl.gateway.dto.TyslResponseDTO;
import com.shmashine.cameratysl.gateway.dto.VoiceTalkbackResponseDTO;
import com.shmashine.cameratysl.gateway.tysl.common.Constant;
import com.shmashine.cameratysl.properties.TyslProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/21 15:54
 * @since v1.0
 */

@Slf4j
@Primary
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class VoiceGatewayImpl implements VoiceGateway {
    private final RestTemplate restTemplate;

    @Override
    public VoiceTalkbackResponseDTO getTalkbackToken(String deviceId) {
        var uri = Constant.VOICE_URI_TALKBACK_TOKEN_POST;
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求参数
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("deviceId", deviceId);
        // 发起请求
        var res = sendPostRequest(uri, headers, postParameters);
        return JSON.parseObject(JSON.toJSONString(res), VoiceTalkbackResponseDTO.class);
    }

    private <T> TyslResponseDTO sendPostRequest(String uri, HttpHeaders headers, T requestBody) {
        if (null == headers) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        var url = Constant.PRO_DOMAIN + uri;

        var entity = new HttpEntity<>(requestBody, headers);
        log.info("tysl voice sendPostRequest uri:{}, request:{}, ", url, JSON.toJSONString(entity));
        var res = restTemplate.exchange(url, HttpMethod.POST, entity, TyslResponseDTO.class);
        log.info("tysl voice sendPostRequest uri:{}, response:{}", url, JSON.toJSONString(res));
        return res.getBody();
    }
}
