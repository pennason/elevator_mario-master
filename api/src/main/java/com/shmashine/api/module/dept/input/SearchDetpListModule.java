package com.shmashine.api.module.dept.input;

import java.util.ArrayList;

import com.shmashine.common.entity.base.PageListParams;

/**
 * 列表查询所需参数类
 */
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

    /**
     * 地址
     */
    private String vAddress;
    /**
     *
     */
    private Integer iDeptTypeId;
    /**
     * 是否是外协 0 否 1 是
     */
    private Integer iOutService;

    /**
     * 权限列表
     */
    private ArrayList<String> permissionDeptIds;

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

    public ArrayList<String> getPermissionDeptIds() {
        return permissionDeptIds;
    }

    public void setPermissionDeptIds(ArrayList<String> permissionDeptIds) {
        this.permissionDeptIds = permissionDeptIds;
    }

    public Integer getiOutService() {
        return iOutService;
    }

    public void setiOutService(Integer iOutService) {
        this.iOutService = iOutService;
    }

    @Override
    public String toString() {
        return "SearchDetpListModule{" +
                "vDeptId='" + vDeptId + '\'' +
                ", vDeptName='" + vDeptName + '\'' +
                ", vParentId='" + vParentId + '\'' +
                ", vAddress='" + vAddress + '\'' +
                ", iDeptTypeId=" + iDeptTypeId +
                '}';
    }
}
