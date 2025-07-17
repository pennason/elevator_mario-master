package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 电梯基础信息关联省市区维保等信息
 *
 * @author jiangheng
 * @since 2022/11/10 10:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TblElevatorInfos extends TblElevator implements Serializable {

    @Serial
    private static final long serialVersionUID = 28465663896790557L;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区县
     */
    private String areaName;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 电梯品牌
     */
    private String elevatorBrandName;

    /**
     * 维保公司
     */
    private String maintainCompanyName;

    /**
     * 维保工
     */
    private String vMaintainPersonName;

    /**
     * 维保工电话
     */
    private String vMaintainPersonTel;

}
