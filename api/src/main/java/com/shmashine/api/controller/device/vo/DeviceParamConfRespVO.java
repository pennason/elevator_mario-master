package com.shmashine.api.controller.device.vo;


import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2023/12/4 14:09
 * @description: 设备参数配置结果返回对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeviceParamConfRespVO extends DeviceParamConfBaseVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 最近配置时间
     */
    private Date lastConfTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
