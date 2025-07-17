package com.shmashine.socket.elevator.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 重定向电梯映射表
 */
@Data
public class TblRedirectElevatorMapping implements Serializable {

    private static final long serialVersionUID = -25949284524752002L;
    /**
     * 唯一ID
     */
    private String id;
    /**
     * 电梯编号
     */
    private String vElevatorCode;
    /**
     * 重定向电梯编号列表
     */
    private String vRedirectCodes;

}