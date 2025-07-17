package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/3/15 10:28
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class EmergencyRescueDetailRespDTO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 告警唯一标识
     */
    private String alarmId;

    /**
     * 电梯注册码
     */
    private String registrationCode;

    /**
     * 紧急救援工单号
     */
    private String emergencyRescueOrderCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 故障类型
     */
    private String faultType;

    /**
     * 故障名
     */
    private String faultName;

    /**
     * 故障描述-故障原因
     */
    private String faultDescription;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯名称
     */
    private String elevatorName;

    /**
     * 电梯类型
     */
    private Integer elevatorType;

    /**
     * 电梯使用类型
     */
    private Integer elevatorUseType;

    /**
     * 小区id
     */
    private Long communityId;

    /**
     * 小区name
     */
    private String communityName;

    /**
     * 地址
     */
    private String address;

    /**
     * 救援人数
     */
    private Integer numberOfRescues;

    /**
     * 安装经度
     */
    private String installLongitude;

    /**
     * 安装纬度
     */
    private String installLatitude;

    /**
     * 故障取证图片
     */
    private String faultImageUrl;

    /**
     * 故障取证视频
     */
    private String faultVideoUrl;

    /**
     * 负责人id
     */
    private Long responsibleUserId;

    /**
     * 负责人
     */
    private String responsible;

    /**
     * 负责人电话
     */
    private String responsiblePhone;

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
    private String floor;

    /**
     * 救援信息
     */
    private String rescueInformation;

    /**
     * 救援过程描述
     */
    private String describe;

    /**
     * 维修时长
     */
    private Integer repairTime;

    /**
     * 流程业务编号
     */
    private String bpmBusinessKey;

    /**
     * 维保流程当前任务名
     */
    private String maintenanceTaskName;

    /**
     * 确认状态 0:待确认 1:已确认 2:已接单 3:已取消
     */
    private Integer confirmStatus;

    /**
     * 工单状态  0待办 1进行中 2已完成 3已确认
     */
    private Integer orderStatus;

    /**
     * 签到时间
     */
    private Date signInTime;

    /**
     * 完成时间
     */
    private Date orderCompletedTime;

    /**
     * 打卡经度
     */
    private String longitude;

    /**
     * 打卡纬度
     */
    private String latitude;

    /**
     * 打卡位置状态
     */
    private Integer signInLocationStatus;

    /**
     * 签退经度
     */
    private String signOutLongitude;

    /**
     * 签退纬度
     */
    private String signOutLatitude;

    /**
     * 签退位置状态
     */
    private Integer signOutLocationStatus;

    /**
     * 签退时间
     */
    private Date signOutTime;

}
