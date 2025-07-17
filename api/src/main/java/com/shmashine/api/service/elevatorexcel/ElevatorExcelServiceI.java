// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevatorexcel;

import java.util.Collection;

import com.shmashine.common.entity.TblElevator;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/1/25 15:04
 * @since v1.0
 */

public interface ElevatorExcelServiceI {

    /**
     * 保存电梯信息
     *
     * @param elevator 电梯信息
     * @param userId   用户ID
     * @return 是否成功
     */
    Boolean saveElevatorInfo(TblElevator elevator, String userId);

    /**
     * 扩展城市信息
     *
     * @param elevator     电梯
     * @param cityName     城市名
     * @param districtName 区名
     */
    void extendCityAreaInfo(TblElevator elevator, String cityName, String districtName);

    /**
     * 扩展小区信息, 无则创建
     *
     * @param elevator     电梯信息
     * @param villageName  小区名称
     * @param street       街道
     * @param neighborhood 委员会
     * @param projectId    项目ID
     * @param userId       用户ID
     */
    void extendVillageInfo(TblElevator elevator, String villageName, String street, String neighborhood,
                           String address, String projectId, String userId);

    /**
     * 扩展小区配货表
     *
     * @param elevator  电梯信息
     * @param projectId 项目ID
     * @param userId    用户ID
     */
    void extendVillageDeviceBillInfo(TblElevator elevator, String projectId, String userId);

    /**
     * 扩展电梯品牌， 无则创建
     *
     * @param elevator          电梯
     * @param elevatorBrand     品牌名
     * @param elevatorBrandName 品牌公司名
     * @param userId            用户ID
     */
    void extendElevatorBrandInfo(TblElevator elevator, String elevatorBrand, String elevatorBrandName, String userId);

    /**
     * 扩展物业信息， 无物业则创建
     *
     * @param elevator          电梯
     * @param propertyCompany   物业名称
     * @param propertySafeMan   安全员
     * @param propertySafePhone 安全员手机
     * @param address           地址
     * @param userId            用户ID
     */
    void extendPropertyCompanyInfo(TblElevator elevator, String propertyCompany, String propertySafeMan,
                                   String propertySafePhone, String address, String userId);

    /**
     * 扩展维保信息， 无物业则创建
     *
     * @param elevator           电梯
     * @param maintainCompany    维保公司名称
     * @param maintainPersonName 维保人员
     * @param maintainPersonTel  维保人员电话
     * @param address            地址
     * @param userId             用户ID
     */
    void extendMaintainCompanyInfo(TblElevator elevator, String maintainCompany, String maintainPersonName,
                                   String maintainPersonTel, String address, String userId);

    /**
     * 更新小区下的电梯数量和项目下的小区数量
     *
     * @param villageIds 小区IDS
     * @param projectId  项目IDs
     */
    void updateVillageProjectCount(Collection<String> villageIds, String projectId);
}
