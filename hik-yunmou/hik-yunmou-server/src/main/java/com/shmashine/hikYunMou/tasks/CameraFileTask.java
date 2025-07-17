// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.hikYunMou.service.CameraServiceI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 17:46
 * @since v1.0
 */

@Slf4j
@Component
@Profile({"prod"})
@EnableScheduling
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraFileTask {
    private final CameraServiceI cameraService;

    /**
     * 执行任务列表中的任务
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 10000)
    public void downloadCameraFileTask() {
        log.info("Scheduled-downloadCameraFileTask");
        cameraService.checkAndDownloadCameraFile();
    }

    /**
     * 状态是 DOWNLOADING 的 任务 后台获取任务状态并更新，
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 15000)
    public void checkAndDoNextDownloadingTask() {
        log.info("Scheduled-checkAndDoNextDownloadingTask");
        cameraService.checkAndDoNextDownloadingTask();
    }

    /**
     * 状态为 WAIT_UPLOAD_OSS 的任务后台更新到OSS上
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 20000)
    public void checkAndDoNextOssUploadTask() {
        log.info("Scheduled-checkAndDoNextOssUploadTask");
        cameraService.checkAndDoNextOssUploadTask();
    }

    /**
     * 执行任务列表中的任务
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 25000)
    public void retryDownloadCameraFileTask() {
        log.info("Scheduled-retryDownloadCameraFileTask");
        cameraService.retryFailedCameraFile();
    }

    /**
     * 清理守夜模式过期的记录
     */
    @Scheduled(cron = "0 5 2 * * ? ")
    public void deleteNightWatchExpiredRecord() {
        log.info("Scheduled-deleteNightWatchExpiredRecord");
        cameraService.deleteNightWatchExpiredRecord();
    }

    /**
     * 国标级联同步
     */
    @Scheduled(cron = "0 25 7,9,11,13,15,17,19 * * ? ")
    public void syncCascadeChannelToDb() {
        log.info("Scheduled-syncCascadeChannelToDb");
        var total = cameraService.syncCascadeChannelToDb();
        log.info("Scheduled-syncCascadeChannelToDb finished with result {}", total);
    }

}
