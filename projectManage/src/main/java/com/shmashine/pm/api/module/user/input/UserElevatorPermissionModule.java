package com.shmashine.pm.api.module.user.input;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

/**
 * 用户授权
 *
 * @author chenxue
 */

@Data
@ToString
public class UserElevatorPermissionModule implements Serializable {

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
