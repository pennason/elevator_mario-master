// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.cameratysl.client.dto.callback.CallbackDeviceImage1400NotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackHeartbeatEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackPlaybackCutVideoNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackStatusChangeEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackZxPlatformStatusEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.TyslCallbackResponseDTO;
import com.shmashine.cameratysl.service.CallbackNotifyServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 10:13
 * @since v1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/tysl-callback")
@Tag(name = "天翼视联回调CallbackNotifyController", description = "天翼视联数据推送通知 开发者：chenxue")
public class CallbackNotifyController {
    private final CallbackNotifyServiceI service;

    @Operation(summary = "设备事件通知")
    @PostMapping("/device-event-notify")
    public TyslCallbackResponseDTO deviceEventNotify(@RequestBody CallbackStatusChangeEventNotifyDTO request) {
        log.info("callback-deviceEventNotify url {} request:{}", "/tysl-callback/device-event-notify", request);
        return service.deviceEventNotify(request);
    }

    @Operation(summary = "心跳存活通知")
    @PostMapping("/heartbeat-notify")
    public TyslCallbackResponseDTO heartbeatNotify(@RequestBody CallbackHeartbeatEventNotifyDTO request) {
        log.info("callback-heartbeatNotify url {} request:{}", "/tysl-callback/heartbeat-notify", request);
        return service.heartbeatNotify(request);
    }

    @Operation(summary = "中兴上下级平台状态通知")
    @PostMapping("/zx-platform-status-notify")
    public TyslCallbackResponseDTO zxPlatformStatusNotify(@RequestBody CallbackZxPlatformStatusEventNotifyDTO request) {
        log.info("callback-zxPlatformStatusNotify url {} request:{}",
                "/tysl-callback/zx-platform-status-notify", request);
        return service.zxPlatformStatusNotify(request);
    }

    @Operation(summary = "设备1400图⽚通知")
    @PostMapping("/device-1400-image-notify")
    public TyslCallbackResponseDTO device1400ImageNotify(@RequestBody CallbackDeviceImage1400NotifyDTO request) {
        log.info("callback-device1400ImageNotify url {} request:{}",
                "/tysl-callback/device-1400-image-notify", request);
        return service.device1400ImageNotify(request);
    }

    @Operation(summary = "获取云回看剪辑⽂件下载地址")
    @PostMapping("/cutting-video-download-url")
    public TyslCallbackResponseDTO cuttingVideoDownloadUrl(@RequestBody CallbackPlaybackCutVideoNotifyDTO request) {
        log.info("callback-cuttingVideoDownloadUrl url {} request:{}",
                "/tysl-callback/cutting-video-download-url", request);
        return service.cuttingVideoDownloadUrl(request);
    }


}
