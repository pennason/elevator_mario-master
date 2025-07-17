package com.shmashine.haierCamera.kafka;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 从配置文件中读取topic名称
 *
 * @author little.li
 */
@Component
public class KafkaTopicConstants {


    @Value("${kafka.topic.fault}")
    public String fault;

    @Value("${kafka.topic.pre-fault}")
    public String preFault;


    public static String FAULT_TOPIC;

    public static String PRE_FAULT_TOPIC;

    @PostConstruct
    private void setProps() {
        FAULT_TOPIC = this.fault;
        PRE_FAULT_TOPIC = this.preFault;
    }

}
