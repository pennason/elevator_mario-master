package com.shmashine.sender.message.kafka;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.sender.config.MessageTopics;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.message.handle.OnOfflineHandle;
import com.shmashine.sender.message.handle.PeriodicHandle;
import com.shmashine.sender.message.handle.TrappedHandle;
import com.shmashine.sender.platform.city.shanghai.YidianSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * kafka消息消费者
 */
@Slf4j
@Profile({"prod", "chongqing"})
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaConsumer {
    private final YidianSender sender;
    private final MonitorHandle monitorHandle;
    private final FaultHandle faultHandle;
    private final TrappedHandle trappedHandle;
    private final OnOfflineHandle onOfflineHandle;
    private final PeriodicHandle periodicHandle;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(512, 2048,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(4096), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "KafkaConsumer");

    //    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

    private final BlockingQueue<String> queueDataAccount = new LinkedBlockingQueue<>(1000);


    /**
     * cube上报monitor实时监控信息
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_MONITOR})
    public void monitor(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String monitorMsg = kafkaMassage.get().toString();
            //            queue.offer(monitorMsg);
            executorService.execute(() -> {

                //                String message = queue.poll();
                monitorHandle.handle(monitorMsg);

            });
        }
    }

    /**
     * cube上报event事件信息
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_EVENT})
    public void tr(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            System.out.println(message);
        }
    }

    /**
     * cube上报fault故障消息
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_FAULT})
    public void monitorMessageTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            faultHandle.handle(message);
        }
    }

    /**
     * cube上报困人消息
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_TRAPPED})
    public void trapped(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            trappedHandle.handle(message);
        }
    }

    /**
     * 电梯在线离线
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_ONLINE_OFFLINE})
    public void onOfflineTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            onOfflineHandle.handle(message);
        }
    }

    /**
     * 统计数据
     */
    @KafkaListener(clientIdPrefix = "sender", topics = {MessageTopics.CUBE_TR})
    public void periodicTopic(ConsumerRecord record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            String message = kafkaMassage.get().toString();
            periodicHandle.handle(message);
        }
    }
}
