package com.shmashine.satoken.dal.dto;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户角色表
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/19 14:42
 * @since v1.0.0
 */
@TableName("tbl_sys_user_role")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoleDO implements Serializable {

    /**
     * 用户ID
     */
    @TableId("v_user_id")
    private String userId;

    /**
     * 角色Id
     */
    @TableField("v_role_id")
    private String roleId;

    /**
     * 创建时间
     */
    @TableField("dt_createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("dt_modifyTime")
    private Date modifyTime;

    /**
     * 创建人
     */
    @TableField("v_createid")
    private String createId;

    /**
     * 更新人
     */
    @TableField("v_modifyid")
    private String modifyid;

}
