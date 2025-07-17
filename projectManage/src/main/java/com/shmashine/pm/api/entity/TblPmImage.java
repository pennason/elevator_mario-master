package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblPmImage implements Serializable {

    private static final long serialVersionUID = 4695089454725989436L;

    private String vPmImageId;

    private String vTargetId;

    private String vImageUrl;

    private Integer iImageType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private int iDelFlag;

    /**
     * 是否是贯通门
     */
    private int iGtDoorFlag;

    @JsonProperty("vPmImageId")
    public String getvPmImageId() {
        return vPmImageId;
    }

    public void setvPmImageId(String vPmImageId) {
        this.vPmImageId = vPmImageId;
    }

    @JsonProperty("vTargetId")
    public String getvTargetId() {
        return vTargetId;
    }

    public void setvTargetId(String vTargetId) {
        this.vTargetId = vTargetId;
    }

    @JsonProperty("vImageUrl")
    public String getvImageUrl() {
        return vImageUrl;
    }

    public void setvImageUrl(String vImageUrl) {
        this.vImageUrl = vImageUrl;
    }

    @JsonProperty("iImageType")
    public Integer getiImageType() {
        return iImageType;
    }

    public void setiImageType(Integer iImageType) {
        this.iImageType = iImageType;
    }

    @JsonProperty("dtCreateTime")
    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    @JsonProperty("dtModifyTime")
    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }

    @JsonProperty("vCreateUserId")
    public String getvCreateUserId() {
        return vCreateUserId;
    }

    public void setvCreateUserId(String vCreateUserId) {
        this.vCreateUserId = vCreateUserId;
    }

    @JsonProperty("vModifyUserId")
    public String getvModifyUserId() {
        return vModifyUserId;
    }

    public void setvModifyUserId(String vModifyUserId) {
        this.vModifyUserId = vModifyUserId;
    }

    @JsonProperty("iDelFlag")
    public int getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(int iDelFlag) {
        this.iDelFlag = iDelFlag;
    }

    @JsonProperty("iGtDoorFlag")
    public int getiGtDoorFlag() {
        return iGtDoorFlag;
    }

    public void setiGtDoorFlag(int iGtDoorFlag) {
        this.iGtDoorFlag = iGtDoorFlag;
    }
}
