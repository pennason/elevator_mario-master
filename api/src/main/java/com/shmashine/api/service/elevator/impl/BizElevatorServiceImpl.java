package com.shmashine.api.service.elevator.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.controller.elevator.VO.ElevatorRunDataStatisticsReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingConfigReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingConfigRespVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingReqVO;
import com.shmashine.api.controller.elevator.VO.ImageRecognitionMattingRespVO;
import com.shmashine.api.controller.elevator.VO.InsertImageRecognitionMattingConfigListReqVO;
import com.shmashine.api.controller.elevator.VO.UserElevatorsRespVO;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizFaultDao;
import com.shmashine.api.dao.TblDeviceDao;
import com.shmashine.api.dao.TblGroupLeasingStatisticsMapper;
import com.shmashine.api.entity.ElevatorForExcel;
import com.shmashine.api.entity.ElevatorRunCount;
import com.shmashine.api.entity.IotCard;
import com.shmashine.api.entity.TblElevatorConf;
import com.shmashine.api.entity.TblElevatorPrincipal;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.ElevatorDetailDownloadModuleMap;
import com.shmashine.api.module.elevator.ElevatorDetailModule;
import com.shmashine.api.module.elevator.ElevatorDetailResModule;
import com.shmashine.api.module.elevator.ElevatorRunDataStatisticsDownload;
import com.shmashine.api.module.elevator.ElevatorScreenModule;
import com.shmashine.api.module.elevator.ElevatorThirdPartyDetail;
import com.shmashine.api.module.elevator.ElevatorThirdPartyList;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.properties.EndpointProperties;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.elevator.DO.ImageRecognitionMattingConfigDO;
import com.shmashine.api.service.elevator.DO.UserElevatorsDO;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.api.service.elevatorproject.BizProjectService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.util.CameraUtils;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.constants.CameraConstants;
import com.shmashine.common.constants.ServiceConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;
import com.shmashine.common.entity.TblSysDept;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.enums.GroupLeasingLevelEnum;
import com.shmashine.common.utils.RequestUtil;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 电梯接口 业务层
 *
 * @version  1.0.0 2020/6/1215:49
 * @author LiuLiFu
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizElevatorServiceImpl implements BizElevatorService {
    private final BizElevatorDao bizElevatorDao;
    private final TblDeviceDao deviceDao;
    private final TblGroupLeasingStatisticsMapper groupLeasingStatisticsMapper;
    private final BizProjectService bizProjectService;
    private final TblVillageServiceI tblVillageServiceI;
    private final RestTemplate restTemplate;
    private final RestTemplate iotCardRestTemplate;
    private final ElevatorCacheService cacheService;
    private final BizFaultDao bizFaultDao;
    private final EndpointProperties endpointProperties;

    @Override
    public Map getElevatorStatisticsInfo(String leaveFactoryNumber, String equipmentCode) {
        ElevatorDetailResModule elevator = bizElevatorDao.getElevatorStatisticsInfo(leaveFactoryNumber, equipmentCode);

        if (elevator == null) {
            return null;
        }

        Map map = new HashMap();

        // (垂直梯)设备开门次数
        map.put("doorOpenCount", elevator.getBiDoorCount());

        // (扶梯/垂直梯)设备累计运行时间 单位小时
        Date startRunningTime = (Date) elevator.getDtInstallTime();
        if (null != startRunningTime) {
            long runTime = System.currentTimeMillis() - startRunningTime.getTime();
            runTime = runTime / (3600 * 1000);
            map.put("totalRunningTime", runTime);
        }
        // (扶梯/垂直梯)设备累计运行次数
        map.put("presentCounterValue", elevator.getBiRunCount());
        // (垂直梯)设备钢丝绳（带）折弯次数
        map.put("ropeBendCount", elevator.getBiBendCount());
        // (扶梯/垂直梯)设备累计运行距离 单位米
        map.put("liftMileage", elevator.getBiRunDistanceCount());

        return map;

    }

    @Override
    public List<String> getAllOnlineElevatorCode() {
        return bizElevatorDao.getAllOnlineElevatorCode();
    }

    /**
     * 根据电梯ID删除电梯信息
     *
     * @param elevatorCode
     */
    @Override
    public void deleteElevatorInfoByElevatorCode(String elevatorCode) {
        TblElevator tblElevator = bizElevatorDao.getElevatorIdByCode(elevatorCode);
        TblVillage tblVillage = tblVillageServiceI.getById(tblElevator.getVVillageId());

        deviceDao.deleteByElevatorCode(elevatorCode);
        bizElevatorDao.deleteElevatorInfoByElevatorCode(elevatorCode);

        if (tblVillage != null) {
            int count = tblVillage.getiElevatorCount() == null ? 0 : tblVillage.getiElevatorCount();
            tblVillage.setiElevatorCount(count);
            tblVillageServiceI.update(tblVillage);
        }
    }

    /**
     * 电梯列表分页展示
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public PageListResultEntity searchElevatorList(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule, pageIndex, pageSize);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map> hashMapPageInfo = new PageInfo<>(bizElevatorDao.searchElevatorList(searchElevatorModule), pageSize);

        // 拼接设备状态
        convertDeviceStatus(hashMapPageInfo);

        tblVillageServiceI.extendVillageInfo(hashMapPageInfo.getList());

        // 扩展是否可疑楼
        extendGroupLeasingInfo(hashMapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    /**
     * 电梯列表分页展示根据部门id
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public PageListResultEntity searchElevatorListByDeptId(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule, pageIndex, pageSize);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        // 查询数据
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> hashMapPageInfo = new PageInfo<>(bizElevatorDao.searchElevatorListByProjectId(searchElevatorModule), pageSize);

        // 拼接设备状态
        convertDeviceStatus(hashMapPageInfo);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(hashMapPageInfo.getList());

        // 扩展是否可疑楼
        extendGroupLeasingInfo(hashMapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());

    }

    /**
     * 电梯列表不分页
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public List<Map> searchElevatorListNoPage(SearchElevatorModule searchElevatorModule) {

        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule, Map.class);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        List<Map> result = bizElevatorDao.searchElevatorList(searchElevatorModule);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(result);

        return result;
    }

    /**
     * 打印电梯列表接口
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public List<ElevatorDetailDownloadModuleMap> searchElevatorListDownload(SearchElevatorModule searchElevatorModule) {
        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule,
                ElevatorDetailDownloadModuleMap.class);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        return bizElevatorDao.searchElevatorListDownload(searchElevatorModule);
    }

    /**
     * 获取电梯详情 字段信息不全
     *
     * @param elevatorId
     * @return
     */
    @Override
    public ElevatorDetailModule getElevatorByElevatorId(String elevatorId) {

        ElevatorDetailModule elevator = bizElevatorDao.getElevatorByElevatorId(elevatorId);
        if (elevator == null) {
            return null;
        }

        //如果为海康摄像头——则要做天翼物联平台处理
        if (elevator.getCameraType() != null && elevator.getCameraType() == CameraConstants.CameraType.HAIKANG_YS.getType()) {
            HashMap<String, String> info = bizElevatorDao.getHKCameraByTYInfo(elevator.getElevatorId());
            if (info != null) {
                String tenantNo = info.get("tenantNo");
                String tenantKey = info.get("tenantKey");
                String deviceNo = info.get("deviceNo");

                String result = CameraUtils.getUrl(tenantNo, tenantKey, deviceNo);

                String messageJson = JSONObject.parseObject(result).getString("result");
                JSONObject jsonObject = JSONObject.parseObject(messageJson);
                String rtmpUrl = jsonObject.getString("rtmpUrl");
                String hlsHttp = jsonObject.getString("hlsHttp");
                elevator.setRtmpUrl(rtmpUrl);
                elevator.setHlsUrl(hlsHttp);
            }
        }

        return elevator;
    }


    /**
     * 通过社会统一注册编号查找电梯
     *
     * @param equipmentCode
     * @return
     */
    @Override
    public ElevatorDetailModule getElevatorByEquipmentCode(String equipmentCode) {
        return bizElevatorDao.getElevatorByEquipmentCode(equipmentCode);
    }

    @Override
    public ElevatorDetailModule getElevatorByEquipmentCodeS(String equipmentCode) {
        return bizElevatorDao.getElevatorByEquipmentCodeS(equipmentCode);
    }

    /**
     * 拼接电梯设备状态
     *
     * @param hashMapPageInfo
     */
    private void convertDeviceStatus(PageInfo<Map> hashMapPageInfo) {
        List<Map> list = hashMapPageInfo.getList();
        List<String> elevatorIdList = new ArrayList<>();
        list.forEach(value -> {
            elevatorIdList.add(String.valueOf(value.get("v_elevator_id")));
        });
        if (CollectionUtils.isEmpty(elevatorIdList)) {
            return;
        }
        List<TblDevice> deviceList = deviceDao.listByElevatorIds(elevatorIdList);

        list.forEach(elevator -> {
            String elevatorId = String.valueOf(elevator.get("v_elevator_id"));
            deviceList.forEach(device -> {
                if (device.getVElevatorId().equals(elevatorId)) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("id", device.getVDeviceId());
                    map.put("sensorType", device.getVSensorType());
                    map.put("onlineStatus", device.getIOnlineStatus());
                    map.put("onlineTime", device.getDtOnlineTime());
                    map.put("offlineTime", device.getDtOfflineTime());
                    map.put("battery", device.getIBattery());
                    map.put("masterVersion", device.getVMasterVersion());

                    if (elevator.get("deviceList") == null) {
                        List<Object> deviceMapList = new ArrayList<>();
                        deviceMapList.add(map);
                        elevator.put("deviceList", deviceMapList);
                    } else {
                        List<Object> deviceMapList = (List<Object>) elevator.get("deviceList");
                        deviceMapList.add(map);
                    }
                }
            });
        });
    }

    @Override
    public Map countElevator(String userId, boolean isAdminFlag, List<String> projectIds, List<String> villageIds) {

        List<Map> maps = bizElevatorDao.countElevator(userId, isAdminFlag, projectIds, villageIds);
        int totalQuantity = 0;
        int trappedPeopleQuantity = 0;
        int faultQuantity = 0;
        int offlineQuantity = 0;
        int overhaulQuantity = 0;
        int normalQuantity = 0;
        int onlineQuantity = 0;

        if (maps == null || maps.size() > 0) {

            //总电梯数
            totalQuantity = maps.size();

            //离线列表
            List<String> offlineElevatorIds = maps.stream().filter(e -> 0 == (int) e.get("i_on_line"))
                    .map(e -> (String) e.get("v_elevator_id")).collect(Collectors.toList());

            //获取困人列表
            List<String> trappedPeopleElevatorIds = bizFaultDao
                    .getFaultElevatorListByFaultType(userId, isAdminFlag, Arrays.asList("7", "8"));
            trappedPeopleElevatorIds.removeAll(offlineElevatorIds);

            //获取故障电梯列表
            List<String> faultElevatorIds = bizElevatorDao.getFaultElevatorList(userId, isAdminFlag);

            //检修列表
            List<String> overhaulElevatorIds = maps.stream().filter(e -> 1 == (int) e.get("i_mode_status")).map(e -> (String) e.get("v_elevator_id")).collect(Collectors.toList());
            overhaulElevatorIds.removeAll(offlineElevatorIds);
            overhaulElevatorIds.removeAll(trappedPeopleElevatorIds);
            overhaulElevatorIds.removeAll(faultElevatorIds);

            //离线数
            offlineQuantity = offlineElevatorIds.size();
            //在线数
            onlineQuantity = totalQuantity - offlineQuantity;
            //检修数
            overhaulQuantity = overhaulElevatorIds.size();
            //困人数
            trappedPeopleQuantity = trappedPeopleElevatorIds.size();
            //故障数
            faultQuantity = faultElevatorIds.size();
            //正常数
            normalQuantity = totalQuantity - offlineQuantity - overhaulQuantity - trappedPeopleQuantity - faultQuantity;
        }

        return Map.of("totalQuantity", totalQuantity,
                "trappedPeopleQuantity", trappedPeopleQuantity,
                "faultQuantity", faultQuantity,
                "offlineQuantity", offlineQuantity,
                "normalQuantity", normalQuantity,
                "onlineQuantity", onlineQuantity,
                "overhaulQuantity", overhaulQuantity);
    }

    /**
     * 获取电梯详情数据 全字段
     *
     * @param elevatorId
     * @return
     */
    @Override
    public ElevatorDetailResModule getElevatorDetail(String elevatorId) {
        return bizElevatorDao.getElevatorDetail(elevatorId);
    }


    /**
     * 根据电梯编号获取梯内小屏展示详情
     *
     * @param elevatorCode 电梯编号
     * @return ElevatorScreenModule
     */
    @Override
    public ElevatorScreenModule getElevatorScreenInfo(String elevatorCode) {
        ElevatorScreenModule elevatorScreenInfo = bizElevatorDao.getElevatorScreenInfo(elevatorCode);
        try {
            // 调用心知天气api
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ServiceConstants.WEATHER_API_URL)
                    .queryParam("key", ServiceConstants.WEATHER_API_KEY)
                    .queryParam("location", "shanghai")
                    .queryParam("language", "zh-Hans")
                    .queryParam("unit", "c");
            ResponseEntity<String> map = RequestUtil.sendGet(builder);

            JSONObject jsonObject = JSONObject.parseObject(map.getBody());
            System.out.printf("%s --- 调用心知天气api --- %s\n", TimeUtils.nowTime(), jsonObject);
            elevatorScreenInfo.setWeather(jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("now"));
        } catch (Exception e) {
            e.printStackTrace();
            elevatorScreenInfo.setWeather(JSONObject.parseObject("{\"code\":\"4\",\"temperature\":\"21\",\"text\":\"晴\"}"));
        }
        return elevatorScreenInfo;
    }


    /**
     * 获取所有安装时间为空的电梯编号
     *
     * @return 电梯编号list
     */
    @Override
    public List<String> getAllElevatorCodeByInstallTimeIsNull() {
        return bizElevatorDao.getAllElevatorCodeByInstallTimeIsNull();
    }

    /**
     * 获取电梯最近一次 上下线事件信息
     *
     * @param elevatorCode 电梯编号
     * @return
     */
    @Override
    public Date getDeviceEventRecordByFirstRecord(String elevatorCode) {
        return bizElevatorDao.getDeviceEventRecordByFirstRecord(elevatorCode);
    }


    /**
     * 根据第一次上线时间 更新电梯设备安装时间
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public void updateInstallTime(String elevatorCode, Date installTime) {
        bizElevatorDao.updateInstallTime(elevatorCode, installTime);
    }


    @Override
    public int updateNextInspectDate(String elevatorCode, Date nextInspectDate) {
        return bizElevatorDao.updateNextInspectDate(elevatorCode, nextInspectDate);
    }

    /**
     * 对外电梯列表
     *
     * @param searchElevatorModule
     * @return
     */
    @Override
    public PageListResultEntity searchElevatorThirdPartyList(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }

        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule, pageIndex, pageSize);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<ElevatorThirdPartyList> elevatorThirdPartyListPageInfo = new PageInfo<>(bizElevatorDao.searchElevatorListThirdParty(searchElevatorModule), pageSize);

        // 封装分页数据结构
        PageListResultEntity<ElevatorThirdPartyList> elevatorThirdPartyListPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) elevatorThirdPartyListPageInfo.getTotal(), elevatorThirdPartyListPageInfo.getList());
        return elevatorThirdPartyListPageListResultEntity;
    }

    /**
     * 对外电梯详情
     *
     * @param equipmentCode
     * @return
     */
    @Override
    public ElevatorThirdPartyDetail getElevatorThirdPartyDetail(String equipmentCode) {
        ElevatorThirdPartyDetail elevatorByElevatorIdThirdParty = bizElevatorDao.getElevatorByElevatorIdThirdParty(equipmentCode);
        return elevatorByElevatorIdThirdParty;
    }

    @Override
    public List<String> getElevatorDeviceList(String elevatorId) {
        return deviceDao.getSensorTypeListByElevatorId(elevatorId);
    }


    /**
     * 根据电梯id 消除故障状态
     *
     * @param elevatorId 电梯id
     */
    @Override
    public void cancelFaultByElevatorId(String elevatorId) {
        bizElevatorDao.cancelFaultByElevatorId(elevatorId);
    }

    @Override
    public Integer getElevatorTypeByElevatorCode(String elevatorCode) {
        return bizElevatorDao.getElevatorTypeByElevatorCode(elevatorCode);
    }

    @Override
    public Integer update(TblElevator tblElevator) {

        if (tblElevator.getIInstallStatus() != null) {
            //获取电梯信息
            Integer installStatus = bizElevatorDao.getElevatorInstallStatusById(tblElevator.getVElevatorId());
            //是否变更为已安装状态
            if (installStatus == 0 && tblElevator.getIInstallStatus() == 1) {
                //更新安装时间
                tblElevator.setDtInstallTime(TimeUtils.nowTime());
            }
        }

        var res = bizElevatorDao.update(tblElevator);
        cacheService.updateCacheByElevatorCodeFromDatabase(tblElevator.getVElevatorCode());
        return res;
    }

    @Override
    public List<TblDevice> getElevatorDeviceDetailList(String elevatorId) {
        return bizElevatorDao.getElevatorDeviceDetailList(elevatorId);
    }


    /**
     * 初始化电梯安装时间
     *
     * @param elevatorId 电梯id
     */
    @Override
    public void initInstallTime(String elevatorId) {
        bizElevatorDao.initInstallTime(elevatorId);
        bizElevatorDao.initElevator2(elevatorId);
        bizElevatorDao.initElevator3(elevatorId);
        bizElevatorDao.initElevator4(elevatorId);
        bizElevatorDao.initElevator5(elevatorId);
        bizElevatorDao.initElevator6(elevatorId);
    }


    /**
     * 懒得配置多行SQL的支持
     *
     * @param elevatorId 电梯id
     */
    @Override
    @Transactional
    public void initElevator(String elevatorId) {
        //删除统计信息
        bizElevatorDao.initInstallTime(elevatorId);
        //删除取证文件
        bizElevatorDao.initElevator1(elevatorId);
        bizElevatorDao.initElevator2(elevatorId);
        bizElevatorDao.initElevator3(elevatorId);
        //删除工单相关
        bizElevatorDao.initElevator4(elevatorId);
        bizElevatorDao.initElevator5(elevatorId);
        //删除故障记录
        bizElevatorDao.initElevator6(elevatorId);
        //删除dLog文件
        bizElevatorDao.initElevator7(elevatorId);
        //删除上下线记录
        bizElevatorDao.initElevator8(elevatorId);
        //删除故障视频
        bizElevatorDao.initElevator9(elevatorId);
        //删除故障审核
        bizElevatorDao.initElevator10(elevatorId);

    }

    /**
     * 查询电梯表，获取所有的电梯code
     *
     * @return 电梯code
     */
    @Override
    public List<String> searchAllElevatorList() {
        return bizElevatorDao.searchAllElevatorList();
    }

    /**
     * excel批量导入电梯
     *
     * @param elevatorList
     */
    @Override
    public void addElevatorByExcel(List<ElevatorForExcel> elevatorList) {
        bizElevatorDao.addElevatorByExcel(elevatorList);
    }

    /**
     * 查询可批量升级的设备
     *
     * @return 可升级设备列表
     */
    @Override
    public PageListResultEntity searchDeviceByHWVersion(SearchElevatorModule searchElevatorModule) {

        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        // 群租逻辑判断
        var extendGroupLeasingStatus = extendGroupLeasingStatus(searchElevatorModule, pageIndex, pageSize);
        if (extendGroupLeasingStatus != null) {
            return extendGroupLeasingStatus;
        }

        TblSysDept dept = bizElevatorDao.getDeptByUserId(searchElevatorModule.getUserId());
        // 部门不存在时直接返回
        if (null == dept || !StringUtils.hasText(dept.getVDeptId())) {
            return null;
        }

        //1递归查询用户关联的部门list
        List<String> projectIdList = bizProjectService.getProjectIdsByParentDeptId(dept.getVDeptId());
        // 部门不存在时直接返回
        if (projectIdList.size() == 0) {
            return null;
        }

        // 查询数据
        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(bizElevatorDao.searchDeviceByHWVersion(searchElevatorModule, projectIdList), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    @Override
    public void addElevatorPrincipal(List<TblElevatorPrincipal> elevatorPrincipals) {

        for (TblElevatorPrincipal elevatorPrincipal : elevatorPrincipals) {
            //先删除已经存在的,保证唯一
            bizElevatorDao.delElevatorPrincipal(elevatorPrincipal);
            if (!elevatorPrincipal.getPrincipalId().isEmpty()) {
                //雪花算法生成id
                elevatorPrincipal.setvPrincipalId(SnowFlakeUtils.nextStrId());
                //创建时间
                elevatorPrincipal.setCreateTime(new Date());
                //添加
                bizElevatorDao.addElevatorPrincipal(elevatorPrincipal);
            }
        }

    }

    @Override
    public List<Map<String, Object>> getElevatorOnlineRate(String projectId) {

        List<Map<String, Object>> elevatorOnlineRate = bizElevatorDao.getElevatorOnlineRate(projectId);

        return elevatorOnlineRate;
    }

    @Override
    public PageListResultEntity searchElevatorIotCardByUserId(IotCard iotCard) {

        Integer pageIndex = iotCard.getPageIndex();
        Integer pageSize = iotCard.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        // 查询数据
        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map> hashMapPageInfo = new PageInfo<>(bizElevatorDao.searchElevatorIotCardByUserId(iotCard));

        // 扩展 小区信息
        tblVillageServiceI.extendVillageInfo(hashMapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());
    }

    @Override
    public void save(List<IotCard> iotCardList) {
        bizElevatorDao.save(iotCardList);
    }

    @Override
    public ResponseEntity updateIotCardInfo(List<String> iccids) {

        String ids = String.join("", iccids);
        System.out.println("ids = " + ids);

        try {

            String str = BusinessConstants.IOT_CARD_APPID + ids + BusinessConstants.IOT_CARD_KEY;
            String md5Str = SecureUtil.md5(str);

            Map<String, Object> map = new HashMap<>();
            map.put("sign", md5Str);
            map.put("appId", BusinessConstants.IOT_CARD_APPID);
            map.put("iccids", iccids);

            String urldetail = "http://iot.iyunjun.com:7007/card/query/now/month/flow/batch";

            // 发出一个post请求
            HashMap<String, Object> detailResult = iotCardRestTemplate.postForObject(urldetail, map, HashMap.class);

            List<HashMap<String, Object>> data = (ArrayList) detailResult.get("data");

            ArrayList<IotCard> iotCards = new ArrayList<>();

            for (HashMap<String, Object> res : data) {
                if (res.get("message") == null) {
                    IotCard iotCard = new IotCard();
                    iotCard.setIccid((String) res.get("iccid"));
                    iotCard.setUsedFlow((double) res.get("usedFlow"));
                    iotCard.setUpdateTime(new Date());
                    iotCards.add(iotCard);
                }
            }

            if (iotCards.size() > 0) {
                //更新表状态
                bizElevatorDao.upDateIotInfo(iotCards);
            }

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(1111).build();
        }
    }

    @Override
    public List<String> searchAllIotCardIccid() {
        return bizElevatorDao.searchAllIotCardIccid();
    }

    @Override
    public int updateIotCard(IotCard iotCard) {
        return bizElevatorDao.iotCardBind(iotCard);
    }

    @Override
    public Map<String, Object> getIotCardRate(String projectId) {

        //获取流量超50%的卡信息
        List<IotCard> iotCardRate = bizElevatorDao.getIotCardRate(projectId);

        Map<String, Object> resultMap = new HashMap<>();
        //构建x轴数据
        ArrayList<String> categories = new ArrayList<>();
        // 构建Y轴数据
        List<Map<String, Object>> seriesList = new ArrayList<>();
        HashMap<String, Object> res1 = new HashMap<>();
        ArrayList<Double> use = new ArrayList<>();
        res1.put("name", "使用流量");
        HashMap<String, Object> res2 = new HashMap<>();
        ArrayList<Double> free = new ArrayList<>();
        res2.put("name", "剩余流量");

        for (IotCard iotCard : iotCardRate) {
            categories.add(iotCard.getvElevatorCode());
            use.add(iotCard.getUsedFlow());
            free.add(iotCard.getPackageSize() - iotCard.getUsedFlow());
        }

        res1.put("data", use);
        res2.put("data", free);
        seriesList.add(res1);
        seriesList.add(res2);
        resultMap.put("categories", categories);
        resultMap.put("series", seriesList);
        return resultMap;
    }

    @Override
    public void searchAllRunCount() {

        //查询所有需要统计的电梯最新的运行次数
        List<TblElevatorConf> listEle = bizElevatorDao.searchAllRunCount();

        //查询所有统计的电梯今日运行次数
        List<ElevatorRunCount> runCountList = bizElevatorDao.searchElevatorRunCount();

        //查询所有统计的电梯的上一天的运行次数
        List<ElevatorRunCount> runCountListY = bizElevatorDao.searchElevatorRunCountY();

        HashMap<String, ElevatorRunCount> elevatorRunCounts = new HashMap<>();
        HashMap<String, ElevatorRunCount> elevatorRunCountsYesterday = new HashMap<>();


        List<ElevatorRunCount> listRunCount = new ArrayList<>();
        List<ElevatorRunCount> listRunCountY = new ArrayList<>();

        for (ElevatorRunCount elevatorRunCount : runCountList) {
            elevatorRunCounts.put(elevatorRunCount.getVElevatorCode(), elevatorRunCount);
        }

        for (ElevatorRunCount elevatorRunCount : runCountListY) {
            elevatorRunCountsYesterday.put(elevatorRunCount.getVElevatorCode(), elevatorRunCount);
        }

        for (TblElevatorConf tblElevator : listEle) {

            //今日数据新增
            ElevatorRunCount runCount = elevatorRunCounts.get(tblElevator.getVElevatorCode());

            if (runCount == null) {

                //获取昨日数据
                ElevatorRunCount runCountY = elevatorRunCountsYesterday.get(tblElevator.getVElevatorCode());

                if (runCountY == null) {

                    runCountY = new ElevatorRunCount();
                    runCountY.setLastRunCount(tblElevator.getBiRunCount());
                    runCountY.setLastDoorCount(tblElevator.getBiDoorCount());
                    runCountY.setLastBendCount(tblElevator.getBiBendCount());
                    runCountY.setLastLevelTriggerCount(tblElevator.getBiLevelTriggerCount());
                    runCountY.setLastRunDistanceCount(tblElevator.getBiRunDistanceCount());
                    runCountY.setLastBackAndForthCount(tblElevator.getBackAndForthCount());
                }

                addCount(listRunCountY, tblElevator, runCountY);

            } else {

                addCount(listRunCount, tblElevator, runCount);
            }

        }

        List<ElevatorRunCount> collect = checkCountData(listRunCount);
        List<ElevatorRunCount> collectY = checkCountData(listRunCountY);

        //更新统计
        if (collect != null && collect.size() > 0) {
            // 按每50个一组分割
            Integer partialLimit = 50;
            // 获取需要分割的次数，注意不能直接除以批次数量
            int limit = (collect.size() + partialLimit - 1) / partialLimit;

            Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
                List<ElevatorRunCount> list = collect.stream().skip(i * partialLimit).limit(partialLimit).collect(Collectors.toList());
                try {
                    bizElevatorDao.updateRunCount(list);
                    log.info("更新每日统计成功! data:{}", list);
                } catch (Exception e) {
                    log.error("更新每日统计失败，error：{}", e.getMessage());
                }
            });

        }

        //新增每日统计
        if (collectY != null && collectY.size() > 0) {

            // 按每50个一组分割
            Integer partialLimit = 50;
            // 获取需要分割的次数，注意不能直接除以批次数量
            int limit = (collectY.size() + partialLimit - 1) / partialLimit;

            Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
                List<ElevatorRunCount> list = collectY.stream().skip(i * partialLimit).limit(partialLimit).collect(Collectors.toList());
                try {
                    bizElevatorDao.addRunCount(list);
                    log.info("新增每日统计成功! data:{}", list);
                } catch (Exception e) {
                    log.error("新增每日统计失败，error：{}", e.getMessage());
                }
            });


        }

    }


    @Override
    public Map<String, List<Map>> radarChart(String projectId) {

        Map<String, List<Map>> res = new HashMap<>();
        ArrayList<Map> hashMaps = new ArrayList<>();

        //在线率
        HashMap<String, Object> hashMap = new HashMap<>();
        Map<String, Object> online = bizElevatorDao.online(projectId);
        hashMap.put("name", "在线率");
        hashMap.put("valve", online.get("value"));
        hashMap.put("max", online.get("max"));
        hashMaps.add(hashMap);

        //设备完好率
        HashMap<String, Object> hashMap1 = new HashMap<>();
        Map<String, Object> device = bizElevatorDao.device(projectId);
        hashMap1.put("name", "设备完好率");
        hashMap1.put("valve", (long) device.get("max") - (long) device.get("value"));
        hashMap1.put("max", device.get("max"));
        hashMaps.add(hashMap1);

        //流量卡正常率：流量卡不超80%数量/卡数 低于90%告警  最大值100%  每一阶梯为95%  90%  85% 80%（含低于）
        HashMap<String, Object> hashMap2 = new HashMap<>();
        Map<String, Object> iotCard = bizElevatorDao.iotCard(projectId);
        hashMap2.put("name", "流量卡正常率");
        hashMap2.put("valve", iotCard.get("value"));
        hashMap2.put("max", iotCard.get("max"));
        hashMaps.add(hashMap2);

        //北向完好率
        String url = endpointProperties.getSenderServer() + "/counter/getLitterScreen?projectId=" + projectId
                + "&groupId=yidian";
        HashMap<String, Object> getSendDataMap = restTemplate.getForObject(url, HashMap.class);
        HashMap<String, Integer> data = (HashMap<String, Integer>) getSendDataMap.get("data");

        HashMap<Object, Object> hashMap3 = new HashMap<>();
        hashMap3.put("name", "北向完好率");
        hashMap3.put("valve", data.get("onlineCount"));
        hashMap3.put("max", data.get("size"));
        hashMaps.add(hashMap3);

        //视频在线率
        HashMap<String, Object> hashMap4 = new HashMap<>();
        hashMap4.put("name", "视频在线率");
        hashMap4.put("valve", online.get("value"));
        hashMap4.put("max", online.get("max"));
        hashMaps.add(hashMap4);

        //电梯正常率：运行总次数 - 当日故障数/当日电梯运行总次数  低于90%预警，最高100%，每一阶梯为90%，80%，70%，低于60%
        HashMap<String, Object> hashMap5 = new HashMap<>();

        var reportDate = DateUtil.beginOfDay(DateUtil.yesterday()).toJdkDate();
        Map<String, Object> normal = bizElevatorDao.normal(projectId, reportDate);
        hashMap5.put("name", "电梯正常率");

        if (normal == null) {
            hashMap5.put("valve", 0);
            hashMap5.put("max", 0);
            hashMaps.add(hashMap5);

            res.put("indicator", hashMaps);
            return res;
        }

        BigDecimal mxa = normal.get("max") == null ? BigDecimal.ZERO : (BigDecimal) normal.get("max");
        BigDecimal value = normal.get("value") == null ? BigDecimal.ZERO : (BigDecimal) normal.get("value");

        hashMap5.put("valve", mxa.subtract(value));
        hashMap5.put("max", mxa);
        hashMaps.add(hashMap5);

        res.put("indicator", hashMaps);
        return res;
    }

    @Override
    public Map<String, Object> getRunDataStatisticsByDateDimension(ElevatorRunDataStatisticsReqVO statisticsModule) {

        //构建时间对象
        ArrayList<Date> time = new ArrayList<>();
        ArrayList<HashMap> series = new ArrayList<>();

        //按日期统计
        List<Map<Object, Object>> res = bizElevatorDao.getRunDataStatisticsByDateDimension(statisticsModule);

        if (!StringUtils.hasText(statisticsModule.getEventType())) {

            ArrayList<Object> runCount = new ArrayList<>();
            ArrayList<Object> doorCount = new ArrayList<>();
            ArrayList<Object> bendCount = new ArrayList<>();
            ArrayList<Object> triggerCount = new ArrayList<>();
            ArrayList<Object> distanceCount = new ArrayList<>();

            res.stream().forEach(it -> {
                time.add((Date) it.get("time"));
                runCount.add(it.get("runCount"));
                doorCount.add(it.get("doorCount"));
                bendCount.add(it.get("bendCount"));
                triggerCount.add(it.get("triggerCount"));
                distanceCount.add(it.get("distanceCount"));
            });

            HashMap<String, Object> runCountMap = new HashMap<>();
            runCountMap.put("data", runCount);
            runCountMap.put("name", "runCount");

            HashMap<String, Object> doorCountMap = new HashMap<>();
            doorCountMap.put("data", doorCount);
            doorCountMap.put("name", "doorCount");

            HashMap<String, Object> bendCountMap = new HashMap<>();
            bendCountMap.put("data", bendCount);
            bendCountMap.put("name", "bendCount");

            HashMap<String, Object> triggerCountMap = new HashMap<>();
            triggerCountMap.put("data", triggerCount);
            triggerCountMap.put("name", "triggerCount");

            HashMap<String, Object> distanceCountMap = new HashMap<>();
            distanceCountMap.put("data", distanceCount);
            distanceCountMap.put("name", "distanceCount");

            series.add(runCountMap);
            series.add(doorCountMap);
            series.add(bendCountMap);
            series.add(triggerCountMap);
            series.add(distanceCountMap);


        } else {

            ArrayList<BigDecimal> count = new ArrayList<>();

            res.stream().forEach(it -> {
                time.add((Date) it.get("time"));
                count.add((BigDecimal) it.get(statisticsModule.getEventType()));
            });

            HashMap<String, Object> countMap = new HashMap<>();
            countMap.put("data", count);
            countMap.put("name", statisticsModule.getEventType());
            series.add(countMap);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("series", series);
        result.put("categories", time);

        return result;
    }

    @Override
    public Map<String, Object> getRunDataStatisticsByElevatorDimension(ElevatorRunDataStatisticsReqVO
                                                                               statisticsModule) {

        ArrayList<String> categories = new ArrayList<>();
        ArrayList<HashMap> series = new ArrayList<>();

        //按电梯统计
        List<Map<Object, Object>> queryResults = bizElevatorDao
                .getRunDataStatisticsByElevatorDimension(statisticsModule);

        if (StringUtils.hasText(statisticsModule.getEventType())) {

            ArrayList<BigDecimal> count = new ArrayList<>();

            //获取前30统计的电梯
            String eventType = statisticsModule.getEventType();
            queryResults.stream()
                    .sorted(Comparator.comparing((Map map) -> (BigDecimal) map.get(eventType)).reversed())
                    .limit(30).forEach(it -> {
                                categories.add((String) it.get("elevatorCode"));
                                count.add((BigDecimal) it.get(statisticsModule.getEventType()));
                            }
                    );

            HashMap<String, Object> countMap = new HashMap<>();
            countMap.put("data", count);
            countMap.put("name", statisticsModule.getEventType());
            series.add(countMap);

        } else {
            //获取前30统计的电梯
            List<Map<Object, Object>> collect = queryResults.stream()
                    .sorted(Comparator.comparing((Map map) -> {
                        BigDecimal sum = BigDecimal.ZERO;
                        sum = sum.add((BigDecimal) map.getOrDefault("runCount", BigDecimal.ZERO));
                        sum = sum.add((BigDecimal) map.getOrDefault("doorCount", BigDecimal.ZERO));
                        sum = sum.add((BigDecimal) map.getOrDefault("bendCount", BigDecimal.ZERO));
                        sum = sum.add((BigDecimal) map.getOrDefault("triggerCount", BigDecimal.ZERO));
                        sum = sum.add((BigDecimal) map.getOrDefault("distanceCount", BigDecimal.ZERO));
                        return sum;
                    }).reversed()).limit(30)
                    .collect(Collectors.toList());


            //构建返回数据
            ArrayList<Object> runCount = new ArrayList<>();
            ArrayList<Object> doorCount = new ArrayList<>();
            ArrayList<Object> bendCount = new ArrayList<>();
            ArrayList<Object> triggerCount = new ArrayList<>();
            ArrayList<Object> distanceCount = new ArrayList<>();

            collect.stream().forEach(it -> {
                categories.add((String) it.get("elevatorCode"));
                runCount.add(it.get("runCount"));
                doorCount.add(it.get("doorCount"));
                bendCount.add(it.get("bendCount"));
                triggerCount.add(it.get("triggerCount"));
                distanceCount.add(it.get("distanceCount"));
            });

            HashMap<String, Object> runCountMap = new HashMap<>();
            runCountMap.put("data", runCount);
            runCountMap.put("name", "runCount");

            HashMap<String, Object> doorCountMap = new HashMap<>();
            doorCountMap.put("data", doorCount);
            doorCountMap.put("name", "doorCount");

            HashMap<String, Object> bendCountMap = new HashMap<>();
            bendCountMap.put("data", bendCount);
            bendCountMap.put("name", "bendCount");

            HashMap<String, Object> triggerCountMap = new HashMap<>();
            triggerCountMap.put("data", triggerCount);
            triggerCountMap.put("name", "triggerCount");

            HashMap<String, Object> distanceCountMap = new HashMap<>();
            distanceCountMap.put("data", distanceCount);
            distanceCountMap.put("name", "distanceCount");

            series.add(runCountMap);
            series.add(doorCountMap);
            series.add(bendCountMap);
            series.add(triggerCountMap);
            series.add(distanceCountMap);

        }

        //构建返回数据
        HashMap<String, Object> result = new HashMap<>();
        result.put("series", series);
        result.put("categories", categories);

        return result;
    }

    @Override
    public ResponseEntity updateIotCardInfoById(String iccid) {
        IotCard iotCard = new IotCard();

        String str = BusinessConstants.IOT_CARD_APPID + iccid + BusinessConstants.IOT_CARD_KEY;
        String md5Str = SecureUtil.md5(str);

        Map<String, Object> map = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add(iccid);
        map.put("sign", md5Str);
        map.put("appId", BusinessConstants.IOT_CARD_APPID);
        map.put("iccids", list);

        String url = "http://iot.iyunjun.com:7007/card/query/now/month/flow/batch";

        // 发出一个post请求
        try {
            HashMap<String, Object> usedResult = iotCardRestTemplate.postForObject(url, map, HashMap.class);
            HashMap<String, Object> data = (HashMap<String, Object>) ((ArrayList) usedResult.get("data")).get(0);

            iotCard.setIccid(iccid);
            if (data.get("message") == null) {
                iotCard.setUsedFlow((double) data.get("usedFlow"));
            } else {
                return new ResponseEntity("查询运营商流量异常", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            iotCard.setUpdateTime(new Date());

            ArrayList<IotCard> iotCards = new ArrayList<>();
            iotCards.add(iotCard);
            //更新表状态
            bizElevatorDao.upDateIotInfo(iotCards);

            return ResponseEntity.ok(iotCard);
        } catch (RestClientException e) {
            e.printStackTrace();
            return ResponseEntity.status(1111).build();
        }
    }

    @Override
    public int delIotCardInfoById(String iccid) {
        return bizElevatorDao.delIotCardInfoById(iccid);
    }

    @Override
    public void updateIotCardPackage(String iccid) {

        IotCard iotCard = new IotCard();

        String str = BusinessConstants.IOT_CARD_APPID + iccid + BusinessConstants.IOT_CARD_KEY;
        String md5Str = SecureUtil.md5(str);

        Map<String, Object> map = new HashMap<>();
        map.put("sign", md5Str);
        map.put("appId", BusinessConstants.IOT_CARD_APPID);
        map.put("iccid", iccid);
        //String message = JSON.toJSONString(map);

        String urlpackage = "http://iot.iyunjun.com:7007/card/flow/package/history/query";
        String urldetail = "http://iot.iyunjun.com:7007/card/query/detail";

        // 发出一个post请求
        try {
            HashMap<String, Object> packageResult = restTemplate.postForObject(urlpackage, map, HashMap.class);
            HashMap<String, Object> detailResult = restTemplate.postForObject(urldetail, map, HashMap.class);

            HashMap<String, Object> packageData = (HashMap<String, Object>) ((ArrayList) packageResult.get("data")).get(0);
            HashMap<String, Object> detailData = (HashMap<String, Object>) detailResult.get("data");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss:mm");

            iotCard.setIccid(iccid);
            iotCard.setFlowPackageName((String) packageData.get("flowPackageName"));
            iotCard.setStartTime(simpleDateFormat.parse((String) packageData.get("startTime")));
            iotCard.setEndTime(simpleDateFormat.parse((String) packageData.get("endTime")));
            iotCard.setPackagePrice((int) packageData.get("packagePrice"));
            iotCard.setOperatorStatus((String) detailData.get("operatorStatus"));

            if (detailData.get("operatorActiveTime") != null) {
                iotCard.setOperatorActiveTime(simpleDateFormat.parse((String) detailData.get("operatorActiveTime")));
            }
            iotCard.setStopGprs((int) detailData.get("stopGprs"));
            iotCard.setPackageSize((double) packageData.get("packageSize"));
            iotCard.setUpdateTime(new Date());

            ArrayList<IotCard> iotCards = new ArrayList<>();
            iotCards.add(iotCard);
            //更新表状态
            bizElevatorDao.upDateIotInfo(iotCards);

        } catch (RestClientException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule, HttpServletResponse response) {

        List<ElevatorRunDataStatisticsDownload> data = bizElevatorDao.exportElevatorRunDataStatistics(faultStatisticsModule);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("每日运行统计", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ElevatorRunDataStatisticsDownload.class)
                    .sheet("每日运行统计")
                    .doWrite(data);

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            map.put("code", "500");
            map.put("data", JSONObject.toJSONString(data));
            try {
                response.getWriter().println(JSONObject.toJSONString(map));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public List<ElevatorRunCount> getElevatorPassengerFlow(String elevatorCode, String startTime, String endTime) {

        DateTime end = DateUtil.parseDate(endTime);

        int between = (int) DateUtil.between(DateUtil.parseDate(startTime), end, DateUnit.DAY);

        List<ElevatorRunCount> elevatorPassengerFlows = bizElevatorDao.getElevatorPassengerFlow(elevatorCode, startTime, endTime);

        //补全数据
        if (elevatorPassengerFlows.size() < between + 1) {

            List<ElevatorRunCount> result = new ArrayList<>();

            Map<Date, ElevatorRunCount> collect = elevatorPassengerFlows.stream()
                    .collect(Collectors.toMap(ElevatorRunCount::getDReportDate, it -> it));

            for (int i = 0; i <= between; i++) {

                DateTime report = DateUtil.offsetDay(end, -i);

                ElevatorRunCount passengerFlow = collect.get(report);

                if (passengerFlow == null) {
                    passengerFlow = new ElevatorRunCount();
                    passengerFlow.setDReportDate(report);
                    passengerFlow.setPassengerFlow(0);
                }

                result.add(passengerFlow);

            }

            return result;

        } else {

            return elevatorPassengerFlows;
        }


    }

    @Override
    public List<UserElevatorsDO> searchVillageUserElevators(boolean admin, String adminUserId, String userId, String villageId) {

        //获取用户授权电梯
        List<UserElevatorsDO> userElevators = bizElevatorDao.searchVillageUserElevators(admin, adminUserId, userId, villageId);

        return userElevators;
    }

    @Override
    public Boolean insertImageRecognitionMattingConfig(
            ImageRecognitionMattingConfigReqVO imageRecognitionMattingConfig, String userId) {

        String coordinates = null;
        if (!CollectionUtils.isEmpty(imageRecognitionMattingConfig.getCoordinates())) {
            coordinates = JSON.toJSONString(imageRecognitionMattingConfig.getCoordinates());
        }

        var configDO = ImageRecognitionMattingConfigDO.builder().id(IdUtil.getSnowflakeNextId())
                .coordinates(coordinates)
                .width(imageRecognitionMattingConfig.getWidth())
                .height(imageRecognitionMattingConfig.getHeight())
                .elevatorId(imageRecognitionMattingConfig.getElevatorId())
                .elevatorCode(imageRecognitionMattingConfig.getElevatorCode())
                .faultTypes(imageRecognitionMattingConfig.getFaultTypes())
                .creator(userId)
                .create_time(new Date())
                .build();

        var config = bizElevatorDao.getImageRecognitionMattingConfigByEleIdAndFault(
                imageRecognitionMattingConfig.getElevatorId(), imageRecognitionMattingConfig.getFaultTypes());

        if (config == null) {
            bizElevatorDao.insertImageRecognitionMattingConfig(configDO);
        } else {
            bizElevatorDao.updateImageRecognitionMattingConfig(configDO);
        }

        return true;
    }

    @Override
    public Boolean insertImageRecognitionMattingConfigList(InsertImageRecognitionMattingConfigListReqVO reqVO, String userId) {

        List<InsertImageRecognitionMattingConfigListReqVO.elevator> elevators = reqVO.getElevators();

        for (InsertImageRecognitionMattingConfigListReqVO.elevator elevator : elevators) {

            ImageRecognitionMattingConfigDO config = bizElevatorDao.getImageRecognitionMattingConfigByEleIdAndFault(elevator.getElevatorId(), reqVO.getFaultTypes());

            if (config == null) {
                ImageRecognitionMattingConfigDO configDO = ImageRecognitionMattingConfigDO.builder().id(IdUtil.getSnowflakeNextId())
                        .coordinates(null)
                        .elevatorId(elevator.getElevatorId()).elevatorCode(elevator.getElevatorCode())
                        .faultTypes(reqVO.getFaultTypes()).creator(userId).create_time(new Date())
                        .build();
                bizElevatorDao.insertImageRecognitionMattingConfig(configDO);
            }

        }
        return null;
    }

    @Override
    public PageListResultEntity<ImageRecognitionMattingRespVO>
    getImageRecognitionMattingConfigPage(ImageRecognitionMattingReqVO reqVO) {

        Integer pageNo = reqVO.getPageNo();
        Integer pageSize = reqVO.getPageSize();

        // 查询数据
        PageHelper.startPage(pageNo, pageSize);

        PageInfo<ImageRecognitionMattingRespVO> pageInfo = new PageInfo<>(bizElevatorDao
                .getImageRecognitionMattingConfigPage(reqVO));

        // 封装分页数据结构
        return new PageListResultEntity<>(pageNo, pageSize, (int) pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public ResponseResult delFaultAndStatisticsByElevatorCode(String elevatorCode) {

        //故障逻辑删除
        bizFaultDao.delFaultByElevatorCode(elevatorCode);

        //删除统计数据
        bizElevatorDao.delStatisticsByElevatorCode(elevatorCode);


        return ResponseResult.successObj("success");
    }

    @Override
    public int getRunNumByElevatorCodeAndTime(String elevatorCode, Date startTime, Date endTime) {
        Long count = bizElevatorDao.getRunCountByElevatorCodeAndTime(elevatorCode, startTime, endTime);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    @Override
    public TblElevator getElevatorByCode(String elevatorCode) {
        return bizElevatorDao.getByCode(elevatorCode);
    }

    @Override
    public ImageRecognitionMattingConfigRespVO getImageRecognitionMattingConfig(String elevatorId, String faultTypes) {

        ImageRecognitionMattingConfigDO config = bizElevatorDao.getImageRecognitionMattingConfigByEleIdAndFault(elevatorId, faultTypes);

        if (config == null) {
            return null;
        }
        var coordinates = JSON.parseArray(config.getCoordinates(), ImageRecognitionMattingConfigReqVO.Coordinate.class);

        ImageRecognitionMattingConfigRespVO respVO = new ImageRecognitionMattingConfigRespVO();
        respVO.setId(String.valueOf(config.getId()));
        respVO.setElevatorId(elevatorId);
        respVO.setElevatorCode(config.getElevatorCode());
        respVO.setWidth(config.getWidth());
        respVO.setHeight(config.getHeight());
        respVO.setFaultTypes(faultTypes);
        respVO.setCoordinates(coordinates);

        return respVO;
    }

    @Override
    public List<UserElevatorsRespVO> searchUserElevators(boolean admin, String adminUserId, String userId) {

        HashMap<String, UserElevatorsRespVO> userElevatorsVOMap = new HashMap<>();

        //获取所有项目
        List<Map<String, String>> projects = bizProjectService.searchAllProjectList(adminUserId);

        projects.stream().forEach(it -> userElevatorsVOMap.put(it.get("projectId"), UserElevatorsRespVO.builder().projectName(it.get("projectName")).build()));

        //获取用户授权电梯
        List<UserElevatorsDO> userElevators = bizElevatorDao.searchUserElevators(admin, adminUserId, userId);

        for (UserElevatorsDO ele : userElevators) {

            UserElevatorsRespVO userElevatorsRespVO = userElevatorsVOMap.get(ele.getProjectId());

            if (userElevatorsRespVO != null) {

                userElevatorsRespVO.setTotalElevators(userElevatorsRespVO.getTotalElevators() + 1);
                userElevatorsRespVO.setUserElevators(userElevatorsRespVO.getUserElevators() + ele.getUserFlag());

                List<UserElevatorsDO> elevators = userElevatorsRespVO.getElevators();

                if (elevators == null || elevators.size() == 0) {
                    ArrayList<UserElevatorsDO> eles = new ArrayList<>();
                    eles.add(ele);
                    userElevatorsRespVO.setElevators(eles);
                } else {
                    elevators.add(ele);
                }
            }
        }

        return userElevatorsVOMap.values().stream().sorted(Comparator.comparing(UserElevatorsRespVO::getUserElevators).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<String> getProjectIdsByUserId(boolean isAdmin, String userId) {
        return bizElevatorDao.getProjectIdsByUserId(isAdmin, userId);
    }

    ///////////////////////////////////////private//////////////////////////////////////////////////////////////


    /**
     * 统计运行数据
     *
     * @param listRunCount
     * @param elevator
     * @param runCount
     */
    private void addCount(List<ElevatorRunCount> listRunCount, TblElevatorConf elevator, ElevatorRunCount runCount) {

         /*
        每小时客流 = 电梯往返次数 * 最大乘客数 * 负载系数 / 100
        电梯往返次数 = 终端上报钢丝绳弯折系数 （目前终端定义弯折次数为电梯往返次数）
        最大乘客数 = 电梯标注最大乘客数或基于实际应用场景配置最大乘客数
        负载系数 = 开方（每小时往返次数 / 30 * 开方（楼层数/5））* 100
         */
        Integer passengerFlow = 0;
        long backAndForthCount = 0;
        try {

            backAndForthCount = (elevator.getBackAndForthCount() - runCount.getLastBackAndForthCount()) / 2;

            //最大乘客数
            Integer largestPassengers = elevator.getLargestPassengers() == null ? 20 : elevator.getLargestPassengers();
            //负载系数
            Double loadFactor = elevator.getLoadFactor();
            if (loadFactor == null || loadFactor == 0) {

                int maxfloor = elevator.getIMaxFloor() == null ? 0 : elevator.getIMaxFloor();
                int minfloor = elevator.getIMinFloor() == null ? 0 : elevator.getIMinFloor();
                int floorNum = maxfloor - minfloor;

                loadFactor = Math.sqrt((NumberUtil.div(backAndForthCount, 30, 2) * Math.sqrt(NumberUtil.div(floorNum, 5, 2)))) * 100;

                loadFactor = loadFactor <= 0 ? 1 : loadFactor;
                loadFactor = loadFactor > 100 ? 100 : loadFactor;

                //添加负载系数统计
                bizElevatorDao.updateElevatorConf(elevator.getVElevatorId(), elevator.getVElevatorCode(), loadFactor);
            }

            Double p = backAndForthCount * largestPassengers * loadFactor / 100;
            passengerFlow = p.intValue();

        } catch (Exception e) {
            log.error("客流量统计失败! elevatorCode:{},error:{}", elevator.getVElevatorCode(), e.getMessage());
        }

        runCount.setId(SnowFlakeUtils.nextStrId());
        runCount.setVElevatorCode(elevator.getVElevatorCode());
        runCount.setVProjectId(elevator.getVProjectId());

        //运行次数
        runCount.setBiRunCount(elevator.getBiRunCount() - runCount.getLastRunCount());
        runCount.setLastRunCount(elevator.getBiRunCount());
        //开关门次数
        runCount.setBiDoorCount(elevator.getBiDoorCount() - runCount.getLastDoorCount());
        runCount.setLastDoorCount(elevator.getBiDoorCount());
        //钢丝绳折弯次数
        runCount.setBiBendCount(elevator.getBiBendCount() - runCount.getLastBendCount());
        runCount.setLastBendCount(elevator.getBiBendCount());
        //平层触发次数
        runCount.setBiLevelTriggerCount(elevator.getBiLevelTriggerCount() - runCount.getLastLevelTriggerCount());
        runCount.setLastLevelTriggerCount(elevator.getBiLevelTriggerCount());
        //累计运行距离
        runCount.setBiRunDistanceCount(elevator.getBiRunDistanceCount() - runCount.getLastRunDistanceCount());
        runCount.setLastRunDistanceCount(elevator.getBiRunDistanceCount());
        //往返次数
        runCount.setBackAndForthCount(backAndForthCount);
        runCount.setLastBackAndForthCount(elevator.getBackAndForthCount());
        //客流量
        runCount.setPassengerFlow(passengerFlow);

        Date date = new Date();

        runCount.setDReportDate(date);
        runCount.setUpdateTime(date);
        runCount.setDtCreateTime(date);

        listRunCount.add(runCount);
    }

    private List<ElevatorRunCount> checkCountData(List<ElevatorRunCount> listRunCount) {

        return listRunCount.stream().map(it -> {

            if (it.getBiRunCount() > Integer.MAX_VALUE || it.getBiRunCount() < 0) {
                it.setBiRunCount(0L);
            }
            if (it.getBiDoorCount() > Integer.MAX_VALUE || it.getBiDoorCount() < 0) {
                it.setBiDoorCount(0L);
            }
            if (it.getBiBendCount() > Integer.MAX_VALUE || it.getBiBendCount() < 0) {
                it.setBiBendCount(0L);
            }
            if (it.getBiLevelTriggerCount() > Integer.MAX_VALUE || it.getBiLevelTriggerCount() < 0) {
                it.setBiLevelTriggerCount(0L);
            }
            if (it.getBiRunDistanceCount() > Integer.MAX_VALUE || it.getBiRunDistanceCount() < 0) {
                it.setBiRunDistanceCount(0L);
            }
            if (it.getBackAndForthCount() > Integer.MAX_VALUE || it.getBackAndForthCount() < 0) {
                it.setBackAndForthCount(0L);
            }
            return it;

        }).collect(Collectors.toList());
    }

    private void extendGroupLeasingInfo(List<Map> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<String> elevatorCodeList = list.stream()
                .filter(item -> item.get("v_elevator_code") != null || item.get("elevatorCode") != null)
                .map(item -> item.get("v_elevator_code") == null
                        ? (item.get("elevatorCode") == null ? "" : item.get("elevatorCode").toString())
                        : item.get("v_elevator_code").toString())
                .filter(org.springframework.util.StringUtils::hasText)
                .toList();
        if (CollectionUtils.isEmpty(elevatorCodeList)) {
            return;
        }
        // 可疑电梯
        var elevatorGroupLeasingMap = groupLeasingStatisticsMapper
                .listByElevatorCodes(elevatorCodeList, "elevator", GroupLeasingLevelEnum.VERY_SUSPICIOUS.getLevel())
                .stream()
                .collect(Collectors.toMap(TblGroupLeasingStatisticsEntity::getElevatorCode, Function.identity()));
        if (!CollectionUtils.isEmpty(elevatorGroupLeasingMap)) {
            list.forEach(item -> {
                var elevatorCode = item.get("v_elevator_code") == null
                        ? (item.get("elevatorCode") == null ? "" : item.get("elevatorCode").toString())
                        : item.get("v_elevator_code").toString();
                if (elevatorGroupLeasingMap.containsKey(elevatorCode)) {
                    item.put("groupLeasingElevatorSuspicious", 1);
                }
            });
        }
        // 可疑楼层
        var floorGroupLeasingMap = groupLeasingStatisticsMapper
                .listByElevatorCodes(elevatorCodeList, "floor", GroupLeasingLevelEnum.VERY_SUSPICIOUS.getLevel())
                .stream()
                .collect(Collectors.groupingBy(TblGroupLeasingStatisticsEntity::getElevatorCode));
        if (!CollectionUtils.isEmpty(floorGroupLeasingMap)) {
            list.forEach(item -> {
                var elevatorCode = item.get("v_elevator_code") == null
                        ? (item.get("elevatorCode") == null ? "" : item.get("elevatorCode").toString())
                        : item.get("v_elevator_code").toString();
                if (floorGroupLeasingMap.containsKey(elevatorCode)) {
                    item.put("groupLeasingFloorSuspicious", floorGroupLeasingMap.get(elevatorCode)
                            .stream()
                            .map(TblGroupLeasingStatisticsEntity::getFloor)
                            .sorted()
                            .collect(Collectors.joining(",")));

                }
            });
        }
    }

    /**
     * 如果有群租查询条件，就设置条件
     *
     * @param searchElevatorModule 查询条件
     * @param pageIndex            页码
     * @param pageSize             分页大小
     * @return 结果
     */
    private PageListResultEntity<Object> extendGroupLeasingStatus(SearchElevatorModule searchElevatorModule, Integer pageIndex, Integer pageSize) {
        if (searchElevatorModule.getGroupLeasingStatus() != null) {
            var villages = tblVillageServiceI.getByEntity(TblVillage.builder()
                    .groupLeasingStatus(searchElevatorModule.getGroupLeasingStatus())
                    //.groupLeasingResult(searchElevatorModule.getGroupLeasingResult())
                    .build());
            if (CollectionUtils.isEmpty(villages)) {
                return new PageListResultEntity<>(pageIndex, pageSize, 0, Collections.emptyList());
            }
            var villageIds = villages.stream().map(TblVillage::getVVillageId).toList();
            searchElevatorModule.setVillageIds(villageIds);
        }
        return null;
    }

    /**
     * 如果有群租查询条件，就设置条件
     *
     * @param searchElevatorModule 查询条件
     * @return 结果
     */
    private <T> List<T> extendGroupLeasingStatus(SearchElevatorModule searchElevatorModule, Class<T> clazz) {
        if (searchElevatorModule.getGroupLeasingStatus() != null
                || searchElevatorModule.getGroupLeasingResult() != null) {
            var villages = tblVillageServiceI.getByEntity(TblVillage.builder()
                    .groupLeasingStatus(searchElevatorModule.getGroupLeasingStatus())
                    .groupLeasingResult(searchElevatorModule.getGroupLeasingResult())
                    .build());
            if (CollectionUtils.isEmpty(villages)) {
                return Collections.emptyList();
            }
            var villageIds = villages.stream().map(TblVillage::getVVillageId).toList();
            searchElevatorModule.setVillageIds(villageIds);
        }
        return null;
    }

}
