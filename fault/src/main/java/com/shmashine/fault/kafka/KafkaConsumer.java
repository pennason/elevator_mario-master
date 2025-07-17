package com.shmashine.fault.kafka;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cn.hutool.core.exceptions.ExceptionUtil;

import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.fault.message.MessageHandle;
import com.shmashine.fault.message.handle.HlsHandle;

import lombok.extern.slf4j.Slf4j;

/**
 * kafka 消费者
 *
 * @author little.li
 */
@Slf4j
@Component
public class KafkaConsumer {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 32,
            8L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "KafkaConsumer");


    private final MessageHandle messageHandle;

    private final HlsHandle hlsHandle;

    @Autowired
    public KafkaConsumer(MessageHandle messageHandle, HlsHandle hlsHandle) {
        this.messageHandle = messageHandle;
        this.hlsHandle = hlsHandle;
    }


    /**
     * 设备故障落库消息监听
     *
     * @param record record
     */
    @KafkaListener(topics = "${kafka.topic.consumer.fault}")
    public void nettyTopic(ConsumerRecord<String, Object> record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String message = kafkaMassage.get().toString();
                    messageHandle.messageHandle(message);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}] {} error:{} {}", "pro_oreo_fault",
                            kafkaMassage.get().toString(), e.getMessage(),
                            ExceptionUtil.stacktraceToString(e));
                }
            });

        }
    }

    /**
     * 模式切换消息
     */
    @KafkaListener(topics = "${kafka.topic.consumer.event}")
    public void eventTopic(ConsumerRecord<String, Object> record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String message = kafkaMassage.get().toString();
                    messageHandle.eventMessageHandle(message);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}]error:{}", "pro_oreo_event", e.getMessage());
                }

            });


        }

    }


    /**
     * 设备统计落库消息监听
     *
     * @param record record
     */
    @KafkaListener(topics = "${kafka.topic.consumer.tr}")
    public void trTopic(ConsumerRecord<String, Object> record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String message = kafkaMassage.get().toString();
                    messageHandle.trMessageHandle(message);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}]error:{}", "pro_oreo_tr", e.getMessage());
                }

            });


        }
    }


    /**
     * 待确认故障
     */
    @KafkaListener(topics = "${kafka.topic.consumer.preFault}")
    public void preFaultTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String message = kafkaMassage.get().toString();
                    messageHandle.preFaultMessageHandle(message);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}] {} error:{} {}", "pro_oreo_preFault",
                            kafkaMassage.get().toString(), e.getMessage(),
                            ExceptionUtil.stacktraceToString(e));
                }

            });


        }
    }


    /**
     * hls录制视频消息监听
     *
     * @param record record
     */
    @KafkaListener(topics = "${kafka.topic.consumer.hls}")
    public void hlsTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String message = kafkaMassage.get().toString();
                    System.out.printf("hls-topic-listener：%s\n", message);
                    hlsHandle.proHandle(message);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}]error:{}", "pro_hls_nezha", e.getMessage());
                }

            });


        }
    }

    /**
     * 非平层停梯识别有人
     */
    @KafkaListener(topics = "${kafka.topic.consumer.faultConfirmBy6}")
    public void faultConfirmBy6(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {

            executorService.execute(() -> {

                try {
                    String faultId = kafkaMassage.get().toString();
                    messageHandle.faultConfirmBy6(faultId);
                } catch (Exception e) {
                    log.error("kafka-consumer-error, topci:[{}]error:{}", "fault_confirm_by_6", e.getMessage());
                }

            });

        }
    }

    /**
     * 人流量统计消息
     */
    @KafkaListener(topics = "${kafka.topic.consumer.peopleFlowStatistics}")
    public void peopleFlowStatistics(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            messageHandle.peopleFlowStatistics(message);
        }
    }

}
