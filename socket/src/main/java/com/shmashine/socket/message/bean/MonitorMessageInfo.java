package com.shmashine.socket.message.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 监控消息处理
 *
 * @author little.li
 */
@Data
public class MonitorMessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //注册编号(平台编号)
    private String code;
    //电梯注册码 (上海市统一)
    private String registerNumber;

    /*
     * 轿顶 信息
     */
    // 当前服务模式 0:停止服务, 1:正常运行, 2:检修, 3:消防返回, 4:消防员运行, 5:应急电源运行, 6:地震模式, 7:未知
    private String serviceMode;
    // 运行状态 0：停止, 1：运行
    private String carStatus;
    //运行方向 0：无方向, 1：上行,  2：下行
    private String carDirection;
    //开锁区域  True：轿厢在开锁区域, False：轿厢在非开锁区
    private Boolean doorZone;
    //电梯当前楼层
    private Integer carFloor;
    //轿内是否有人 True：有人, False：无人
    private Boolean hasPeople;
    //轿门状态 0: 未知, 1:正在关门, 2:关门到位, 3:正在开门, 4:开门到位, 5:门锁锁止, 6:保持不完全关闭状态
    private String doorStatus;
    //关门是否到位 True：关门到位, False：无关门到位信号
    private Boolean doorCloseStatus;
    //轿厢超载 True：超载, False：未超载
    private Boolean carOverload;

    /**
     * 机房 信息
     */
    //机房温度 单位为摄氏度
    private String machineRoomTemperature;
    //厅门状态 True：门锁锁止, False：无门锁锁止信号
    private Boolean histwayDoor;
    //曳引机状态 0:待机, 1:曳引机制动器提起, 2:曳引机制动器释放
    private Integer liftCarDriveStatus;

    /**
     * 其他 信息
     */
    //人脸识别实时人数
    private Integer peopleNumber;
    //乘梯人行为模式 0：正常, 1：不文明行为, 2：危险行为
    private Integer activityMode;
    //采样时间必填，格式（yyyy-MM-dd HH:mm:ss）
    private Date samplingTime;

}
