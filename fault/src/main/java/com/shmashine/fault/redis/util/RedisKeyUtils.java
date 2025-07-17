package com.shmashine.fault.redis.util;

/**
 * redis key 工具
 *
 * @author little.li
 */
public class RedisKeyUtils {


    /**
     * 电梯状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @return 缓存key
     */
    public static String getElevatorStatus(String elevatorCode) {
        return "ELEVATOR:STATUS:" + elevatorCode;
    }


    /**
     * 设备状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @return 缓存key
     */
    public static String getDeviceStatus(String elevatorCode, String sensorType) {
        return "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
    }


}
