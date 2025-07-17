// Copyright (C) 2022 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.kafka.forward.check.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.kafka.forward.check.message.kafka.KafkaProducer;


/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2022/9/19 15:47
 * @since v1.0
 */

@SpringBootTest
public class ApplicationTest {
    @Autowired
    KafkaProducer kafkaProducer;

    @Test
    public void testSendKafkaMessage() {
        kafkaProducer.sendMessageToKafka("kafka_forward_test",
                "==========================={this is a message check for kafka_forward_test}=============");

    }
}
