// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.platform.city.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblCameraCascadePlatformEntity;
import com.shmashine.common.entity.TblCameraExtendInfoEntity;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.message.FaultWithVideoPhoto;
import com.shmashine.common.message.MessageData;
import com.shmashine.common.message.OnOfflineMessage;
import com.shmashine.common.message.PeriodicMessage;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.mgppf.components.dto.elevator.CityDTO;
import com.shmashine.mgppf.components.dto.elevator.ElevatorModelDTO;
import com.shmashine.mgppf.components.dto.enums.GovernCitySupportEnum;
import com.shmashine.mgppf.components.dto.enums.GovernClientPlatformEnum;
import com.shmashine.mgppf.components.dto.enums.GovernElevatorRegisterEnum;
import com.shmashine.mgppf.components.dto.enums.GovernFaultTypeEnum;
import com.shmashine.mgppf.components.dto.govern.GovernAccessTokenRequestDTO;
import com.shmashine.mgppf.components.dto.govern.GovernTokenDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushFaultWithPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushRegisterDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushRunningWithPeriodicDTO;
import com.shmashine.mgppf.components.dto.pushinfo.ElevatorPushStatusDTO;
import com.shmashine.sender.dao.TblBuildingMapper;
import com.shmashine.sender.dao.TblCameraCascadePlatformMapper;
import com.shmashine.sender.dao.TblCameraDownloadTaskMapper;
import com.shmashine.sender.dao.TblCameraExtendInfoMapper;
import com.shmashine.sender.dao.TblCameraMapper;
import com.shmashine.sender.dao.TblDeviceMapper;
import com.shmashine.sender.dao.TblElevatorBrandMapper;
import com.shmashine.sender.dao.TblSysDeptMapper;
import com.shmashine.sender.dao.TblSysProvincialCityMapper;
import com.shmashine.sender.dao.TblVillageMapper;
import com.shmashine.sender.platform.convert.MashineToGovernPushInfoConvertor;
import com.shmashine.sender.properties.EndpointProperties;
import com.shmashine.sender.redis.utils.RedisUtils;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 14:32
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CityTool {
    private final EndpointProperties properties;

    private final RedisUtils redisUtils;
    private final MashineToGovernPushInfoConvertor convertor;
    private final TblDeviceMapper deviceMapper;
    private final TblElevatorBrandMapper brandMapper;
    private final TblSysDeptMapper sysDeptMapper;
    private final TblBuildingMapper buildingMapper;
    private final TblVillageMapper villageMapper;
    private final TblSysProvincialCityMapper provincialCityMapper;
    private final TblCameraMapper cameraMapper;
    private final TblCameraCascadePlatformMapper cameraCascadePlatformMapper;
    private final TblCameraExtendInfoMapper cameraExtendInfoMapper;
    private final TblCameraDownloadTaskMapper cameraDownloadTaskMapper;

    private final BizElevatorService elevatorService;
    private final RestTemplate restTemplate;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(512, 2048,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "cityTool");

    private static final String URI_GET_TOKEN = "/api/govern/oauth/token";
    private static final String URI_SAVE_PROJECT = "/api/govern/project-manage/save-project";
    private static final String URI_SAVE_ELEVATOR = "/api/govern/elevator-manage/save-elevator";
    private static final String URI_PUSH_PROJECT_REGISTER = "/api/govern/push/project-register";
    private static final String URI_PUSH_ELEVATOR_REGISTER = "/api/govern/push/elevator-register";
    private static final String URI_PUSH_STATUS_INFO = "/api/govern/push/status-info/{elevatorCode}";
    private static final String URI_PUSH_PERIODIC_INFO = "/api/govern/push/periodic-info/{elevatorCode}";
    private static final String URI_PUSH_RUNNING_INFO = "/api/govern/push/running-info/{elevatorCode}";
    private static final String URI_PUSH_FAULT_INFO = "/api/govern/push/fault-info/{elevatorCode}";
    //private static final String URI_PUSH_FAULT_RECOVERY_INFO = "/api/govern/push/fault-recovery-info/{elevatorCode}";
    private static final String URI_PUSH_FAULT_ENTRAP_INFO = "/api/govern/push/fault-entrap-info/{elevatorCode}";

    //private static final String ENV_PROD = "prod";


    public void sendRunningDataToGovern(MessageData message, String ptCode) {
        if (null == message) {
            return;
        }
        var elevator = checkAndInitElevator(message.getElevatorCode(), ptCode);
        if (null == elevator) {
            return;
        }
        // 设备
        var device = deviceMapper.getOneByElevatorCode(elevator.getVElevatorCode());
        var data = convertor.convertRunningWithPeriodicToGovern(elevator, message, device);
        pushRunningDataToGovern(elevator, data);
    }

    public SingleResponse<String> sendEntrapDataToGovern(FaultMessage message, String ptCode) {
        if (null == message) {
            return null;
        }
        if (!GovernFaultTypeEnum.ENTRAP.getShmashineCode().equals(message.getFault_type())
                && !GovernFaultTypeEnum.ENTRAP2.getShmashineCode().equals(message.getFault_type())) {
            return null;
        }
        var elevator = checkAndInitElevator(message.getElevatorCode(), ptCode);
        if (null == elevator) {
            return null;
        }
        var data = convertor.convertFaultEntrapToGovern(elevator, message, ptCode);
        var res = pushFaultEntrapDataToGovern(elevator, data);
        log.info("sendEntrapDataToGovern {} {} push entrap result {}", elevator.getVElevatorCode(),
                elevator.getVLeaveFactoryNumber(), res);
        return res;
    }

    public <T extends FaultMessage> SingleResponse<String> sendFaultDataToGovern(T message, String ptCode) {
        if (null == message) {
            return null;
        }
        log.info("checkAndSendFault sendFault {} step 4", message.getElevatorCode());
        var elevator = checkAndInitElevator(message.getElevatorCode(), ptCode);
        if (null == elevator) {
            log.info("checkAndSendFault sendFault {} no elevator step 5", message.getElevatorCode());
            return null;
        }

        var faultWithVideoPhoto = new FaultWithVideoPhoto();
        BeanUtils.copyProperties(message, faultWithVideoPhoto);
        if (!StringUtils.hasText(faultWithVideoPhoto.getFaultVideoUrl())
                && !StringUtils.hasText(faultWithVideoPhoto.getFaultImageUrl())) {
            // 直接使用故障ID在 摄像头下载任务中查询是否有图
            var faultVideoPhoto = cameraDownloadTaskMapper.listSuccessesByTaskCustomId(message.getFaultId());
            if (!CollectionUtils.isEmpty(faultVideoPhoto)) {
                faultVideoPhoto.forEach(taskEntity -> {
                    if (CameraMediaTypeEnum.getMediaTypeListByFileType("image").contains(taskEntity.getMediaType())) {
                        faultWithVideoPhoto.setFaultImageUrl(taskEntity.getOssUrl());
                    }
                    if (CameraMediaTypeEnum.getMediaTypeListByFileType("video").contains(taskEntity.getMediaType())) {
                        faultWithVideoPhoto.setFaultVideoUrl(taskEntity.getOssUrl());
                    }
                });
            }
            log.info("checkAndSendFault sendFault {} step 6", JSON.toJSONString(faultWithVideoPhoto));
        }

        var data = convertor.convertFaultToGovern(elevator, faultWithVideoPhoto, ptCode);
        var res = pushFaultDataToGovern(elevator, data);
        log.info("checkAndSendFault sendFaultDataToGovern {} {} push fault result {} step 7",
                elevator.getVElevatorCode(), elevator.getVLeaveFactoryNumber(), res);
        return res;
    }

    public void sendOnlineStatusToGovern(OnOfflineMessage message, String ptCode) {
        if (null == message) {
            return;
        }
        var elevator = checkAndInitElevator(message.getElevatorCode(), ptCode);
        if (null == elevator) {
            return;
        }
        var status = convertor.convertStatusToGovern(elevator, message);
        pushStatusDataToGovern(elevator, status);
    }


    public void sendPeriodicToGovern(PeriodicMessage message, String ptCode) {
        if (null == message) {
            return;
        }
        var elevator = checkAndInitElevator(message.getElevatorCode(), ptCode);
        if (null == elevator) {
            return;
        }
        var periodic = convertor.convertPeriodicToGovern(elevator);
        pushPeriodicDataToGovern(elevator, periodic);
    }

    public void sendProjectDataToGovern(String elevatorCode, String ptCode) {
        if (!StringUtils.hasText(elevatorCode)) {
            return;
        }
        var elevator = elevatorService.getByElevatorCode(elevatorCode);
        if (null == elevator) {
            return;
        }
        var citySupport = GovernCitySupportEnum.getBySlug(ptCode);
        if (null == citySupport) {
            log.info("govern-check {} platform not support {}", elevator.getVElevatorCode(), ptCode);
            return;
        }
        // 城市
        var city = CityDTO.builder()
                .areaCode(citySupport.getAreaCode())
                .cityName(citySupport.getTitle())
                .build();
        // 品牌
        var brand = brandMapper.getByBrandId(elevator.getVElevatorBrandId());
        // 维保公司
        var company = sysDeptMapper.getMaintainCompanyById(elevator.getVMaintainCompanyId());
        // 物业公司
        var property = sysDeptMapper.getPropertyCompanyById(elevator.getVPropertyCompanyId());
        // 楼宇信息
        var building = buildingMapper.getOneByVillageAndBuildingId(elevator.getVVillageId(), elevator.getVBuildingId());
        // 小区信息
        var village = villageMapper.getOneById(elevator.getVVillageId());
        // 省
        var provinceInfo = provincialCityMapper.getByAreaId(elevator.getIProvinceId());
        // 市
        var cityInfo = provincialCityMapper.getByAreaId(elevator.getICityId());
        // 区
        var areaInfo = provincialCityMapper.getByAreaId(elevator.getIAreaId());
        // CHECKSTYLE:OFF
        var extendInfo = new HashMap<String, String>() {
            {
                put("brandId", elevator.getVElevatorBrandId());
                put("brandName", brand == null ? null : brand.getVElevatorBrandName());
                put("maintainCompanyId", elevator.getVMaintainCompanyId());
                put("maintainCompanyName", company == null ? null : company.getVDeptName());
                put("propertyCompanyId", elevator.getVPropertyCompanyId());
                put("propertyCompanyName", property == null ? null : property.getVDeptName());
                put("buildingId", elevator.getVBuildingId());
                put("buildingName", building == null ? null : building.getVBuildingName());
                put("villageId", elevator.getVVillageId());
                put("villageName", village == null ? null : village.getVVillageName());
                put("street", village == null ? null : village.getStreet());
                put("provinceId", String.valueOf(elevator.getIProvinceId()));
                put("provinceName", provinceInfo == null ? null : provinceInfo.getAreaName());
                put("cityId", String.valueOf(elevator.getICityId()));
                put("cityName", cityInfo == null ? null : cityInfo.getAreaName());
                put("areaId", String.valueOf(elevator.getIAreaId()));
                put("areaName", areaInfo == null ? null : areaInfo.getAreaName());
            }
        };
        // CHECKSTYLE:ON
        var projectModel = convertor.convertProjectShmashineToGovern(village, extendInfo, elevator, city, ptCode);
        if (null == projectModel) {
            log.info("projectModel is null because of village is null");
            return;
        }
        // 存储项目信息
        sendHttpRequestToGovernmentPlatform(URI_SAVE_PROJECT, elevatorCode, getToken(), projectModel, null);
        // 推送到对应政府平台
        sendHttpRequestToGovernmentPlatform(URI_PUSH_PROJECT_REGISTER, elevatorCode, getToken(), projectModel, null);
    }

    public void sendRegisterElevatorToGovern(String elevatorCode, String ptCode) {
        if (!StringUtils.hasText(elevatorCode)) {
            return;
        }
        var elevator = elevatorService.getByElevatorCode(elevatorCode);
        if (null == elevator) {
            return;
        }
        registerElevator(elevator);
    }


    // 远程调取 城市推送模块


    public TblElevator checkAndInitElevator(String elevatorCode, String ptCode) {
        if (!StringUtils.hasText(elevatorCode)) {
            return null;
        }
        var elevator = elevatorService.getByElevatorCode(elevatorCode);
        var checkRes = checkAndRegisterElevator(elevator, ptCode);
        if (!checkRes) {
            log.info("handleMonitor checkAndRegisterElevator fail, elevatorCode:{} platformCity:{}",
                    elevatorCode, ptCode);
            return null;
        }
        return elevator;
    }

    public Boolean checkAndRegisterElevator(TblElevator elevator, String ptCode) {
        // 检查是否已经注册过该梯
        var elevatorRedisKey = RedisKeyUtils.getCityPushPlatformElevatorExistsKey(elevator.getVElevatorCode());
        var redisRes = redisUtils.get(elevatorRedisKey);
        if (!"null".equals(redisRes) && StringUtils.hasText(redisRes)) {
            log.info("govern-check {} exits", elevator.getVElevatorCode());
            return true;
        }

        // 不存在，需要注册
        var citySupport = GovernCitySupportEnum.getBySlug(ptCode);
        if (null == citySupport) {
            log.info("govern-check {} platform not support {}", elevator.getVElevatorCode(), ptCode);
            return false;
        }
        // 品牌
        var brand = brandMapper.getByBrandId(elevator.getVElevatorBrandId());
        // 维保公司
        var company = sysDeptMapper.getMaintainCompanyById(elevator.getVMaintainCompanyId());
        // 物业公司
        var property = sysDeptMapper.getPropertyCompanyById(elevator.getVPropertyCompanyId());
        // 楼宇信息
        var building = buildingMapper.getOneByVillageAndBuildingId(elevator.getVVillageId(),
                elevator.getVBuildingId());
        // 小区信息
        var village = villageMapper.getOneById(elevator.getVVillageId());
        // 省
        var provinceInfo = provincialCityMapper.getByAreaId(elevator.getIProvinceId());
        // 市
        var cityInfo = provincialCityMapper.getByAreaId(elevator.getICityId());
        // 区
        var areaInfo = provincialCityMapper.getByAreaId(elevator.getIAreaId());
        // 摄像头
        var camera = cameraMapper.getCameraByElevatorCode(elevator.getVElevatorCode());

        // 摄像头扩展
        TblCameraCascadePlatformEntity cascadePlatform = null;
        TblCameraExtendInfoEntity cameraExtendInfo = null;
        if (camera != null) {
            if (CameraTypeEnum.TYYY.getCode().equals(camera.getCameraType())
                    || CameraTypeEnum.TYBD.getCode().equals(camera.getCameraType())) {
                // 电信视联摄像头扩展
                cameraExtendInfo = cameraExtendInfoMapper.getByElevatorCode(elevator.getVElevatorCode());
            } else {
                // 摄像头国标
                cascadePlatform = cameraCascadePlatformMapper.getByElevatorCode(elevator.getVElevatorCode());
            }
        }
        //TblCameraCascadePlatformEntity finalCascadePlatform = cascadePlatform;

        // CHECKSTYLE:OFF
        var extendInfo = new HashMap<String, String>() {
            {
                put("brandId", elevator.getVElevatorBrandId());
                put("brandName", brand == null ? null : brand.getVElevatorBrandName());
                put("brandNameMark", brand == null ? null : brand.getVRemarks());
                put("maintainCompanyId", elevator.getVMaintainCompanyId());
                put("maintainCompanyName", company == null ? null : company.getVDeptName());
                put("propertyCompanyId", elevator.getVPropertyCompanyId());
                put("propertyCompanyName", property == null ? null : property.getVDeptName());
                put("propertyCompanyPhone", property == null ? null : property.getVMobilephone());
                put("buildingId", elevator.getVBuildingId());
                put("buildingName", building == null ? null : building.getVBuildingName());
                put("villageId", elevator.getVVillageId());
                put("villageName", village == null ? null : village.getVVillageName());
                put("street", village == null ? null : village.getStreet());
                put("neighborhood", village == null ? null : village.getNeighborhood());
                put("provinceId", String.valueOf(elevator.getIProvinceId()));
                put("provinceName", provinceInfo == null ? null : provinceInfo.getAreaName());
                put("cityId", String.valueOf(elevator.getICityId()));
                put("cityName", cityInfo == null ? null : cityInfo.getAreaName());
                put("areaId", String.valueOf(elevator.getIAreaId()));
                put("areaName", areaInfo == null ? null : areaInfo.getAreaName());
                put("cameraId", camera == null ? null : camera.getCameraId());
                put("cameraType", camera == null ? null : String.valueOf(camera.getCameraType()));
                put("cameraCloudNumber", camera == null ? null : camera.getCloudNumber());
                put("cameraPlatformId", null);
                put("cameraPlatformName", null);
                put("cameraChannelId", null);
                put("cameraChannelCode", camera == null ? null : camera.getDeviceCode());
                put("cameraChannelNo", null);
            }
        };
        // CHECKSTYLE:ON
        if (cascadePlatform != null) {
            extendInfo.put("cameraPlatformId", cascadePlatform.getPlatformId());
            extendInfo.put("cameraPlatformName", cascadePlatform.getPlatformName());
            extendInfo.put("cameraChannelId", cascadePlatform.getChannelId());
            extendInfo.put("cameraChannelCode", cascadePlatform.getChannelCode());
            extendInfo.put("cameraChannelNo", String.valueOf(cascadePlatform.getChannelNo()));
        }
        if (cameraExtendInfo != null) {
            extendInfo.put("cameraPlatformId", cameraExtendInfo.getPlatformId());
            extendInfo.put("cameraPlatformName", cameraExtendInfo.getPlatformName());
            extendInfo.put("cameraChannelId", cameraExtendInfo.getGuid());
            extendInfo.put("cameraChannelCode", cameraExtendInfo.getDeviceCode());
            extendInfo.put("cameraChannelNo", String.valueOf(cameraExtendInfo.getChannelSeq()));
        }

        // 城市
        var city = CityDTO.builder()
                .areaCode(citySupport.getAreaCode())
                .cityName(citySupport.getTitle())
                .build();
        // 存储项目信息
        checkAndRegisterProject(village, company, property, extendInfo, elevator, city, ptCode);

        // 设备
        var device = deviceMapper.getOneByElevatorCode(elevator.getVElevatorCode());
        // 转换电梯模型
        var elevatorModel = convertor.convertElevatorShmashineToGovern(elevator, city, device, camera, extendInfo,
                ptCode);
        saveElevatorInfo(elevator, elevatorModel);

        redisUtils.set(elevatorRedisKey, JSON.toJSONString(elevatorModel), 4 * 23 * 3600);

        // 注册电梯
        registerElevator(elevator);
        // 一些特殊城市需要上报状态信息，才能上传运行数据
        // 台州需要先上报感知设备基本信息
        if (GovernCitySupportEnum.TAIZHOU.getSlug().equals(ptCode)) {
            var message = OnOfflineMessage.builder()
                    .elevatorCode(elevator.getVElevatorCode())
                    .sensorType(device.getVSensorType())
                    .online(true)
                    .time(DateUtil.now())
                    .build();
            var status = convertor.convertStatusToGovern(elevator, message);
            pushStatusDataToGovern(elevator, status);
            return true;
        }
        return true;
    }

    /**
     * 将小区信息 存储成推送平台的 项目信息
     *
     * @param village    小区信息
     * @param company    维保公司
     * @param property   物业公司
     * @param extendInfo 扩展信息
     * @param elevator   电梯信息
     * @param city       城市信息
     */
    private void checkAndRegisterProject(TblVillage village, TblSysDept company,
                                         TblSysDept property, Map<String, String> extendInfo, TblElevator elevator,
                                         CityDTO city, String ptCode) {
        if (null == village) {
            return;
        }
        var redisKey = RedisKeyUtils.getCityPushPlatformProjectExistsKey(village.getVVillageId());
        var redisRes = redisUtils.get(redisKey);
        if (!"null".equals(redisRes) && StringUtils.hasText(redisRes)) {
            log.info("govern-check project {} exits", village.getVVillageId());
            return;
        }
        var projectModel = convertor.convertProjectShmashineToGovern(village, extendInfo, elevator, city, ptCode);
        if (null == projectModel) {
            log.info("projectModel is null because of village is null");
            return;
        }
        // 加锁存储项目信息
        if (!redisUtils.setIfAbsent("LOCK:" + redisKey, "saveProject", 60L)) {
            log.info("govern-check {} {} save project lock failed", village.getVVillageId(), URI_SAVE_PROJECT);
            return;
        }
        sendHttpRequestToGovernmentPlatform(URI_SAVE_PROJECT, elevator.getVElevatorCode(), getToken(), projectModel,
                null);
        redisUtils.set(redisKey, JSON.toJSONString(projectModel), 7 * 24 * 3600L);
        redisUtils.del("LOCK:" + redisKey);
        // 推送到对应政府平台
        sendHttpRequestToGovernmentPlatform(URI_PUSH_PROJECT_REGISTER, elevator.getVElevatorCode(), getToken(),
                projectModel, null);
    }

    private void saveElevatorInfo(TblElevator elevator, ElevatorModelDTO elevatorModel) {
        // 存储电梯
        sendHttpRequestToGovernmentPlatform(URI_SAVE_ELEVATOR, elevator.getVElevatorCode(), getToken(),
                elevatorModel, null);
    }

    private void registerElevator(TblElevator elevator) {
        var register = ElevatorPushRegisterDTO.builder()
                .elevatorCode(elevator.getVElevatorCode())
                .withDetail(true)
                .registerEnum(GovernElevatorRegisterEnum.REGISTER)
                .build();
        sendHttpRequestToGovernmentPlatform(URI_PUSH_ELEVATOR_REGISTER, elevator.getVElevatorCode(), getToken(),
                register, null);
    }

    private void pushStatusDataToGovern(TblElevator elevator, ElevatorPushStatusDTO status) {
        sendHttpRequestToGovernmentPlatform(URI_PUSH_STATUS_INFO, elevator.getVElevatorCode(), getToken(), status,
                Map.of("elevatorCode", elevator.getVElevatorCode()));
    }


    private void pushPeriodicDataToGovern(TblElevator elevator, ElevatorPushPeriodicDTO periodic) {
        sendHttpRequestToGovernmentPlatform(URI_PUSH_PERIODIC_INFO, elevator.getVElevatorCode(), getToken(), periodic,
                Map.of("elevatorCode", elevator.getVElevatorCode()));
    }

    private void pushRunningDataToGovern(TblElevator elevator, ElevatorPushRunningWithPeriodicDTO running) {
        sendHttpRequestToGovernmentPlatform(URI_PUSH_RUNNING_INFO, elevator.getVElevatorCode(), getToken(),
                running, Map.of("elevatorCode", elevator.getVElevatorCode()));
    }

    private SingleResponse<String> pushFaultDataToGovern(TblElevator elevator,
                                                         ElevatorPushFaultWithPeriodicDTO fault) {
        var res = sendHttpRequestToGovernmentPlatform(URI_PUSH_FAULT_INFO, elevator.getVElevatorCode(), getToken(),
                JSON.toJSONString(fault), SingleResponse.class, Map.of("elevatorCode", elevator.getVElevatorCode()));
        log.info("govern-check {} {} push fault {} result {}", elevator.getVElevatorCode(), URI_PUSH_FAULT_INFO,
                JSON.toJSONString(fault), res);
        return res;
    }

    private SingleResponse<String> pushFaultEntrapDataToGovern(TblElevator elevator,
                                                               ElevatorPushFaultWithPeriodicDTO fault) {
        var res = sendHttpRequestToGovernmentPlatform(URI_PUSH_FAULT_ENTRAP_INFO, elevator.getVElevatorCode(),
                getToken(), JSON.toJSONString(fault), SingleResponse.class,
                Map.of("elevatorCode", elevator.getVElevatorCode()));
        log.info("govern-check {} {} push fault entrap result {}", elevator.getVElevatorCode(),
                URI_PUSH_FAULT_ENTRAP_INFO, res);
        return res;
    }

    /**
     * 构建请求地址, 正式环境使用内网地址
     *
     * @param uri 请求uri
     * @return 请求地址
     */
    private String buildHttpUrl(String uri) {
        if (uri.startsWith("http")) {
            return uri;
        }
        var url = properties.getPushCityGovernDomain() + uri;
        log.info("govern-check build url {}", url);
        return url;
    }

    private <T> void sendHttpRequestToGovernmentPlatform(String uri, String elevatorCode, String token, T request,
                                                         Map<String, Object> uriVariables) {
        executorService.execute(() -> {
            final String[] url = {buildHttpUrl(uri)};
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (StringUtils.hasText(token)) {
                headers.set("Authorization", "Bearer " + token);
            }
            if (null != uriVariables) {
                uriVariables.forEach((key, value) -> url[0] = url[0].replace("{" + key + "}", value.toString()));
            }
            log.info("govern-check async {} {} body {}", url, elevatorCode, JSON.toJSONString(request));
            HttpRequest.post(url[0]).body(JSON.toJSONString(request)).bearerAuth(token).executeAsync();
            /*try {
                var body = new HttpEntity<>(request, headers);
                log.info("govern-check {} body {}", url, body);

                var res = null == uriVariables
                        ? restTemplate.postForObject(url, body, String.class)
                        : restTemplate.postForObject(url, body, String.class, uriVariables);
                log.info("govern-check {} {} result {}", url, body, res);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("govern-check {} {} error {}", url, request, e.getMessage());
            }*/
        });
    }

    private <T> T sendHttpRequestToGovernmentPlatform(String uri, String elevatorCode, String token, String request,
                                                      Class<T> responseType, Map<String, Object> uriVariables) {
        var url = buildHttpUrl(uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(token)) {
            headers.set("Authorization", "Bearer " + token);
        }
        try {
            var body = new HttpEntity<>(JSON.parseObject(request), headers);
            log.info("govern-check {} {} body {}", url, elevatorCode, body);

            var res = null == uriVariables
                    ? restTemplate.postForObject(url, body, responseType)
                    : restTemplate.postForObject(url, body, responseType, uriVariables);
            log.info("govern-check {} {} {} result {}", url, elevatorCode, body, res);
            return res;
        } catch (Exception e) {
            log.error("govern-check {} {} {} error {}", url, elevatorCode, request, ExceptionUtils.getStackTrace(e));
            log.info("govern-check {} {} {} error {}", url, elevatorCode, request, e.getMessage());
            return null;
        }
    }

    private String getToken() {
        var tokenRedisKey = RedisKeyUtils.getGovernPlatformKey(GovernClientPlatformEnum.MAI_XIN.getAppKey());
        var token = redisUtils.get(tokenRedisKey);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return getTokenFromGovernmentPlatform(tokenRedisKey);
    }

    private String getTokenFromGovernmentPlatform(String tokenRedisKey) {
        var url = buildHttpUrl(URI_GET_TOKEN);
        var request = GovernAccessTokenRequestDTO.builder()
                .appKey(GovernClientPlatformEnum.MAI_XIN.getAppKey())
                .appSecret(GovernClientPlatformEnum.MAI_XIN.getAppSecret())
                .build();

        var resString = sendHttpRequestToGovernmentPlatform(URI_GET_TOKEN, null, null, JSON.toJSONString(request),
                String.class, null);
        log.info("govern-check get token result {}", resString);
        var res = JSON.parseObject(resString, SingleResponse.class);
        if (null == res || !res.isSuccess() || null == res.getData()) {
            log.error("govern-check get token failed {}", res);
            return null;
        }
        var tokenInfo = JSON.parseObject(JSON.toJSONString(res.getData()), GovernTokenDTO.class);
        // 缓存
        var token = tokenInfo.getAccessToken();
        var expire = tokenInfo.getExpiresTime();
        redisUtils.set(tokenRedisKey, token, expire - 180L);
        return token;
    }

}
