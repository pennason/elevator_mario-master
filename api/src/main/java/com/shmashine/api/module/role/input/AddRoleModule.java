package com.shmashine.api.module.role.input;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

/**
 * 添加角色参数类
 *
 * @Date: 2020/6/99:36
 * @Author: LiuLiFu
 */
public class AddRoleModule {

    @NotNull(message = "请输入角色名称")
    @Length(max = 100, min = 1, message = "角色名称不能超过100个字，且不能小于1个字")
    /**角色名称*/
    private String vRoleName;

    /**
     * 角色备注
     */
    private String vRemark;


    /**
     * 拥有的菜单
     */
    @NotNull(message = "权限不能为空")
    @Size(min = 1, message = "最少请选择一个菜单权限")
    private List<String> menu;

    public String getvRoleName() {
        return vRoleName;
    }

    public void setvRoleName(String vRoleName) {
        this.vRoleName = vRoleName;
    }

    public String getvRemark() {
        return vRemark;
    }

    public void setvRemark(String vRemark) {
        this.vRemark = vRemark;
    }

    public List<String> getMenu() {
        return menu;
    }

    public void setMenu(List<String> menu) {
        this.menu = menu;
    }
}
