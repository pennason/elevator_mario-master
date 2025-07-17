// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevatorexcel.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateUtil;

import com.shmashine.api.service.elevatorbrand.TblElevatorBrandServiceI;
import com.shmashine.api.service.elevatorexcel.ElevatorExcelServiceI;
import com.shmashine.api.service.elevatorproject.TblProjectServiceI;
import com.shmashine.api.service.system.BizSystemService;
import com.shmashine.api.service.system.TblElevatorServiceI;
import com.shmashine.api.service.system.TblSysDeptServiceI;
import com.shmashine.api.service.village.TblVillageDeviceBillServiceI;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblElevatorBrand;
import com.shmashine.common.entity.TblProject;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.entity.TblVillageDeviceBill;
import com.shmashine.common.utils.SnowFlakeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/1/25 15:04
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorExcelServiceImpl implements ElevatorExcelServiceI {
    private final BizSystemService systemService;
    private final TblVillageServiceI tblVillageService;
    private final TblVillageDeviceBillServiceI villageDeviceBillService;
    private final TblElevatorBrandServiceI elevatorBrandService;
    private final TblSysDeptServiceI sysDeptService;
    private final TblElevatorServiceI elevatorService;
    private final TblProjectServiceI projectService;
    private final RedisTemplate redisTemplate;

    private static final Integer DEPT_TYPE_ID_PROPERTY = 2;
    private static final Integer DEPT_TYPE_ID_MAINTENANCE = 1;

    @Override
    public Boolean saveElevatorInfo(TblElevator elevator, String userId) {
        var hasRecord = elevatorService.getByOneOfChoose(elevator.getVElevatorId(), elevator.getVElevatorCode(),
                elevator.getVEquipmentCode());
        if (null != hasRecord) {
            elevator.setDtModifyTime(new Date());
            elevator.setVModifyUserId(userId + "(importExcel)");
            var res = elevatorService.update(elevator);
            return res > 0;
        }
        if (!StringUtils.hasText(elevator.getVElevatorId())) {
            var nextId = SnowFlakeUtils.nextStrId();
            elevator.setVElevatorId(nextId);
        }
        if (!StringUtils.hasText(elevator.getVElevatorCode())) {
            elevator.setVElevatorCode(elevator.getVElevatorId());
        }
        elevator.setDtCreateTime(new Date());
        elevator.setVCreateUserId(userId + "(importExcel)");
        var success = elevatorService.insertIsNotEmpty(elevator);
        var res = success > 0;

        return res;
    }

    @Override
    public void extendCityAreaInfo(TblElevator elevator, String cityName, String districtName) {
        // 市信息
        if (!StringUtils.hasText(cityName.trim())) {
            return;
        }
        var cityInfo = systemService.getAreaCityByNameAndLevel(cityName.replace("市", "").replace("城区", "").trim(),
                2, null);
        if (null == cityInfo) {
            return;
        }
        elevator.setICityId(cityInfo.getAreaId());
        // 省份信息
        var provinceInfo = systemService.getAreaProvinceByAreaCode(cityInfo.getAreaCode().substring(0, 2) + "0000");
        if (null != provinceInfo) {
            elevator.setIProvinceId(provinceInfo.getAreaId());
        }
        // 区信息
        if (!StringUtils.hasText(districtName)) {
            return;
        }
        var districtInfo = systemService.getAreaCityByNameAndLevel(districtName.trim(), 3, cityInfo.getCityCode());
        if (null != districtInfo) {
            elevator.setIAreaId(districtInfo.getAreaId());
        }
    }

    @Override
    public void extendVillageInfo(TblElevator elevator, String villageName, String street, String neighborhood,
                                  String address, String projectId, String userId) {
        if (!StringUtils.hasText(villageName)) {
            return;
        }
        // 获取小区信息
        var villageInfo = tblVillageService.getVillageByProjectIdAndVillageName(projectId, villageName.trim());
        if (null == villageInfo) {
            // 添加小区信息
            var villageId = SnowFlakeUtils.nextStrId();
            tblVillageService.insert(TblVillage.builder()
                    .vVillageId(villageId)
                    .vVillageName(villageName)
                    .vAddress(address)
                    .vProjectId(projectId)
                    .vRemarks("导入梯时自动生成")
                    .dtCreateTime(DateUtil.date())
                    .vCreateUserId(userId + "(importExcel)")
                    .vContactsName(elevator.getPropertySafeMan())
                    .vContactsPhone(elevator.getPropertySafePhone())
                    .street(street)
                    .neighborhood(neighborhood)
                    .iDelFlag(0)
                    .build());
            elevator.setVVillageId(villageId);
            return;
        }
        elevator.setVVillageId(villageInfo.getVVillageId());
    }

    @Override
    public void extendVillageDeviceBillInfo(TblElevator elevator, String projectId, String userId) {
        var villageId = elevator.getVVillageId();
        if (!StringUtils.hasText(villageId)) {
            return;
        }
        var villageDeviceBill = villageDeviceBillService.getByVillageId(villageId);
        if (villageDeviceBill != null) {
            return;
        }
        String billId = SnowFlakeUtils.nextStrId();
        villageDeviceBillService.addVillageDeviceBill(TblVillageDeviceBill.builder()
                .vVillageDeviceBillId(billId)
                .vVillageId(villageId)
                .vDeviceType("MX201")
                .build());
    }

    @Override
    public void extendElevatorBrandInfo(TblElevator elevator, String elevatorBrand, String elevatorBrandName,
                                        String userId) {
        if (!StringUtils.hasText(elevatorBrand)) {
            return;
        }
        elevatorBrand = elevatorBrand.replace("有限公司", "");
        var brandInfo = elevatorBrandService.getBrandByBrandName(elevatorBrand);
        if (null == brandInfo) {
            // 添加电梯品牌
            var brandId = SnowFlakeUtils.nextStrId();
            elevatorBrandService.insert(TblElevatorBrand.builder()
                    .vElevatorBrandId(brandId)
                    .vElevatorBrandName(elevatorBrand)
                    .vRemarks(elevatorBrandName)
                    .dtCreateTime(DateUtil.date())
                    .vCreateUserId(userId + "(importExcel)")
                    .iDelFlag(0)
                    .build());
            elevator.setVElevatorBrandId(brandId);
            return;
        }
        elevator.setVElevatorBrandId(brandInfo.getVElevatorBrandId());
    }

    @Override
    public void extendPropertyCompanyInfo(TblElevator elevator, String propertyCompany, String propertySafeMan,
                                          String propertySafePhone, String address, String userId) {
        if (!StringUtils.hasText(propertyCompany)) {
            return;
        }
        var propertyCompanyInfo = sysDeptService.getDeptInfoByNameAndType(propertyCompany, DEPT_TYPE_ID_PROPERTY);
        if (null == propertyCompanyInfo) {
            // 添加物业信息
            var deptId = SnowFlakeUtils.nextStrId();
            sysDeptService.insert(TblSysDept.builder()
                    .vDeptId(deptId)
                    .vParentId("1274176398029885440")
                    .iDeptTypeId(DEPT_TYPE_ID_PROPERTY)
                    .vDeptName(propertyCompany)
                    .iStatus(0)
                    .vMobilephone(propertySafePhone.trim())
                    .vProvinceId(elevator.getIProvinceId())
                    .vAddress(address)
                    .vRemarks("导梯生成")
                    .dtCreatetime(DateUtil.date())
                    .vCreateid(userId + "(importExcel)")
                    .vContacts(propertySafeMan)
                    .iWuyePlatform(2)
                    .build());
            elevator.setVPropertyCompanyId(deptId);

            //删除客户缓存
            Set<String> keys = redisTemplate.keys(RedisConstants.USER_DEPT_INFO + "*");
            redisTemplate.delete(keys);

            return;
        }
        elevator.setVPropertyCompanyId(propertyCompanyInfo.getVDeptId());
    }

    @Override
    public void extendMaintainCompanyInfo(TblElevator elevator, String maintainCompany, String maintainPersonName,
                                          String maintainPersonTel, String address, String userId) {
        if (!StringUtils.hasText(maintainCompany)) {
            return;
        }
        var maintainCompanyInfo = sysDeptService.getDeptInfoByNameAndType(maintainCompany, DEPT_TYPE_ID_MAINTENANCE);
        if (null == maintainCompanyInfo) {
            // 添加物业信息
            var deptId = SnowFlakeUtils.nextStrId();
            sysDeptService.insert(TblSysDept.builder()
                    .vDeptId(deptId)
                    .vParentId("1274176398029885440")
                    .iDeptTypeId(DEPT_TYPE_ID_MAINTENANCE)
                    .vDeptName(maintainCompany)
                    .iStatus(0)
                    .vMobilephone(maintainPersonTel.trim())
                    .vProvinceId(elevator.getIProvinceId())
                    .vAddress(address)
                    .vRemarks("导梯生成")
                    .dtCreatetime(DateUtil.date())
                    .vCreateid(userId + "(importExcel)")
                    .vContacts(maintainPersonName)
                    .iWuyePlatform(2)
                    .build());
            elevator.setVPropertyCompanyId(deptId);

            //删除客户缓存
            Set<String> keys = redisTemplate.keys(RedisConstants.USER_DEPT_INFO + "*");
            redisTemplate.delete(keys);

            return;
        }
        elevator.setVPropertyCompanyId(maintainCompanyInfo.getVDeptId());

    }

    @Override
    public void updateVillageProjectCount(Collection<String> villageIds, String projectId) {


        villageIds.forEach(villageId -> {

            // 小区下的电梯数量
            int elevatorCount = elevatorService.countByEntity(TblElevator.builder().vVillageId(villageId).build());

            //小区电梯数量
            tblVillageService.update(TblVillage.builder().vVillageId(villageId).iElevatorCount(elevatorCount).build());

            //单双盒数量
            villageDeviceBillService.updateByVillageId(TblVillageDeviceBill.builder().vVillageId(villageId)
                    .iSingleBoxCount(elevatorCount).build());

        });


        // 更新项目下的小区数量
        var villageIdList = tblVillageService.getVillageIdsByProjectId(projectId);
        if (!CollectionUtils.isEmpty(villageIdList)) {
            projectService.update(TblProject.builder()
                    .vProjectId(projectId)
                    .iVillageCount(villageIdList.size()).build());
        }
    }
}
