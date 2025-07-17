package com.shmashine.socket.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka生产者
 *
 * @author little.li
 */
@Component
public class KafkaProducer {

    private static Logger faultMessageLogger = LoggerFactory.getLogger("faultMessageLogger");

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
        if (KafkaTopicConstants.PRE_FAULT_TOPIC.equals(topic) || KafkaTopicConstants.FAULT_TOPIC.equals(topic)) {
            faultMessageLogger.info("send to kafka with topic: " + topic + ", message: " + message);
        }
    }

    /**
     * 发送消息到kafka
     *
     * @param topic topic名称
     */
    public void sendMessageToKafka(String topic, String key, String message) {
        this.kafkaTemplate.send(topic, key, message);
        if (KafkaTopicConstants.PRE_FAULT_TOPIC.equals(topic) || KafkaTopicConstants.FAULT_TOPIC.equals(topic)) {
            faultMessageLogger.info("send to kafka with topic: " + topic + ", message: " + message);
        }
    }

    /**
     * 立即推送，不等待缓存打满 故障类型专用
     */
    public void sendAndFlush(String topic, String message) {
        this.kafkaTemplate.send(topic, message);
        this.kafkaTemplate.flush();
    }

}
