package com.shmashine.api.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.controller.device.vo.DeviceParamConfReqVO;
import com.shmashine.api.controller.device.vo.DeviceParamConfRespVO;
import com.shmashine.api.controller.device.vo.DeviceParamSyncReqVO;
import com.shmashine.api.controller.device.vo.DeviceSensorAndStatusRespVO;
import com.shmashine.api.controller.device.vo.GetDeviceParamConfRespVO;
import com.shmashine.api.controller.device.vo.SearchDeviceConfPageReqVO;
import com.shmashine.api.convert.DeviceSensorAndStatusConvert;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizFaultDao;
import com.shmashine.api.dao.TblDeviceDao;
import com.shmashine.api.dao.TblElevatorConfigDao;
import com.shmashine.api.entity.TblDeviceSensor;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.device.SearchDeviceEventRecordModule;
import com.shmashine.api.module.fault.input.SearchSensorFaultModule;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevatorConfig;
import com.shmashine.common.utils.TimeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备服务实现类
 *
 * @author chenx
 */
@Service
@Slf4j
public class TblDeviceServiceImpl implements TblDeviceServiceI {

    @Resource(type = TblDeviceDao.class)
    private TblDeviceDao tblDeviceDao;

    @Resource
    private BizElevatorDao bizElevatorDao;

    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    @Resource
    private TblElevatorConfigDao tblElevatorConfigDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BizFaultDao bizFaultDao;

    @Override
    public TblDeviceDao getTblDeviceDao() {
        return tblDeviceDao;
    }

    @Override
    public TblDevice getById(String vDeviceId) {
        return tblDeviceDao.getById(vDeviceId);
    }

    @Override
    public List<TblDevice> getByEntity(TblDevice tblDevice) {
        return tblDeviceDao.getByEntity(tblDevice);
    }

    @Override
    public List<TblDevice> listByEntity(TblDevice tblDevice) {
        return tblDeviceDao.listByEntity(tblDevice);
    }

    @Override
    public List<TblDevice> listByIds(List<String> ids) {
        return tblDeviceDao.listByIds(ids);
    }

    @Override
    public int insert(TblDevice tblDevice) {
        Date date = new Date();
        tblDevice.setDtCreateTime(date);
        tblDevice.setDtModifyTime(date);
        return tblDeviceDao.insert(tblDevice);
    }

    @Override
    public int insertBatch(List<TblDevice> list) {
        return tblDeviceDao.insertBatch(list);
    }

    @Override
    public int update(TblDevice tblDevice) {
        tblDevice.setDtModifyTime(new Date());
        return tblDeviceDao.update(tblDevice);
    }

    @Override
    public int updateBatch(List<TblDevice> list) {
        return tblDeviceDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vDeviceId) {
        return tblDeviceDao.deleteById(vDeviceId);
    }

    @Override
    public int deleteByEntity(TblDevice tblDevice) {
        return tblDeviceDao.deleteByEntity(tblDevice);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblDeviceDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblDeviceDao.countAll();
    }

    @Override
    public int countByEntity(TblDevice tblDevice) {
        return tblDeviceDao.countByEntity(tblDevice);
    }

    @Override
    public List<TblDevice> deviceListByElevatorCode(String elevatorCode) {
        return tblDeviceDao.deviceListByElevatorCode(elevatorCode);
    }

    @Override
    public PageListResultEntity searchDeviceEventRecord(SearchDeviceEventRecordModule module) {
        Integer pageIndex = module.getPageIndex();
        Integer pageSize = module.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var hashMapPageInfo = new PageInfo<>(tblDeviceDao.searchDeviceEventRecord(module), pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                hashMapPageInfo.getList());
    }

