// Copyright (C) 2025 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2025/5/21 14:02
 * @since v1.0
 */

@TableName("tbl_third_party_ruijin_maintenance")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TblThirdPartyRuijinMaintenanceDO extends BaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 注册代码
     */
    private String registerNumber;
    /**
     * 安装地址
     */
    private String address;
    /**
     * 电梯内部编号
     */
    private String internalNumber;
    /**
     * 维保单位代码
     */
    private String maintenanceCompanyCode;
    /**
     * 维保单位名称
     */
    private String maintenanceCompanyName;
    /**
     * 维保单位负责人
     */
    private String maintenancePrinciple;
    /**
     * 维保单位负责人电话
     */
    private String maintenancePrincipleTel;
    /**
     * 维保周期
     */
    private Integer maintenancePeriod;
    /**
     * 许可证编号
     */
    private String certNumber;
    /**
     * 资质范围
     */
    private String qualificationScope;
    /**
     * 注册地址
     */
    private String registerAddress;
    /**
     * 办公地点
     */
    private String officeAddress;
    /**
     * 维保驻点
     */
    private String maintenanceStation;
    /**
     * 驻点地址
     */
    private String stationAddress;
    /**
     * 驻点电话
     */
    private String stationTel;
    /**
     * 驻点负责人
     */
    private String stationPrinciple;
    /**
     * 电梯司机
     */
    private String driver;
    /**
     * 应急救援电话
     */
    private String rescueTel;
    /**
     * 自检日期
     */
    private Object checkDate;
    /**
     * 自检单位名称
     */
    private String checkCompanyName;
    /**
     * 下次自检日期
     */
    private Object nextCheckDate;
    /**
     * 合同使用单位名称
     */
    private String contractUseCompanyName;
    /**
     * 使用单位统一社会信用代码
     */
    private String useCompanyCode;
    /**
     * 使用单位负责人
     */
    private String useCompanyPrinciple;
    /**
     * 使用单位负责人电话
     */
    private String useCompanyPrincipleTel;
    /**
     * 使用场所名称
     */
    private String usePlaceName;
    /**
     * 所在地区
     */
    private String usePlaceAreaCode;
    /**
     * 所在街道(镇)
     */
    private String usePlaceTownCode;
    /**
     * 使用场所地址
     */
    private String usePlaceAddress;
    /**
     * 使用场所类型
     */
    private String usePlaceType;
    /**
     * 安全管理员
     */
    private String safetyAdministrator;
    /**
     * 安全管理员电话
     */
    private String safetyAdminTel;
    /**
     * 第一维保人员姓名
     */
    private String firstMaintainerName;
    /**
     * 第一维保人员身份证号
     */
    private String firstMaintainerIdNumber;
    /**
     * 第一维保人员手机号
     */
    private String firstMaintainerTel;
    /**
     * 第二维保人员姓名
     */
    private String secondMaintainerName;
    /**
     * 第二维保人员身份证号
     */
    private String secondMaintainerIdNumber;
    /**
     * 第二维保人员手机号
     */
    private String secondMaintainerTel;
}
