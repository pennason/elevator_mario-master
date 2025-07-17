// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.platform.city;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.OnOfflineMessage;
import com.shmashine.common.message.PeriodicMessage;
import com.shmashine.sender.message.cache.MessageCache;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.message.handle.OnOfflineHandle;
import com.shmashine.sender.message.handle.TrappedHandle;
import com.shmashine.sender.message.send.FaultSend;
import com.shmashine.sender.message.send.MonitorSend;
import com.shmashine.sender.message.send.OnOfflineSend;
import com.shmashine.sender.message.send.PeriodicSend;
import com.shmashine.sender.message.send.TrappedSend;
import com.shmashine.sender.platform.city.tools.CityTool;
import com.shmashine.sender.platform.city.tools.FaultResendTool;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 上师大推送
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/27 17:29
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UniversityShnuSender implements MonitorSend, TrappedSend, FaultSend, OnOfflineSend, PeriodicSend {
    private final CityTool cityTool;
    private final FaultResendTool faultResendTool;
    private final MessageCache messageCache;

    private static final String PT_CODE = "universityShnu";

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        MonitorHandle.register(this);
        TrappedHandle.register(this);
        FaultHandle.register(this);
        OnOfflineHandle.register(this);
        //PeriodicHandle.register(this);
    }

    @Override
    public String getPtCode() {
        return PT_CODE;
    }

    /**
     * 监控消息处理
     *
     * @param message 监控消息
     */
    @Override
    public void handleMonitor(MessageData message) {
        log.info("handleMonitor for {}: {}", PT_CODE, message.toString());
        //cityTool.sendRunningDataToGovern(message, PT_CODE);
        // do null
    }

    /**
     * 困人消息处理
     *
     * @param message 消息json格式
     */
    @Override
    public void handleTrapped(FaultMessage message) {
        log.info("handleTrapped for {}: {}", PT_CODE, message.toString());
        cityTool.sendEntrapDataToGovern(message, PT_CODE);
        faultResendTool.saveSenderFault(message, PT_CODE, true);
    }

    /**
     * convertFaultEntrapToGovern
     *
     * @param message 故障消息
     */
    @Override
    public void handleFault(FaultMessage message) {
        // 防止重复发送
        if (null != messageCache.cacheFaultNX(message)) {
            return;
        }
        log.info("handleFault for {}: {}", PT_CODE, message);
        cityTool.sendFaultDataToGovern(message, PT_CODE);
        faultResendTool.saveSenderFault(message, PT_CODE, false);
    }

    /**
     * 在线离线消息处理
     *
     * @param message 在线离线消息
     */
    @Override
    public void handleOnOfflineStatus(OnOfflineMessage message) {
        //cityTool.sendOnlineStatusToGovern(message, PT_CODE);
        // do null
    }

    @Override
    public void handlePeriodic(PeriodicMessage message) {
        log.info("handlePeriodic for {}: {}", PT_CODE, message.toString());
        //cityTool.sendPeriodicToGovern(message, PT_CODE);
    }
}
