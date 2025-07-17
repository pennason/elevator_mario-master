package com.shmashine.fault.dal.dataobject;

import lombok.Data;

/**
 * 设备参数配置
 *
 * @author jiangheng
 * @version V1.0.0 - 2023/12/4 13:52
 */
@Data
public class DeviceParamConfDO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 电梯表主键id
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 设备表主键id
     */
    private String deviceId;

    /**
     * 设备类型
     */
    private String sensorType;

    /**
     * 数据服务器IP:PORT
     */
    private String serverIpPort;

    /**
     * 数据服务器配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer serverIpPortStatus;

    /**
     * dlog服务器IP:PORT
     */
    private String dlogIpPort;

    /**
     * dlog服务器配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer dlogIpPortStatus;


    /**
     * 最低和最高楼层，格式如：-1~12
     */
    private String floor;

    /**
     * 楼层配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer floorStatus;

    /**
     * 1：单门 2：贯通门
     */
    private Integer doorNum;

    /**
     * 门配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer doorNumStatus;

    /**
     * 基准楼层号，0：无基准楼层
     */
    private Integer baseFloor;

    /**
     * 基准楼层配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer baseFloorStatus;

    /**
     * 困人延时上报时间，单位：分钟
     */
    private Integer peopleRptTm;

    /**
     * 困人延时上报时间配置0：未配置 1：已下发 2：已配置
     */
    private Integer peopleRptTmStatus;

    /**
     * 统计数据上报周期，单位：分钟
     */
    private Integer statData;

    /**
     * 统计数据上报周期配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer statDataStatus;

    /**
     * 超速上报阈值，单位：米/秒
     */
    private String overSpeed;

    /**
     * 超速上报阈值配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer overSpeedStatus;

    /**
     * 传感器配置值，按位有效
     */
    private String sensorCfg;

    /**
     * 传感器配置值配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer sensorCfgStatus;

    /**
     * 传感器极性配置，按位有效
     */
    private String sensorInvertCfg;

    /**
     * 传感器极性配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer sensorInvertCfgStatus;

    /**
     * 人感电平
     * 0：无人感
     * 1：Focus电平，
     * 2：简易人感电平
     */
    private Integer humanType;

    /**
     * 人感电平配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer humanTypeStatus;

    /**
     * 模式
     * 0：轿顶从设备
     * 1：轿顶主设备
     * 2：机房设备
     */
    private Integer mode;

    /**
     * 模式配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer modeStatus;

    /**
     * 正常dlog开关
     */
    private Integer normalDlog;

    /**
     * 异常dlog开关
     */
    private Integer abortDlog;

    /**
     * AI 识别dlog开关
     */
    private Integer aiDlog;

    /**
     * dlog开关配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer dlogStatus;

    /**
     * 开门时间故障 单位：秒，默认：30s
     */
    private Integer openWaitingTime;

    /**
     * 关门时间故障 单位：秒，默认：30s
     */
    private Integer closeWaitingTime;

    /**
     * 阻挡门次数 单位：次数，默认20次
     */
    private Integer closeRepeatCount;

    /**
     * 开关门故障时间和阻挡门次数配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer doorFaultStatus;

    /**
     * 设备IP类型 DHCP/STATIC
     */
    private String devIpType;

    /**
     * 设备IP地址
     */
    private String devIp;

    /**
     * 设备掩码
     */
    private String devNetmask;

    /**
     * 设备网关
     */
    private String devGateway;

    /**
     * 设备IP网络配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer devIpStatus;

    /**
     * 设备配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer deviceConfStatus;

}
