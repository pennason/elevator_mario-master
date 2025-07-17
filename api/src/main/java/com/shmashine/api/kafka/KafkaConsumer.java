package com.shmashine.api.kafka;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.kafka.handler.ElectricBikeIdentifyHandler;
import com.shmashine.api.kafka.handler.GroupLeasingHandler;
import com.shmashine.api.kafka.handler.HeatMapNewHandler;
import com.shmashine.api.kafka.handler.NightWatchHandler;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * kafka 消费者
 *
 * @author little.li
 */
@Slf4j
@Component
@Profile({"prod"})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaConsumer {

    private final HeatMapNewHandler heatMapNewHandler;
    private final NightWatchHandler nightWatchHandler;
    private final GroupLeasingHandler groupLeasingHandler;
    private final ElectricBikeIdentifyHandler electricBikeIdentifyHandler;

    @Value("${kafka.topic.monitor}")
    public String monitorTopic;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(16, 64,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(4096), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("countElevatorFloorNumberInfo"), "KafkaConsumer");

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(30000);

    /**
     * 统计电梯楼层热力图 (新的逻辑 )， 夜间守护redis
     *
     * @param record 记录信息
     */
    @KafkaListener(topics = {"#{__listener.monitorTopic}"})
    //@Profile({"prod"})
    public void countElevatorFloorNumberInfo(ConsumerRecord<String, Object> record) {
        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());
        if (kafkaMassage.isPresent()) {
            try {

                queue.offer(kafkaMassage.get().toString());

                executorService.submit(() -> {
                    String message = queue.poll();
                    if (null != message) {
                        //log.info("kafka message is {}", message);
                        //heatMapNewHandler.heatMapCount(message);
                        var messageJson = JSON.parseObject(message);
                        var elevatorCode = messageJson.getString("elevatorCode");
                        // yyyy-MM-dd HH:mm:ss
                        var time = messageJson.getString("time");
                        var monitorMessage = (JSONObject) messageJson.get("monitorMessage");
                        var sensorType = monitorMessage.getString("sensorType");
                        if ("CarRoof".equals(sensorType) || "SINGLEBOX".equals(sensorType)) {
                            // 统计电梯楼层热力图 (新的逻辑 )
                            heatMapNewHandler.heatMapCount(elevatorCode, monitorMessage);
                            // 如果是1楼，门关闭，上行，则需抓拍一张图片，用于自己平台的电动车识别
                            //electricBikeIdentifyHandler.checkAndSaveByElevator(elevatorCode, monitorMessage, time);
                            // 夜间守护模式
                            //nightWatchHandler.checkAndSaveNeedWatchElevator(elevatorCode, monitorMessage, time);
                            // 群租问题
                            //groupLeasingHandler.checkAndSaveByElevator(elevatorCode, monitorMessage, time);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
