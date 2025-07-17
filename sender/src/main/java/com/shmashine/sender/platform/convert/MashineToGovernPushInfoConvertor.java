// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.platform.convert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.enums.GovernFaultTypeChongqingEnum;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.FaultWithVideoPhoto;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.MonitorMessage;
import com.shmashine.common.message.OnOfflineMessage;
import com.shmashine.mgppf.components.dto.elevator.CityDTO;
import com.shmashine.mgppf.components.dto.elevator.ElevatorModelDTO;
import com.shmashine.mgppf.components.dto.elevator.ProjectModelDTO;
import com.shmashine.mgppf.components.dto.enums.GovernClientPlatformEnum;
import com.shmashine.mgppf.components.dto.enums.GovernFaultTypeEnum;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushFaultDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushFaultWithPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushModeChangeDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushRunningDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushRunningWithPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushStatusDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/22 17:56
 * @since v1.0
 */

@Slf4j
@Component
public class MashineToGovernPushInfoConvertor {

    /**
     * 将小区信息转为 政府平台的项目信息，并注册
     *
     * @param village    小区信息
     * @param extendInfo 扩展信息
     */
    public ProjectModelDTO convertProjectShmashineToGovern(TblVillage village, Map<String, String> extendInfo,
                                                           TblElevator elevator, CityDTO city, String ptCode) {
        if (null == village) {
            return null;
        }
        return ProjectModelDTO.builder()
                .projectId(village.getVVillageId())
                .projectName(village.getVVillageName())
                .projectAddress(village.getVAddress())
                .projectPhoto(village.getVImgUrl())
                .maintainPerson(elevator.getVMaintainPersonName())
                .maintainPersonMobile(elevator.getVMaintainPersonTel())
                .maintainCompanyId(elevator.getVMaintainCompanyId())
                .maintainCompanyName(extendInfo.get("maintainCompanyName"))
                .maintainType("小修")
                .emergencyPersonMobile(elevator.getVEmergencyPersonTel())
                .safeMan(elevator.getPropertySafeMan())
                .safeManMobile(elevator.getPropertySafePhone())
                .dutyPhone(elevator.getVEmergencyPersonTel())
                .propertyCompanyId(elevator.getVPropertyCompanyId())
                .propertyCompanyName(extendInfo.get("propertyCompanyName"))
                .locationProvince(extendInfo.get("provinceName"))
                .locationCity(extendInfo.get("cityName"))
                .locationDistrict(extendInfo.get("areaName"))
                .locationSubDistrict(extendInfo.get("street"))
                .cityAreaCode(city.getAreaCode())
                .cityAreaName(city.getCityName())
                .pushGovern(buildPushGovern(elevator.getVHttpPtCodes(), ptCode))
                .clientPlatform(GovernClientPlatformEnum.MAI_XIN.name())
                .build();
    }

