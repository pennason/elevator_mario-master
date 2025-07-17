package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/10/17 15:14
 * @description: 麦信维保平台维保工单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResultDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 电梯id
     */
    private String elevatorId;
    /**
     * 维保站
     */
    private String maintenanceStationId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 注册码
     */
    private String registrationCode;
    /**
     * 电梯名称
     */
    private String elevatorName;
    /**
     * 设备品种
     */
    private Integer equipmentVariety;
    /**
     * 设备型号
     */
    private Integer equipmentModel;
    /**
     * 电梯类型
     */
    private Integer elevatorType;
    /**
     * 电梯使用类型
     */
    private Integer elevatorUseType;
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
     * 小区id
     */
    private String communityId;
    /**
     * 小区name
     */
    private String communityName;
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
    private String propertySafeManId;
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
    private String maintainPrincipalPersonId;
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
    private String maintainSubordinatePersonId;
    /**
     * 维保第二责任人
     */
    private String maintainSubordinatePerson;
    /**
     * 维保第二责任人电话
     */
    private String maintainSubordinatePhone;
    /**
     * 保养工单
     */
    private String maintenanceCode;
    /**
     * 保养内容
     */
    private String contentItems;
    /**
     * 保养结果
     */
    private String maintenanceResult;
    /**
     * 保养等级
     */
    private Integer maintenanceLevel;
    /**
     * 维保类型 - 1：半月保  2：季保  3：半年保  4：年保
     */
    private Integer maintenanceType;
    /**
     * 保养频率
     */
    private Integer maintenanceFrequency;
    /**
     * 保养时间
     */
    private Date maintenanceTime;
    /**
     * 计划保养时间
     */
    private Date maintenancePlannedTime;
    /**
     * 上次保养时间
     */
    private Date lastMaintenanceTime;
    /**
     * 下次保养时间
     */
    private Date nextMaintenanceTime;
    /**
     * 是否超期[0：正常，1：超期]
     */
    private Integer overdueStatus;

    /**
     * 签到经度
     */
    private String signInLongitude;

    /**
     * 签到纬度
     */
    private String signInLatitude;

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
     * 工单状态  0待办 1进行中 2已完成 3已确认
     */
    private Integer orderStatus;

    /**
     * 0：系统待审核 1：系统审核通过 2：系统审核不通过
     */
    private Integer sysAuditStatus;

    /**
     * 0：人工待审核 1：人工审核通过 2：人工审核不通过
     */
    private Integer mtAuditStatus;

    /**
     * 0：物业待审核 1：物业审核通过 2：物业审核不通过
     */
    private Integer pmAuditStatus;

    /**
     * 签到时间
     */
    private Date signInTime;

    /**
     * 签退时间
     */
    private Date signOutTime;

    /**
     * 工单完成时间
     */
    private Date orderCompletedTime;

    /**
     * 工单执行耗时 毫秒值
     */
    private Long orderExecutionTime;

    /**
     * 维保实例id
     */
    private String processInstanceId;
    /**
     * 流程业务编号
     */
    private String bpmBusinessKey;
    /**
     * 维保流程当前任务名
     */
    private String maintenanceTaskName;

}
