package com.shmashine.socket.message.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传感器参数预配置消息
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/5/15 11:50
 * @Since: 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("checkstyle:MemberName")
public class ParamPreCfgMessage {

    private String etype;

    private String eid;

    @Builder.Default
    private String TY = "Update";

    @Builder.Default
    private String ST = "ParamPreCfg";

    /**
     * 最低楼层
     */
    private String floorMin;

    /**
     * 最高楼层
     */
    private String floorMax;

    /**
     * 门数量，默认：单门
     * 1：单门 2：贯通门
     */
    private Integer doorNum;

    /**
     * 基准楼层号，0：无基准楼层
     */
    private Integer baseFloor;

    /**
     * 困人延时上报时间，单位：秒
     */
    private Integer trappedRptTm;

    /**
     * 统计数据上报周期，单位：分钟
     */
    private Integer statData;

    /**
     * 超速上报阈值，单位：米/秒
     */
    private String overSpeed;

    /**
     * 终端模式
     * 0：轿顶从设备（单盒模式）
     * 1：轿顶主设备
     * 2：机房设备
     */
    private Integer mode;

    /**
     * 传感器配置
     */
    private String sensorMap;

    /**
     * 机房传感器极性配置
     */
    private String sensorInvert;

    /**
     * 人感传感器电平类型
     * 0：无人感传感器
     * 1：Focus 电平类
     * 2：简易人感（蘑菇头）电平类
     */
    private Integer humanType;

    /**
     * IP类型
     * static：静态IP
     * DHCP：动态获取
     */
    private String ipType;

    /**
     * IP地址
     */
    private String IP;

    /**
     * 掩码
     */
    private String netmask;

    /**
     * 网关
     */
    private String gateway;
}
