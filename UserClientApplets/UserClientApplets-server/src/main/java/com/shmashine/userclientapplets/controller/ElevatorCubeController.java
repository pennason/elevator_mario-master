// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.userclientapplets.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.UUID;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.userclient.dto.DeviceMessageDTO;
import com.shmashine.userclientapplets.entity.BaseRequestEntity;
import com.shmashine.userclientapplets.enums.MessageCubeEnum;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.utils.SendMessageToCubeUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/5 17:10
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/elevator-cube")
@Tag(name = "小程序电梯设备接口", description = "小程序电梯设备接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ElevatorCubeController extends BaseRequestEntity {
    @Autowired
    private ElevatorService elevatorService;
    @Autowired
    private SendMessageToCubeUtil sendMessageToCubeUtil;

    private static final List<String> ACTION_LIST = Arrays.asList("start", "stop");
    private static final String NOT_MATCH_TYPE = "MotorRoom";


    @SaIgnore
    @Operation(summary = "自检", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/self-check/{action}/{elevatorId}")
    public ResponseEntity elevatorSelfCheckAction(@PathVariable("action") String action,
                                                  @PathVariable("elevatorId") String elevatorId) {
        if (!ACTION_LIST.contains(action)) {
            return new ResponseEntity("action 必须是 " + ACTION_LIST, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var elevator = elevatorService.getElevatorInfoById(elevatorId);
        if (elevator == null) {
            return new ResponseEntity("电梯不存在", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var deviceList = elevator.getJSONArray("devices");
        if (CollectionUtils.isEmpty(deviceList)) {
            return new ResponseEntity("该电梯无任何设备", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var etype = "";
        var sensorType = "SINGLEBOX";
        for (var device : deviceList) {
            var deviceObj = JSON.parseObject(JSON.toJSONString(device), JSONObject.class);
            if (!NOT_MATCH_TYPE.equalsIgnoreCase(deviceObj.get("v_sensor_type").toString())) {
                etype = deviceObj.get("eType").toString();
                sensorType = deviceObj.get("v_sensor_type").toString();
                break;
            }
        }
        // 如果没有 CarRoof的类型
        if (!StringUtils.hasText(etype)) {
            return new ResponseEntity("没有获取到设备传感器类型", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var dlogAction = MessageCubeEnum.DLOG_START;
        var selfCheckAction = MessageCubeEnum.SELF_CHECK_START;
        if ("stop".equals(action)) {
            dlogAction = MessageCubeEnum.DLOG_STOP;
            selfCheckAction = MessageCubeEnum.SELF_CHECK_STOP;
        }
        // 开启Dlog
        sendMessageToCube(elevator.getString("elevatorCode"), etype, dlogAction, sensorType);
        // 开启自检
        var selfCheckRes = sendMessageToCubeAndGetResult(elevator.getString("elevatorCode"), etype, selfCheckAction,
                sensorType);
        log.info("self-check {} log for elevator {}, result is {}", action, elevator, selfCheckRes);
        return ResponseEntity.ok(selfCheckRes);
    }

    private void sendMessageToCube(String elevatorCode, String etype, MessageCubeEnum cubeEnum, String sensorType) {
        var requestId = UUID.fastUUID().toString(true);
        sendMessageToCubeUtil.restTemplateSendMessageToCube(DeviceMessageDTO.builder()
                .requestId(requestId)
                .messageType(cubeEnum.getType())
                .subMessageType(cubeEnum.getSubType())
                .elevatorType(etype)
                .elevatorCode(elevatorCode)
                .sensorType(StringUtils.hasText(sensorType) ? sensorType : cubeEnum.getSensorType())
                .time(DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                        .toString("yyyyMMddHHmmss"))
                .build());
    }

    /**
     * 向Cube 发送消息
     *
     * @param elevatorCode 电梯编号
     * @param etype        MX301
     * @param cubeEnum     MessageCubeEnum
     * @param sensorType   CarRoof 不传则使用 MessageCubeEnum 中的
     * @return 结果
     */
    private Object sendMessageToCubeAndGetResult(String elevatorCode, String etype, MessageCubeEnum cubeEnum,
                                                 String sensorType) {
        var requestId = UUID.fastUUID().toString(true);
        sendMessageToCubeUtil.restTemplateSendMessageToCube(DeviceMessageDTO.builder()
                .requestId(requestId)
                .messageType(cubeEnum.getType())
                .subMessageType(cubeEnum.getSubType())
                .elevatorType(etype)
                .elevatorCode(elevatorCode)
                .sensorType(StringUtils.hasText(sensorType) ? sensorType : cubeEnum.getSensorType())
                .time(DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                        .toString("yyyyMMddHHmmss"))
                .build());
        // redis 中获取结果
        var redisKey = "DEVICE:" + cubeEnum.getType() + "-" + cubeEnum.getSubType() + ":"
                + elevatorCode + "-" + requestId;
        // 每 1000 ms 获取一次， 如果获取不到则认为失败
        for (var i = 0; i < 4; i++) {
            try {
                Thread.sleep(1000);
                log.info("send-message sleep {}", i);
            } catch (InterruptedException e) {
                log.error("sleep error", e);
            }
            var res = sendMessageToCubeUtil.getResultFromRedis(redisKey);
            log.info("send-message get from redis {}, res is {}", redisKey, res);
            if (res != null) {
                return res;
            }
        }
        var mapRes = new HashMap<String, Object>() {
            {
                put("result", 0);
                put("msg", String.format("未能获取到返回结果:%s:%s:%s",
                        elevatorCode, cubeEnum.getType(), cubeEnum.getSubType()));
            }
        };
        log.info("send-message res is {}", JSON.toJSONString(mapRes));
        return JSON.toJSON(mapRes);
    }
}
