// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.service;

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
    ResponseEntity<String> saveCameraDownloadTask(CamareMediaDownloadRequestDTO request);

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
    ResponseEntity<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            CamaraMediaDownloadBaseRequestDTO request);


    /**
     * 同步海康云眸平台的摄像头 国际级联数据
     *
     * @return 受影响行数
     */
    Integer syncCascadeChannelToDb();
}
