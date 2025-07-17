package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * iot卡信息
 *
 * @author jiangheng
 * @version 1.0 2021/8/6 14:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IotCard extends PageListParams implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 电梯编号
     */
    @ExcelProperty("vElevatorCode")
    private String vElevatorCode;

    /**
     * Iccid卡号
     */
    @ExcelProperty("iccid")
    private String iccid;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 安装状态
     */
    private Integer installStatus;

    /**
     * 套餐包名称
     */
    private String flowPackageName;

    /**
     * 套餐开始时间
     */
    private Date startTime;

    /**
     * 套餐结束时间
     */
    private Date endTime;

    /**
     * 套餐价格（分）
     */
    private Integer packagePrice;

    /**
     * 卡状态
     */
    private String operatorStatus;

    /**
     * 激活时间
     */
    private Date operatorActiveTime;

    /**
     * 是否停止数据流量服务(0：未停用，1：停用)
     */
    private Integer stopGprs;

    /**
     * 已使用流量大小（MB）
     */
    private Double usedFlow;

    /**
     * 套装包流量大小（MB）
     */
    private Double packageSize;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间(上一次同步时间)
     */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public Integer getInstallStatus() {
        return installStatus;
    }

    public void setInstallStatus(Integer installStatus) {
        this.installStatus = installStatus;
    }

    public String getFlowPackageName() {
        return flowPackageName;
    }

    public void setFlowPackageName(String flowPackageName) {
        this.flowPackageName = flowPackageName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Integer packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getOperatorStatus() {
        return operatorStatus;
    }

    public void setOperatorStatus(String operatorStatus) {
        this.operatorStatus = operatorStatus;
    }

    public Date getOperatorActiveTime() {
        return operatorActiveTime;
    }

    public void setOperatorActiveTime(Date operatorActiveTime) {
        this.operatorActiveTime = operatorActiveTime;
    }

    public Integer getStopGprs() {
        return stopGprs;
    }

    public void setStopGprs(Integer stopGprs) {
        this.stopGprs = stopGprs;
    }

    public Double getUsedFlow() {
        return usedFlow;
    }

    public void setUsedFlow(Double usedFlow) {
        this.usedFlow = usedFlow;
    }

    public Double getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Double packageSize) {
        this.packageSize = packageSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
