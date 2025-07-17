// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.service.impl;

import org.springframework.stereotype.Component;

import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.hkcamerabyys.entity.ResponseCustom;
import com.shmashine.hkcamerabyys.service.CameraTypeServiceI;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/10 9:50
 * @since v1.0
 */

@Component
public class CameraTypeServiceImplXiongmai implements CameraTypeServiceI {

    @Override
    public void downloadVideoOrImage(TblCameraDownloadTaskEntity entity) {

    }

    @Override
    public void doDownloadingCameraFile(TblCameraDownloadTaskEntity task) {

    }

    @Override
    public ResponseCustom renewCameraOnlineStatus(String cloudNumber) {
        return null;
    }
}
