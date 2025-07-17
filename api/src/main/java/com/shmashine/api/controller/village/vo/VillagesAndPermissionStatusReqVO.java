package com.shmashine.api.controller.village.vo;

import javax.validation.constraints.NotNull;

import com.shmashine.api.module.village.input.SearchVillaSelectListModule;

/**
 * @PackgeName: com.shmashine.api.module.village.input
 * @ClassName: SearchVillaModule
 * @Date: 2020/7/611:07
 * @Author: LiuLiFu
 * @Description: 查找小区下拉框接口模型
 */
public class VillagesAndPermissionStatusReqVO extends SearchVillaSelectListModule {

    @NotNull(message = "请输入授权用户Id")
    private String permissionUserId;

    public String getPermissionUserId() {
        return permissionUserId;
    }

    public void setPermissionUserId(String permissionUserId) {
        this.permissionUserId = permissionUserId;
    }
}
