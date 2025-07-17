package com.shmashine.socket.redis.utils;

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
     * 电梯故障状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @return 缓存key
     */
    public static String getElevatorFaultStatus(String sensorType, String elevatorCode) {
        return "ELEVATOR:FAULT:" + sensorType + ":" + elevatorCode;
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


    ////////////////////////////////////////nezha_web使用


    /**
     * 记录电梯在线时间key
     */
    public static String systemKey(String elevatorCode) {
        return String.format("%s:%s:%s", "elevator", "system", elevatorCode);
    }


    public static String getRegisterKey(String registerNum) {
        return String.format("%s:%s:%s", "REGISTER", "STATUS", registerNum);
    }

    public static String getXiongMaiHlsUrl(String elevatorCode) {
        return "CAMERA:HLSURL:" + elevatorCode;
    }
}
