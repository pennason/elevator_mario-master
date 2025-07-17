// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 导入电梯信息（电信数集） 模板地址：<a href="https://oss-mashine.oss-cn-qingdao.aliyuncs.com/web/static/iot/demo-excel/demo-elevator-telecom-digital-set-import.xlsx">demo-elevator-telecom-digital-set-import.xlsx</a>
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/1/23 13:51
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ElevatorExcelTelecomDigitalSetDTO implements Serializable {
    /**
     * 城市  tbl_sys_provincial_city -> v_areaName, v_areaCode, i_level
     */
    private String cityName;
    /**
     * 区县   tbl_sys_provincial_city -> v_areaName, v_areaCode, i_level
     */
    private String districtName;
    /**
     * 街道（乡）tbl_village -> street
     */
    private String street;
    /**
     * 小区名称 tbl_village -> v_village_name
     */
    private String villageName;
    /**
     * 硕鼠居委 tbl_village -> neighborhood
     */
    private String neighborhood;
    /**
     * 楼栋号 v_building_id
     */
    private String buildingId;
    /**
     * 梯号 单位内编号（使用单位内部编号） v_unit_code
     */
    private String unitCode;
    /**
     * 注册代码 v_equipment_code
     */
    private String equipmentCode;
    /**
     * 品牌型号 tbl_elevator_brand -> v_elevator_brand_id, v_elevator_brand_name, v_remarks
     */
    private String elevatorBrand;
    /**
     * 电梯制造商 tbl_elevator_brand -> v_remarks
     */
    private String elevatorBrandName;
    /**
     * 物业公司 v_property_company_id <->  tbl_sys_dept -> (i_dept_type_id = 2)  v_dept_id, v_dept_name
     */
    private String propertyCompany;
    /**
     * 物业联系人 property_safe_man, 放在小区管理人
     */
    private String propertySafeMan;
    /**
     * 物业联系人电话 property_safe_phone 放在小区联系电话
     */
    private String propertySafePhone;
    /**
     * 维保公司 v_maintain_company_id <-> tbl_sys_dept (i_dept_type_id = 1)  v_dept_id, v_dept_name
     */
    private String maintainCompany;
    /**
     * 紧急救援人 v_emergency_person_name
     */
    private String emergencyPersonName;
    /**
     * 救援电话 v_emergency_person_tel, maintenance_phone
     */
    private String emergencyPersonTel;
    /**
     * 下次检验日期, 需要转成  yyyy-MM-dd   next_inspection_date
     */
    private String nextInspectionDate;
    /**
     * 电梯安装日期 yyyy-MM-dd     installation_date （dt_install_time）
     */
    private String installationDate;
    /**
     * 位置码 v_sgln_code
     */
    private String sglnCode;
    /**
     * 检验机构 inspection_unit, v_registration_mechanism
     */
    private String registrationMechanism;
    /**
     * 登记机关 v_registration_authority
     */
    private String registrationAuthority;
    /**
     * 安装地址 v_address
     */
    private String address;
    /**
     * 额定载重(KG) nominal_load_capacity
     */
    private String nominalLoadCapacity;
    /**
     * 额定速度 m/s dc_speed
     */
    private BigDecimal speed;
    /**
     * 曳引比 traction
     */
    private String traction;
    /**
     * 楼层数 floor_count
     */
    private Integer floorCount;
    /**
     * 电梯门数 lift_gate
     */
    private String liftGate;
    /**
     * 停站数 station_count
     */
    private Integer stationCount;
    /**
     * 显示楼层 show_floor
     */
    private String showFloor;
}
