// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server.fault.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblSenderFaultEntity;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.sender.dao.TblCameraDownloadTaskMapper;
import com.shmashine.sender.dao.TblSenderFaultMapper;
import com.shmashine.sender.dao.TblSysFileMapper;
import com.shmashine.sender.platform.city.tools.FaultResendTool;
import com.shmashine.sender.server.fault.SendFaultService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 17:08
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SendFaultServiceImpl implements SendFaultService {
    private final FaultResendTool faultResendTool;
    private final TblSenderFaultMapper senderFaultMapper;
    private final TblSysFileMapper sysFileMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;

    @Override
    public ResponseEntity<String> checkAndSendFault(String faultId, Integer cameraType) {
        var senderFaultEntity = senderFaultMapper.getByFaultId(faultId);
        if (null == senderFaultEntity) {
            log.info("故障不需要重发到相关城市平台(无记录) {}, {}", faultId, cameraType);
            return ResponseEntity.ok("故障不需要重发到相关城市平台");
        }
        log.info("checkAndSendFault {}, {} step 1", faultId, cameraType);
        var needSend = extendSendFaultEntity(faultId, senderFaultEntity);
        if (!needSend) {
            log.info("checkAndSendFault 故障不需要重发到相关城市平台（无下载） {}, {}", faultId, senderFaultEntity.getElevatorCode());
            return ResponseEntity.ok("故障不需要重发到相关城市平台");
        }
        log.info("checkAndSendFault {}, {} step 2", faultId, senderFaultEntity.getElevatorCode());
        senderFaultMapper.update(senderFaultEntity);

        // 重新发送故障
        var res = faultResendTool.sendFault(senderFaultEntity);
        log.info("checkAndSendFault 重发故障到相关城市平台 {}, {}， {}", faultId, cameraType, JSON.toJSONString(res));
        return ResponseEntity.ok(JSON.toJSONString(res));
    }

    private Boolean extendSendFaultEntity(String faultId, TblSenderFaultEntity senderFaultEntity) {
        // 从 tbl_camera_download_task 中查询是否有对应记录，有则直接使用
        var cameraDownloadTaskList = cameraDownloadTaskMapper.listByTaskCustomId(faultId);
        if (!CollectionUtils.isEmpty(cameraDownloadTaskList)) {
            log.info("checkAndSendFault {}, {} step 1-1", faultId, senderFaultEntity.getElevatorCode());
            cameraDownloadTaskList.forEach(item -> {
                var fileType = CameraMediaTypeEnum.getFileTypeByMediaType(item.getMediaType());
                if ("image".equals(fileType)) {
                    senderFaultEntity.setNeedPhoto(1);
                    senderFaultEntity.setUrlPhoto(item.getOssUrl());
                } else {
                    senderFaultEntity.setNeedVideo(1);
                    senderFaultEntity.setUrlVideo(item.getOssUrl());
                }
            });
            return true;
        }

        // sys file 中查询当前故障的 视频， 并更新 sender_fault 表，同时重新发送fault到对应政府平台
        var sysFileList = sysFileMapper.getFileListByFaultId(faultId);
        if (!CollectionUtils.isEmpty(sysFileList)) {
            log.info("checkAndSendFault {}, {} step 1-2", faultId, senderFaultEntity.getElevatorCode());
            sysFileList.forEach(item -> {
                if ("0".equals(item.getVFileType())) {
                    senderFaultEntity.setNeedPhoto(1);
                    senderFaultEntity.setUrlPhoto(item.getVUrl());
                } else if ("1".equals(item.getVFileType())) {
                    senderFaultEntity.setNeedVideo(1);
                    senderFaultEntity.setUrlVideo(item.getVUrl());
                }
            });
            return true;
        }
        log.info("checkAndSendFault {}, {} step 1-3", faultId, senderFaultEntity.getElevatorCode());
        return false;
    }
}
