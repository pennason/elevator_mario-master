package com.shmashine.camera.kafka;


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


    @Value("${kafka.topic.hls}")
    public String hlsTopic;

    @Value("${kafka.topic.socket}")
    public String socketTopic;


    public static String HLS_TOPIC;

    public static String SOCKET_TOPIC;


    @PostConstruct
    private void setProps() {
        HLS_TOPIC = this.hlsTopic;
        SOCKET_TOPIC = this.socketTopic;
    }

}
