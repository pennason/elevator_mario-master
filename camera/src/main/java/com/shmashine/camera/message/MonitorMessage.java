package com.shmashine.camera.message;

import lombok.Data;

/**
 * 监控消息
 *
 * @author little.li
 */
@Data
public class MonitorMessage {

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 消息类型
     */
    private String type = "Monitor";
    /**
     * 消息子类型
     */
    private String stype = "status";

    /**
     * 设备类型 Cube：轿顶（初版设备），CarRoof：轿顶，MotorRoom：机房，FRONT：前装设备
     */
    private String sensorType;

    /**
     * 设备固件monitor版本
     * 1：直梯单门，45（2D）：直梯双门
     */
    private int version;


    //////////////// 轿顶上报数据  ////////////////

    /**
     * 当前设备状态 0 正常 1 故障 2 平层关人 3 非平层关人
     */
    private Integer nowStatus;
    /**
     * 服务模式 0：正常，1：检修模式，2：停止服务
     * 检修模式从机房上报，与轿顶公用一个字段
     */
    private Integer modeStatus;
    /**
     * 设备当前电量 0-100
     * 轿顶和机房公用字段
     */
    private Integer battery;
    /**
     * 轿厢运行状态 0：停止，1：运行
     */
    private Integer runStatus;
    /**
     * 轿厢运行方向 0：停留，1：上行，2：下行
     */
    private Integer direction;
    /**
     * 电梯当前楼层
     */
    private String floor;
    /**
     * 平层状态 0：平层，1：非平层
     */
    private Integer floorStatus;
    /**
     * 关门到位 0：无关门到位，1：关门到位
     */
    private Integer droopClose;
    /**
     * 关门到位 0：无关门到位，1：关门到位
     */
    private Integer droopClose2;
    /**
     * 轿内是否有人 0：无人，1：有人
     */
    private Integer hasPeople;
    /**
     * 轿门状态-门锁锁止 0：锁止，1：非锁止
     */
    private Integer carStatus;
    /**
     * 供电状态 0：电源，1：电池，2：其他
     */
    private Integer powerStatus;
    /**
     * 电梯运行速度
     */
    private float speed;


    //////////////// 机房上报数据  ////////////////

    /**
     * 机房温度
     */
    private Float temperature;
    /**
     * 厅门状态-门锁锁止 0：非锁止，1：锁止
     */
    private Integer doorStatus;
    /**
     * 曳引机状态-制动器提起或释放  0：提起，1：释放
     */
    private Integer driveStatus;
    /**
     * 安全回路状态-正常或断开 0：正常 1：断开
     */
    private Integer safeLoop;

}
