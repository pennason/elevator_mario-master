package com.shmashine.camera.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.camera.service.CameraServer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Dean Winchester
 */
@Slf4j
@Profile({"prod"})
@EnableScheduling
@EnableAsync
@Component
public class CameraTask {

    @Autowired
    private CameraServer cameraServer;

    /**
     * 定时处理 请求雄迈去拉取视频 actionType：1表示 请求雄迈
     */
    @Async
    @Scheduled(fixedDelay = 10000, initialDelay = 60000)
    public void automaticRequestXmServer() {
        cameraServer.requestXmTask();
    }

    /**
     * 定时处理 请求雄迈去拉取视频 actionType：2表示 雄迈响应处理 文件上传
     */
    @Async
    @Scheduled(fixedDelay = 300000)
    public void automaticVideo() {
        cameraServer.responeXmTask();
    }

    /**
     * 每天凌晨执行一次，清理下载中超过一天的视频0 0 0 1/1 * ?
     */
    @Async
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void autoClearnDownloadingVideo() {
        cameraServer.autoClearnDownloadingVideo();
    }

    /**
     * 平层困人故障——重新下载取证失败的照片（10s执行一次）
     */
    @Async
    @Scheduled(cron = "0/10 * * * * ?")
    public void afreshDownloadImage() {
        cameraServer.afreshDownloadImage();
    }

    /**
     * 重新调取二次识别
     */
    @Async
    @Scheduled(cron = "0/10 * * * * ?")
    public void afreshGetRecognitionResult() {
        cameraServer.afreshGetRecognitionResult();
    }

}
