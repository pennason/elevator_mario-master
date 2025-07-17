package com.shmashine.hkcamerabyys.service;

import java.util.Date;

import com.shmashine.hkcamerabyys.entity.HikCameraAlarmConfig;

/**
 * 故障service
 *
 * @author jiangheng
 * @version V1.0 - 2024/2/28 14:42:00
 */
public interface FaultService {

    /**
     * 电动车乘梯故障
     *
     * @param elevatorId   电梯id
     * @param elevatorCode 电梯code
     * @param faultType    故障类型
     * @param faultStatus  故障状态
     * @param alarmTime    告警时间
     * @param url          告警图片
     */
    void batteryCarFault(String elevatorId, String elevatorCode, String faultType,
                         Integer faultStatus, Date alarmTime, String url);

    /**
     * 困人故障
     *
     * @param elevatorId   电梯id
     * @param elevatorCode 电梯code
     * @param faultType    故障类型
     * @param faultStatus  故障状态
     * @param alarmTime    告警时间
     * @param url          告警图片
     */
    void trappedPeopleFault(String elevatorId, String elevatorCode, String faultType,
                            Integer faultStatus, Date alarmTime, String url);

    /**
     * 普通故障
     *
     * @param elevatorId   电梯id
     * @param elevatorCode 电梯code
     * @param faultType    故障类型
     * @param faultStatus  故障状态
     * @param alarmTime    告警时间
     * @param url          告警图片
     */
    void defaultFault(String elevatorId, String elevatorCode, String faultType,
                      Integer faultStatus, Date alarmTime, String url);

    /**
     * 获取摄像头故障告警配置
     *
     * @param devSerial 摄像头序列号
     * @param alarmType 萤石告警类型
     */
    HikCameraAlarmConfig getCameraAlarmConfig(String devSerial, String alarmType);
}
