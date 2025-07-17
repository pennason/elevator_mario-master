package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/5/19 14:35
 * @description: 维保平台电梯
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenancePlatformElevatorRespDTO {

    //主键
    private String id;

    //电梯编号
    private String elevatorCode;

    //注册码
    private String registrationCode;

    //维堡站
    private Long maintenanceStationId;

    /**
     * 电梯收藏标识（0：未收藏；1：收藏）
     */
    @Builder.Default
    private Integer isCollect = 0;

    //项目
    private Long projectId;

    //小区id
    private Long communityId;

    //小区name
    private String communityName;

    //设备品种
    private Integer equipmentVariety;

    //设备型号
    private Integer equipmentModel;

    //设备代码
    private String equCode;

    //电梯名称
    private String elevatorName;

    //合同项目id
    private String contractProjectId;

    //合同id
    private String contractId;

    //合同状态 0：未认领 1：在保 2：过保
    private Integer contractStatus;

    //合同结束时间
    private Date contractExpirationDate;

    //规格型号
    private String specifications;

    //电梯类型
    private Integer elevatorType;

    //电梯使用类型
    private Integer elevatorUseType;

    //省
    private String province;

    //市
    private String city;

    //区、县
    private String district;

    //电梯地址
    private String address;

    //电梯经度
    private String longitude;

    //电梯纬度
    private String latitude;

    //责任人id
    private Long responsibleUserId;

    //责任人
    private String responsible;

    //责任人联系电话
    private String responsiblePhone;

    //物业安全管理员id
    private Long propertySafeManId;

    //物业安全管理员
    private String propertySafeMan;

    //物业安全管理员电话
    private String propertySafePhone;

    //维保第一责任人id
    private Long maintainPrincipalPersonId;

    //维保第一责任人
    private String maintainPrincipalPerson;

    //维保第一责任人电话
    private String maintainPrincipalPhone;

    //维保第二责任人id
    private Long maintainSubordinatePersonId;

    //维保第二责任人
    private String maintainSubordinatePerson;

    //维保第二责任人电话
    private String maintainSubordinatePhone;

    //生产厂家
    private String manufacturer;

    //电梯状态[0：正常，1故障中，3：检修中]
    private Integer elevatorStatus;

    //电梯检验时间 年检时间
    private Date inspectDate;

    //上次保养时间
    private Date lastMaintenanceTime;

    //下次保养时间
    private Date nextMaintenanceTime;

    //下次季保时间
    private Date nextQuarterMaintenanceTime;

    //下次季保类型【1：季保1，2：半年保，3：季保2，4：年保】
    private Integer nextQuarterMaintenanceType;

    //维保间隔
    private Integer maintenanceInterval;

    //供应商
    private String supplier;

    //购买时间
    private Date purchaseTime;

    //启用时间
    private Date enableTime;

    //创建时间
    private Date createTime;
}
