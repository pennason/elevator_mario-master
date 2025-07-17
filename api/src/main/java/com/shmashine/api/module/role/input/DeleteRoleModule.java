package com.shmashine.api.module.role.input;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.role.input
 * @ClassName: DeleteRoleModule
 * @Date: 2020/6/99:55
 * @Author: LiuLiFu
 * @Description: 删除角色参数类
 */
public class DeleteRoleModule {
    @NotNull(message = "请输入角色编号")
    /**角色编号*/
    private String vRoleId;

    public String getvRoleId() {
        return vRoleId;
    }

    public void setvRoleId(String vRoleId) {
        this.vRoleId = vRoleId;
    }
}
