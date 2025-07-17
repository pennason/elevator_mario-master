package com.shmashine.api.module.ruijin;

import lombok.Data;

@Data
public class EventDownloadModuleMap {
    /**
     * 电梯注册码
     */
    private String elevatorName;
    /**
     * 电梯编号
     */
    private String address;
    /**
     * 省ID
     */
    private String reportTime;
    /**
     * 市ID
     */
    private String faultName;
    /**
     * 区ID
     */
    private String status;
}
