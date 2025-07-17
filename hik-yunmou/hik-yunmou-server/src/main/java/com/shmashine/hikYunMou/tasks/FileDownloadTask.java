package com.shmashine.hikYunMou.tasks;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.hikYunMou.entity.HikCameraVideoFileDownloadTask;
import com.shmashine.hikYunMou.handle.HiKPlatformHandle;
import com.shmashine.hikYunMou.service.HikCameraVideoFileDownloadTaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  jiangheng
 * @version 2022/11/10 16:56
 * @description: com.shmashine.hikvPlatform.Task
 */
@Slf4j
@Component
@Profile({"prod"})
public class FileDownloadTask {


    @Autowired
    private HikCameraVideoFileDownloadTaskService videoFileDownloadTaskService;

    @Autowired
    private HiKPlatformHandle hiKPlatformHandle;

    /**
     * 文件下载任务 每分钟一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void downloadTask() {

        //获取待下载及下载失败任务
        List<HikCameraVideoFileDownloadTask> downloadTasks = videoFileDownloadTaskService.queryDownloadtaskList();

        for (HikCameraVideoFileDownloadTask downloadTask : downloadTasks) {

            //下载视频
            if (downloadTask.getFileType() == 1) {
                hiKPlatformHandle.reDownloadVideo(downloadTask);
            }

            if (downloadTask.getFileType() == 0) {
                hiKPlatformHandle.reDownloadPicFile(downloadTask);
            }

        }

    }

}
