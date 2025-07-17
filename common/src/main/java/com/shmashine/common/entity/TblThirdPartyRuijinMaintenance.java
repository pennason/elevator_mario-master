package com.shmashine.common.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * (TblThirdPartyRuijinMaintenance)实体类
 *
 * @author makejava
 * @since 2020-07-24 14:44:44
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblThirdPartyRuijinMaintenance implements Serializable {
    @Serial
    private static final long serialVersionUID = 224622554382849727L;

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
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("dtCreateTime")
    private Date dtCreateTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("dtModifyTime")
    private Date dtModifyTime;
    /**
     * 创建记录用户
     */
    @JsonProperty("vCreateUserId")
    private String vCreateUserId;
    /**
     * 修改记录用户
     */
    @JsonProperty("vModifyUserId")
    private String vModifyUserId;

}