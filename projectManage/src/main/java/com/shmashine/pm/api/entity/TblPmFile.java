package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 现勘任务上传文档
 */
public class TblPmFile implements Serializable {

    private static final long serialVersionUID = 1449664362887421053L;

    private String vPmFileId;

    private String vTargetId;

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

    @JsonProperty("vPmFileId")
    public String getvPmFileId() {
        return vPmFileId;
    }

    public void setvPmFileId(String vPmFileId) {
        this.vPmFileId = vPmFileId;
    }

    @JsonProperty("vTargetId")
    public String getvTargetId() {
        return vTargetId;
    }

    public void setvTargetId(String vTargetId) {
        this.vTargetId = vTargetId;
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

    @Override
    public String toString() {
        return "TblPmFile{" +
                "vPmFileId='" + vPmFileId + '\'' +
                ", vTargetId='" + vTargetId + '\'' +
                ", vFileUrl='" + vFileUrl + '\'' +
                ", dtCreateTime=" + dtCreateTime +
                ", dtModifyTime=" + dtModifyTime +
                ", vCreateUserId='" + vCreateUserId + '\'' +
                ", vModifyUserId='" + vModifyUserId + '\'' +
                ", iDelFlag=" + iDelFlag +
                '}';
    }
}
