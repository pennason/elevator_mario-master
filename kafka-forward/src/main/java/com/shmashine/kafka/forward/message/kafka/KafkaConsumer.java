package com.shmashine.kafka.forward.message.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.shmashine.kafka.forward.service.MessageServiceI;

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

    private final MessageServiceI messageService;

    @Value("${kafka.topic.forward.forward-test}")
    public String forwardTest;

    @Value("${kafka.topic.forward.fault-etyy01}")
    public String faultEtyy01;

    @Value("${kafka.topic.forward.fault-kone}")
    public String faultKone;

    @Value("${kafka.topic.forward.fault-ljlc}")
    public String faultLjlc;

    @Value("${kafka.topic.forward.fault-shdc}")
    public String faultShdc;

    @Value("${kafka.topic.forward.monitor-etyy01}")
    public String monitorEtyy01;

    @Value("${kafka.topic.forward.monitor-kone}")
    public String monitorKone;

    @Value("${kafka.topic.forward.monitor-ljlc}")
    public String monitorLjlc;

    @Value("${kafka.topic.forward.monitor-shdc}")
    public String monitorShdc;

    @Value("${kafka.topic.forward.monitor-cxywtg}")
    public String monitorCxywtg;

    /**
     * 测试使用
     *
     * @param record 消息
     */
    @KafkaListener(topics = {"#{__listener.forwardTest}"})
    public void forwardTest(ConsumerRecord record) {
        messageService.dealMessage(record, forwardTest);
    }

    @KafkaListener(topics = {"#{__listener.faultEtyy01}"})
    public void faultEtyy01(ConsumerRecord record) {
        messageService.dealMessage(record, faultEtyy01);
    }

    @KafkaListener(topics = {"#{__listener.faultKone}"})
    public void faultKone(ConsumerRecord record) {
        messageService.dealMessage(record, faultKone);
    }

    @KafkaListener(topics = {"#{__listener.faultLjlc}"})
    public void faultLjlc(ConsumerRecord record) {
        messageService.dealMessage(record, faultLjlc);
    }

    @KafkaListener(topics = {"#{__listener.faultShdc}"})
    public void faultShdc(ConsumerRecord record) {
        messageService.dealMessage(record, faultShdc);
    }

    @KafkaListener(topics = {"#{__listener.monitorEtyy01}"})
    public void monitorEtyy01(ConsumerRecord record) {
        messageService.dealMessage(record, monitorEtyy01);
    }

    @KafkaListener(topics = {"#{__listener.monitorKone}"})
    public void monitorKone(ConsumerRecord record) {
        messageService.dealMessage(record, monitorKone);
    }

    @KafkaListener(topics = {"#{__listener.monitorLjlc}"})
    public void monitorLjlc(ConsumerRecord record) {
        messageService.dealMessage(record, monitorLjlc);
    }

    @KafkaListener(topics = {"#{__listener.monitorShdc}"})
    public void monitorShdc(ConsumerRecord record) {
        messageService.dealMessage(record, monitorShdc);
    }

    @KafkaListener(topics = {"#{__listener.monitorCxywtg}"})
    public void monitorCxywtg(ConsumerRecord record) {
        messageService.dealMessage(record, monitorCxywtg);
    }
}
