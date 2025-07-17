package com.shmashine.socket.kafka;


import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.socket.netty.ChannelManager;

import lombok.extern.slf4j.Slf4j;

/**
 * kafka 消费者
 *
 * @author little.li
 */
@Slf4j
@Component
public class KafkaConsumer {


    /**
     * 设备落库消息监听
     *
     * @param record record
     */
    @KafkaListener(topics = "${kafka.topic.consumer.nettyTopic}")
    public void nettyTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            // 消息推送到设备
            String message = kafkaMassage.get().toString();
            JSONObject jsonObject = JSONObject.parseObject(message);
            String elevatorCode = jsonObject.getString("elevatorCode");
            String sensorType = jsonObject.getString("sensorType");
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, message);
        }
    }


}
