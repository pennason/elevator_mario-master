package com.shmashine.satoken.dal.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户表
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/18 16:28
 * @since v1.0.0
 */
@TableName("tbl_sys_user")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminUserDO implements Serializable {

    /**
     * 用户ID
     */
    @TableId("v_user_id")
    private String id;

    /**
     * 用户账号
     */
    @TableField("v_username")
    private String username;

    /**
     * 加密后的密码
     */
    @TableField("v_password")
    private String password;

    /**
     * 用户微信ID
     */
    @TableField("open_id")
    private String openId;

    /**
     * 用户昵称
     */
    @TableField("v_name")
    private String nickname;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 部门ID
     */
    @TableField(exist = false)
    private Long deptId;

    /**
     * 岗位编号数组
     */
    @TableField(exist = false)
    private Set<Long> postIds;

    /**
     * 用户邮箱
     */
    @TableField("v_email")
    private String email;

    /**
     * 手机号码
     */
    @TableField("v_mobile")
    private String mobile;

    /**
     * 用户性别
     */
    @TableField("v_sex")
    private Integer sex;

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String avatar;

    /**
     * 帐号状态 (0, "开启"), (1, "关闭");
     */
    @TableField("i_status")
    private Integer status;

    /**
     * 最后登录IP
     */
    @TableField(exist = false)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @TableField(exist = false)
    private LocalDateTime loginDate;

}
