package com.shmashine.hkCameraForTY.kfk;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.hkCameraForTY.service.HkVideoService;

/**
 * kafka消费者
 * @AUTHOR: jiangheng
 * @DATA: 2021/2/25 - 15:48
 */
@Component
public class KafkaConsumer {

    @Autowired
    private HkVideoService hkVideoService;

    /**
     * 故障视频录制并上传阿里云
     *
     * @param record record
     */
    @KafkaListener(topics = {"pro_fault_video_ty"})
    public void nettyTopic(ConsumerRecord<String, Object> record) {

        Optional<Object> kafkaMassage = Optional.ofNullable(record.value());

        String type = null;
        String workOrderId = null;
        String elevatorCode = null;
        Integer faultType = null;

        if (kafkaMassage.isPresent()) {

            String messages = kafkaMassage.get().toString();

            JSONObject jsonObject = JSONObject.parseObject(messages);
            type = jsonObject.getString("type");
            workOrderId = jsonObject.getString("workOrderId");
            elevatorCode = jsonObject.getString("elevatorCode");
            faultType = Integer.parseInt(jsonObject.getString("faultType"));

        }

        if ("add".equals(type)) {
            hkVideoService.start(workOrderId, elevatorCode, faultType);
        }

    }

}
