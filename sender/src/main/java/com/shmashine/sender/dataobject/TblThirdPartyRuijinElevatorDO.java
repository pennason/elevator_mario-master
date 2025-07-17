// Copyright (C) 2025 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.dataobject;

import java.util.Date;

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
 * @version v1.0  -  2025/5/21 11:51
 * @since v1.0
 */

@TableName("tbl_third_party_ruijin_elevator")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TblThirdPartyRuijinElevatorDO extends BaseDO {
    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 注册代码
     */
    private String registerNumber;
    /**
     * 使用证号
     */
    private String useCertNumber;
    /**
     * 注册登记机构
     */
    private String registerOrg;
    /**
     * 注册登记日期
     */
    private Date registerDate;
    /**
     * 注册登记人员
     */
    private String registerPerson;
    /**
     * 设计规范
     */
    private String designStandard;
    /**
     * 制造国
     */
    private String manufactureCountry;
    /**
     * 制造单位代码
     */
    private String manufacturerCode;
    /**
     * 制造单位名称
     */
    private String manufacturerName;
    /**
     * 出厂编号
     */
    private String leaveFactoryNumber;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 出厂日期
     */
    private Date leaveFactoryDate;
    /**
     * 设计使用年限
     */
    private Date designServiceLife;
    /**
     * 设备种类
     */
    private String kind;
    /**
     * 设备类别
     */
    private String category;
    /**
     * 设备品种
     */
    private String type;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 制造规范
     */
    private String produceStandard;
    /**
     * 监检形式
     */
    private String inspectionModality;
    /**
     * 监检机构各称
     */
    private String supervisorName;
    /**
     * 监检机构核准证编号
     */
    private String supervisorApprNumber;
    /**
     * 监检证书编号
     */
    private String supervisorCertNumber;
    /**
     * 型式试验机构各称
     */
    private String modelTestOrg;
    /**
     * 型式试验机构核准证编号
     */
    private String modelTestApprNumber;
    /**
     * 型式试验证书编号
     */
    private String modelTestCertNumber;
    /**
     * 经营单位代码
     */
    private String sellerCode;
    /**
     * 经营单位名称
     */
    private String sellerName;
    /**
     * 经营单位地址
     */
    private String sellerAddress;
    /**
     * 经营单位联系人
     */
    private String sellerContact;
    /**
     * 经营单位联系电话
     */
    private String sellerTel;
    /**
     * 安装告知单编号
     */
    private String noticeNumber;
    /**
     * 安装告知日期
     */
    private Date noticeDate;
    /**
     * 安装单位代码
     */
    private String setupCompanyCode;
    /**
     * 安装单位名称
     */
    private String setupCompanyName;
    /**
     * 安装单位许可证编号
     */
    private String setupCompanyCertNumber;
    /**
     * 安装负责人
     */
    private String setupPrinciple;
    /**
     * 安装负责人联系电话
     */
    private String setupPrincipleTel;
    /**
     * 安装竣工日期
     */
    private Date setupFinishDate;
    /**
     * 安装监督检验机构代码
     */
    private String setupSupervisorCode;
    /**
     * 安装监督检验机构名称
     */
    private String setupSupervisorName;
    /**
     * 安装监督检验机构核准证编号
     */
    private String setupSupervisorApprNumber;
    /**
     * 购买日期
     */
    private Date buyDate;
    /**
     * 产权单位代码
     */
    private String ownerCode;
    /**
     * 产权单位名称
     */
    private String ownerName;
    /**
     * 产权单位地址
     */
    private String ownerAddress;
    /**
     * 产权单位联系人
     */
    private String ownerContact;
    /**
     * 产权单位联系电话
     */
    private String ownerTel;
    /**
     * 行政区划
     */
    private String areaCode;
    /**
     * 乡镇(街道)
     */
    private String town;
    /**
     * 归属管理机构
     */
    private String adminOrg;
    /**
     * 设备使用场所
     */
    private String usePlace;
    /**
     * 管理单元(小区楼宇)
     */
    private String buildingUnit;
    /**
     * 安装地址
     */
    private String address;
    /**
     * 安装经度
     */
    private String longitude;
    /**
     * 安装纬度
     */
    private String latitude;
    /**
     * 位置
     */
    private String rescueNumber;
    /**
     * 使用单位类别
     */
    private String useCompanyType;
    /**
     * 使用单位代码
     */
    private String useCompanyCode;
    /**
     * 使用单位名称
     */
    private String useCompanyName;
    /**
     * 使用单位简称
     */
    private String useCompanyAlias;
    /**
     * 使用单位负责人
     */
    private String useCompanyPerson;
    /**
     * 使用单位负责人电话
     */
    private String useCompanyTel;
    /**
     * 管理单位（分支机构）名称
     */
    private String adminBranchName;
    /**
     * 管理单位联系人
     */
    private String adminBranchContact;
    /**
     * 管理单位联系电话
     */
    private String adminBranchTel;
    /**
     * 管理单位地址
     */
    private String adminBranchAddress;
    /**
     * 安全管理部门
     */
    private String safetyDept;
    /**
     * 安全管理部门地址
     */
    private String safetyDeptAddress;
    /**
     * 安全管理员
     */
    private String safetyAdministrator;
    /**
     * 安全管理员证书编号
     */
    private String safetyAdmCertNumber;
    /**
     * 安全管理人员联系电话
     */
    private String safetyAdminTel;
    /**
     * 使用状态
     */
    private String useStatus;
    /**
     * 新旧状况
     */
    private String conditions;
    /**
     * 改造单位代码
     */
    private String remouldEntCode;
    /**
     * 改造单位名称
     */
    private String remouldEntName;
    /**
     * 改造日期
     */
    private Date remouldDate;
    /**
     * 启用日期
     */
    private Date startUseDate;
    /**
     * 停用日期
     */
    private Date stopUseDate;
    /**
     * 注销日期
     */
    private Date cancelDate;
    /**
     * 检验责任所在单位
     */
    private String inspectDutyCompany;
    /**
     * 设备安装单位
     */
    private String iotInstallCompany;
    /**
     * 数据类型
     */
    private String dataType;
}
