// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.cameratysl.service.CameraServiceI;
import com.shmashine.cameratysl.service.impl.CameraServiceImpl;
import com.shmashine.cameratysl.utils.RedisUtils;

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
@Profile({"prod", "local"})
@EnableScheduling
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraFileTask {
    private final CameraServiceI cameraService;
    private final RedisUtils redisUtils;

    /**
     * 执行任务列表中的任务, 缓存触发
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void downloadCameraFileTaskByCache() {
        var hasTask = redisUtils.getSetCacheObject(CameraServiceImpl.REDIS_KEY_NEED_DOWNLOAD_CAMERA_TASK, "0");
        if (hasTask == null || "0".equals(hasTask)) {
            log.info("Scheduled-downloadCameraFileTaskByCache: no task");
            return;
        }
        log.info("Scheduled-downloadCameraFileTaskByCache");
        cameraService.checkAndDownloadCameraFile();
    }

    /**
     * 执行任务列表中的任务， 定时触发
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 10000)
    public void downloadCameraFileTask() {
        log.info("Scheduled-downloadCameraFileTask");
        cameraService.checkAndDownloadCameraFile();
    }

    /**
     * 状态是 DOWNLOADING 的 任务 后台获取任务状态并更新，
     */
    /*@Scheduled(fixedDelay = 60000, initialDelay=15000)
    public void checkAndDoNextDownloadingTask() {
        log.info("Scheduled-checkAndDoNextDownloadingTask");
        cameraService.checkAndDoNextDownloadingTask();
    }*/

    /**
     * 状态为 WAIT_UPLOAD_OSS 的任务后台更新到OSS上
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 20000)
    public void checkAndDoNextOssUploadTask() {
        log.info("Scheduled-checkAndDoNextOssUploadTask");
        cameraService.checkAndDoNextOssUploadTask();
    }

    /**
     * 执行任务列表中的任务, 之前返回状态为300的需要忽略
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
     * 清理已完成状态的本地文件
     */
    @Scheduled(cron = "0 1 0 1/1 * ? ")
    public void deleteDownloadSuccessfulLocalFile() {
        log.info("Scheduled-deleteDownloadSuccessfulLocalFile");
        cameraService.deleteDownloadSuccessfulLocalFile();
    }

}