    /**
     * 将电梯信息转换为推送信息, 用于在推送模块保存电梯信息，并注册电梯
     *
     * @param source     电梯信息
     * @param city       城市信息
     * @param device     设备信息
     * @param extendInfo 扩展信息
     * @return 推送信息
     */
    // CHECKSTYLE:OFF
    public ElevatorModelDTO convertElevatorShmashineToGovern(TblElevator source, CityDTO city, TblDevice device,
                                                             TblCameraDTO camera,
                                                             Map<String, String> extendInfo, String ptCode) {
        return ElevatorModelDTO.builder()
                .elevatorId(Long.valueOf(source.getVElevatorId()))
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(StringUtils.hasText(source.getFactoryNumber())
                        ? source.getFactoryNumber() : source.getVLeaveFactoryNumber())
                .deviceIdentificationNumber(StringUtils.hasText(source.getCode96333())
                        ? source.getCode96333() : source.getIdentificationNumber())
                .clientPlatform(GovernClientPlatformEnum.MAI_XIN.name())
                .deviceRegisterNumber(StringUtils.hasText(source.getRegistrationCode())
                        ? source.getRegistrationCode() : source.getVEquipmentCode())
                .equCode(source.getEquCode())
                .deviceVariety(source.getCategory())
                .deviceModel(source.getEquipmentNumber())
                .installAddress(StringUtils.hasText(source.getAddr()) ? source.getAddr() : source.getVAddress())
                .adaptorPlace(source.getSuitablePlace())
                .manufactureUnitName(StringUtils.hasText(extendInfo.get("brandNameMark"))
                        ? extendInfo.get("brandNameMark") : extendInfo.get("brandName"))
                .deviceInternalNumber(StringUtils.hasText(source.getInsideNumber())
                        ? source.getInsideNumber() : source.getVUnitCode())
                .deviceVendor(extendInfo.get("brandName"))
                .elevatorBrandId(Long.valueOf(null == extendInfo.get("brandId") ? "0" : extendInfo.get("brandId")))
                .elevatorBrandName(extendInfo.get("brandName"))
                .deviceImportDealer(source.getManufacturer())
                .deviceReleaseDate(null == source.getProductionDate()
                        ? null : DateUtil.parse(source.getProductionDate().toString()))
                .deviceRenovationUnit(source.getReformingUnit())
                .deviceRenovationDate(null == source.getReformingDate()
                        ? null : DateUtil.parse(source.getReformingDate().toString()))
                .deviceInstallationUnit(null)
                .deviceInstallationDate(null == source.getDtInstallTime()
                        ? null : DateUtil.parseDate(source.getDtInstallTime().toString()))
                .maintenanceId(StringUtils.hasText(source.getInnerCode())
                        ? source.getInnerCode() : source.getVMaintainCompanyId())
                .maintenanceUnit(StringUtils.hasText(source.getMaintenanceUnit())
                        ? source.getMaintenanceUnit() : extendInfo.get("maintainCompanyName"))
                .emergencyRescueMobile(StringUtils.hasText(source.getEmergencyPhone())
                        ? source.getEmergencyPhone() : source.getVEmergencyPersonTel())
                .httpPushPlatformCode(source.getVHttpPtCodes())
                .longitude(buildGpsLonLat(source.getLon(), source.getGcj02Longitude(), source.getVLongitude()))
                .latitude(buildGpsLonLat(source.getLat(), source.getGcj02Latitude(), source.getVLatitude()))
                .mapType(StringUtils.hasText(source.getGcj02Longitude())
                        ? 3 : (StringUtils.hasText(source.getVLongitude()) ? 2 : 0))
                .pushGovern(buildPushGovern(source.getVHttpPtCodes(), ptCode))
                .useUnitName(StringUtils.hasText(source.getUseUnitName())
                        ? source.getUseUnitName() : extendInfo.get("propertyCompanyName"))
                .useUnitPhone(StringUtils.hasText(source.getUsePhone())
                        ? source.getUsePhone()
                        : (StringUtils.hasText(source.getUseUnitPhone())
                        ? source.getUseUnitPhone() : extendInfo.get("propertyCompanyPhone")))
                .layerStationNumber(null == source.getFloorCount()
                        ? null : source.getFloorCount() + "层/" + source.getStationCount() + "站")
                .maxFloor(source.getIMaxFloor())
                .minFloor(source.getIMinFloor())
                .floorCount((null != source.getFloorNumber() && source.getFloorNumber() > 0)
                        ? source.getFloorNumber() : source.getFloorCount())
                .stationCount((null != source.getStopsNumber() && source.getStopsNumber() > 0)
                        ? source.getStopsNumber() : source.getStationCount())
                .baseStation(source.getBaseStation())
                .floorDetail(source.getVFloorDetail())
                .nominalSpeed(null == source.getSpeed()
                        ? (null == source.getDcSpeed()
                        ? null : String.valueOf(source.getDcSpeed())) : source.getSpeed().toString())
                .nominalSpeedUp(null == source.getNominalSpeedUp()
                        ? null : BigDecimal.valueOf(source.getNominalSpeedUp()))
                .nominalSpeedDown(null == source.getNominalSpeedDown()
                        ? null : BigDecimal.valueOf(source.getNominalSpeedDown()))
                .nominalLoadCapacity(null == source.getLoadCapacity()
                        ? source.getNominalLoadCapacity() : source.getLoadCapacity().toString())
                .showFloor(source.getShowFloor())
                .controlMode(source.getControlMode())
                .cityAreaCode(city.getAreaCode())
                .cityAreaName(city.getCityName())
                .manufactureLicenseNo(null)
                .manufactureEnterpriseNumber(null)
                .terminalMonitorType(null == device ? null : device.getVSensorType())
                .terminalMonitorNumber(null == device ? null : device.getVDeviceCode())
                .deviceType(null == device ? null : device.geteType())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .positionCode(source.getVSglnCode())
                .mashineCube(source.getVElevatorCode())
                .mashineSensorType(null == device ? null : device.getVSensorType())
                .elevatorType(source.getIElevatorType())
                .deleted(false)
                .extendDevVariety(null)
                .provinceId(Long.valueOf(String.valueOf(source.getIProvinceId())))
                .provinceName(extendInfo.get("provinceName"))
                .cityId(Long.valueOf(String.valueOf(source.getICityId())))
                .cityName(extendInfo.get("cityName"))
                .districtId(Long.valueOf(String.valueOf(source.getIAreaId())))
                .districtName(extendInfo.get("areaName"))
                .subDistrictName(extendInfo.get("street"))
                .neighborhood(extendInfo.get("neighborhood"))
                .doorType(source.getDoorType())
                .maintenanceParticipateIn(null == source.getIsMaintenaince()
                        ? "0" : String.valueOf(source.getIsMaintenaince()))
                .maintenanceParticipateInDate(null == source.getMaintenanceParticipateInDate()
                        ? null : DateUtil.parse(source.getMaintenanceParticipateInDate().toString()))
                .nextInspectionDate(null == source.getNextInspection()
                        ? (null == source.getNextInspectionDate()
                        ? null : DateUtil.parse(source.getNextInspectionDate().toString()))
                        : DateUtil.parse(source.getNextInspection().toString()))
                .inspectionUnit(source.getInspectionUnit())
                .insuranceStatus(source.getInsuranceStatus())
                .insuranceType(source.getInsuranceType())
                .contractor(source.getContractor())
                .contractorStartDate(null == source.getContractorStart()
                        ? null : DateUtil.parse(source.getContractorStart().toString()))
                .contractorEndDate(null == source.getContractorEnd()
                        ? null : DateUtil.parse(source.getContractorEnd().toString()))
                .emergencyPersonName(source.getVEmergencyPersonName())
                .registrationAuthority(source.getVRegistrationAuthority())
                .buildingId(source.getVBuildingId())
                .buildingName(null == extendInfo.get("buildingName")
                        ? source.getVBuildingId() : extendInfo.get("buildingName"))
                .buildingType(source.getBuildingType())
                .villageId(Long.valueOf(source.getVVillageId()))
                .villageName(null == extendInfo.get("villageName")
                        ? source.getVVillageId() : extendInfo.get("villageName"))
                .projectId(source.getVVillageId())
                .cameraId(null == camera ? null : camera.getCameraId())
                .cameraType(null == camera ? 0 : camera.getCameraType())
                .cameraCloudNumber(null == camera ? null : camera.getCloudNumber())
                .cameraPlatformId(extendInfo.get("cameraPlatformId"))
                .cameraPlatformName(extendInfo.get("cameraPlatformName"))
                .cameraChannelId(extendInfo.get("cameraChannelId"))
                .cameraChannelCode(StringUtils.hasText(extendInfo.get("cameraChannelCode"))
                        ? extendInfo.get("cameraChannelCode")
                        : (null == camera ? null : camera.getDeviceCode()))
                .cameraChannelNo(null == extendInfo.get("cameraChannelNo")
                        ? null : Integer.parseInt(extendInfo.get("cameraChannelNo")))
                .propertySafeMan(StringUtils.hasText(source.getSafetyManager())
                        ? source.getSafetyManager() : source.getPropertySafeMan())
                .propertySafePhone(StringUtils.hasText(source.getSafetyManagerPhone())
                        ? source.getSafetyManagerPhone() : source.getPropertySafePhone())
                .maintenancePersonName(source.getSupervisorName())
                .maintenancePersonMobile(source.getSupervisorPhone())
                .maintainPrincipalPerson(source.getMaintainPrincipalPerson())
                .maintainPrincipalPhone(source.getMaintainPrincipalPhone())
                .maintainSubordinatePerson(source.getMaintainSubordinatePerson())
                .maintainSubordinatePhone(source.getMaintainSubordinatePhone())
                .maintainEquInsuranceInfo(source.getMaintainEquInsuranceInfo())
                .maintainEmergencyPhone(StringUtils.hasText(source.getMaintenancePhone())
                        ? source.getMaintenancePhone() : source.getMaintainEmergencyPhone())
                .maintainIntervalDays(null == source.getMaintenanceCycle()
                        ? null : Integer.valueOf(source.getMaintenanceCycle().replace("天", "")))
                .normalRiseHeight(source.getNormalRiseHeight())
                .normalAngle(source.getNormalAngle())
                .normalWidth(source.getNormalWidth())
                .normalLength(source.getNormalLength())
                .build();
    }
    // CHECKSTYLE:ON

