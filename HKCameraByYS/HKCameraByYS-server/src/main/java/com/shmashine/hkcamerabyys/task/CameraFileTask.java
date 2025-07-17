// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.hkcamerabyys.service.CameraServiceI;

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
@Profile("prod")
@Component
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
     * 状态是 DOWNLOADING 的 任务 后台获取任务状态并更新
     */
    @Scheduled(fixedDelay = 90000, initialDelay = 15000)
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
     * 重试任务列表中的任务-一分钟一次-失败0~5次
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 25000)
    public void retryDownloadCameraFileTask1() {
        log.info("Scheduled-retryDownloadCameraFileTask");
        cameraService.retryFailedCameraFile(0, 5);
    }

    /**
     * 重试任务列表中的任务-十分钟一次-失败5~10次
     */
    @Scheduled(fixedDelay = 600000, initialDelay = 26000)
    public void retryDownloadCameraFileTask2() {
        log.info("Scheduled-retryDownloadCameraFileTask");
        cameraService.retryFailedCameraFile(5, 10);
    }


    /**
     * 重试任务列表中的任务-三十分钟一次-失败10~100次
     */
    @Scheduled(fixedDelay = 1800000, initialDelay = 27000)
    public void retryDownloadCameraFileTask3() {
        log.info("Scheduled-retryDownloadCameraFileTask");
        cameraService.retryFailedCameraFile(10, 100);
    }

    /**
     * 清理守夜模式过期的记录
     */
    @Scheduled(cron = "0 5 2 * * ? ")
    public void deleteNightWatchExpiredRecord() {
        log.info("Scheduled-deleteNightWatchExpiredRecord");
        cameraService.deleteNightWatchExpiredRecord();
    }

}
