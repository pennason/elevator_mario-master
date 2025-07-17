// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/16 14:03
 * @since v1.0
 */

public interface MessageServiceI {
    /**
     * 转发或处理 Kafka消息
     *
     * @param record    消息记录
     * @param topicName 主题
     */
    void forwardMessage(ConsumerRecord record, String topicName);

    /**
     * 处理消息
     *
     * @param record    消息记录
     * @param topicName 主题
     */
    void dealMessage(ConsumerRecord record, String topicName);
}