    private BigDecimal buildGpsLonLat(String data1, String data2, String data3) {
        if (StringUtils.hasText(data1)) {
            return new BigDecimal(data1);
        }
        if (StringUtils.hasText(data2)) {
            return new BigDecimal(data2);
        }
        if (StringUtils.hasText(data3)) {
            return new BigDecimal(data3);
        }
        return null;
    }

    private String buildPushGovern(String vHttpPtCodes, String ptCode) {
        if (!StringUtils.hasText(vHttpPtCodes) || "null".equals(vHttpPtCodes)) {
            return ptCode;
        }
        /*var codes = new ArrayList<String>();
        for (var code : vHttpPtCodes.split(",")) {
            if ("yidian".equals(code)) {
                codes.add("shanghaiYidian");
                continue;
            }
            codes.add(code);
        }
        return StringUtils.collectionToCommaDelimitedString(codes);*/
        return vHttpPtCodes.replace(",yidian", "").replace("yidian,", "").replace("yidian", "");
    }

    /**
     * 运行数据
     */
    // CHECKSTYLE:OFF
    public ElevatorPushRunningDTO convertRunningToGovern(TblElevator source, MonitorMessage message, TblDevice device,
                                                         String time) {
        if (null == message) {
            return null;
        }
        var now = DateUtil.now();
        return ElevatorPushRunningDTO.builder()
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .deviceRegisterNumber(source.getVEquipmentCode())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .deviceIdentificationNumber(source.getIdentificationNumber())
                .equCode(source.getEquCode())
                .elevatorType(source.getIElevatorType())
                .currentTime(now)
                .parking(null)
                .serviceMode(changeServiceMode(message.getModeStatus()))
                .carDirection(message.getDirection())
                .carStatus(message.getSpeed() <= 0.01)
                .doorStatus(buildDoorStatus(message.getDroopClose()))
                .doorCloseStatus(1 == message.getDroopClose())
                .doorZone(0 == message.getFloorStatus())
                .carPosition(message.getFloor())
                .passengerStatus(1 == message.getHasPeople())
                .passengerActivityMode(0)
                .carOverload(false)
                .wireBendingCount(source.getBiBendCount())
                .carCall(null)
                .hallCall(null)
                .maintenance(message.getModeStatus())
                .collectTime(StringUtils.hasText(time) ? time : now)
                .passengerNumber(null)
                .speed(Double.valueOf(String.valueOf(message.getSpeed())))
                .carTemperature(null)
                .pressure(null)
                .escalatorOperationDirection(source.getIElevatorType() == 1
                        ? null : message.getModeStatus() > 1 ? 0 : (message.getModeStatus() + 1))
                .escalatorOperationDirection(2 == message.getDirection() ? 3 : message.getDirection())
                .machineRoomTemperature(null == message.getTemperature()
                        ? null : Double.valueOf(String.valueOf(message.getTemperature())))
                .machineRoomDoorStatus(true)
                .hoistwayDoor(null != message.getDoorStatus() && 1 == message.getDoorStatus())
                .carDoorStatus(null != message.getCarStatus() && 1 == message.getCarStatus())
                .hallDoorStatus(null != message.getDoorStatus() && 1 == message.getDoorStatus())
                .liftCarDriveStatus(null == message.getDriveStatus() ? 0 : (message.getDriveStatus() + 1))
                .deviceType(null == device ? null : device.geteType())
                .build();
    }
    // CHECKSTYLE:ON

