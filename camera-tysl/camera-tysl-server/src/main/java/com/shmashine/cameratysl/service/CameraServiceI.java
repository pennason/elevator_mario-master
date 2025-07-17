// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service;

import java.util.List;

import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.client.dto.requests.FaultForHistoryPhotoVideoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDevicePlaybackUrlResponseDTO;
import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 16:59
 * @since v1.0
 */

public interface CameraServiceI {

    /**
     * 根据电梯编号， 时间段下载对应的图像视频信息
     *
     * @param request 请求参数
     * @return 结果
     */
    ResponseCustom<String> saveCameraDownloadTask(CamareMediaDownloadRequestDTO request);

    /**
     * 根据任务ID 删除相关记录 和 OSS文件
     *
     * @param id 任务ID
     * @return 结果
     */
    ResponseCustom<String> removeTaskAndOssFile(Long id);

    /**
     * 检测任务并处理未下载的任务
     */
    void checkAndDownloadCameraFile();

    /**
     * 任务状态为 DOWNLOADING 的 查询执行进度并继续流程
     */
    //void checkAndDoNextDownloadingTask();

    /**
     * 任务状态为 WAIT_UPLOAD_OSS 的 查询执行进度并继续流程
     */
    void checkAndDoNextOssUploadTask();

    /**
     * 重试 失败的任务
     */
    void retryFailedCameraFile();

    /**
     * 删除 夜间守护模式 超过一定时间的记录， 仅保留最近的记录
     */
    void deleteNightWatchExpiredRecord();

    /**
     * 获取下载成功的记录
     *
     * @param request 查询条件
     * @return 结果
     */
    ResponseCustom<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            CamaraMediaDownloadBaseRequestDTO request);

    /**
     * 同步所有设备信息
     */
    void syncAllCameraExtendInfo();

    /**
     * 根据电梯编号获取视频回放地址
     *
     * @param request 请求参数
     * @return 结果
     */
    ResponseCustom<TyslDevicePlaybackUrlResponseDTO.PlayBackUrlData> getVideoPlaybackUrl(
            CamareMediaDownloadRequestDTO request);

    /**
     * 根据电梯编号获取视频直播流地址
     *
     * @param elevatorCode 电梯编号
     * @param protoString  协议 rtsp, hls
     * @return 结果
     */
    ResponseCustom<String> getCameraStreamUrl(String elevatorCode, String protoString);

    /**
     * 根据电梯编号获取视频直播流截图
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    ResponseCustom<String> liveSnapshot(String elevatorCode);

    /**
     * 根据电梯编号获取视频直播流录像
     *
     * @param elevatorCode  电梯编号
     * @param duringSeconds 持续时长
     * @return 结果
     */
    ResponseCustom<String> liveRecord(String elevatorCode, Long duringSeconds);

    /**
     * 获取故障时的照片
     *
     * @param request 请求参数
     * @return 结果
     */
    ResponseCustom<String> downloadPictureFile(FaultForHistoryPhotoVideoRequestDTO request);

    /**
     * 获取故障时的视频
     *
     * @param request 请求参数
     * @return 结果
     */
    ResponseCustom<String> downloadFaultVideoFile(FaultForHistoryPhotoVideoRequestDTO request);

    /**
     * 获取语音对讲 WSS地址
     *
     * @param elevatorCode 电梯编号
     * @param domain       1:域名 0：IP+端口
     * @return 结果
     */
    ResponseCustom<Object> getVoiceWssInfo(String elevatorCode, Integer domain);

    /**
     * 获取语音对讲鉴权Token
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    ResponseCustom<String> getVoiceStreamToken(String elevatorCode);

    /**
     * 删除下载成功的本地文件
     */
    void deleteDownloadSuccessfulLocalFile();

}
