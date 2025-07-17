package com.shmashine.commonbigscreen.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 电梯表
 *
 * @author jiangheng
 * @version V1.0.0 -  2022/3/3 16:13
 */
@TableName("tbl_elevator")
@Data
public class Elevator {

    /**
     * 电梯唯一ID
     */
    private String elevatorId;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 唯一注册码
     */
    private String equipmentCode;

    /**
     * 地址
     */
    private String address;

    /**
     * 电梯名称
     */
    private String elevatorName;

    /**
     * 电梯类型
     */
    private String elevatorType;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 楼宇id
     */
    private String buildingId;
}
