package com.shmashine.userclientapplets.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信用户表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tbl_sys_we_chat_user")
public class WeChatUser {

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 微信用户唯一标识
     */
    private String unionId;

    /**
     * 微信用户应用唯一标识
     */
    private String openId;

    /**
     * 用户名
     */
    private String userId;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 性别(0: 女 1：男)
     */
    private Integer gender;

    /**
     * 小程序name
     */
    private String appName;

    /**
     * 角色
     */
    private String role;

    /**
     * 登录密码
     */
    private String passWord;

    /**
     * 是否注册（0：未注册 1：注册）
     */
    private Integer isRegister;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 逻辑删除标识（0：正常 1：删除）
     */
    private Integer isDeleted;
    /**
     * 系统角色id
     */
    private String roleId;

    /**
     * 部门id
     */
    private String deptId;

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


}
