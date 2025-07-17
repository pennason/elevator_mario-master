package com.shmashine.hkcamerabyys.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.hkcamerabyys.client.entity.DownLoadByHKYSRequestBody;

/**
 * HKCameraByYSService
 *
 * @author jiangheng
 * @version v1.0 - 2021/11/5 15:24
 */
public interface HKCameraByYSService {

    /**
     * 海康萤石指定时间段下载视频文件
     *
     * @param downLoadByHKYSRequestBody 下载请求体
     */
    ResponseEntity downloadVideoFile(DownLoadByHKYSRequestBody downLoadByHKYSRequestBody);

    /**
     * 海康萤石下载图片文件
     *
     * @param downLoadByHKYSRequestBody 下载请求体
     */
    ResponseEntity downloadPictureFile(DownLoadByHKYSRequestBody downLoadByHKYSRequestBody);

    /**
     * 下载故障取证文件
     */
    void downloadFaultFileTask();

    /**
     * 下载失败重试
     */
    void retryDownloadFailReportTask();

    /**
     * 取证视频重新下载
     *
     * @param faultId   故障id
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    String videoFileReDownload(String faultId, String startTime, String endTime);

    /**
     * 凌晨重新下载今日取证失败文件
     */
    void freeTimeRetryDownloadTask();

    /**
     * 萤石平台告警通知
     *
     * @param body 请求体
     */
    String ysPlatformNotify(JSONObject body);

    ResponseEntity<HashMap<String, String>> getElevatorPicByElevators(List<String> elevatorCodes);

    /**
     * 根据电梯编号获取一张实时图片
     *
     * @param elevatorCode 电梯编号
     * @return 图片地址
     */
    ResponseEntity<String> getLivePictureByElevatorCode(String elevatorCode);

    /**
     * 摄像头语音下发
     *
     * @param vCloudNumber 摄像头序列号
     * @param faultType    故障类型
     */
    ResponseEntity<String> pushCameraVoice(String vCloudNumber, String faultType);
}
