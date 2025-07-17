package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TblCheckingBill implements Serializable {

    /**
     * 验收单id
     */
    private String vCheckingBillId;
    /**
     * 验收id
     */
    private String vCheckingTaskId;
    /**
     * 项目
     */
    private String vProjectId;
    /**
     * 小区
     */
    private String vVillageId;
    /**
     * 电梯麦信号
     */
    private String vElevatorCode;
    /**
     * 状态
     */
    private Integer iStatus;
    /**
     * 备注
     */
    private String vRemark;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private Boolean iDelFlag;

    private String vDeviceType;

    private static final long serialVersionUID = 1L;

    public String getvCheckingBillId() {
        return vCheckingBillId;
    }

    public void setvCheckingBillId(String vCheckingBillId) {
        this.vCheckingBillId = vCheckingBillId == null ? null : vCheckingBillId.trim();
    }

    public String getvCheckingTaskId() {
        return vCheckingTaskId;
    }

    public void setvCheckingTaskId(String vCheckingTaskId) {
        this.vCheckingTaskId = vCheckingTaskId == null ? null : vCheckingTaskId.trim();
    }

    public String getvProjectId() {
        return vProjectId;
    }

    public void setvProjectId(String vProjectId) {
        this.vProjectId = vProjectId == null ? null : vProjectId.trim();
    }

    public String getvVillageId() {
        return vVillageId;
    }

    public void setvVillageId(String vVillageId) {
        this.vVillageId = vVillageId == null ? null : vVillageId.trim();
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode == null ? null : vElevatorCode.trim();
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark == null ? null : vRemark.trim();
    }

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId == null ? null : vCreateUserId.trim();
    }

    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId == null ? null : vModifyUserId.trim();
    }

    public Boolean getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Boolean iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    public String getvDeviceType() {
        return vDeviceType;
    }

    public void setvDeviceType(String vDeviceType) {
        this.vDeviceType = vDeviceType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", vCheckingBillId=").append(vCheckingBillId);
        sb.append(", vCheckingTaskId=").append(vCheckingTaskId);
        sb.append(", vProjectId=").append(vProjectId);
        sb.append(", vVillageId=").append(vVillageId);
        sb.append(", vElevatorCode=").append(vElevatorCode);
        sb.append(", iStatus=").append(iStatus);
        sb.append(", vRemark=").append(vRemark);
        sb.append(", dtCreateTime=").append(dtCreateTime);
        sb.append(", dtModifyTime=").append(dtModifyTime);
        sb.append(", vCreateUserId=").append(vCreateUserId);
        sb.append(", vModifyUserId=").append(vModifyUserId);
        sb.append(", iDelFlag=").append(iDelFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}
