// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.message.handle;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.message.OnOfflineMessage;
import com.shmashine.sender.message.send.OnOfflineSend;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/24 13:32
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OnOfflineHandle {
    private final BizElevatorService bizElevatorService;

    /**
     * 第三方平台code ： 第三方平台推送处理类
     */
    private static ConcurrentHashMap<String, OnOfflineSend> senders = new ConcurrentHashMap<>();

    /**
     * 需要监听故障消息的 推送到第三方平台的处理类
     */
    public static void register(OnOfflineSend sender) {
        if (null != sender && StringUtils.hasText(sender.getPtCode())) {
            senders.put(sender.getPtCode(), sender);
        }
    }

    /**
     * 将kafka的在线离线消息转发到具体的第三方推送平台
     *
     * @param message 消息
     */
    public void handle(String message) {
        if (!StringUtils.hasText(message)) {
            return;
        }
        try {
            // step1 : message转jsonObject（json格式校验）
            var messageData = JSON.parseObject(message, OnOfflineMessage.class);
            if (null == messageData || !StringUtils.hasText(messageData.getElevatorCode())) {
                // 无效报文落日志
                log.info("invalid message : " + message);
                return;
            }
            // 电梯上下线消息转发到第三方平台
            dispatch(messageData);

            // 报文落日志
            log.info("send online offline : " + message);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void dispatch(OnOfflineMessage messageData) {
        String elevatorCode = messageData.getElevatorCode();
        // 获取电梯的推送平台code
        TblElevator elevator = bizElevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }
        String ptCodeStr = elevator.getVHttpPtCodes();
        if (!StringUtils.hasText(ptCodeStr) && !"null".equals(ptCodeStr)) {
            return;
        }
        String[] ptCodeList = ptCodeStr.split(",");
        for (String ptCode : ptCodeList) {
            var sender = senders.get(ptCode);
            if (null != sender) {
                try {
                    sender.handleOnOfflineStatus(messageData);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
    }
}
