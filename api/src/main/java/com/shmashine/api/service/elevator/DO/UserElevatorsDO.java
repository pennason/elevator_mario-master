package com.shmashine.api.service.elevator.DO;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/5/25 14:53
 * @description: 用户授权电梯 resp VO
 */
@Data
public class UserElevatorsDO {

    /**
     * 电梯id
     */
    private String elevatorId;
    /**
     * 电梯code
     */
    private String elevatorCode;
    /**
     * 电梯name
     */
    private String elevatorName;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 电梯地址
     */
    private String address;
    /**
     * 电梯类型
     */
    private Integer elevatorType;
    /**
     * 在线状态
     */
    private Integer online;
    /**
     * 小区id
     */
    private String villageId;
    /**
     * 小区name
     */
    private String villageName;
    /**
     * 是否授权给用户
     */
    private Integer userFlag;


}
