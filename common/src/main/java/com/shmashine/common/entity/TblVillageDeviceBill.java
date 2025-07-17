package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 小区设备清单
 */

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblVillageDeviceBill implements Serializable {

    private static final long serialVersionUID = 1300417734116173459L;

    @JsonProperty("vVillageDeviceBillId")
    private String vVillageDeviceBillId;
    /**
     * 单盒
     */
    @JsonProperty("iSingleBoxCount")
    private Integer iSingleBoxCount;
    /**
     * 双盒
     */
    @JsonProperty("iDoubleBoxCount")
    private Integer iDoubleBoxCount;
    /**
     * 是否有摄像头
     */
    @JsonProperty("iCamera")
    private Integer iCamera;
    /**
     * 摄像头类型
     */
    @JsonProperty("iCameraType")
    private Integer iCameraType;

    // 人感
    @JsonProperty("iBodySensor")
    private Integer iBodySensor;

    //轿顶门机电机电流传感器
    @JsonProperty("iCarroofDoorSensor")
    private Integer iCarroofDoorSensor;
    //轿顶轿门锁止电流传感器
    @JsonProperty("iCarroofLockSensor")
    private Integer iCarroofLockSensor;
    //机房停止服务电流传感器
    @JsonProperty("iMachRoomServiceSensor")
    private Integer iMachRoomServiceSensor;
    //机房检修服务电流传感器
    @JsonProperty("iMachRoomOverhaulSensor")
    private Integer iMachRoomOverhaulSensor;
    //机房抱闸服务电流传感器
    @JsonProperty("iMachRoomSwitchSensor")
    private Integer iMachRoomSwitchSensor;
    //机房紧急呼叫电流传感器
    @JsonProperty("iMachRoomCallSensor")
    private Integer iMachRoomCallSensor;
    //机房安全回路电流传感器
    @JsonProperty("iMachRoomCircuitSensor")
    private Integer iMachRoomCircuitSensor;
    //机房厅门回路电流传感器
    @JsonProperty("iMachRoomHallCircuitSensor")
    private Integer iMachRoomHallCircuitSensor;
    //机房抱闸回路电流传感器
    @JsonProperty("iMachRoomSwitchCircuitSensor")
    private Integer iMachRoomSwitchCircuitSensor;
    //机房停电服务电流传感器
    @JsonProperty("iMachRoomElectricitySensor")
    private Integer iMachRoomElectricitySensor;

    /**
     * 备注
     */
    @JsonProperty("vRemarks")
    private String vRemarks;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("dtCreateTime")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("dtModifyTime")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    @JsonProperty("vCreateUserId")
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    @JsonProperty("vModifyUserId")
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    @JsonProperty("iDelFlag")
    private Integer iDelFlag;

    @JsonProperty("vVillageId")
    private String vVillageId;

    /**
     * 设备类型
     */
    @JsonProperty("vDeviceType")
    private String vDeviceType;

    /**
     * 设备安装位置
     */
    @JsonProperty("vDevicePosition")
    private String vDevicePosition;

    /**
     * 是否用户温度传感器
     */
    @JsonProperty("iTempSensor")
    private Integer iTempSensor;

    /**
     * 人体传感器型号
     */
    @JsonProperty("vBodySensorMode")
    private String vBodySensorMode;
    /**
     * 平层传感器
     */
    @JsonProperty("iFloorSensor")
    private Integer iFloorSensor;

    /**
     * 基准平层传感器
     */
    @JsonProperty("iCollateFloorSensor")
    private Integer iCollateFloorSensor;

    /**
     * 门磁传感器
     */
    @JsonProperty("iMagnetSensor")
    private Integer iMagnetSensor;

    /**
     * 摄像头型号
     */
    @JsonProperty("vCameraMode")
    private String vCameraMode;

    /**
     * 是否控梯
     */
    @JsonProperty("iControlElevator")
    private Integer iControlElevator;

}
