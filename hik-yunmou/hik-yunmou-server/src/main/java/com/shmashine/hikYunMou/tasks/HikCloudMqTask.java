// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shmashine.hikYunMou.client.dto.HikCloudMessageResponseDTO;
import com.shmashine.hikYunMou.properties.HikCloudMqProperties;
import com.shmashine.hikYunMou.service.HikCloudMessageService;
import com.shmashine.hikYunMou.utils.HikPlatformUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 海康云眸消息消费任务
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/16 17:24
 * @since v1.0
 */

@Slf4j
@Component
@Profile({"prod"})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HikCloudMqTask {
    private final HikPlatformUtil hikPlatformUtil;
    private final HikCloudMqProperties properties;
    private final HikCloudMessageService hikCloudMessageService;

    /**
     * 海康云眸消息消费任务 上下线消费
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void hikCloudMqTask() {
        log.info("海康云眸消息消费任务开始");
        var consumerId = hikPlatformUtil.getConsumerId();
        var messageResponseDTO = hikPlatformUtil.listConsumeMessage(consumerId);
        if (!messageResponseDTO.getSuccess()) {
            log.info("海康云眸消息消费任务-失败-code:{}-message:{}", messageResponseDTO.getCode(),
                    messageResponseDTO.getMessage());
            return;
        }
        if (CollectionUtils.isEmpty(messageResponseDTO.getData())) {
            log.info("海康云眸消息消费任务-无消息");
            return;
        }
        messageResponseDTO.getData().forEach(this::consumeMessage);
        log.info("海康云眸消息消费任务结束");
    }

    private void consumeMessage(HikCloudMessageResponseDTO.HikMessageInfo hikMessageInfo) {
        log.info("海康云眸消息消费任务-消息ID：{}-消息类型:{}-消息内容:{}", hikMessageInfo.getMsgId(), hikMessageInfo.getMsgType(),
                hikMessageInfo.getContent());
        // 上下线
        if (properties.getTopic().getDeviceOnoffline().equals(hikMessageInfo.getMsgType())) {
            hikCloudMessageService.dealDeviceOnOffline(hikMessageInfo);
            return;
        }        // 云录制 转码录制结果
        if (properties.getTopic().getCloudVideoRecord().equals(hikMessageInfo.getMsgType())) {
            hikCloudMessageService.dealCloudVideoRecord(hikMessageInfo);
            return;
        }
        log.info("海康云眸消息消费任务-未知的消息类型:{}", hikMessageInfo.getMsgType());
    }
}
