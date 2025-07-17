package com.shmashine.api.controller.device.vo;


import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/12/4 14:09
 * @description: 设备参数配置结果返回对象
 */
@Data
public class DeviceParamConfReqVO {

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
    @NotBlank(message = "电梯编号不能为空")
    private String elevatorCode;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空")
    private String sensorType;

    /**
     * 设备表主键id
     */
    private String deviceId;

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
     * 1：单门 2：贯通门
     */
    private Integer doorNum;

    /**
     * 基准楼层号，0：无基准楼层
     */
    private Integer baseFloor;

    /**
     * 困人延时上报时间，单位：分钟
     */
    private Integer peopleRptTm;

    /**
     * 统计数据上报周期，单位：分钟
     */
    private Integer statData;

    /**
     * 超速上报阈值，单位：米/秒
     */
    private String overSpeed;

    /**
     * 传感器配置值，按位有效
     */
    private String sensorCfg;

    /**
     * 传感器极性配置，按位有效
     */
    private String sensorInvertCfg;

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
     * 人感电平
     * 0：无人感
     * 1：Focus电平，
     * 2：简易人感电平
     */
    private String humanType;

    /**
     * 模式
     * 0：轿顶从设备
     * 1：轿顶主设备
     * 2：机房设备
     */
    private Integer mode;

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

}
