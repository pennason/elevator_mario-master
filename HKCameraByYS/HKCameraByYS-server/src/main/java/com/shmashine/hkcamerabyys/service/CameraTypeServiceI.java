// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.service;

import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.hkcamerabyys.entity.ResponseCustom;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:35
 * @since v1.0
 */

public interface CameraTypeServiceI {

    /**
     * 获取视频OR图片
     *
     * @param entity 请求参数
     */
    void downloadVideoOrImage(TblCameraDownloadTaskEntity entity);

    /**
     * 根据远程任务ID获取执行状态，如果已生成文件，则继续下一步
     *
     * @param task 任务信息
     */
    void doDownloadingCameraFile(TblCameraDownloadTaskEntity task);

    /**
     * 更新摄像头在线状态
     *
     * @param cloudNumber 云台编号
     * @return 结果
     */
    ResponseCustom renewCameraOnlineStatus(String cloudNumber);
}
