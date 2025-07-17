package com.shmashine.common.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.annotation.FieldEncrypt;
import com.shmashine.common.annotation.SensitiveData;

/**
 * 用户表(TblSysUser)实体类
 *
 * @author makejava
 * @since 2020-07-17 15:51:13
 */
@SensitiveData
public class TblSysUser implements Serializable {
    private static final long serialVersionUID = -20991897258596186L;
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
    @FieldEncrypt(key = "vName")
    public String vName;
    /**
     * 邮箱
     */
    @FieldEncrypt(key = "vEmail")
    public String vEmail;
    /**
     * 手机号
     */
    @FieldEncrypt(key = "vMobile")
    public String vMobile;
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
     * 备注
     */
    private String remark;
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
    /**
     * 岗位
     */
    private String vPosition;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名
     */
    private String deptName;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 是否仪电平台 1 仪电 2 麦信
     */
    private Integer iWuyePlatform;

    /**
     * 身份证
     */
    @FieldEncrypt(key = "vIdentity")
    public String vIdentity;
    /**
     * 电梯操作证书编号
     */
    private String vOpsCertCode;
    /**
     * 电梯操作证书发证机关
     */
    private String vOpsCertAgency;

    /**
     * 登录是否需要验证码-role表配置
     */
    private Boolean bLoginVerifyPhone;
    /**
     * 证书过期时间
     */
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date dtOpsCertExpire;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getvPosition() {
        return vPosition;
    }

    public void setvPosition(String vPosition) {
        this.vPosition = vPosition;
    }

    public String getvIdentity() {
        return vIdentity;
    }

    public void setvIdentity(String vIdentity) {
        this.vIdentity = vIdentity;
    }

    public String getvOpsCertCode() {
        return vOpsCertCode;
    }

    public void setvOpsCertCode(String vOpsCertCode) {
        this.vOpsCertCode = vOpsCertCode;
    }

    public String getvOpsCertAgency() {
        return vOpsCertAgency;
    }

    public void setvOpsCertAgency(String vOpsCertAgency) {
        this.vOpsCertAgency = vOpsCertAgency;
    }

    public Date getDtOpsCertExpire() {
        return dtOpsCertExpire;
    }

    public void setDtOpsCertExpire(Date dtOpsCertExpire) {
        this.dtOpsCertExpire = dtOpsCertExpire;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getiWuyePlatform() {
        return iWuyePlatform;
    }

    public void setiWuyePlatform(Integer iWuyePlatform) {
        this.iWuyePlatform = iWuyePlatform;
    }

    public Boolean getbLoginVerifyPhone() {
        return bLoginVerifyPhone;
    }

    public void setbLoginVerifyPhone(Boolean bLoginVerifyPhone) {
        this.bLoginVerifyPhone = bLoginVerifyPhone;
    }
}