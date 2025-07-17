package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblOrderInstall implements Serializable {

    private static final long serialVersionUID = -2667525814425855553L;

    private String vOrderInstallId;

    private String vOrderBlankId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dtInstallDate;

    private String vInstaller;

    private Integer iStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreateAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifyAt;

    private String vUserId;

    private TblOrderBlank tblOrderBlank;

    private Integer iDelFlag;

    @JsonProperty("vOrderInstallId")
    public String getvOrderInstallId() {
        return vOrderInstallId;
    }

    public void setvOrderInstallId(String vOrderInstallId) {
        this.vOrderInstallId = vOrderInstallId;
    }

    @JsonProperty("vOrderBlankId")
    public String getvOrderBlankId() {
        return vOrderBlankId;
    }

    public void setvOrderBlankId(String vOrderBlankId) {
        this.vOrderBlankId = vOrderBlankId;
    }

    @JsonProperty("dtInstallDate")
    public Date getDtInstallDate() {
        return dtInstallDate;
    }

    public void setDtInstallDate(Date dtInstallDate) {
        this.dtInstallDate = dtInstallDate;
    }

    @JsonProperty("vInstaller")
    public String getvInstaller() {
        return vInstaller;
    }

    public void setvInstaller(String vInstaller) {
        this.vInstaller = vInstaller;
    }

    @JsonProperty("iStatus")
    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("dtCreateAt")
    public Date getDtCreateAt() {
        return dtCreateAt;
    }

    public void setDtCreateAt(Date dtCreateAt) {
        this.dtCreateAt = dtCreateAt;
    }

    @JsonProperty("dtModifyAt")
    public Date getDtModifyAt() {
        return dtModifyAt;
    }

    public void setDtModifyAt(Date dtModifyAt) {
        this.dtModifyAt = dtModifyAt;
    }

    @JsonProperty("vUserId")
    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    @JsonProperty("tblOrderBlank")
    public TblOrderBlank getTblOrderBlank() {
        return tblOrderBlank;
    }

    public void setTblOrderBlank(TblOrderBlank tblOrderBlank) {
        this.tblOrderBlank = tblOrderBlank;
    }

    @JsonProperty("iDelFlag")
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }
}
