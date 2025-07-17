package com.shmashine.hkcamerabyys.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.hkcamerabyys.service.HKCameraByYSService;

/**
 * 海康萤石历史取证下载任务
 *
 * @author jiangheng
 * @version 1.0.0
 */

@Profile("prod")
@Component
@EnableScheduling
public class DownloadTask {

    @Autowired
    private HKCameraByYSService hkCameraByYSService;

    /**
     * 定时下载故障取证文件（3分钟触发一次）
     */
    @Scheduled(fixedDelay = 120000, initialDelay = 10000)
    public void downloadFaultFileTask() {
        hkCameraByYSService.downloadFaultFileTask();
    }

    /**
     * 定时扫描下载失败记录（1分钟触发一次）
     */
    @Scheduled(fixedDelay = 120000, initialDelay = 10000)
    public void retryDownloadFailReportTask() {
        hkCameraByYSService.retryDownloadFailReportTask();
    }

    /**
     * 凌晨今日下载失败重试
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void freeTimeRetryDownloadTask() {
        hkCameraByYSService.freeTimeRetryDownloadTask();
    }
}
