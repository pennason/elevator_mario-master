// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cn.hutool.core.date.DateUtil;

import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.sender.message.handle.MonitorHandle;
import com.shmashine.sender.platform.city.NanjingSender;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/20 18:24
 * @since v1.0
 */

@SpringBootTest
public class SenderNanjingTest {

    @Autowired
    NanjingSender nanjingSender;
    @Autowired
    MonitorHandle monitorHandle;

    // CHECKSTYLE:OFF
    //@Test
    public void testNanjingSender() {
        var monitorMessage = new MonitorMessage() {
            {
                setType("Monitor");
                setStype("status");
                setSensorType("SINGLEBOX");
                setVersion(1);
                setNowStatus(0);
                setModeStatus(0);
                setBattery(100);
                setRunStatus(0);
                setDirection(0);
                setFloor("1");
                setFloorStatus(0);
                setDroopClose(1);
                setDroopClose2(1);
                setHasPeople(0);
                setCarStatus(0);
                setPowerStatus(0);
                setSpeed(0.0f);
                setTemperature(0.0f);
                setDoorStatus(1);
                setDriveStatus(1);
                setSafeLoop(0);
                setDoorLoop(0);
                setStopOutLockArea(0);
                setCarroofAccidentShift(0);
            }
        };

        var messageData = new MessageData() {
            {
                setTime(DateUtil.now());
                setElevatorCode("MX5263");
                setSensorType("SINGLEBOX");
                setTY("Monitor");
                setST("start");
                setMonitorMessage(monitorMessage);
            }
        };
        //nanjingSender.handleMonitor(messageData);
    }
    // CHECKSTYLE:ON

    //@Test
    public void testMonitorHandler() {
        var msg = "{\"ST\":\"S\",\"TY\":\"M\",\"elevatorCode\":\"MX5264\","
                + "\"messageJson\":{\"elevatorCode\":\"MX5264\",\"ST\":\"S\","
                + "\"D\":\"5euYZAEAAAAAAAAAAQAAAAABACwAAAA=\",\"TY\":\"M\",\"sensorType\":\"SINGLEBOX\","
                + "\"time\":\"2023-06-26 09:37:41\"},\"monitorMessage\":{\"battery\":0,\"carStatus\":0,"
                + "\"carroofAccidentShift\":0,\"direction\":0,\"doorLoop\":0,\"doorStatus\":1,"
                + "\"driveStatus\":1,\"droopClose\":1,\"droopClose2\":1,\"floor\":\"1\",\"floorStatus\":0,"
                + "\"hasPeople\":0,\"modeStatus\":0,\"nowStatus\":0,\"powerStatus\":0,\"runStatus\":0,"
                + "\"safeLoop\":0,\"sensorType\":\"SINGLEBOX\",\"speed\":0.0,\"stopOutLockArea\":0,"
                + "\"stype\":\"status\",\"temperature\":44.0,\"type\":\"Monitor\",\"version\":1},"
                + "\"requestId\":\"\",\"sensorType\":\"SINGLEBOX\",\"time\":\"2023-06-26 11:31:41\"}";
        //monitorHandle.handle(msg);
        //var messageData = JSONObject.parseObject(msg, MessageData.class);
        //monitorHandle.dispatch(messageData);
    }
}
