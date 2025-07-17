package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 13:23
 * @description: 麦信维保平台维修工单
 */
@Data
public class RepairOrderResultDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 单号
     */
    private String orderCode;

    /**
     * 注册码
     */
    private String registrationCode;

    /**
     * 安装地址
     */
    private String installAddress;

    /**
     * 责任人
     */
    private String responsible;

    /**
     * 责任人联系电话
     */
    private String responsiblePhone;

    /**
     * 保养图片
     */
    private Long maintenancePic;

    /**
     * 维修工单
     */
    private String repairNumber;

    /**
     * 故障图片
     */
    private Long repairPic;

    /**
     * 故障取证图片
     */
    private String faultImageUrl;

    /**
     * 故障取证视频
     */
    private String faultVideoUrl;

    /**
     * 故障原因
     */
    private String faultReason;

    /**
     * 维修人员
     */
    private String repairPeople;

    /**
     * 维修人员电话
     */
    private String repairPhone;

    /**
     * 维修结果
     */
    private String repairResult;

    /**
     * 维修完成时间
     */
    private Date repairCompletionTime;

    /**
     * 维修时长
     */
    private Integer repairTime;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 工单状态  0待办 1进行中 2已完成 3已确认
     */
    private Integer orderStatus;

    /**
     * 故障子类型
     */
    private String faultSecondType;

    /**
     * 电梯名称
     */
    private String elevatorName;
    /**
     * 小区id
     */
    private Long communityId;

    /**
     * 备件出库表
     */
    private Long materialStockOutId;

    /**
     * 上报时间
     */
    private Date reportTime;

    /**
     * 报修楼层
     */
    private Integer reportFloor;

}
