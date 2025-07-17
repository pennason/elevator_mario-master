// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.shmashine.api.service.camera.TblCameraImageIdentifyServiceI;
import com.shmashine.api.task.ElectricBikeIdentifyTask;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/17 9:09
 * @since v1.0
 */

@Profile("prod")
@SpringBootTest
public class ElectricBikeIdentifyTaskTest {

    @Autowired
    private ElectricBikeIdentifyTask electricBikeIdentifyTask;
    @Resource
    private TblCameraImageIdentifyServiceI imageIdentifyService;

    @Test
    public void testElectricBikeIdentifyTask() {
        System.out.println("testElectricBikeIdentifyTask");
        var message = "{\"carStatus\":1,\"stype\":\"status\",\"nowStatus\":0,\"battery\":96,\"type\":\"Monitor\",\"version\":1,\"speed\":0.3,\"doorLoop\":0,\"elevatorCode\":\"MX3750\",\"droopClose2\":0,\"powerStatus\":0,\"safeLoop\":0,\"carroofAccidentShift\":0,\"droopClose\":0,\"sensorType\":\"CarRoof\",\"temperature\":39.0,\"stopOutLockArea\":0,\"hasPeople\":1,\"modeStatus\":0,\"time\":\"2023-10-16 18:32:40\",\"floor\":\"1\",\"floorStatus\":0,\"runStatus\":1,\"direction\":1}";
        electricBikeIdentifyTask.doElectricBikeIdentifyTask(message);
    }

    @Test
    public void testCameraInfo() {
        var identifyEntity = imageIdentifyService.getById(1L);
        electricBikeIdentifyTask.doCameraDownloadImage(identifyEntity);
    }
}
