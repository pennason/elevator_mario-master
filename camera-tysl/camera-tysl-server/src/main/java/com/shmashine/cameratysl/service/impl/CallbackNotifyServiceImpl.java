// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.shmashine.cameratysl.client.dto.callback.CallbackDeviceImage1400NotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackHeartbeatEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackPlaybackCutVideoNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackStatusChangeEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackZxPlatformStatusEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.TyslCallbackResponseDTO;
import com.shmashine.cameratysl.dao.TblCameraExtendInfoMapper;
import com.shmashine.cameratysl.enums.TyslEventTypeEnum;
import com.shmashine.cameratysl.enums.TyslMessageTypeEnum;
import com.shmashine.cameratysl.service.CallbackNotifyServiceI;
import com.shmashine.common.entity.TblCameraExtendInfoEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 10:30
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CallbackNotifyServiceImpl implements CallbackNotifyServiceI {
    private final TblCameraExtendInfoMapper cameraExtendInfoMapper;

    @Override
    public TyslCallbackResponseDTO deviceEventNotify(CallbackStatusChangeEventNotifyDTO request) {
        var msgType = request.getMsgType();
        log.info("收到设备事件通知，消息类型：{}，流水号：（{}）", msgType, request.getBizId());
        var hasError = checkMessageType(msgType, TyslMessageTypeEnum.DEVICE_EVENT, request.getBizId());
        if (null != hasError) {
            return hasError;
        }
        var deviceEvent = request.getDeviceEvent();
        log.info("收到消息通知 {}", JSON.toJSONString(deviceEvent));

        var eventTypeEnum = TyslEventTypeEnum.getByCode(deviceEvent.getEventType());
        if (eventTypeEnum == null) {
            log.error("无相关事件类型枚举 {}，{}", deviceEvent.getEventType(), JSON.toJSONString(deviceEvent));
            return TyslCallbackResponseDTO.builder()
                    .code(500)
                    .msg("无相关事件类型")
                    .build();
        }
        switch (eventTypeEnum) {
            case STATUS_CHANGE -> saveStatusChange(deviceEvent);
            case ZX_SUB_PLATFORM_STATUS -> log.info("不处理的事件类型 中兴下级平台 {}，{}", eventTypeEnum,
                    JSON.toJSONString(deviceEvent));
            case ZX_SUPER_PLATFORM_STATUS -> log.info("不处理的事件类型 中兴上级平台 {}，{}", eventTypeEnum,
                    JSON.toJSONString(deviceEvent));
            default -> log.info("不处理的事件类型 {}，{}", eventTypeEnum, JSON.toJSONString(deviceEvent));
        }
        return TyslCallbackResponseDTO.builder()
                .code(200)
                .msg("成功收到消息通知")
                .build();
    }

    @Override
    public TyslCallbackResponseDTO heartbeatNotify(CallbackHeartbeatEventNotifyDTO request) {
        var msgType = request.getMsgType();
        log.info("收到心跳存活通知，消息类型：{}，流水号：（{}）", msgType, request.getBizId());
        var hasError = checkMessageType(msgType, TyslMessageTypeEnum.HEARTBEAT_EVENT, request.getBizId());
        if (null != hasError) {
            return hasError;
        }
        log.info("收到心跳存活通知，{}", JSON.toJSONString(request));
        return TyslCallbackResponseDTO.builder()
                .code(200)
                .msg("成功收到消息通知，暂未处理")
                .build();
    }

    @Override
    public TyslCallbackResponseDTO zxPlatformStatusNotify(CallbackZxPlatformStatusEventNotifyDTO request) {
        var msgType = request.getMsgType();
        log.info("收到中兴上下级平台状态通知，消息类型：{}，流水号：（{}）", msgType, request.getBizId());
        var needMessageType = TyslMessageTypeEnum.ZX_SUPER_PLATFORM_STATUS;
        if (TyslMessageTypeEnum.ZX_SUPER_PLATFORM_STATUS.getCode().equals(msgType)) {
            // TODO 处理上级平台状态通知
        } else if (TyslMessageTypeEnum.ZX_SUB_PLATFORM_STATUS.getCode().equals(msgType)) {
            needMessageType = TyslMessageTypeEnum.ZX_SUB_PLATFORM_STATUS;
            // TODO 处理下级平台状态通知
        } else {
            log.info("消息类型不匹配 需要：{}|{}，实际：{}， 流水号：（{}）",
                    TyslMessageTypeEnum.ZX_SUPER_PLATFORM_STATUS.getCode(),
                    TyslMessageTypeEnum.ZX_SUB_PLATFORM_STATUS.getCode(), msgType, request.getBizId());
            return TyslCallbackResponseDTO.builder()
                    .code(500)
                    .msg("消息类型不匹配，需要："
                            + TyslMessageTypeEnum.ZX_SUPER_PLATFORM_STATUS.getCode()
                            + " 或者 " + TyslMessageTypeEnum.ZX_SUPER_PLATFORM_STATUS.getCode()
                            + "，实际：" + msgType
                            + "， 流水号：（" + request.getBizId() + "）")
                    .build();
        }
        log.info("收到中兴上下级平台状态通知，{}， {}", needMessageType, JSON.toJSONString(request.getData()));
        return TyslCallbackResponseDTO.builder()
                .code(200)
                .msg("成功收到消息通知，暂未处理")
                .build();
    }

    @Override
    public TyslCallbackResponseDTO device1400ImageNotify(CallbackDeviceImage1400NotifyDTO request) {
        log.info("收到设备1400图片通知");
        log.info("收到设备1400图片通知，{}", JSON.toJSONString(request));
        return TyslCallbackResponseDTO.builder()
                .code(200)
                .msg("成功收到消息通知，暂未处理")
                .build();
    }

    @Override
    public TyslCallbackResponseDTO cuttingVideoDownloadUrl(CallbackPlaybackCutVideoNotifyDTO request) {
        log.info("收到回看剪辑文件下载地址，{}", JSON.toJSONString(request));
        return TyslCallbackResponseDTO.builder()
                .code(200)
                .msg("成功收到剪辑回调消息通知，暂未处理")
                .build();
    }

    private TyslCallbackResponseDTO checkMessageType(String msgType, TyslMessageTypeEnum needMessageType,
                                                     String bizId) {
        if (!needMessageType.getCode().equals(msgType)) {
            log.info("消息类型不匹配 {}，需要：{}，实际：{}， 流水号：（{}）",
                    needMessageType.getName(),
                    needMessageType.getCode(), msgType, bizId);
            return TyslCallbackResponseDTO.builder()
                    .code(500)
                    .msg("消息类型不匹配 " + needMessageType.getName() + "，需要："
                            + needMessageType.getCode() + "，实际：" + msgType
                            + "， 流水号：（" + bizId + "）")
                    .build();
        }
        return null;
    }

    private void saveStatusChange(CallbackStatusChangeEventNotifyDTO.DeviceEventData data) {
        var status = JSON.parseObject(JSON.toJSONString(data.getData()),
                CallbackStatusChangeEventNotifyDTO.StatusChangeData.class);
        var guid = data.getGuid();
        cameraExtendInfoMapper.updateCameraStatus(TblCameraExtendInfoEntity.builder()
                .guid(guid)
                .status(status.getStatus())
                .onlineState(status.getStatus() >= 1 ? 1 : 0)
                .build());
    }
}
