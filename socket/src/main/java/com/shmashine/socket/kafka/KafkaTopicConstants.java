package com.shmashine.socket.kafka;


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

    @Value("${kafka.topic.producer.monitor}")
    public String monitorTopic;

    @Value("${kafka.topic.producer.fault}")
    public String faultTopic;

    @Value("${kafka.topic.producer.http}")
    public String httpTopic;

    @Value("${kafka.topic.producer.preFault}")
    public String preFaultTopic;

    @Value("${kafka.topic.producer.tr}")
    public String statisticsTopic;

    @Value("${kafka.topic.producer.mergeMonitor}")
    public String mergeMonitorTopic;

    @Value("${kafka.topic.producer.event}")
    public String eventTopic;

    @Value("${kafka.topic.producer.detectedPeopleNums}")
    public String detectedPeopleNums;

    @Value("${kafka.topic.producer.cubeMonitor}")
    public String cubeMonitor;

    @Value("${kafka.topic.producer.cubeTr}")
    public String cubeTr;

    @Value("${kafka.topic.producer.cubeEvent}")
    public String cubeEvent;

    @Value("${kafka.topic.producer.cubeFault}")
    public String cubeFault;

    @Value("${kafka.topic.producer.cubeTrapped}")
    public String cubeTrapped;

    @Value("${kafka.topic.producer.cubeOnlineOffline}")
    public String cubeOnlineOffline;


    /**
     * 监控消息
     */
    public static String MONITOR_TOPIC;

    /**
     * 监控消息(机房和轿顶信息合并)
     */
    public static String MERGE_MONITOR_TOPIC;

    /**
     * 故障消息
     */
    public static String FAULT_TOPIC;

    /**
     * 模式切换消息
     */
    public static String EVENT_TOPIC;

    /**
     * 统计消息
     */
    public static String STATISTICS_TOPIC;

    /**
     * 人流量统计
     */
    public static String STATISTICS_PEOPLE_TOPIC;

    /**
     * 对外推送
     */
    public static String HTTP_TOPIC;

    /**
     * 待确认的故障
     */
    public static String PRE_FAULT_TOPIC;

    /**
     * cube上报monitor实时监控信息
     */
    public static String CUBE_MONITOR;

    /**
     * cube上报阶段性累计运行数据
     */
    public static String CUBE_TR;

    /**
     * cube上报event事件信息
     */
    public static String CUBE_EVENT;

    /**
     * cube上报fault故障消息
     */
    public static String CUBE_FAULT;

    /**
     * cube上报困人消息
     */
    public static String CUBE_TRAPPED;

    /**
     * 在线离线
     */
    public static String CUBE_ONLINE_OFFLINE;

    @PostConstruct
    private void setProps() {
        MONITOR_TOPIC = this.monitorTopic;
        FAULT_TOPIC = this.faultTopic;
        HTTP_TOPIC = this.httpTopic;
        PRE_FAULT_TOPIC = this.preFaultTopic;
        STATISTICS_TOPIC = this.statisticsTopic;
        MERGE_MONITOR_TOPIC = this.mergeMonitorTopic;
        EVENT_TOPIC = this.eventTopic;
        STATISTICS_PEOPLE_TOPIC = this.detectedPeopleNums;
        CUBE_MONITOR = this.cubeMonitor;
        CUBE_TR = this.cubeTr;
        CUBE_EVENT = this.cubeEvent;
        CUBE_FAULT = this.cubeFault;
        CUBE_TRAPPED = this.cubeTrapped;
        CUBE_ONLINE_OFFLINE = this.cubeOnlineOffline;
    }
}
