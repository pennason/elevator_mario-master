package com.shmashine.kafka.forward.message.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.RequiredArgsConstructor;

/**
 * kafka生产者
 *
 * @author little.li
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;


    /**
     * 发送消息到kafka
     *
     * @param topic   topic名称
     * @param message 消息内容
     */
    public void sendMessageToKafka(String topic, String message) {
        kafkaTemplate.send(topic, "", message);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   topic名称
     * @param message 消息内容
     */
    public ListenableFuture<SendResult<String, String>> sendMessageToKafka(String topic, String key, String message) {
        return kafkaTemplate.send(topic, 2, key, message);
    }


}
