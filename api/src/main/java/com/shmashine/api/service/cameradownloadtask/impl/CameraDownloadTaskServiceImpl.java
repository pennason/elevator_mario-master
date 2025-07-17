// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.cameradownloadtask.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblCameraDownloadTaskMapper;
import com.shmashine.api.service.cameradownloadtask.CameraDownloadTaskServiceI;
import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.enums.CameraDownloadFileStatusEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/14 11:09
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CameraDownloadTaskServiceImpl implements CameraDownloadTaskServiceI {
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;

    @Override
    public List<TblCameraDownloadTaskEntity> listRecentNightWatchSuccess(String elevatorCode) {
        return cameraDownloadTaskMapper.findByEntity(TblCameraDownloadTaskEntity.builder()
                .elevatorCode(elevatorCode)
                .taskType(CameraTaskTypeEnum.NIGHT_WATCH.getCode())
                .fileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus())
                .build());
    }

    @Override
    public List<TblCameraDownloadTaskEntity> listGroupLeasingSuccess(TblCameraDownloadTaskEntity entity) {
        entity.setTaskType(CameraTaskTypeEnum.GROUP_LEASING.getCode());
        entity.setFileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus());
        return cameraDownloadTaskMapper.findByEntity(entity);
    }

    @Override
    public List<TblCameraDownloadTaskEntity> listSuccessDownloadRecords(CamaraMediaDownloadBaseRequestDTO request) {
        return cameraDownloadTaskMapper.findByEntity(TblCameraDownloadTaskEntity.builder()
                .elevatorCode(request.getElevatorCode())
                .floor(request.getFloor())
                .taskType(request.getTaskType() == null ? null : request.getTaskType().getCode())
                .mediaType(request.getMediaType() == null ? null : request.getMediaType().getMediaType())
                .fileStatus(CameraDownloadFileStatusEnum.SUCCESS.getStatus())
                .build());
    }
}
