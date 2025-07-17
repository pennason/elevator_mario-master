package com.shmashine.pm.api.module.project.input;

import java.util.ArrayList;

public class SearchElevatorProjectModule {
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