    /**
     * 运行数据，带周期统计数据
     *
     * @param source  电梯信息
     * @param content 运行数据
     * @param device  设备信息
     * @return 推送信息
     */
    public ElevatorPushRunningWithPeriodicDTO convertRunningWithPeriodicToGovern(TblElevator source,
                                                                                 MessageData content,
                                                                                 TblDevice device) {
        var running = convertRunningToGovern(source, content.getMonitorMessage(), device, content.getTime());
        var periodic = convertPeriodicToGovern(source);
        return ElevatorPushRunningWithPeriodicDTO.builder()
                .running(running)
                .periodic(periodic)
                .build();
    }

    /**
     * 周期数据
     *
     * @param source 电梯信息
     * @return 推送信息
     */
    public ElevatorPushPeriodicDTO convertPeriodicToGovern(TblElevator source) {
        var now = DateUtil.now();
        var totalRunTime = countRunTime(source.getBiRunTime(), source.getDtInstallTime());
        return ElevatorPushPeriodicDTO.builder()
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .deviceRegisterNumber(source.getVEquipmentCode())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .deviceIdentificationNumber(source.getIdentificationNumber())
                .equCode(source.getEquCode())
                .elevatorType(source.getIElevatorType())
                .currentTime(now)
                .totalRunMileage(source.getBiRunDistanceCount())
                .doorOpenCount(source.getBiDoorCount())
                .totalRunningTime(totalRunTime)
                .totalRunCount(source.getBiRunCount())
                .ropeBendCount(source.getBiBendCount())
                .brakeTorque(false)
                .traction(false)
                .powerConsumption(null)
                .collectTime(DateUtil.today())
                .passengerCounter(null)
                .build();
    }

