// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.service.impl;

import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.kafka.forward.enums.KafkaTopicDealTypeEnum;
import com.shmashine.kafka.forward.message.kafka.KafkaProducer;
import com.shmashine.kafka.forward.service.MessageServiceI;
import com.shmashine.kafka.forward.service.RedisServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 14:03
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MessageServiceImpl implements MessageServiceI {
    private final RedisServiceI redisService;
    private final KafkaProducer kafkaProducer;
    private final RestTemplate restTemplate;

    @Override
    public void forwardMessage(ConsumerRecord record, String topicName) {
        // 转发 kafka消息
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            log.info("consumer topic {} with message {}", topicName, message);
            kafkaProducer.sendMessageToKafka(topicName, message);
        }
    }

    @Override
    public void dealMessage(ConsumerRecord record, String topicName) {
        var topicType = KafkaTopicDealTypeEnum.getByTopicName(topicName);
        if (topicType == null) {
            forwardMessage(record, topicName);
            return;
        }
        // 是否需要转发消息
        if (Boolean.TRUE.equals(topicType.getForward())) {
            forwardMessage(record, topicName);
        }
        if (!StringUtils.hasText(topicType.getUrlPath())) {
            return;
        }
        var message = JSON.parseObject(record.value().toString(), Map.class);
        var elevatorCode = message.containsKey("elevatorCode")
                ? message.get("elevatorCode").toString()
                : (message.containsKey("eid") ? message.get("eid").toString() : null);

        if (HttpMethod.GET.name().equals(topicType.getHttpMethod())) {
            // 存入 Redis , 用户使用 GET 获取
            if (topicType.getTopicName().contains("FAULT")) {
                redisService.cacheKafkaFaultElevator(elevatorCode, record.value().toString());
            } else {
                redisService.cacheKafkaMonitorElevator(elevatorCode, record.value().toString());
            }
        } else if (HttpMethod.POST.name().equals(topicType.getHttpMethod())) {
            // 根据URL地址推送到指定的服务
            sendMessageToUrl(topicType.getUrlPath(), record.value().toString());
        } else {
            log.error("topic {} http method {} not support", topicName, topicType.getHttpMethod());
            return;
        }

    }

    /**
     * POST 方式提交 kafka消息
     *
     * @param urlPath URL地址
     * @param message 消息
     */
    private void sendMessageToUrl(String urlPath, String message) {
        var res = restTemplate.postForObject(urlPath, message, String.class);
        log.info("send message to url {} with message {} result {}", urlPath, message, res);
    }
}
