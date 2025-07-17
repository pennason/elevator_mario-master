package com.shmashine.api.controller.device.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2023/12/4 13:52
 * @description: 设备参数配置
 */
@Data
@ToString
public class DeviceParamConfBaseVO implements Serializable {

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
     * 数据服务器配置下发时间
     */
    private Date serverIpPortSendTime;

    /**
     * dlog服务器IP:PORT
     */
    private String dlogIpPort;

    /**
     * dlog服务器配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer dlogIpPortStatus;

    /**
     * dlog服务器配置时间
     */
    private Date dlogIpPortSendTime;

    /**
     * 最低和最高楼层，格式如：-1~12
     */
    private String floor;

    /**
     * 楼层配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer floorStatus;

    /**
     * 楼层配置下发时间
     */
    private Date floorSendTime;

    /**
     * 1：单门 2：贯通门
     */
    private Integer doorNum;

    /**
     * 门配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer doorNumStatus;

    /**
     * 门配置下发时间
     */
    private Date doorNumSendTime;

    /**
     * 基准楼层号，0：无基准楼层
     */
    private Integer baseFloor;

    /**
     * 基准楼层配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer baseFloorStatus;

    /**
     * 基准楼层配置下发时间
     */
    private Date baseFloorSendTime;

    /**
     * 困人延时上报时间，单位：分钟
     */
    private Integer peopleRptTm;

    /**
     * 困人延时上报时间配置0：未配置 1：已下发 2：已配置
     */
    private Integer peopleRptTmStatus;

    /**
     * 困人延时上报时间配置下发时间
     */
    private Date peopleRptTmSendTime;

    /**
     * 统计数据上报周期，单位：分钟
     */
    private Integer statData;

    /**
     * 统计数据上报周期配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer statDataStatus;

    /**
     * 统计数据上报周期配置下发时间
     */
    private Date statDataSendTime;

    /**
     * 超速上报阈值，单位：米/秒
     */
    private String overSpeed;

    /**
     * 超速上报阈值配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer overSpeedStatus;

    /**
     * 超速上报阈值配置下发时间
     */
    private Date overSpeedSendTime;

    /**
     * 传感器配置值，按位有效
     */
    private String sensorCfg;

    /**
     * 传感器配置值配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer sensorCfgStatus;

    /**
     * 传感器配置值配置下发时间
     */
    private Date sensorCfgSendTime;

    /**
     * 传感器极性配置，按位有效
     */
    private String sensorInvertCfg;

    /**
     * 传感器极性配置状态 0：未配置 1：已下发 2：已配置
     */
    private Integer sensorInvertCfgStatus;

    /**
     * 传感器极性配置下发时间
     */
    private Date sensorInvertCfgSendTime;

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
     * 开关门故障时间和阻挡门次数配置时间
     */
    private Date doorFaultSendTime;

    /**
     * 人感电平
     * 0：无人感
     * 1：Focus电平，
     * 2：简易人感电平
     */
    private String humanType;

    /**
     * 人感电平配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer humanTypeStatus;

    /**
     * 人感电平配置下发时间
     */
    private Date humanTypeSendTime;

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
     * 模式配置下发时间
     */
    private Date modeSendTime;

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
     * AI 识别dlog开关配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer dlogStatus;

    /**
     * dlog开关配置下发时间
     */
    private Date dlogSendTime;

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
     * 设备ip网络配置时间
     */
    private Date devIpSendTime;

    /**
     * 设备配置状态0：未配置 1：已下发 2：已配置
     */
    private Integer deviceConfStatus;
}