    /**
     * 模式切换, 暂时不支持
     *
     * @param source 电梯信息
     * @return 推送信息
     */
    public ElevatorPushModeChangeDTO convertModeChangeToGovern(TblElevator source) {
        return null;
    }

    /**
     * 困人 故障信息，带统计信息
     *
     * @param source  电梯信息
     * @param message 故障信息
     * @return 推送信息
     */
    public ElevatorPushFaultWithPeriodicDTO convertFaultEntrapToGovern(TblElevator source, FaultMessage message,
                                                                       String ptCode) {
        var now = DateUtil.now();
        var faultImageUrl = "";
        var faultVideoUrl = "";
        if (message instanceof FaultWithVideoPhoto) {
            var messageWithWideoPhoto = (FaultWithVideoPhoto) message;
            faultImageUrl = messageWithWideoPhoto.getFaultImageUrl();
            faultVideoUrl = messageWithWideoPhoto.getFaultVideoUrl();
        }

        var fault = ElevatorPushFaultDTO.builder()
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .deviceRegisterNumber(source.getVEquipmentCode())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .deviceIdentificationNumber(source.getIdentificationNumber())
                .equCode(source.getEquCode())
                .elevatorType(source.getIElevatorType())
                .currentTime(now)
                .collectTime(message.getTime())
                .faultNo(message.getFaultId())
                .sourceFaultTypeCode(message.getFault_type())
                .sourceFaultTypeName(buildFaultType(message.getFault_type(), ptCode, "faultTypeName"))
                .sourceFaultTypeSlug(buildFaultType(message.getFault_type(), ptCode, "faultTypSlug"))
                .recovery("add".equals(message.getST()) ? 0 : 1)
                .entrap(GovernFaultTypeEnum.ENTRAP.getShmashineCode().equals(message.getFault_type()) ? 1 : 0)
                .entrap2(GovernFaultTypeEnum.ENTRAP2.getShmashineCode().equals(message.getFault_type()) ? 1 : 0)
                .remark(message.getFault_type())
                .faultImageUrl(faultImageUrl)
                .faultVideoUrl(faultVideoUrl)
                .build();
        var running = convertRunningToGovern(source, message.getMonitorMessage(), null, message.getTime());
        var periodic = convertPeriodicToGovern(source);
        return ElevatorPushFaultWithPeriodicDTO.builder()
                .faults(Collections.singletonList(fault))
                .periodic(periodic)
                .running(running)
                .build();
    }

