package com.shmashine.sender.message.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * kafka生产者
 *
 * @author little.li
 */

@Component
public class KafkaProducer {


    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    /**
     * 发送消息到kafka
     *
     * @param topic   topic名称
     * @param message 消息内容
     */
    public void sendMessageToKafka(String topic, String message) {
        this.kafkaTemplate.send(topic, "", message);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   topic名称
     * @param message 消息内容
     */
    public ListenableFuture<SendResult<String, String>> sendMessageToKafka(String topic, String key, String message) {
        return this.kafkaTemplate.send(topic, 2, key, message);
    }


}
