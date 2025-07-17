package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblDistributionTaskFile implements Serializable {

    private static final long serialVersionUID = -1696106735034781959L;

    private String vDistributionTaskFileId;

    private String vDistributionTaskId;

    private String vFileUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyTime;

    private String vCreateUserId;

    private String vModifyUserId;

    private int iDelFlag;

    @JsonProperty("vDistributionTaskFileId")
    public String getvDistributionTaskFileId() {
        return vDistributionTaskFileId;
    }

    public void setvDistributionTaskFileId(String vDistributionTaskFileId) {
        this.vDistributionTaskFileId = vDistributionTaskFileId;
    }

    @JsonProperty("vDistributionTaskId")
    public String getvDistributionTaskId() {
        return vDistributionTaskId;
    }

    public void setvDistributionTaskId(String vInvestigateTaskId) {
        this.vDistributionTaskId = vDistributionTaskId;
    }

    @JsonProperty("vFileUrl")
    public String getvFileUrl() {
        return vFileUrl;
    }

    public void setvFileUrl(String vFileUrl) {
        this.vFileUrl = vFileUrl;
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
}
