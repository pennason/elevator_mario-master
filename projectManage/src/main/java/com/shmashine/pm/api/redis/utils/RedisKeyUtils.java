package com.shmashine.pm.api.redis.utils;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtils {
    /**
     * 构建路由 缓存key
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 缓存key
     */
    public static String getGateWayRouteKey(String elevatorCode, String sensorType) {
        return "GATEWAY:ROUTE:" + elevatorCode + ":" + sensorType;
    }

    /**
     * 电梯状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return 缓存key
     */
    public static String getElevatorStatus(String elevatorCode, String sensorType) {
        return "ELEVATOR:STATUS:" + elevatorCode + ":" + sensorType;
    }


    public static String getCameraHistoryStatus(String elevatorCode) {
        return "ELEVATOR:CAMERA:" + elevatorCode;
    }

    public static String getXiongMaiHlsUrl(String elevatorCode) {
        return "CAMERA:HLSURL:" + elevatorCode;
    }

    /**
     * 获取设备传感器配置
     *
     * @param vElevatorCode
     * @param sensorType
     * @return
     */
    public static String getSensorConfigKey(String vElevatorCode, String sensorType) {
        return "ELEVATOR:SENSOR:STATUS:" + vElevatorCode + ":" + sensorType;
    }

}
