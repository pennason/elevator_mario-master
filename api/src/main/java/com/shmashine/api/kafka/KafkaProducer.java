package com.shmashine.api.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka生产者
 */
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


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
    public void sendMessageToKafka(String topic, String key, String message) {
        this.kafkaTemplate.send(topic, key, message);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   topic名称
     * @param message 消息内容
     */
    public void sendMessageToKafkaWithFlush(String topic, String key, String message) {
        this.kafkaTemplate.send(topic, key, message);
        this.kafkaTemplate.flush();
    }

}
