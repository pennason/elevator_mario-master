// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.cameratysl.service.CameraServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 17:30
 * @since v1.0
 */

@Slf4j
@Component
@Profile({"prod", "local"})
@EnableScheduling
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TyslTask {
    private final CameraServiceI cameraService;

    /**
     * 获取设备列表， 包括增删改
     */
    @Scheduled(fixedDelay = 3600000, initialDelay = 60000)
    public void tyslDeviceListSync() {
        log.info("同步天翼视联设备列表");
        cameraService.syncAllCameraExtendInfo();
    }
}
