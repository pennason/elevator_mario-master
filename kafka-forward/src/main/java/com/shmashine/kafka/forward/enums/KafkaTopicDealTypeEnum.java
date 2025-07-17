// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.enums;

import org.springframework.http.HttpMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 11:14
 * @since v1.0
 */

@AllArgsConstructor
public enum KafkaTopicDealTypeEnum {
    // kafka主题转发规则
    FORWARD_TEST("forward-test", "kafka_forward_test", HttpMethod.GET.name(), "", true),
    FAULT_ETYY01("fault-etyy01", "SEND_TO_USER_FAULT_etyy01", HttpMethod.GET.name(), "", true),
    FAULT_KONE("fault-kone", "SEND_TO_USER_FAULT_KONE", HttpMethod.GET.name(), "", true),
    FAULT_LJLC("fault-ljlc", "SEND_TO_USER_FAULT_ljlc", HttpMethod.GET.name(), "", true),
    FAULT_SHDC("fault-shdc", "SEND_TO_USER_FAULT_shdc", HttpMethod.GET.name(), "/message/fault/{elevatorCode}", true),
    MONITOR_ETYY01("monitor-etyy01", "SEND_TO_USER_MONITOR_etyy01", HttpMethod.GET.name(), "", true),
    MONITOR_KONE("monitor-kone", "SEND_TO_USER_MONITOR_KONE", HttpMethod.GET.name(), "", true),
    MONITOR_LJLC("monitor-ljlc", "SEND_TO_USER_MONITOR_ljlc", HttpMethod.GET.name(), "", true),
    MONITOR_SHDC("monitor-shdc", "SEND_TO_USER_MONITOR_shdc", HttpMethod.GET.name(), "/message/monitor/{elevatorCode}", true),
    MONITOR_CXYWTG("monitor-cxywtg", "SEND_TO_USER_MONITOR_cxywtg", HttpMethod.GET.name(), "", true);

    @Getter
    private final String slug;
    @Getter
    private final String topicName;
    @Getter
    private final String httpMethod;
    @Getter
    private final String urlPath;
    @Getter
    private final Boolean forward;

    public static KafkaTopicDealTypeEnum getByTopicName(String topicName) {
        for (var item : values()) {
            if (item.getTopicName().equals(topicName)) {
                return item;
            }
        }
        return null;
    }
}
