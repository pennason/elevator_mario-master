package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/3/15 10:14
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class RepairDetailRespDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 告警唯一标识
     */
    private String alarmId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上报人
     */
    private String reportUserName;

    /**
     * 电梯id
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 电梯类型
     */
    private Integer elevatorType;

    /**
     * 电梯使用类型
     */
    private Integer elevatorUseType;

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
     * 安装经度
     */
    private String installLongitude;

    /**
     * 安装纬度
     */
    private String installLatitude;

    /**
     * 责任人id
     */
    private Long responsibleUserId;

    /**
     * 责任人
     */
    private String responsible;

    /**
     * 责任人联系电话
     */
    private String responsiblePhone;

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
     * 小区name
     */
    private String communityName;

    /**
     * 备件出库单
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

    /**
     * 上报时间
     */
    private Date reportTime;

    /**
     * 报修楼层
     */
    private String reportFloor;

    /**
     * 上报人id
     */
    private Long reportUserId;

    /**
     * 处理意见
     */
    private String remark;

    /**
     * 工单状态  0待办 1进行中 2已完成 3已确认
     */
    private Integer orderStatus;

    /**
     * 签到时间
     */
    private Date signInTime;

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

}
