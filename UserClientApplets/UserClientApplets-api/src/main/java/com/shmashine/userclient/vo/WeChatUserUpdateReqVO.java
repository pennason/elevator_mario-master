package com.shmashine.userclient.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 小程序用户更新请求体
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/4/16 13:38
 * @Since: 1.0.0
 */
@Data
public class WeChatUserUpdateReqVO {

    /**
     * 主键id
     */
    @NotBlank
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 角色
     */
    private String role;

    /**
     * 备注
     */
    private String comment;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 授权电梯列表
     */
    private List<String> elevatorIds;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 状态（1：通过，2：拒绝）
     */
    private Integer isRegister;

    /**
     * 是否推送电瓶车统计短信
     */
    private Integer pushBatteryCar;

    /**
     * 推送电瓶车统计短信时间点
     */
    private Integer pushBatteryCarTime;

    /**
     * 是否推送困人短信
     */
    private Integer pushTrappedPeople;

    /**
     * 删除标识（0：正常，1：删除）
     */
    private Integer isDeleted;

}
