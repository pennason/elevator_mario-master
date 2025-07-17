package com.shmashine.api.module.elevator;


import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

/**
 * 梯内屏幕实体类
 *
 * @author little.li
 */
@Data
public class ElevatorScreenModule {

    /**
     * 电梯地址
     */
    private String address;

    /**
     * 维保单位名称
     */
    private String maintainCompanyName;

    /**
     * 物业单位名称
     */
    private String propertyCompanyName;

    /**
     * 上次维保日期
     */
    private Object lastMaintainDate;

    /**
     * 上次年检时间
     */
    private Object lastInspectDate;

    /**
     * 下次检验日期
     */
    private Object nextInspectDate;

    /**
     * 维保电话
     */
    private String maintainPersonTel;

    /**
     * 应急服务电话
     */
    private String emergencyPersonTel;

    /**
     * 电梯位置码
     */
    private String sglnCode;

    /**
     * 物联设备编号
     */
    private String equipmentCode;


    private JSONObject weather;


}