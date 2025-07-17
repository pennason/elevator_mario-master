package com.shmashine.api.module.user.input;

import javax.validation.constraints.NotNull;

/**
 * @PackgeName: com.shmashine.api.module.user.input
 * @ClassName: ChangePassword
 * @Date: 2020/6/917:07
 * @Author: LiuLiFu
 * @Description: 用户修改密码 接口参数类
 */
public class ChangePasswordModule {

    @NotNull(message = "用户 id 不能为空")
    private String vUserId;

    @NotNull(message = "请输入旧密码")
    private String oldPassword;
    @NotNull(message = "请输入新密码")
    private String newPassword;
    @NotNull(message = "请输入第二次新密码")
    private String changePassword;

    public String getvUserId() {
        return vUserId;
    }

    public void setvUserId(String vUserId) {
        this.vUserId = vUserId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(String changePassword) {
        this.changePassword = changePassword;
    }
}
