package com.shmashine.api.module.user.input;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @PackgeName: com.shmashine.api.module.user.input
 * @ClassName: DeleteUserModule
 * @Date: 2020/6/916:35
 * @Author: LiuLiFu
 * @Description: 删除用户所需参数类
 */
public class DeleteUserModule {

    @NotNull(message = "请选择用户")
    @Length(max = 50, min = 1, message = "请选择删除用户的编号")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
