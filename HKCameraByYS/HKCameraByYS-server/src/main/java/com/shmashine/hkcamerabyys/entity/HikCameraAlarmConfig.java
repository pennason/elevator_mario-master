package com.shmashine.hkcamerabyys.entity;

import java.util.Date;

import lombok.Data;

/**
 * 海康摄像头告警配置
 *
 * @author jiangheng
 * @version V1.0 - 2024/2/28 13:44
 */
@Data
public class HikCameraAlarmConfig {

    /**
     * 表主键ID
     */
    private Long id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯id
     */
    private String elevatorId;

    /**
     * 摄像头设备序列号
     */
    private String devSerial;

    /**
     * 萤石告警类型
     */
    private String alarmType;

    /**
     * 麦信故障标准
     */
    private String faultType;

    /**
     * 麦信故障状态 0:消除 1:新增
     */
    private Integer faultStatus;

    /**
     * 摄像头固件版本号
     */
    private String cameraFirmwareVersion;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
