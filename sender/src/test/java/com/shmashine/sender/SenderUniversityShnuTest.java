// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shmashine.sender.message.handle.FaultHandle;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.platform.city.tools.CityTool;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/27 18:24
 * @since v1.0
 */


@ActiveProfiles("test")
@DisplayName("上师大测试")
@SpringBootTest
public class SenderUniversityShnuTest {
    @Autowired
    CityTool cityTool;
    @Autowired
    MonitorHandle monitorHandle;
    @Autowired
    FaultHandle faultHandle;

    @Test
    public void testRegisterElevator() {
        var elevatorCode = "MX5165";
        var ptCode = "shanghaiYizhi";
        //cityTool.sendRegisterElevatorToGovern(elevatorCode, ptCode);
    }

    @Test
    public void testSendRunning() {
        var message = "{\"ST\":\"S\",\"TY\":\"M\",\"elevatorCode\":\"MX5165\","
                + "\"messageJson\":{\"elevatorCode\":\"MX5165\",\"ST\":\"S\","
                + "\"D\":\"Tu3WZAEAAAAAAAEBDAAAAAEBAB8ghbo=\",\"TY\":\"M\",\"sensorType\":\"SINGLEBOX\","
                + "\"time\":\"2023-08-12 10:24:15\"},\"monitorMessage\":{\"battery\":0,\"carStatus\":0,"
                + "\"carroofAccidentShift\":0,\"direction\":0,\"doorLoop\":0,\"doorStatus\":1,\"driveStatus\":1,"
                + "\"droopClose\":1,\"droopClose2\":1,\"floor\":\"12\",\"floorStatus\":0,\"hasPeople\":1,"
                + "\"modeStatus\":0,\"nowStatus\":0,\"powerStatus\":1,\"runStatus\":1,\"safeLoop\":0,"
                + "\"sensorType\":\"SINGLEBOX\",\"speed\":0.0,\"stopOutLockArea\":0,\"stype\":\"status\","
                + "\"temperature\":31.0,\"type\":\"Monitor\",\"version\":1},\"requestId\":\"\","
                + "\"sensorType\":\"SINGLEBOX\",\"time\":\"2023-08-12 10:24:15\"}";
        //var messageData = JSONObject.parseObject(message, MessageData.class);
        //monitorHandle.dispatch(messageData);
    }

    @Test
    public void testSendFault() {
        var message = "{\"faultName\":\"开门走车\",\"elevatorCode\":\"MX5150\",\"ST\":\"add\","
                + "\"uncivilizedBehaviorFlag\":0,\"D\":\"Ze+sZAEQAAH9AAABAwAAAAABAB4AAGE=\",\"TY\":\"Fault\","
                + "\"sensorType\":\"SINGLEBOX\",\"fault_type\":5,\"faultId\":\"8466816331685494785\","
                + "\"time\":\"2023-08-27 02:44:34\"}";

        /*var messageData = JSONObject.parseObject(message, FaultMessage.class);
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
        faultHandle.dispatch(messageData);*/
    }
}
