package com.shmashine.haierCamera.entity;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/21 10:37
 */
@Data
public class HaierCameraFault {

    /**
     * 电梯登记注册代码
     */
    private String registerCode;

    /**
     * 故障编码，电动车入梯编码为10101
     */
    private String faultCode;

    /**
     * 类型，1-故障发生，2-故障恢复
     */
    private Integer type;

    /**
     * 故障发生或恢复时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String createTime;

    /**
     * 故障发生楼层
     */
    private String floor;
}
