package com.shmashine.fault.kafka;

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


    @Value("${kafka.topic.producer.hls}")
    public String hlsTopic;

    @Value("${kafka.topic.producer.socket}")
    public String socketTopic;

    @Value("${kafka.topic.producer.ty}")
    public String tyTopic;

    @Value("${kafka.topic.producer.cubeFault}")
    public String cubeFaultTopic;

    public static String HLS_TOPIC;

    public static String SOCKET_TOPIC;

    public static String TY_TOPIC;

    public static String CUBE_FAULT_TOPIC;


    @PostConstruct
    private void setProps() {
        HLS_TOPIC = this.hlsTopic;
        SOCKET_TOPIC = this.socketTopic;
        TY_TOPIC = this.tyTopic;
        CUBE_FAULT_TOPIC = this.cubeFaultTopic;
    }

}