    /**
     * 故障信息，带统计信息
     *
     * @param source  电梯信息
     * @param message 故障信息
     * @return 推送信息
     */
    public ElevatorPushFaultWithPeriodicDTO convertFaultToGovern(TblElevator source, FaultMessage message,
                                                                 String ptCode) {
        var now = DateUtil.now();
        var faultImageUrl = "";
        var faultVideoUrl = "";
        if (message instanceof FaultWithVideoPhoto messageWithVideoPhoto) {
            log.info("checkAndSendFault sendFault {} step 5-1", JSON.toJSONString(messageWithVideoPhoto));
            faultImageUrl = messageWithVideoPhoto.getFaultImageUrl();
            faultVideoUrl = messageWithVideoPhoto.getFaultVideoUrl();
        }

        var fault = ElevatorPushFaultDTO.builder()
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .deviceRegisterNumber(source.getVEquipmentCode())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .deviceIdentificationNumber(source.getIdentificationNumber())
                .equCode(source.getEquCode())
                .elevatorType(source.getIElevatorType())
                .currentTime(now)
                .collectTime(message.getTime())
                .faultNo(message.getFaultId())
                .sourceFaultTypeCode(message.getFault_type())
                .sourceFaultTypeName(buildFaultType(message.getFault_type(), ptCode, "faultTypeName"))
                .sourceFaultTypeSlug(buildFaultType(message.getFault_type(), ptCode, "faultTypSlug"))
                .recovery("add".equals(message.getST()) ? 0 : 1)
                .remark(message.getFault_type())
                .faultImageUrl(faultImageUrl)
                .faultVideoUrl(faultVideoUrl)
                .build();
        // 设置故障类型，并返回是否成功设置
        var successChangeFault = changeFaultToGovernFault(fault, message, ptCode);
        if (Boolean.FALSE.equals(successChangeFault)) {
            log.info("checkAndSendFault sendFault {} {} end step 5-2", source.getVElevatorCode(), successChangeFault);
            return null;
        }
        // 实时运行数据
        var running = convertRunningToGovern(source, message.getMonitorMessage(), null, message.getTime());
        var periodic = convertPeriodicToGovern(source);
        return ElevatorPushFaultWithPeriodicDTO.builder()
                .faults(Collections.singletonList(fault))
                .periodic(periodic)
                .running(running)
                .build();
    }

    /**
     * 在线状态
     *
     * @param source 电梯信息
     * @return 推送信息
     */
    public ElevatorPushStatusDTO convertStatusToGovern(TblElevator source, OnOfflineMessage message) {
        var now = DateUtil.now();
        return ElevatorPushStatusDTO.builder()
                .elevatorCode(source.getVElevatorCode())
                .leaveFactoryNumber(source.getVLeaveFactoryNumber())
                .deviceRegisterNumber(source.getVEquipmentCode())
                .manufactureUnitSocialCreditCode(source.getVManufacturerCode())
                .deviceIdentificationNumber(source.getIdentificationNumber())
                .equCode(source.getEquCode())
                .elevatorType(source.getIElevatorType())
                .currentTime(now)
                .status(null == message.getOnline() ? -1 : (message.getOnline() ? 0 : 1))
                .conformDate(message.getTime())
                .build();
    }

