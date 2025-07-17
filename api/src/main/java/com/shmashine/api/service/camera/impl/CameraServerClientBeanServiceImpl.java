// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.camera.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shmashine.api.dao.TblCameraDao;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.camera.CameraServerClientBeanService;
import com.shmashine.cameratysl.client.RemoteCameraTyslClient;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.hikYunMou.client.RemoteHikCloudClient;
import com.shmashine.hkcamerabyys.client.RemoteHikEzvizClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/12 11:21
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraServerClientBeanServiceImpl implements CameraServerClientBeanService {
    private final TblCameraDao tblCameraDao;
    private final RemoteHikCloudClient remoteHikCloudClient;
    private final RemoteHikEzvizClient remoteHikEzvizClient;
    private final RemoteCameraTyslClient remoteCameraTyslClient;

    @Override
    public ResponseEntity<String> downloadCameraFileByElevatorCode(CamareMediaDownloadRequestDTO entity) {
        var camera = tblCameraDao.getCameraInfoByElevatorCode(entity.getElevatorCode());
        if (camera == null || !StringUtils.hasText(camera.getCloudNumber())) {
            return ResponseEntity.ok("未获取到摄像头相关信息！");
        }
        if (CameraTypeEnum.HIK_EZVIZ.getCode().equals(camera.getCameraType())) {
            return remoteHikEzvizClient.downloadCameraFileByElevatorCode(entity);
        }
        if (CameraTypeEnum.HIK_CLOUD.getCode().equals(camera.getCameraType())) {
            return remoteHikCloudClient.downloadCameraFileByElevatorCode(entity);
        }
        if (CameraTypeEnum.TYBD.getCode().equals(camera.getCameraType())
                || CameraTypeEnum.TYYY.getCode().equals(camera.getCameraType())) {
            var res = remoteCameraTyslClient.downloadCameraFileByElevatorCode(entity);
            if (HttpStatus.OK.value() == res.getCode()) {
                return ResponseEntity.ok(res.getData());
            }
            return ResponseEntity.ok(res.getMessage());
        }
        return ResponseEntity.ok("不支持的摄像头类型！");
    }

    @Override
    public ResponseResult getLivePictureByElevatorCode(String elevatorCode) {

        var camera = tblCameraDao.getCameraInfoByElevatorCode(elevatorCode);
        if (camera == null || !StringUtils.hasText(camera.getCloudNumber())) {
            return ResponseResult.error("未获取到摄像头相关信息！");
        }

        if (CameraTypeEnum.HIK_EZVIZ.getCode().equals(camera.getCameraType())) {

            ResponseEntity res = remoteHikEzvizClient.getLivePictureByElevatorCode(elevatorCode);

            if (HttpStatus.OK.equals(res.getStatusCode())) {
                return ResponseResult.successObj(res.getBody());
            }
            return ResponseResult.error(res.getBody().toString());
        }

        if (CameraTypeEnum.TYBD.getCode().equals(camera.getCameraType())
                || CameraTypeEnum.TYYY.getCode().equals(camera.getCameraType())) {

            var res = remoteCameraTyslClient.liveSnapshot(elevatorCode);

            if (HttpStatus.OK.value() == res.getCode()) {
                return ResponseResult.successObj(res.getData());
            }
            return ResponseResult.error(res.getMessage());

        }

        return ResponseResult.error("不支持的摄像头类型！");
    }

}
