package com.shmashine.api.module.role.input;

import com.shmashine.common.entity.base.PageListParams;

public class SearchRoleListModule extends PageListParams {
    /**
     * 角色编号
     */
    private String vRoleId;
    /**
     * 角色名称
     */
    private String vRoleName;

    public String getvRoleId() {
        return vRoleId;
    }

    public void setvRoleId(String vRoleId) {
        this.vRoleId = vRoleId;
    }

    public String getvRoleName() {
        return vRoleName;
    }

    public void setvRoleName(String vRoleName) {
        this.vRoleName = vRoleName;
    }

    @Override
    public String toString() {
        return "SearchRoleListModule{" +
                "vRoleId='" + vRoleId + '\'' +
                ", vRoleName='" + vRoleName + '\'' +
                '}';
    }
}
