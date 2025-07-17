package com.shmashine.api.module.user.input;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.user.input
 * @ClassName: UserElevatorPermissionModule
 * @Date: 2020/6/1915:59
 * @Author: LiuLiFu
 * @Description: 用户数据授权接口所需参数
 */
public class UserElevatorPermissionModule {

    // 需要授权的数据
    private List<String> resourceIds;

    @NotNull(message = "请输入授权的用户")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<String> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
