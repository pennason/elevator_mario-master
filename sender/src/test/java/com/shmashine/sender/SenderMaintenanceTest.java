// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.platform.city.tools.CityTool;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/7 18:24
 * @since v1.0
 */


@ActiveProfiles("test")
@DisplayName("维保平台测试")
@SpringBootTest
public class SenderMaintenanceTest {
    @Autowired
    CityTool cityTool;
    @Autowired
    MonitorHandle monitorHandle;
    @Autowired
    FaultHandle faultHandle;

    @Test
    public void testSendProject() {
        var elevatorCode = "MX3737";
        var ptCode = "maintenance";
        //cityTool.sendProjectDataToGovern(elevatorCode, ptCode);
    }

    @Test
    public void testSaveElevator() {
        var elevatorCode = "MX3737";
        var ptCode = "maintenance";
        //var elevator = cityTool.checkAndInitElevator(elevatorCode, ptCode);
        //System.out.println(JSON.toJSONString(elevator));
    }

    @Test
    public void testRegisterElevator() {
        var elevatorCode = "MX3737";
        var ptCode = "maintenance";
        //cityTool.sendRegisterElevatorToGovern(elevatorCode, ptCode);
    }

    @Test
    public void testSendRunning() {
        var message = "{\"ST\":\"S\",\"TY\":\"M\",\"elevatorCode\":\"MX3737\","
                + "\"messageJson\":{\"elevatorCode\":\"MX3737\",\"ST\":\"S\","
                + "\"D\":\"nmkKZQEAAAAAAAAAAQAAAAABABs=\",\"TY\":\"M\",\"sensorType\":\"CarRoof\","
                + "\"time\":\"2023-09-20 11:40:13\"},\"monitorMessage\":{\"battery\":0,\"carStatus\":0,"
                + "\"carroofAccidentShift\":0,\"direction\":0,\"doorLoop\":0,\"doorStatus\":1,"
                + "\"driveStatus\":1,\"droopClose\":1,\"droopClose2\":0,\"floor\":\"1\",\"floorStatus\":0,"
                + "\"hasPeople\":0,\"modeStatus\":0,\"nowStatus\":0,\"powerStatus\":0,\"runStatus\":0,"
                + "\"safeLoop\":0,\"sensorType\":\"CarRoof\",\"speed\":0.0,\"stopOutLockArea\":0,"
                + "\"stype\":\"status\",\"temperature\":20.0,\"type\":\"Monitor\",\"version\":1},"
                + "\"requestId\":\"\",\"sensorType\":\"CarRoof\",\"time\":\"2023-09-20 11:40:13\"}";
        //var messageData = JSONObject.parseObject(message, MessageData.class);
        //monitorHandle.dispatch(messageData);
    }

    //@Test
    public void testSendFault() {
        var message = "";
        var messageData = JSONObject.parseObject(message, FaultMessage.class);
        if ("add".equals(messageData.getST())) {
            // 运行数据消息
            JSONObject messageJson = JSONObject.parseObject(message);
            if (messageJson.containsKey("D")) {
                MonitorMessage monitorMessage = new MonitorMessage();
                monitorMessage.setFromBase64(messageJson);
                messageData.setMonitorMessage(monitorMessage);
            }
            System.out.println(JSON.toJSONString(messageData));
        }
        faultHandle.dispatch(messageData);
    }
}
