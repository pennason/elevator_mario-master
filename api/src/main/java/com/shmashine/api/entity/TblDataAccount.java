package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 北向数据用户实体类
 */
public class TblDataAccount implements Serializable {

    private static final long serialVersionUID = -208040115381131757L;

    private String vDataAccountId;

    private String vAccountName;

    private String vAccountCode;

    /**
     * 推送地址
     **/
    private String vPushUrl;

    /**
     * 延迟时间（单位小时）
     **/
    private Integer iDelayDuration;

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

    @JsonProperty("vDataAccountId")
    public String getvDataAccountId() {
        return vDataAccountId;
    }

    public void setvDataAccountId(String vDataAccountId) {
        this.vDataAccountId = vDataAccountId;
    }

    @JsonProperty("vAccountName")
    public String getvAccountName() {
        return vAccountName;
    }

    public void setvAccountName(String vAccountName) {
        this.vAccountName = vAccountName;
    }

    @JsonProperty("vAccountCode")
    public String getvAccountCode() {
        return vAccountCode;
    }

    public void setvAccountCode(String vAccountCode) {
        this.vAccountCode = vAccountCode;
    }

    @JsonProperty("vPushUrl")
    public String getvPushUrl() {
        return vPushUrl;
    }

    public void setvPushUrl(String vPushUrl) {
        this.vPushUrl = vPushUrl;
    }

    @JsonProperty("iDelayDuration")
    public Integer getiDelayDuration() {
        return iDelayDuration;
    }

    public void setiDelayDuration(Integer iDelayDuration) {
        this.iDelayDuration = iDelayDuration;
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
    public Integer getiDelFlag() {
        return iDelFlag;
    }

    public void setiDelFlag(Integer iDelFlag) {
        this.iDelFlag = iDelFlag;
    }
}
