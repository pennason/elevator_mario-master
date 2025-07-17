package com.shmashine.api.module.mashineService;

import java.util.ArrayList;

import com.shmashine.common.entity.base.PageListParams;

public class MashineServiceBizMaintenanceModule extends PageListParams {
    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }
}
