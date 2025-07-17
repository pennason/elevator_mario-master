// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.kafka.handler.GroupLeasingHandler;
import com.shmashine.api.kafka.handler.NightWatchHandler;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/2 16:41
 * @since v1.0
 */

@SpringBootTest
public class NightWatchAndGroupLeasingTest {

    @Resource
    private NightWatchHandler nightWatchHandler;
    @Resource
    private GroupLeasingHandler groupLeasingHandler;

    @Test
    public void testKafkaMessage() {
        var message = """
                {"ST":"S","TY":"M","elevatorCode":"MX4000","messageJson":{"elevatorCode":"MX4000","ST":"S","D":"1sfhYwEAAQEOAAEBBAAAAAEBAA+lpaU=","TY":"M","sensorType":"SINGLEBOX","time":"2023-03-02 11:39:03"},"monitorMessage":{"battery":0,"carStatus":0,"carroofAccidentShift":0,"direction":1,"doorLoop":0,"doorStatus":1,"driveStatus":1,"droopClose":1,"droopClose2":1,"floor":"4","floorStatus":0,"hasPeople":1,"modeStatus":0,"nowStatus":0,"powerStatus":1,"runStatus":1,"safeLoop":0,"sensorType":"SINGLEBOX","speed":1.4,"stopOutLockArea":0,"stype":"status","temperature":15.0,"type":"Monitor","version":1},"requestId":"","sensorType":"SINGLEBOX","time":"2023-03-02 11:39:03"}
                """;
        var message2 = """
                {"ST":"S","TY":"M","elevatorCode":"MX4000","messageJson":{"elevatorCode":"MX4000","ST":"S","D":"1sfhYwEAAQEOAAEBBAAAAAEBAA+lpaU=","TY":"M","sensorType":"SINGLEBOX","time":"2023-03-02 11:39:13"},"monitorMessage":{"battery":0,"carStatus":0,"carroofAccidentShift":0,"direction":1,"doorLoop":0,"doorStatus":1,"driveStatus":1,"droopClose":1,"droopClose2":1,"floor":"4","floorStatus":0,"hasPeople":0,"modeStatus":0,"nowStatus":0,"powerStatus":1,"runStatus":1,"safeLoop":0,"sensorType":"SINGLEBOX","speed":0,"stopOutLockArea":0,"stype":"status","temperature":15.0,"type":"Monitor","version":1},"requestId":"","sensorType":"SINGLEBOX","time":"2023-03-02 11:39:13"}
                """;

        var messageJson = JSON.parseObject(message2);
        var elevatorCode = messageJson.getString("elevatorCode");
        // yyyy-MM-dd HH:mm:ss
        var time = messageJson.getString("time");
        var monitorMessage = (JSONObject) messageJson.get("monitorMessage");
        var sensorType = monitorMessage.getString("sensorType");
        if ("CarRoof".equals(sensorType) || "SINGLEBOX".equals(sensorType)) {
            // 夜间守护模式
            nightWatchHandler.checkAndSaveNeedWatchElevator(elevatorCode, monitorMessage, time);
            // 群租问题
            groupLeasingHandler.checkAndSaveByElevator(elevatorCode, monitorMessage, time);
        }
    }
}
