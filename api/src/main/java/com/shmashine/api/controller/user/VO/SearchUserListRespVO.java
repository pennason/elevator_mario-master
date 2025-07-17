package com.shmashine.api.controller.user.VO;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shmashine.common.annotation.FieldEncrypt;
import com.shmashine.common.annotation.SensitiveData;

import lombok.Data;

/**
 * 默认说明
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/5/21 16:21
 * @Since: 1.0.0
 */
@Data
@SensitiveData
public class SearchUserListRespVO implements Serializable {

    @JsonProperty("vUserId")
    private String vUserId;
    /**
     * 用户名
     */
    @JsonProperty("vUsername")
    private String vUsername;
    /**
     * 密码
     */
    @JsonProperty("vPassword")
    private String vPassword;
    /**
     * open_id 微信小程序使用
     */
    private String openId;
    /**
     * 姓名
     */
    @JsonProperty("vName")
    @FieldEncrypt(key = "vName")
    private String vName;
    /**
     * 邮箱
     */
    @JsonProperty("vEmail")
    @FieldEncrypt(key = "vEmail")
    private String vEmail;
    /**
     * 手机号
     */
    @JsonProperty("vMobile")
    @FieldEncrypt(key = "vMobile")
    private String vMobile;
    /**
     * 状态 0:禁用，1:正常
     */
    @JsonProperty("iStatus")
    private Integer iStatus;

    @JsonProperty("iStatusName")
    private String iStatusName;

    private String wecomUserId;

    private String wecomUserName;

    /**
     * 是否接收工单 1:不接收, 0接收
     */
    @JsonProperty("iWorkOrderFlag")
    private Integer iWorkOrderFlag;

    @JsonProperty("iWorkOrderFlagName")
    private String iWorkOrderFlagName;

    /**
     * 是否接收故障短信 1：不接收，0：接收
     */
    @JsonProperty("iSendPhoneStatus")
    private Integer iSendPhoneStatus;

    @JsonProperty("iSendPhoneStatusName")
    private String iSendPhoneStatusName;

    /**
     * 是否接收故障电话 1：不接收，0：接收
     */
    @JsonProperty("iSendMessageStatus")
    private Integer iSendMessageStatus;

    @JsonProperty("iSendMessageStatusName")
    private String iSendMessageStatusName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 岗位
     */
    @JsonProperty("vPosition")
    private String vPosition;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门名
     */
    @JsonProperty("vDeptName")
    private String vDeptName;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名
     */
    private String roleName;

}
