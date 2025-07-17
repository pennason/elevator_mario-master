// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.userclient.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/5 18:26
 * @since v1.0
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeviceMessageDTO implements Serializable {
    /**
     * 消息类型
     */
    @JsonProperty(value = "TY", required = true)
    private String messageType;

    /**
     * 子消息类型
     */
    @JsonProperty(value = "ST", required = true)
    private String subMessageType;

    /**
     * 电梯类型 如 MX301
     */
    @JsonProperty(value = "etype", required = true, defaultValue = "MX301")
    private String elevatorType;
    /**
     * 设备编号 MX301_xxxxx
     */
    @JsonProperty(value = "eid", required = true)
    private String elevatorCode;

    /**
     * 传感器类型 机房或轿顶 CarRoof, CarDoor, MotorRoom
     */
    @JsonProperty(value = "sensorType", required = true, defaultValue = "CarRoof")
    private String sensorType;

    /**
     * 请求时间 yyyyMMddHHmmss
     */
    @JsonProperty(value = "time", required = true)
    private String time;

    /**
     * 请求ID
     */
    @JsonProperty(value = "requestId", required = true)
    private String requestId;
}
