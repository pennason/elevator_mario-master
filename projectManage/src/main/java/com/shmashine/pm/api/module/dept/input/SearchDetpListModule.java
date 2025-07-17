package com.shmashine.pm.api.module.dept.input;

import java.util.ArrayList;

import com.shmashine.pm.api.entity.base.PageListParams;

public class SearchDetpListModule extends PageListParams {

    /**
     * 部门编号
     */
    private String vDeptId;

    /**
     * 部门名称
     */
    private String vDeptName;

    /**
     * 部门父类编号
     */
    private String vParentId;

    private String vAddress;

    private Integer iDeptTypeId;

    public String getvParentId() {
        return vParentId;
    }

    public void setvParentId(String vParentId) {
        this.vParentId = vParentId;
    }

    public String getvDeptId() {
        return vDeptId;
    }

    public void setvDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    public String getvDeptName() {
        return vDeptName;
    }

    public void setvDeptName(String vDeptName) {
        this.vDeptName = vDeptName;
    }

    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    public Integer getiDeptTypeId() {
        return iDeptTypeId;
    }

    public void setiDeptTypeId(Integer iDeptTypeId) {
        this.iDeptTypeId = iDeptTypeId;
    }

    private ArrayList<String> permissionDeptIds;

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

    @Override
    public String toString() {
        return "SearchDetpListModule{" +
                "vDeptId='" + vDeptId + '\'' +
                ", vDeptName='" + vDeptName + '\'' +
                '}';
    }
}
