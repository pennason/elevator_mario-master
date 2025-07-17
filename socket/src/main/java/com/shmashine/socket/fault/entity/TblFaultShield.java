package com.shmashine.socket.fault.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 故障上报屏蔽规则表(TblFaultShield)实体类
 *
 * @author little.li
 * @since 2020-06-14 15:18:15
 */
public class TblFaultShield implements Serializable {


    private static final long serialVersionUID = -12381867799735366L;


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


    public String getVFaultShieldId() {
        return vFaultShieldId;
    }

    public void setVFaultShieldId(String vFaultShieldId) {
        this.vFaultShieldId = vFaultShieldId;
    }

    public String getVElevatorId() {
        return vElevatorId;
    }

    public void setVElevatorId(String vElevatorId) {
        this.vElevatorId = vElevatorId;
    }

    public String getVElevatorCode() {
        return vElevatorCode;
    }

    public void setVElevatorCode(String vElevatorCode) {
        this.vElevatorCode = vElevatorCode;
    }

    public Integer getIFaultType() {
        return iFaultType;
    }

    public void setIFaultType(Integer iFaultType) {
        this.iFaultType = iFaultType;
    }

    public String getVFaultName() {
        return vFaultName;
    }

    public void setVFaultName(String vFaultName) {
        this.vFaultName = vFaultName;
    }

    public Integer getIIsUserVisible() {
        return iIsUserVisible;
    }

    public void setIIsUserVisible(Integer iIsUserVisible) {
        this.iIsUserVisible = iIsUserVisible;
    }

    public Integer getIIsReport() {
        return iIsReport;
    }

    public void setIIsReport(Integer iIsReport) {
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

    public String getVCreateUserId() {
        return vCreateUserId;
    }

    public void setVCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    public String getVModifyUserId() {
        return vModifyUserId;
    }

    public void setVModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    public Integer getIDelFlag() {
        return iDelFlag;
    }

    public void setIDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

}