// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:23
 * @since v1.0
 */

public interface CameraServiceI {

    /**
     * 根据电梯编号， 时间段下载对应的图像视频信息
     *
     * @param request 请求参数
     * @return 结果
     */
    void saveCameraDownloadTask(CamareMediaDownloadRequestDTO request);

    /**
     * 获取摄像头厂的bean
     *
     * @param cameraType 摄像头类型
     * @return bean
     */
    CameraTypeServiceI getCameraTypeBean(Integer cameraType);

    /**
     * 根据任务ID 删除相关记录 和 OSS文件
     *
     * @param id 任务ID
     * @return 结果
     */
    ResponseEntity<String> removeTaskAndOssFile(Long id);

    /**
     * 检测任务并处理未下载的任务
     */
    void checkAndDownloadCameraFile();

    /**
     * 任务状态为 DOWNLOADING 的 查询执行进度并继续流程
     */
    void checkAndDoNextDownloadingTask();

    /**
     * 任务状态为 WAIT_UPLOAD_OSS 的 查询执行进度并继续流程
     */
    void checkAndDoNextOssUploadTask();

    /**
     * 重试 失败的任务
     */
    void retryFailedCameraFile(int start, int end);

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
    ResponseEntity<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            CamaraMediaDownloadBaseRequestDTO request);

    /**
     * 手动触发 摄像头在线状态更新
     *
     * @return 结果
     */
    ResponseEntity<String> renewCameraOnlineStatus();

    /**
     * 开始OSS上传任务
     *
     * @param taskId 任务ID
     */
    void startOssUploadCameraFileByTaskId(String taskId);

    /**
     * 云录制失败
     *
     * @param taskId    任务ID
     * @param errorCode 错误码
     * @param errorMsg  错误原因
     */
    void cloudRecordingFailed(String taskId, String errorCode, String errorMsg);

    /**
     * 根据电梯编号获取摄像头在线状态
     *
     * @param elevatorCode 电梯编号
     * @return 在线状态 0：离线，1：在线
     */
    Integer getCameraOnlineStatusByElevatorCode(String elevatorCode);
}
