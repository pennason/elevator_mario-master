package com.shmashine.api.module.login.input;

import javax.validation.constraints.NotNull;

public class MobileLoginModule {

    // 手机号码
    @NotNull(message = "手机号不能为空")
    private String mobileNum;

    @NotNull(message = "验证码不能为空")
    private String verifyCode;

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
