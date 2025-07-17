package com.shmashine.api.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/27 9:50
 * @info 海尔摄像头推送松江电信项目信息
 */
@Data
public class TblHaierProject {

    /**
     * 项目projectId
     */
    private String id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目所在地址
     */
    private String address;

    /**
     * 项目图片地址
     */
    private String imgPath;

    /**
     * 电梯安全管理员手机号
     */
    private String safeManMobile;

    /**
     * 维保公司名称
     */
    private String maintenanceName;

    /**
     * 电梯安全管理员
     */
    private String safeMan;

    /**
     * 城市名
     */
    private String cityName;

    /**
     * 值守电话
     */
    private String phone;

    /**
     * 物业公司名称
     */
    private String propertyName;

    /**
     * 分区名
     */
    private String countryName;

    /**
     * 省份名
     */
    private String provinceName;

    /**
     * 维保类型
     */
    private String maintenanceType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;
}
