package com.shmashine.sender.entity;

import java.util.Date;

/**
 * 故障屏蔽实体
 */
public class TblFaultShield {

    /**
     * 故障屏蔽规则唯一ID
     */
    private String vFaultShieldId;
    /**
     * 电梯ID
     */
    private String vElevatorId;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 故障类型
     */
    private Integer iFaultType;
    /**
     * 故障名称
     */
    private String vFaultName;
    /**
     * 对用户是否可见，0 用户不可见，1 用户可见
     */
    private Integer iIsUserVisible;
    /**
     * 是否推送到其他平台，0 不推送，1 推送
     */
    private Integer iIsReport;
    /**
     * 创建时间
     */
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    private String vModifyUserId;
    /**
     * 删除标识 0-未删除，1-已删除
     */
    private Integer iDelFlag;

    public String getvFaultShieldId() {
        return vFaultShieldId;
    }

    public void setvFaultShieldId(String vFaultShieldId) {
        this.vFaultShieldId = vFaultShieldId;
    }

    public String getvElevatorId() {
        return vElevatorId;
    }

    public void setvElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getvElevatorCode() {
        return vElevatorCode;
    }

    public void setvElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getiFaultType() {
        return iFaultType;
    }

    public void setiFaultType(Integer iFaultType) {
        this.iFaultType = iFaultType;
    }

    public String getvFaultName() {
        return vFaultName;
    }

    public void setvFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    public Integer getiIsUserVisible() {
        return iIsUserVisible;
    }

    public void setiIsUserVisible(Integer iIsUserVisible) {
        this.iIsUserVisible = iIsUserVisible;
    }

    public Integer getiIsReport() {
        return iIsReport;
    }

    public void setiIsReport(Integer iIsReport) {
        this.iIsReport = iIsReport;
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
        this.vCreateUserId = vCreateUserId;
    }

    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }
}
