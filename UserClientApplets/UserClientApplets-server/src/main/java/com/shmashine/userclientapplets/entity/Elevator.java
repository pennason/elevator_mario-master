package com.shmashine.userclientapplets.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电梯实体类
 *
 * @author jiangheng
 * @version v1.0.0 - 2022/2/8 14:35
 */
@TableName("tbl_elevator")
@Data
@EqualsAndHashCode(callSuper = false)
public class Elevator extends BaseEntity {

    /**
     * 电梯id
     */
    private String vElevatorId;

    /**
     * 电梯name
     */
    private String vElevatorName;

    /**
     * 电梯code
     */
    private String vElevatorCode;

    /**
     * 电梯安装地址
     */
    private String vAddress;

    @TableField("v_village_id")
    private String villageId;

    /**
     * 电梯收藏标识（0：未收藏；1：收藏）
     */
    private Integer isCollect;

    /**
     * 电梯类型
     */
    private String elevatorTypeName;

    /**
     * 唯一注册码
     */
    private String equipmentCode;

    /**
     * 物业单位
     */
    private String propertyCompanyName;

    /**
     * 维保单位
     */
    private String maintainCompanyName;

    /**
     * 电梯品牌
     */
    private String elevatorBrandName;

    /**
     * 政府部门
     */
    private String governmentName;

    /**
     * 维保人
     */
    private String maintainPersonName;

    /**
     * 应急处理人
     */
    private String emergencyPersonName;

    /**
     * 上次维护
     */
    private String lastMaintainDate;

    /**
     * 下次维护
     */
    private String nextMaintainDate;

    /**
     * 下次年检
     */
    private String nextInspectDate;

    /**
     * 监控地址
     */
    private String hlsUrl;

}
