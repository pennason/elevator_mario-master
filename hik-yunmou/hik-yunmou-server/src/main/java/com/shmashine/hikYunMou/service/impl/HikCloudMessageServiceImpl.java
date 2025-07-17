// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;

import com.shmashine.common.entity.CameraStatusRecordEntity;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.enums.CameraDownloadFileStatusEnum;
import com.shmashine.hikYunMou.client.dto.HikCloudMessageResponseDTO;
import com.shmashine.hikYunMou.dao.CameraStatusRecordMapper;
import com.shmashine.hikYunMou.dao.TblCameraDownloadTaskMapper;
import com.shmashine.hikYunMou.dao.TblCameraMapper;
import com.shmashine.hikYunMou.service.HikCloudMessageService;

import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/17 10:16
 * @since v1.0
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HikCloudMessageServiceImpl implements HikCloudMessageService {
    private final CameraStatusRecordMapper cameraStatusRecordMapper;
    private final TblCameraMapper tblCameraMapper;
    private final TblCameraDownloadTaskMapper tblCameraDownloadTaskMapper;
    private static final String ERROR_CODE_OK = "0";

    @Override
    public void dealDeviceOnOffline(HikCloudMessageResponseDTO.HikMessageInfo hikMessageInfo) {
        var contentString = hikMessageInfo.getContent();
        var content = JSONUtil.toBean(contentString, HikCloudMessageResponseDTO.DeviceOnOffline.class);
        var cameraStatusRecord = CameraStatusRecordEntity.builder()
                .id(IdUtil.getSnowflakeNextId())
                .messageId(hikMessageInfo.getMsgId())
                .serialNumber(content.getSubSerial())
                .occurTime(content.getOccurTime())
                .deviceName(content.getDeviceName())
                .status("ONLINE".equals(content.getMsgType()) ? 1 : 0)
                .createTime(DateTime.now())
                .build();
        cameraStatusRecordMapper.insert(cameraStatusRecord);
        // 更新摄像头表
        tblCameraMapper.updateCameraStatus(cameraStatusRecord);
    }

    @Override
    public void dealCloudVideoRecord(HikCloudMessageResponseDTO.HikMessageInfo hikMessageInfo) {
        var contentString = hikMessageInfo.getContent();
        var content = JSONUtil.toBean(contentString, HikCloudMessageResponseDTO.CloudVideoRecord.class);
        // 当错误码为0且其文件数大于0时表示结果正常，否则异常
        if (ERROR_CODE_OK.equals(content.getErrorCode()) && content.getFileNum() > 0L) {
            // 成功了，可以获取视频文件了
            var entity = TblCameraDownloadTaskEntity.builder()
                    .cloudTaskId(content.getTaskId())
                    .returnCode(Integer.parseInt(content.getErrorCode()))
                    .errMessage(content.getErrorMsg())
                    .fileStatus(CameraDownloadFileStatusEnum.DOWNLOADING.getStatus())
                    .build();
            tblCameraDownloadTaskMapper.updateFileStatusByCloudTaskId(entity);
            return;
        }
        // 失败了，可以根据错误码进行处理
        var entity = TblCameraDownloadTaskEntity.builder()
                .cloudTaskId(content.getTaskId())
                .returnCode(Integer.parseInt(content.getErrorCode()))
                .errMessage(content.getErrorMsg())
                .fileStatus(CameraDownloadFileStatusEnum.REQUEST_FAILED.getStatus())
                .build();
        tblCameraDownloadTaskMapper.updateFileStatusByCloudTaskId(entity);
        tblCameraDownloadTaskMapper.increaseRequestFailedCountByCloudTaskId(content.getTaskId());
    }
}
