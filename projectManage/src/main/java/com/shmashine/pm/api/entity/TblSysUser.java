package com.shmashine.pm.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TblSysUser implements Serializable {

    private static final long serialVersionUID = -4198967277496531140L;

    private String vUserId;
    /**
     * 用户名
     */
    private String vUsername;
    /**
     * 密码
     */
    private String vPassword;
    /**
     * open_id 微信小程序使用
     */
    private String openId;
    /**
     * 姓名
     */
    private String vName;
    /**
     * 邮箱
     */
    private String vEmail;
    /**
     * 手机号
     */
    private String vMobile;
    /**
     * 状态 0:禁用，1:正常
     */
    private Integer iStatus;
    /**
     * 性别
     */
    private Long vSex;
    /**
     * 出身日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date vBirth;
    /**
     * 图片id
     */
    private String vPicId;
    /**
     * 省市区id
     */
    private String vProvincialCity;
    /**
     * 现居住地
     */
    private String vAddress;
    /**
     * 爱好
     */
    private String vHobby;
    /**
     * 是否接收工单 1:不接收, 0接收
     */
    private Integer iWorkOrderFlag;
    /**
     * 是否接收故障短信 1：不接收，0：接收
     */
    private Integer iSendPhoneStatus;
    /**
     * 是否接收故障电话 1：不接收，0：接收
     */
    private Integer iSendMessageStatus;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtCreatetime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dtModifytime;
    /**
     * 创建人
     */
    private String vCreateid;
    /**
     * 修改人
     */
    private String vModifyid;

    @JsonProperty("vUserId")
    public String getVUserId() {
        return vUserId;
    }

    public void setVUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    @JsonProperty("vUsername")
    public String getVUsername() {
        return vUsername;
    }

    public void setVUsername(String vUsername) {
        this.vUsername = vUsername;
    }

    @JsonProperty("vPassword")
    public String getVPassword() {
        return vPassword;
    }

    public void setVPassword(String vPassword) {
        this.vPassword = vPassword;
    }

    @JsonProperty("openId")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @JsonProperty("vName")
    public String getVName() {
        return vName;
    }

    public void setVName(String vName) {
        this.vName = vName;
    }

    @JsonProperty("vEmail")
    public String getVEmail() {
        return vEmail;
    }

    public void setVEmail(String vEmail) {
        this.vEmail = vEmail;
    }

    @JsonProperty("vMobile")
    public String getVMobile() {
        return vMobile;
    }

    public void setVMobile(String vMobile) {
        this.vMobile = vMobile;
    }

    @JsonProperty("iStatus")
    public Integer getIStatus() {
        return iStatus;
    }

    public void setIStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    @JsonProperty("vSex")
    public Long getVSex() {
        return vSex;
    }

    public void setVSex(Long vSex) {
        this.vSex = vSex;
    }

    @JsonProperty("vBirth")
    public Date getVBirth() {
        return vBirth;
    }

    public void setVBirth(Date vBirth) {
        this.vBirth = vBirth;
    }

    @JsonProperty("vPicId")
    public String getVPicId() {
        return vPicId;
    }

    public void setVPicId(String vPicId) {
        this.vPicId = vPicId;
    }

    @JsonProperty("vProvincialCity")
    public String getVProvincialCity() {
        return vProvincialCity;
    }

    public void setVProvincialCity(String vProvincialCity) {
        this.vProvincialCity = vProvincialCity;
    }

    @JsonProperty("vAddress")
    public String getVAddress() {
        return vAddress;
    }

    public void setVAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    @JsonProperty("vHobby")
    public String getVHobby() {
        return vHobby;
    }

    public void setVHobby(String vHobby) {
        this.vHobby = vHobby;
    }

    @JsonProperty("iWorkOrderFlag")
    public Integer getIWorkOrderFlag() {
        return iWorkOrderFlag;
    }

    public void setIWorkOrderFlag(Integer iWorkOrderFlag) {
        this.iWorkOrderFlag = iWorkOrderFlag;
    }

    @JsonProperty("iSendPhoneStatus")
    public Integer getISendPhoneStatus() {
        return iSendPhoneStatus;
    }

    public void setISendPhoneStatus(Integer iSendPhoneStatus) {
        this.iSendPhoneStatus = iSendPhoneStatus;
    }

    @JsonProperty("iSendMessageStatus")
    public Integer getISendMessageStatus() {
        return iSendMessageStatus;
    }

    public void setISendMessageStatus(Integer iSendMessageStatus) {
        this.iSendMessageStatus = iSendMessageStatus;
    }

    @JsonProperty("dtCreatetime")
    public Date getDtCreatetime() {
        return dtCreatetime;
    }

    public void setDtCreatetime(Date dtCreatetime) {
        this.dtCreatetime = dtCreatetime;
    }

    @JsonProperty("dtModifytime")
    public Date getDtModifytime() {
        return dtModifytime;
    }

    public void setDtModifytime(Date dtModifytime) {
        this.dtModifytime = dtModifytime;
    }

    @JsonProperty("vCreateid")
    public String getVCreateid() {
        return vCreateid;
    }

    public void setVCreateid(String vCreateid) {
        this.vCreateid = vCreateid;
    }

    @JsonProperty("vModifyid")
    public String getVModifyid() {
        return vModifyid;
    }

    public void setVModifyid(String vModifyid) {
        this.vModifyid = vModifyid;
    }
}
