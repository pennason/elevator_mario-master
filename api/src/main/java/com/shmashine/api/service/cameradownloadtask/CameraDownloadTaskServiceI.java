// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.cameradownloadtask;

import java.util.List;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/14 11:09
 * @since v1.0
 */

public interface CameraDownloadTaskServiceI {

    /**
     * 根据电梯编号，获取最近的夜间守护记录（成功获取到视频的）
     *
     * @param elevatorCode 电梯编号
     * @return 列表
     */
    List<TblCameraDownloadTaskEntity> listRecentNightWatchSuccess(String elevatorCode);

    /**
     * 根据查询条件获取 群租相关记录
     *
     * @param entity 查询条件
     * @return 列表
     */
    List<TblCameraDownloadTaskEntity> listGroupLeasingSuccess(TblCameraDownloadTaskEntity entity);

    /**
     * 查询相关成功的下载记录
     *
     * @param request 查询条件
     * @return 结果
     */
    List<TblCameraDownloadTaskEntity> listSuccessDownloadRecords(CamaraMediaDownloadBaseRequestDTO request);
}
