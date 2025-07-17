// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.elevatorexcel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import com.shmashine.common.dto.ElevatorExcelTelecomDigitalSetDTO;
import com.shmashine.common.entity.TblElevator;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/1/23 13:42
 * @since v1.0
 */

@Slf4j
public class ElevatorExcelTelecomDigitalSetReadListener
        extends AnalysisEventListener<ElevatorExcelTelecomDigitalSetDTO> {

    private final ElevatorExcelServiceI elevatorExcelService;

    private static final int BATCH_COUNT = 100;
    private final List<ElevatorExcelTelecomDigitalSetDTO> elevatorList = new ArrayList<>();
    private Integer totalRows;
    private Integer successRows;
    private final String projectId;
    private final String userId;
    /**
     * 记录相关小区ID， 用于更新小区下的电梯数量
     */
    private Set<String> villageIds;

    public ElevatorExcelTelecomDigitalSetReadListener(ElevatorExcelServiceI elevatorExcelService, String projectId,
                                                      String userId) {
        this.elevatorExcelService = elevatorExcelService;
        this.projectId = projectId;
        this.userId = userId;
        this.totalRows = 0;
        this.successRows = 0;
        this.villageIds = new HashSet<>();
    }

    @Override
    public void invoke(ElevatorExcelTelecomDigitalSetDTO data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        elevatorList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (elevatorList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            elevatorList.clear();
            villageIds.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        elevatorList.clear();
        villageIds.clear();
        log.info("所有数据解析完成！");
    }

    public Map<String, Integer> getResult() {
        return Map.of("total", totalRows, "success", successRows);
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", elevatorList.size());
        try {
            var rows = saveElevatorTelecomDigitalSet(elevatorList);
            totalRows += elevatorList.size();
            successRows += rows;
            // 更新项目下小区数量，更新小区下电梯数量
            updateVillageProjectCount();
        } finally {
            elevatorList.clear();
            villageIds.clear();
        }
        log.info("存储数据库成功！");
    }

    /**
     * 保存电信数集Excel格式
     *
     * @param elevatorList 电梯Excel
     * @return 影响的行数
     */
    private Integer saveElevatorTelecomDigitalSet(List<ElevatorExcelTelecomDigitalSetDTO> elevatorList) {
        log.info("elevator list is {}", JSON.toJSONString(elevatorList));
        var rows = 0;
        if (CollectionUtils.isEmpty(elevatorList)) {
            return rows;
        }
        for (var item : elevatorList) {
            var res = saveElevatorTelecomDigitalSetItem(item);
            if (Boolean.TRUE.equals(res)) {
                rows += 1;
            }
        }
        return rows;
    }

    private Boolean saveElevatorTelecomDigitalSetItem(ElevatorExcelTelecomDigitalSetDTO elevatorDTO) {
        var elevator = TblElevator.builder().vProjectId(projectId)
                .vAddress(elevatorDTO.getAddress())
                .vBuildingId(elevatorDTO.getBuildingId().trim())
                .vUnitCode(elevatorDTO.getUnitCode().trim())
                .vEquipmentCode(elevatorDTO.getEquipmentCode())
                .propertySafeMan(elevatorDTO.getPropertySafeMan())
                .propertySafePhone(elevatorDTO.getPropertySafePhone())
                // .vMaintainPersonName(elevatorDTO.getMaintainPersonName())
                .vEmergencyPersonName(elevatorDTO.getEmergencyPersonName())
                .maintenancePhone(elevatorDTO.getEmergencyPersonTel())
                .maintainEmergencyPhone(elevatorDTO.getEmergencyPersonTel())
                .vEmergencyPersonTel(elevatorDTO.getEmergencyPersonTel())
                .nextInspectionDate(StringUtils.hasText(elevatorDTO.getNextInspectionDate())
                        ? DateUtil.format(DateUtil.parse(elevatorDTO.getNextInspectionDate()),
                        DatePattern.NORM_DATE_PATTERN)
                        : null)
                .installationDate(StringUtils.hasText(elevatorDTO.getInstallationDate())
                        ? DateUtil.format(DateUtil.parse(elevatorDTO.getInstallationDate()),
                        DatePattern.NORM_DATE_PATTERN)
                        : null)
                .vSglnCode(elevatorDTO.getSglnCode())
                .inspectionUnit(elevatorDTO.getRegistrationMechanism())
                .vRegistrationMechanism(elevatorDTO.getRegistrationMechanism())
                .vRegistrationAuthority(elevatorDTO.getRegistrationAuthority())
                .nominalLoadCapacity(elevatorDTO.getNominalLoadCapacity())
                .dcSpeed(null == elevatorDTO.getSpeed() ? null : elevatorDTO.getSpeed().doubleValue())
                .traction(elevatorDTO.getTraction())
                .floorCount(elevatorDTO.getFloorCount())
                .liftGate(elevatorDTO.getLiftGate())
                .stationCount(elevatorDTO.getStationCount())
                .showFloor(elevatorDTO.getShowFloor())
                .iInstallStatus(0)
                // 1:待现勘; 2:待配货; 3:待安装; 4:待调测; 5:待验收; 6:运行中; 7:托管; 11:现勘中; 12:配货中; 13:安装中; 14:调测中; 15:验收中; 其他数字：待现勘
                .iPmStatus(1)
                .build();
        // 省市区信息
        elevatorExcelService.extendCityAreaInfo(elevator, elevatorDTO.getCityName(), elevatorDTO.getDistrictName());
        // 小区信息
        elevatorExcelService.extendVillageInfo(elevator, elevatorDTO.getVillageName(), elevatorDTO.getStreet(),
                elevatorDTO.getNeighborhood(), elevatorDTO.getAddress(), projectId, userId);
        // 小区配货表
        elevatorExcelService.extendVillageDeviceBillInfo(elevator, projectId, userId);
        // 电梯平台扩展
        elevatorExcelService.extendElevatorBrandInfo(elevator, elevatorDTO.getElevatorBrand(),
                elevatorDTO.getElevatorBrandName(), userId);
        // 扩展物业信息
        elevatorExcelService.extendPropertyCompanyInfo(elevator, elevatorDTO.getPropertyCompany(),
                elevatorDTO.getPropertySafeMan(), elevatorDTO.getPropertySafePhone(), elevatorDTO.getAddress(), userId);
        // 扩展维保信息
        elevatorExcelService.extendMaintainCompanyInfo(elevator, elevatorDTO.getMaintainCompany(),
                elevatorDTO.getEmergencyPersonName(), elevatorDTO.getEmergencyPersonTel(), elevatorDTO.getAddress(),
                userId);
        // 记录小区ID， 用于更新小区下电梯数量
        villageIds.add(elevator.getVVillageId());

        // 存储电梯
        return elevatorExcelService.saveElevatorInfo(elevator, userId);
    }

    private void updateVillageProjectCount() {
        elevatorExcelService.updateVillageProjectCount(villageIds, projectId);
    }
}
