package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 14:07
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class EmergencyRescueOrderResultDTO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 紧急救援工单号
     */
    private String emergencyRescueOrderCode;

    /**
     * 确认状态 0:待确认 1:已确认 2:已接单 3:已取消
     */
    private Integer confirmStatus;

    /**
     * 电梯注册码
     */
    private String registrationCode;

    /**
     * 工单状态  0待办 1进行中 2已完成 3已确认
     */
    private Integer orderStatus;

    /**
     * 小区name
     */
    private String communityName;

    /**
     * 是否困人（1是/0否）
     */
    private Integer personTrapped;

    /**
     * 是否紧急（1是/0否）
     */
    private Integer isUrgent;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障子类型
     */
    private String faultSecondType;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯名称
     */
    private String elevatorName;

    /**
     * 小区id
     */
    private Long communityId;

    /**
     * 地址
     */
    private String address;

    /**
     * 故障取证图片
     */
    private String faultImageUrl;

    /**
     * 故障取证视频
     */
    private String faultVideoUrl;

    /**
     * 负责人
     */
    private String responsible;

    /**
     * 物业安全管理员id
     */
    private Long propertySafeManId;
    /**
     * 物业安全管理员
     */
    private String propertySafeMan;
    /**
     * 物业安全管理员电话
     */
    private String propertySafePhone;
    /**
     * 维保第一责任人id
     */
    private Long maintainPrincipalPersonId;
    /**
     * 维保第一责任人
     */
    private String maintainPrincipalPerson;
    /**
     * 维保第一责任人电话
     */
    private String maintainPrincipalPhone;
    /**
     * 维保第二责任人id
     */
    private Long maintainSubordinatePersonId;
    /**
     * 维保第二责任人
     */
    private String maintainSubordinatePerson;
    /**
     * 维保第二责任人电话
     */
    private String maintainSubordinatePhone;

    /**
     * 报修时间
     */
    private Date reportTime;

    /**
     * 报修楼层
     */
    private String reportFloor;

    /**
     * 救援过程描述
     */
    private String describe;

    /**
     * 急修工单id
     */
    private Long repairOrderId;

    /**
     * 备件出库表
     */
    private Long materialStockOutId;

    /**
     * 流程业务编号
     */
    private String bpmBusinessKey;

    /**
     * 维保流程当前任务名
     */
    private String maintenanceTaskName;

}
