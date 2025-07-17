package com.shmashine.api.module.elevator;


import lombok.Data;

@Data
public class ElevatorResourceModule {

    /**
     * 所属项目
     */
    private String projectId;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 电梯编码
     */
    private String codes;
}