    @Override
    public PageListResultEntity searchDeviceWaveForm(SearchDeviceEventRecordModule module) {
        Integer pageIndex = module.getPageIndex();
        Integer pageSize = module.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var hashMapPageInfo = new PageInfo<>(tblDeviceDao.searchDeviceWaveForm(module), pageSize);
        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(),
                hashMapPageInfo.getList());
    }

    @Override
    public List<String> getMasterVersions(SearchDeviceEventRecordModule module) {
        return tblDeviceDao.getMasterVersions(module);
    }

    @Override
    public GetDeviceParamConfRespVO getDeviceParamConf(String elevatorId) {

        List<DeviceParamConfRespVO> deviceParamConf = tblDeviceDao.getDeviceParamConf(elevatorId);

        if (CollectionUtils.isEmpty(deviceParamConf)) {
            return null;
        } else {

            GetDeviceParamConfRespVO resp = GetDeviceParamConfRespVO.builder().confList(deviceParamConf).build();
            if (deviceParamConf.size() == 1) {
                resp.setDeviceConfStatus(deviceParamConf.get(0).getDeviceConfStatus());
                resp.setLastConfTime(deviceParamConf.get(0).getLastConfTime());
                return resp;
            }

            DeviceParamConfRespVO d0 = deviceParamConf.get(0);
            DeviceParamConfRespVO d1 = deviceParamConf.get(1);

            Integer s0 = d0.getDeviceConfStatus();
            if (s0 == 1) {
                resp.setDeviceConfStatus(d0.getDeviceConfStatus());
                resp.setLastConfTime(d0.getLastConfTime());
                return resp;
            }

            Integer s1 = d1.getDeviceConfStatus();
            if (s1 == 1) {
                resp.setDeviceConfStatus(d1.getDeviceConfStatus());
                resp.setLastConfTime(d1.getLastConfTime());
                return resp;
            }

            if (s0 == 2) {
                resp.setDeviceConfStatus(d0.getDeviceConfStatus());
                resp.setLastConfTime(d0.getLastConfTime());
                return resp;
            }

            if (s1 == 2) {
                resp.setDeviceConfStatus(d1.getDeviceConfStatus());
                resp.setLastConfTime(d1.getLastConfTime());
                return resp;
            }

            resp.setDeviceConfStatus(d0.getDeviceConfStatus());
            resp.setLastConfTime(d0.getLastConfTime());

            //获取【平层传感器】备注, 1:烟杆 2:小平层 3:U型光电 4:门磁
            Integer floorSensorRemark = tblDeviceDao.getFloorSensorRemarkByElevatorId(elevatorId);

            resp.setFloorSensorRemark(floorSensorRemark);

            return resp;
        }
    }

    // CHECKSTYLE:OFF
    @Override
    public Boolean deviceParamConfigure(List<DeviceParamConfReqVO> deviceParamConfReqVO, String userId) {
        //更新配置表
        for (var paramConf : deviceParamConfReqVO) {
            var conf = tblDeviceDao.getDeviceParamConfByElevatorCodeAndSensorType(paramConf.getElevatorCode(),
                    paramConf.getSensorType());

            //更新新增配置表
            if (conf != null) {
                paramConf.setId(Long.parseLong(conf.getId()));
                tblDeviceDao.updateDeviceParamConf(paramConf);
            } else {
                paramConf.setId(IdUtil.getSnowflakeNextId());
                tblDeviceDao.insertDeviceParamConf(paramConf);
            }

            //更新电梯表
            bizElevatorDao.updateDeviceConfStatusByCode(paramConf.getElevatorCode(), 1);

            //数据服务器IP:PORT 配置下发
            String serverIpPort = paramConf.getServerIpPort();
            if (StringUtils.hasText(serverIpPort)) {
                String[] ipPort = serverIpPort.split(":");
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "Srv",
                        "IP", ipPort[0],
                        "Port", Integer.valueOf(ipPort[1])));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //dlog服务器IP:PORT 配置下发
            String dlogIpPort = paramConf.getDlogIpPort();
            if (StringUtils.hasText(dlogIpPort)) {
                String[] ipPort = dlogIpPort.split(":");
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "DlogSrv",
                        "IP", ipPort[0],
                        "Port", Integer.valueOf(ipPort[1])));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }


            // 1：单门 2：贯通门  配置下发
            Integer doorNum = paramConf.getDoorNum();
            if (doorNum != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "DoorNum",
                        "Num", doorNum));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //基准楼层  配置下发
            Integer baseFloor = paramConf.getBaseFloor();
            if (baseFloor != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "BaseFloor",
                        "Floor", baseFloor));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //最低最高楼层  配置下发
            String floor = paramConf.getFloor();
            if (StringUtils.hasText(floor)) {
                String[] split = floor.split("~");
                Integer minFloor = Integer.valueOf(split[0]);
                Integer maxFloor = Integer.valueOf(split[1]);

                //更新电梯表楼层配置
                bizElevatorDao.updateFloorByElevatorCode(paramConf.getElevatorCode(), minFloor, maxFloor);

                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "limit",
                        "min", minFloor,
                        "max", maxFloor));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //困人延时上报时间  配置下发
            Integer peopleRptTm = paramConf.getPeopleRptTm();
            if (peopleRptTm != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "PeopleTrapped",
                        "ReportTime", peopleRptTm));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //统计数据上报周期  配置下发
            Integer statData = paramConf.getStatData();
            if (statData != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "StatData",
                        "Cycle", statData));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //超速上报阈值  配置下发
            String overSpeed = paramConf.getOverSpeed();
            if (StringUtils.hasText(overSpeed)) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "OverSpeed",
                        "Speed", overSpeed));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //传感器配置值  配置下发
            String sensorCfg = paramConf.getSensorCfg();
            if (StringUtils.hasText(sensorCfg)) {

                String elevatorCode = paramConf.getElevatorCode();
                String sensorType = paramConf.getSensorType();

                //传感器配置关联故障屏蔽
                char[] chars = sensorCfg.toCharArray();
                List<TblDeviceSensor> tblDeviceSensors = new ArrayList<>();

                if ("MotorRoom".equals(sensorType)) {       //机房

                    for (int i = 0; i <= chars.length - 1; i++) {

                        int sensorChose = 0;
                        if (chars[i] == 49) {
                            sensorChose = 1;
                        }

                        String sensorConfigId = switch (i) {
                            case 0 -> "1";  //检修服务电流传感器 1
                            case 1 -> "2";  //抱闸服务电流传感器 2
                            case 2 -> "3";  //抱闸回路电流传感器 3
                            case 3 -> "4";  //厅门回路电流传感器 4
                            case 4 -> "5";  //安全回路电流传感器 5
                            case 5 -> "6";  //紧急呼叫电流传感器 6
                            case 6 -> "7";  //停止服务电流传感器 7
                            case 7 -> "8";  //停电服务电流传感器 8
                            case 8 -> "9";   //温度传感器 9
                            default -> "";
                        };

                        if (StringUtils.hasText(sensorConfigId)) {
                            TblDeviceSensor deviceSensor = new TblDeviceSensor();
                            deviceSensor.setvDeviceSensorId(IdUtil.getSnowflakeNextIdStr());

                            deviceSensor.setvElevatorCode(elevatorCode);
                            deviceSensor.setvDeviceId(paramConf.getDeviceId());
                            deviceSensor.setvSensorConfigId(sensorConfigId);
                            deviceSensor.setDtCreateTime(new Date());
                            deviceSensor.setDtModifyTime(new Date());
                            deviceSensor.setvCreateUserId(userId);
                            deviceSensor.setvModifyUserId(userId);
                            deviceSensor.setiSensorChose(sensorChose);
                            tblDeviceSensors.add(deviceSensor);
                        }

                    }

                } else {        //轿顶|单盒

                    for (int i = 0; i <= chars.length - 1; i++) {

                        int sensorChose = 0;
                        if (chars[i] == 49) {
                            sensorChose = 1;
                        }

                        String sensorConfigId = switch (i) {
                            case 0, 1 -> "10";  //门磁传感器 10
                            case 2 -> "11";    //平层传感器 11
                            case 3 -> "12";   //基准传感器 12
                            case 4 -> "13";    //门机电机电流传感器 13
                            case 5 -> "14";   //轿门锁止电流传感器 14
                            case 6 -> "15";  //人感 15
                            case 7 -> "16";  //助动车传感器 16
                            case 8 -> "1";    //检修服务电流传感器 1
                            case 9 -> "9";    //温度传感器 9
                            default -> "";
                        };

                        if (StringUtils.hasText(sensorConfigId)) {
                            TblDeviceSensor deviceSensor = new TblDeviceSensor();
                            deviceSensor.setvDeviceSensorId(IdUtil.getSnowflakeNextIdStr());

                            deviceSensor.setvElevatorCode(elevatorCode);
                            deviceSensor.setvDeviceId(paramConf.getDeviceId());
                            deviceSensor.setvSensorConfigId(sensorConfigId);
                            deviceSensor.setDtCreateTime(new Date());
                            deviceSensor.setDtModifyTime(new Date());
                            deviceSensor.setvCreateUserId(userId);
                            deviceSensor.setvModifyUserId(userId);
                            deviceSensor.setiSensorChose(sensorChose);
                            tblDeviceSensors.add(deviceSensor);
                        }

                    }

                }

                if (tblDeviceSensors.size() > 0) {
                    //删除设备关联传感器
                    tblDeviceDao.delDeviceSensorsById(paramConf.getDeviceId());
                    //新增设备关联传感器
                    tblDeviceDao.batchInsertDeviceSensors(tblDeviceSensors);
                }

                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", elevatorCode,
                        "sensorType", sensorType,
                        "TY", "Update",
                        "ST", "sensors",
                        "sensorMap", sensorCfg));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);

            }

            //传感器极性配置值  配置下发
            String sensorInvertCfg = paramConf.getSensorInvertCfg();
            if (StringUtils.hasText(sensorInvertCfg)) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "SensorInvertCfg",
                        "SensorInvert", sensorInvertCfg));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //人感  配置下发
            String humanType = paramConf.getHumanType();
            if (StringUtils.hasText(humanType)) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "humanType",
                        "value", humanType));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //模式  配置下发
            Integer mode = paramConf.getMode();
            if (mode != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "Mode",
                        "Mode", mode));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //dlog开关  配置下发
            Integer normalDlog = paramConf.getNormalDlog();
            Integer abortDlog = paramConf.getAbortDlog();
            Integer aiDlog = paramConf.getAiDlog();
            if (normalDlog != null || abortDlog != null || aiDlog != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "DlogSW",
                        "NormalDlog", normalDlog,
                        "AbortDlog", abortDlog,
                        "AIDlog", aiDlog));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //开关门故障时间和阻挡门次数配置
            Integer openWaitingTime = paramConf.getOpenWaitingTime();
            Integer closeWaitingTime = paramConf.getCloseWaitingTime();
            Integer closeRepeatCount = paramConf.getCloseRepeatCount();
            if (openWaitingTime != null || closeWaitingTime != null || closeRepeatCount != null) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "doorFaultTimeRpt",
                        "openWatingTm", openWaitingTime,
                        "closeWatingTm", closeWaitingTime,
                        "closeRepeatCnt", closeRepeatCount));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

            //设备IP网络 配置下发
            String devIpType = paramConf.getDevIpType();
            String devIp = paramConf.getDevIp();
            String devNetmask = paramConf.getDevNetmask();
            String devGateway = paramConf.getDevGateway();
            if (StringUtils.hasText(devIpType) || StringUtils.hasText(devIp) || StringUtils.hasText(devNetmask)
                    || StringUtils.hasText(devGateway)) {
                //构建请求体
                var message = JSON.toJSONString(Map.of("etype", "MX201",
                        "eid", paramConf.getElevatorCode(),
                        "sensorType", paramConf.getSensorType(),
                        "TY", "Update",
                        "ST", "DevIP",
                        "DevIPType", devIpType,
                        "DevIP", devIp,
                        "DevNetmask", devNetmask,
                        "DevGateway", devGateway));

                //发送请求
                sendDeviceParamConf(paramConf.getElevatorCode(), paramConf.getSensorType(), message);
            }

        }

        return true;
    }
    // CHECKSTYLE:ON

    @Override
    public PageListResultEntity searchDeviceConfPage(SearchDeviceConfPageReqVO searchElevatorModule) {

        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        // 查询数据
        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map> hashMapPageInfo = new PageInfo<>(bizElevatorDao
                .searchElevatorListWithConfStatus(searchElevatorModule), pageSize);

        //参数配置状态信息
        List<Map> resultList = hashMapPageInfo.getList();

        if (resultList.size() == 0) {
            // 封装分页数据结构
            return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), resultList);
        }

        //拼接配置同步时间
        extendParamConfStatus(resultList);
        // 拼接设备状态
        convertDeviceStatus(resultList);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(resultList);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), resultList);
    }

    @Override
    public String deviceParamSync(DeviceParamSyncReqVO reqVO) {

        reqVO.getSensorTypes().stream().forEach(sensorType -> {

            //构建请求体
            var message = JSON.toJSONString(Map.of("etype", "MX201",
                    "eid", reqVO.getElevatorCode(),
                    "sensorType", sensorType,
                    "TY", "Update",
                    "ST", "ReadAllParam"));

            //发送请求
            sendDeviceParamConf(reqVO.getElevatorCode(), sensorType, message);

        });

        return "success";
    }

    @Override
    public String setDetectedPeopleNumsIsOpen(String elevatorCode, Integer detectedPeopleNumsIsOpen) {

        TblElevatorConfig elevatorConfig = tblElevatorConfigDao.getConfigByElevatorCode(elevatorCode);

        if (elevatorConfig == null) {

            elevatorConfig = TblElevatorConfig.builder()
                    .elevatorConfigId(IdUtil.getSnowflakeNextIdStr())
                    .elevatorCode(elevatorCode)
                    .isOpenDetectedPeopleNums(detectedPeopleNumsIsOpen)
                    .build();

            tblElevatorConfigDao.insert(elevatorConfig);

        } else {
            elevatorConfig.setIsOpenDetectedPeopleNums(detectedPeopleNumsIsOpen);
            tblElevatorConfigDao.update(elevatorConfig);
        }

        return "success";
    }

    @Override
    public List<DeviceSensorAndStatusRespVO> getDeviceSensorAndStatus(String elevatorCode) {
        //构建故障列表查询参数
        DateTime now = DateTime.now();
        DateTime beforeMonth = DateUtil.offsetMonth(now, -1);
        SearchSensorFaultModule searchFaultModule = new SearchSensorFaultModule();
        searchFaultModule.setAdminFlag(true);
        searchFaultModule.setvElevatorCode(elevatorCode);
        searchFaultModule.setDtReportTime(beforeMonth.toStringDefaultTimeZone());
        searchFaultModule.setDtEndTime(now.toStringDefaultTimeZone());
        searchFaultModule.setiStatus(0);

        //获取故障中传感器故障列表
        List<Map> faultList = bizFaultDao.searchSensorFaultList(searchFaultModule);

        var deviceSensorList = tblDeviceDao.getDeviceSensorConfig(elevatorCode);
        if (CollectionUtils.isEmpty(faultList)) {
            return DeviceSensorAndStatusConvert.INSTANCE.convertList(deviceSensorList);
        }

        //传感器故障匹配
        List<String> faultTypes = faultList.stream().map(f -> (String) f.get("i_fault_type")).toList();

        faultTypes.forEach(faultType -> deviceSensorList.forEach(d -> {
            if (d.getSensorFaultType() != null && d.getSensorFaultType().contains(faultType)) {
                d.setFaultStatus(1);
            }
        }));

        return DeviceSensorAndStatusConvert.INSTANCE.convertList(deviceSensorList);
    }

    @Override
    public List<String> getDeviceVersionList() {
        return tblDeviceDao.getDeviceVersionList();
    }

    /**
     * 下发设备配置命令
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   传感器类型
     */
    private void sendDeviceParamConf(String elevatorCode, String sensorType, String message) {

        //离线设备不下发
        TblDevice tblDevice = tblDeviceDao.getByElevatorCodeAndSensorType(elevatorCode, sensorType);

        if (tblDevice.getIOnlineStatus() == 0) {
            log.info("设备离线-不下发设备指令消息 - elevatorCode：{} - sensorType：{} - message：{}", elevatorCode, sensorType,
                    message);
            return;
        }

        try {
            String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
            Map<Object, Object> deviceStatus = redisTemplate.opsForHash().entries(key);
            String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
            String nodePort = String.valueOf(deviceStatus.get("server_port"));

            // 请求路径：
            String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

            // 发出一个post请求
            String s = HttpRequest.post(url).timeout(6000).body(message)
                    .header(StpUtil.getTokenName(), StpUtil.getTokenValueNotCut()).execute().body();
            log.info("[{}] ---指令下发成功，下发地址：[{}]，下发消息：[{}]，返回信息：[{}]\n", TimeUtils.nowTime(), url, message, s);

        } catch (Exception e) {
            log.info("接口调用失败,入参为{}, error:{}", message, ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * 拼接电梯设备状态
     */
    private void convertDeviceStatus(List<Map> list) {
        List<String> elevatorIdList = new ArrayList<>();
        list.forEach(value -> {
            elevatorIdList.add(String.valueOf(value.get("v_elevator_id")));
        });
        if (CollectionUtils.isEmpty(elevatorIdList)) {
            return;
        }
        List<TblDevice> deviceList = tblDeviceDao.listByElevatorIds(elevatorIdList);

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

    private void extendParamConfStatus(List<Map> resultList) {

        ExecutorService executorService = Executors.newFixedThreadPool(resultList.size());

        try {

            CompletableFuture[] completableFutures = resultList.stream().map(r -> CompletableFuture.supplyAsync(() -> {

                String elevatorId = (String) r.get("v_elevator_id");

                //查询参数配置列表
                List<DeviceParamConfRespVO> deviceParamConf = tblDeviceDao.getDeviceParamConf(elevatorId);

                if (CollectionUtils.isEmpty(deviceParamConf)) {
                    r.put("deviceConfStatus", 0);
                    r.put("lastConfTime", null);
                } else {

                    if (deviceParamConf.size() == 1) {
                        r.put("lastConfTime", deviceParamConf.get(0).getLastConfTime());
                        return null;
                    }

                    DeviceParamConfRespVO d0 = deviceParamConf.get(0);
                    DeviceParamConfRespVO d1 = deviceParamConf.get(1);

                    Integer s0 = d0.getDeviceConfStatus();
                    if (s0 == 1) {
                        r.put("lastConfTime", d0.getLastConfTime());
                        return null;
                    }

                    Integer s1 = d1.getDeviceConfStatus();
                    if (s1 == 1) {
                        r.put("lastConfTime", d1.getLastConfTime());
                        return null;
                    }

                    if (s0 == 2) {
                        r.put("lastConfTime", d0.getLastConfTime());
                        return null;
                    }

                    if (s1 == 2) {
                        r.put("lastConfTime", d1.getLastConfTime());
                        return null;
                    }

                    r.put("lastConfTime", d0.getLastConfTime());
                    return null;
                }

                return null;

            }, executorService)).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(20, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } finally {
            executorService.shutdown();
        }

    }

}