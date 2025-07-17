// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shmashine.cameratysl.client.dto.MessageSubscribeDTO;
import com.shmashine.cameratysl.client.dto.MessageUnsubscribeDTO;
import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.enums.TyslEventTypeEnum;
import com.shmashine.cameratysl.gateway.TyslGateway;
import com.shmashine.cameratysl.gateway.dto.requests.MessageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.MessageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.properties.TyslProperties;
import com.shmashine.cameratysl.service.MessageServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 17:56
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MessageServiceImpl implements MessageServiceI {
    private final TyslGateway gateway;
    private final TyslProperties properties;

    @Override
    public ResponseCustom<String> subscribeEventMessage(MessageSubscribeDTO request) {
        var eventType = request.getEventType();
        if (eventType == null) {
            return ResponseCustom.buildFailed(500, "eventType is null");
        }
        var eventTypeEnum = TyslEventTypeEnum.getByCode(eventType);
        if (eventTypeEnum == null) {
            return ResponseCustom.buildFailed(500, "eventType is invalid");
        }
        return switch (eventTypeEnum) {
            case HEARTBEAT -> subscribeHeartbeatEvent();
            case ZX_SUPER_PLATFORM_STATUS -> subscribeZxSuperPlatformStatusEvent();
            case ZX_SUB_PLATFORM_STATUS -> subscribeZxSubPlatformStatusEvent();
            default -> subscribeStatusChangeEvent();
        };
    }

    @Override
    public ResponseCustom<String> unsubscribeEventMessage(MessageUnsubscribeDTO request) {
        var subscriptionId = request.getSubscriptionId();
        if (subscriptionId == null) {
            return ResponseCustom.buildFailed(500, "subscriptionId is null");
        }
        var res = gateway.unsubscribeMessage(MessageUnsubscribeRequestDTO.builder()
                .subscriptionId(subscriptionId)
                .build());
        if (res == null) {
            return ResponseCustom.buildFailed(500, "unsubscribe failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success("成功");
    }

    @Override
    public ResponseCustom<Object> querySubscribeEventMessage() {
        var res = gateway.listMessageSubs();
        if (res == null) {
            return ResponseCustom.buildFailed(500, "unsubscribe failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getData());
    }

    /**
     * 订阅心跳消息
     *
     * @return 订阅结果
     */
    private ResponseCustom<String> subscribeHeartbeatEvent() {
        var request = MessageSubscribeRequestDTO.builder()
                .eventType(TyslEventTypeEnum.HEARTBEAT.getCode())
                .notifyUrl(properties.getCallbackBaseUrl() + TyslProperties.CALLBACK_URI_STATUS_CHANGE_NOTIFY)
                .networkType(1)
                .build();
        var res = gateway.subscribeMessage(request);
        if (res == null) {
            return ResponseCustom.buildFailed(500, "subscribe heartbeat event failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getMsg());
    }

    /**
     * 订阅中兴上级平台状态通知
     *
     * @return 订阅结果
     */
    private ResponseCustom<String> subscribeZxSuperPlatformStatusEvent() {
        var request = MessageSubscribeRequestDTO.builder()
                .eventType(TyslEventTypeEnum.ZX_SUPER_PLATFORM_STATUS.getCode())
                .notifyUrl(properties.getCallbackBaseUrl() + TyslProperties.CALLBACK_URI_ZX_PLATFORM_STATUS_NOTIFY)
                .networkType(1)
                .build();
        var res = gateway.subscribeMessage(request);
        if (res == null) {
            return ResponseCustom.buildFailed(500, "subscribe ZxSuperPlatform event failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getMsg());
    }

    /**
     * 订阅中兴下级平台状态通知
     *
     * @return 订阅结果
     */
    private ResponseCustom<String> subscribeZxSubPlatformStatusEvent() {
        var request = MessageSubscribeRequestDTO.builder()
                .eventType(TyslEventTypeEnum.ZX_SUB_PLATFORM_STATUS.getCode())
                .notifyUrl(properties.getCallbackBaseUrl() + TyslProperties.CALLBACK_URI_ZX_PLATFORM_STATUS_NOTIFY)
                .networkType(1)
                .build();
        var res = gateway.subscribeMessage(request);
        if (res == null) {
            return ResponseCustom.buildFailed(500, "subscribe ZxSubPlatform event failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getMsg());
    }

    /**
     * 订阅设备状态变更通知
     *
     * @return 订阅结果
     */
    private ResponseCustom<String> subscribeStatusChangeEvent() {
        var request = MessageSubscribeRequestDTO.builder()
                .eventType(TyslEventTypeEnum.STATUS_CHANGE.getCode())
                .notifyUrl(properties.getCallbackBaseUrl() + TyslProperties.CALLBACK_URI_STATUS_CHANGE_NOTIFY)
                .networkType(1)
                .build();
        var res = gateway.subscribeMessage(request);
        if (res == null) {
            return ResponseCustom.buildFailed(500, "subscribe statusChange event failed, res is null");
        }
        if (HttpStatus.OK.value() != res.getCode()) {
            return ResponseCustom.buildFailed(res.getCode(), res.getMsg());
        }
        return ResponseCustom.success(res.getMsg());
    }
}