    /**
     * 计算运行时长
     *
     * @param biRunTime     运行时长
     * @param dtInstallTime 安装时间
     * @return 运行时长
     */
    private Long countRunTime(Long biRunTime, Object dtInstallTime) {
        if (null != biRunTime) {
            return biRunTime * 60;
        }
        if (null == dtInstallTime) {
            return null;
        }
        var installTime = dtInstallTime.toString();
        return DateUtil.between(DateUtil.parseDate(installTime), DateUtil.date(), DateUnit.MINUTE);
    }


    /**
     * 电梯关门状态
     *
     * @param droopClose 关门到位 0：无关门到位，1：关门到位
     * @return 开关门状态 0: 未知 1: 正在关门 2: 关门到位 3: 正在开门 4: 开门到位 5: 门锁锁止 6: 保持不完全关闭状态
     */
    private Integer buildDoorStatus(Integer droopClose) {
        return null == droopClose ? 0 : (droopClose == 1 ? 2 : 0);
    }

    /**
     * 故障类型转换
     *
     * @param res   故障结果
     * @param fault 故障信息
     * @return 是否成功转换
     */
    private Boolean changeFaultToGovernFault(ElevatorPushFaultDTO res, FaultMessage fault, String ptCode) {
        String faultCode = "";
        // 重庆
        if ("chongqing".equals(ptCode)) {
            for (var faultType : GovernFaultTypeChongqingEnum.values()) {
                if (faultType.getFaultType().equals(fault.getFault_type())) {
                    faultCode = faultType.getFaultCode();
                }
            }
        } else {
            // 非重庆
            for (var faultType : GovernFaultTypeEnum.values()) {
                if (faultType.getShmashineCode().equals(fault.getFault_type())) {
                    faultCode = faultType.getFaultCode();
                }
            }
        }
        if (!StringUtils.hasText(faultCode)) {
            return false;
        }
        // 匹配字段
        var fields = ElevatorPushFaultDTO.class.getDeclaredFields();
        for (var field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(faultCode)) {
                try {
                    field.set(res, 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    log.error("MashineToGovernPushInfoConvertor 故障信息转换失败", e);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 管控模式转换  0：正常运行, 1：检修模式， 2：停止服务, 3：消防返回， 4：消防员运行, 5：应急电源运行， 7：地震模式
     *
     * @return 0: 停止服务 1: 正常运行 2: 检修 3: 消防返回 4: 消防员运行 5: 应急电源运行 6: 地震模式 7: 未知
     */
    private Integer changeServiceMode(Integer modeStatus) {
        return switch (modeStatus) {
            case 1 -> 2;
            case 2 -> 0;
            case 3 -> 3;
            case 4 -> 4;
            case 5 -> 5;
            case 7 -> 6;
            default -> 1;
        };
    }


    private String buildFaultType(String faultType, String ptCode, String nameOrCode) {
        // 重庆
        if ("chongqing".equals(ptCode)) {
            var faultTypeEnum = GovernFaultTypeChongqingEnum.getEnumByFaultType(faultType);
            if (faultTypeEnum != null) {
                return nameOrCode.contains("Slug") ? faultTypeEnum.getFaultCode() : faultTypeEnum.getFaultName();
            }
            return null;
        }
        // 非重庆
        var faultTypeEnum = GovernFaultTypeEnum.getEnumByShmashineCode(faultType);
        if (faultTypeEnum != null) {
            return nameOrCode.contains("Slug") ? faultTypeEnum.getFaultCode() : faultTypeEnum.getFaultName();
        }
        return null;
    }
}
