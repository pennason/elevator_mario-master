package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TblSensorConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String vSensorConfigId;
    /**
     * 传感器id
     */
    private String vSensorId;
    /**
     * 传感器名称
     */
    private String vSensorName;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
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

    /**
     * 传感器类型
     */
    private String vSensorType;

    public String getvSensorConfigId() {
        return vSensorConfigId;
    }

    public void setvSensorConfigId(String vSensorConfigId) {
        this.vSensorConfigId = vSensorConfigId;
    }

    public String getvSensorId() {
        return vSensorId;
    }

    public void setvSensorId(String vSensorId) {
        this.vSensorId = vSensorId;
    }

    public String getvSensorName() {
        return vSensorName;
    }

    public void setvSensorName(String vSensorName) {
        this.vSensorName = vSensorName;
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

    public String getvSensorType() {
        return vSensorType;
    }

    public void setvSensorType(String vSensorType) {
        this.vSensorType = vSensorType;
    }
}
