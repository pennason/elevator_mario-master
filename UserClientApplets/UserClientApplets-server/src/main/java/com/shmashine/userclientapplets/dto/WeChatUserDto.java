package com.shmashine.userclientapplets.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 微信用户DTO
 */
@Data
public class WeChatUserDto {

    /**
     * 小程序登录码，用于获取用户openid
     */
    @NotBlank(message = "jsCode不为空")
    private String jsCode;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不为空")
    private String userName;

    /**
     * 角色
     */
    @NotBlank(message = "角色不为空")
    private String role;

    /**
     * 备注
     */
    private String comment;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不为空")
    private String phoneNumber;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不为空")
    private String code;

    /**
     * 小程序name
     */
    @NotBlank(message = "小程序name不为空")
    private String appName;

}
