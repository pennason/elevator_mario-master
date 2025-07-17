package com.shmashine.api.module.user.input;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 添加用户所需参数类
 *
 * @Date: 2020/6/915:34
 * @Author: LiuLiFu
 */
public class AddUserModule {

    @NotNull(message = "用户账号不能为空")
    @Length(max = 20, min = 1, message = "用户账号，不能超过20位数，且不能小于一位")
    /** 用户账号 */
    private String vUserId;

    @Length(max = 20, min = 1, message = "用户名，不能超过20位数，且不能小于一位")
    /**用户名*/
    private String vUsername;
    @Length(max = 100, message = "姓名不能超过100位")
    /**姓名*/
    private String vName;
    @Length(max = 100, message = "邮箱不能超过100位")
    /**邮箱*/
    private String vEmail;
    @Length(max = 100, message = "手机号不能超过100位")
    /**手机号*/
    private String vMobile;

    /**
     * 所属部门
     */
    @NotNull(message = "所属部门不能为空")
    private String deptId;

    /**
     * 所属角色
     */
    @NotNull(message = "所属角色不能为空")
    private String roleId;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getiWorkOrderFlag() {
        return iWorkOrderFlag;
    }

    public void setiWorkOrderFlag(Integer iWorkOrderFlag) {
        this.iWorkOrderFlag = iWorkOrderFlag;
    }

    public Integer getiSendPhoneStatus() {
        return iSendPhoneStatus;
    }

    public void setiSendPhoneStatus(Integer iSendPhoneStatus) {
        this.iSendPhoneStatus = iSendPhoneStatus;
    }

    public Integer getiSendMessageStatus() {
        return iSendMessageStatus;
    }

    public void setiSendMessageStatus(Integer iSendMessageStatus) {
        this.iSendMessageStatus = iSendMessageStatus;
    }

    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    public String getvUsername() {
        return vUsername;
    }

    public void setvUsername(String vUsername) {
        this.vUsername = vUsername;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvEmail() {
        return vEmail;
    }

    public void setvEmail(String vEmail) {
        this.vEmail = vEmail;
    }

    public String getvMobile() {
        return vMobile;
    }

    public void setvMobile(String vMobile) {
        this.vMobile = vMobile;
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
}
