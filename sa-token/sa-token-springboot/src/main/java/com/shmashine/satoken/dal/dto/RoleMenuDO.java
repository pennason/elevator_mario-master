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
 * 角色菜单表
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/3/19 14:54
 * @since v1.0.0
 */
@TableName("tbl_sys_role_menu")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleMenuDO implements Serializable {

    /**
     * 角色Id
     */
    @TableId("v_role_id")
    private String roleId;

    /**
     * 菜单Id
     */
    @TableField("v_menu_id")
    private String menuId;

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

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    @TableField("i_del_flag")
    private Integer delFlag;

}
