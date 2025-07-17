package com.shmashine.kafka.forward.check.message.kafka;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * kafka消息消费者
 *
 * @author chenx
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaConsumer {

    @Value("${kafka.topic.forward.forward-test}")
    public String forwardTest;


    /**
     * 测试使用
     *
     * @param record 消息
     */
    @KafkaListener(topics = {"#{__listener.forwardTest}"})
    public void forwardTest(ConsumerRecord record) {
        forwardMessage(record, forwardTest);
    }

    private void forwardMessage(ConsumerRecord record, String topicName) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            log.info("consumer check topic {} with message {}", topicName, message);
        }
    }
}
