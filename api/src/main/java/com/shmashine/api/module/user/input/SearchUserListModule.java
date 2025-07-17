package com.shmashine.api.module.user.input;

import java.util.ArrayList;

import com.shmashine.common.entity.base.PageListParams;
import com.shmashine.common.annotation.FieldEncrypt;
import com.shmashine.common.annotation.SensitiveData;

/**
 * 查询用户列表入参模型
 */
@SensitiveData
public class SearchUserListModule extends PageListParams {
    /**
     * 用户名
     */
    private String vUserId;
    /**
     * 姓名
     */
    @FieldEncrypt(key = "vName")
    private String vName;
    /**
     * 部门编号
     */
    private String vDeptId;

    private Integer iStatus;
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

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvDeptId() {
        return vDeptId;
    }

    public void setvDeptId(String vDeptId) {
        this.vDeptId = vDeptId;
    }

    @Override
    public String toString() {
        return "SearchUserListModule{"
                + "vUsername='" + vUserId + '\''
                + ", vName='" + vName + '\''
                + ", vDeptId='" + vDeptId + '\'' + '}';
    }
}
