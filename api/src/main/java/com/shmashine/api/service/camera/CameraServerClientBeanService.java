// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.camera;

import org.springframework.http.ResponseEntity;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/12 11:21
 * @since v1.0
 */


public interface CameraServerClientBeanService {

    /**
     * 下载摄像头历史视频或图片
     *
     * @param entity 请求参数
     * @return ResponseEntity<String>
     */
    ResponseEntity<String> downloadCameraFileByElevatorCode(CamareMediaDownloadRequestDTO entity);

    /**
     * 根据电梯编号获取一张实时图片
     *
     * @param elevatorCode 电梯编号
     * @return 图片地址
     */
    ResponseResult getLivePictureByElevatorCode(String elevatorCode);

}
