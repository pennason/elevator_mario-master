package com.shmashine.api.module.login.input;

import javax.validation.constraints.NotNull;

public class ReqestLoginModule {


    // 用户名
    @NotNull(message = "用户名不能为空")
    private String userName;
    // 密码
    @NotNull(message = "密码不能为空")
    private String passWord;
    //验证码
    private String verifyCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "ReqestLoginModule{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}