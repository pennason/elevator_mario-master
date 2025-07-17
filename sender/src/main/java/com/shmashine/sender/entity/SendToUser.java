package com.shmashine.sender.entity;

import lombok.Data;

/**
 * 推送对接用户
 */
@Data
public class SendToUser {

    /**
     * 麦信电梯code
     */
    private String elevatorCode;

    /**
     * 推送用户
     */
    private String userName;

    /**
     * 电梯注册码
     */
    private String registrtionCode;

    /**
     * 制造单位统一社会信用代码 新装电梯必填
     */
    private String manufacturerCode;

    /**
     * 出厂编号 新装电梯必填
     */
    private String leaveFactoryNumber;
}
