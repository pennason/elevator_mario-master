package com.shmashine.api.kafka;


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
    public String faultTopic;

    @Value("${kafka.topic.http}")
    public String httpTopic;

    @Value("${kafka.topic.pre-fault}")
    public String preFaultTopic;

    @Value("${kafka.topic.socket}")
    public String socketTopic;

    public static String FAULT_TOPIC;

    public static String HTTP_TOPIC;

    /**
     * 待确认的故障
     */
    public static String PRE_FAULT_TOPIC;

    public static String SOCKET_TOPIC;

    @PostConstruct
    private void setProps() {
        FAULT_TOPIC = this.faultTopic;
        HTTP_TOPIC = this.httpTopic;
        PRE_FAULT_TOPIC = this.preFaultTopic;
        SOCKET_TOPIC = this.socketTopic;
    }

}
