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
 * @version v1.0  -  2023/7/7 18:24
 * @since v1.0
 */


@ActiveProfiles("test")
@DisplayName("上海翼智测试New")
@SpringBootTest
public class SenderShanghaiYizhiNewTest {
    @Autowired
    CityTool cityTool;
    @Autowired
    MonitorHandle monitorHandle;
    @Autowired
    FaultHandle faultHandle;


    @Test
    public void testSendFault() {
        var message = "{\"ST\":\"add\",\"TY\":\"Fault\",\"elevatorCode\":\"MX5576\","
                + "\"faultId\":\"8453996707360735232\",\"faultName\":\"电动车乘梯\",\"fault_type\":\"37\","
                + "\"monitorMessage\":{\"battery\":0,\"carStatus\":1,\"carroofAccidentShift\":0,\"direction\":0,"
                + "\"doorLoop\":10,\"droopClose\":0,\"droopClose2\":0,\"floor\":\"1\",\"floorStatus\":0,"
                + "\"hasPeople\":0,\"modeStatus\":0,\"nowStatus\":9,\"powerStatus\":1,\"runStatus\":0,"
                + "\"safeLoop\":0,\"speed\":0.0,\"stopOutLockArea\":0,\"stype\":\"status\",\"type\":\"Monitor\","
                + "\"version\":1},\"sensorType\":\"SINGLEBOX\",\"time\":\"2024-03-07 16:28:16\","
                + "\"uncivilizedBehaviorFlag\":1}";

        /*var messageData = JSON.parseObject(message, FaultMessage.class);
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
