package com.shmashine.api.service.fault.impl;

import static com.shmashine.common.constants.RedisConstants.SENSOR_FAULT_SHIELD_CACHE;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import cn.hutool.core.date.DateUtil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.controller.fault.vo.ElevatorStopStatisticsVO;
import com.shmashine.api.controller.fault.vo.EventStatisticsReqVO;
import com.shmashine.api.controller.fault.vo.SearchFaultsReqVO;
import com.shmashine.api.dao.BizCameraDao;
import com.shmashine.api.dao.BizFaultDao;
import com.shmashine.api.dao.TblDeviceDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblFaultDefinitionDao;
import com.shmashine.api.dao.TblFaultShieldDao;
import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.entity.ElevatorEventStatistics;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.kafka.KafkaProducer;
import com.shmashine.api.kafka.KafkaTopicConstants;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.input.SearchSensorFaultModule;
import com.shmashine.api.module.fault.output.EventElevatorDownloadModuleMap;
import com.shmashine.api.module.fault.output.FaultDetailDownloadModuleMap;
import com.shmashine.api.module.fault.output.FaultResponseModule;
import com.shmashine.api.module.fault.output.FaultStatisticsExportModule;
import com.shmashine.api.module.fault.output.SensorFaultExportModule;
import com.shmashine.api.module.fault.output.UncivilizedBehaviorDownloadModuleMap;
import com.shmashine.api.properties.FaultMaskingProperties;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.util.CameraUtils;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblCamera;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblFault;
import com.shmashine.common.entity.TblFaultDefinition0902;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.socketClients.SocketClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 故障相关接口
 *
 * @Date: 2020/6/1718:06
 * @Author: LiuLiFu
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizFaultServiceImpl implements BizFaultService {
    private final FaultMaskingProperties faultMaskingProperties;

    private final BizFaultDao bizFaultDao;
    private final TblFaultDefinitionDao faultDefinitionDao;
    private final TblSysFileDao fileDao;
    private final BizCameraDao cameraDao;
    private final TblDeviceDao tblDeviceDao;
    private final TblElevatorDao tblElevatorDao;
    private final KafkaProducer kafkaProducer;
    private final BizElevatorService elevatorService;
    private final TblVillageServiceI tblVillageServiceI;
    private final SocketClient socketClient;
    private final AliOssProperties aliOssProperties;
    private final RedisUtils redisUtils;
    private final TblFaultShieldDao tblFaultShieldDao;


    HashMap<String, List<String>> sensorLinkFault = new HashMap<>();

    @PostConstruct
    public void init() {

        //获取传感器关联故障表
        List<HashMap<String, String>> sensorConfig = bizFaultDao.getSensorConfig();
        if (!CollectionUtils.isEmpty(sensorConfig)) {
            sensorConfig.forEach(it -> {
                if (it != null) {
                    List<String> faults = Arrays.stream(it.get("v_fault_type").split(",")).toList();
                    Arrays.stream(it.get("v_sensorFault_type").split(","))
                            .forEach(f -> sensorLinkFault.put(f, faults));
                }
            });
        }
    }

    /**
     * 获取故障列表
     */
    @Override
    public PageListResultEntity searchFaultSList(SearchFaultsReqVO searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizFaultDao.searchFaultList(searchFaultModule), pageSize);

        // 扩展 Village信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        var mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(),
                mapPageInfo.getList());
        return mapPageListResultEntity;
    }

    /**
     * 获取实时故障
     *
     * @param searchFaultModule 查询条件
     * @return 列表
     */
    @Override
    public List<Map> searchRealTimeFault(SearchFaultsReqVO searchFaultModule) {

        List<Map> result = bizFaultDao.searchFaultList(searchFaultModule);
        // 扩展 Village信息
        tblVillageServiceI.extendVillageInfo(result);
        return result;
    }


    /**
     * 获取故障详情
     */
    @Override
    public Map<String, Object> getFaultSDetail(String faultId) {
        TblFault fault = bizFaultDao.getByFaultId(faultId);
        TblCamera camera = cameraDao.getByElevatorId(fault.getVElevatorId());

        String privateUrl = "";
        String serialNumber = "";
        String cloudNumber = "";
        Integer cameraType = 2;

        if (camera != null) {
            serialNumber = camera.getVSerialNumber();
            cloudNumber = camera.getVCloudNumber();
            privateUrl = camera.getVPrivateUrl();
            cameraType = camera.getICameraType();
            if (cameraType == 1) {
                // 海康摄像头历史回看处理
                Date reportTime = fault.getDtReportTime();
                Date endTime = fault.getDtEndTime();
                privateUrl = CameraUtils.getEzOpenHistoryUrl(reportTime, endTime, privateUrl);
            }
        }

        // 故障录制视频
        String videoUrl = fileDao.getVideoUrl(faultId, 2, 1);
        // 故障截取图片
        List<String> imageList = fileDao.getByBusinessIdAndBusinessTypeAndFileType(faultId, 2, 0);

        Map<String, Object> result = new HashMap<>();
        result.put("detail", bizFaultDao.getFaultDetail(faultId));
        result.put("cameraType", cameraType);
        result.put("serialNumber", serialNumber);
        result.put("cloudNumber", cloudNumber);
        result.put("privateUrl", privateUrl);
        result.put("videoUrl", videoUrl);
        result.put("imageList", imageList);
        return result;
    }


    /**
     * 故障统计接口
     */
    @Override
    public Map<String, Object> getFaultStatistics(FaultStatisticsModule faultStatisticsModule) {
        Map<String, Object> resultMap = new HashMap<>();

        // 查询故障统计列表
        String faultType = faultStatisticsModule.getFaultType();
        Integer elevatorType = faultStatisticsModule.getElevatorType();
        Integer uncivilizedBehaviorFlag = faultStatisticsModule.getUncivilizedBehaviorFlag();
        String eventType = faultStatisticsModule.getEventType();
        var faultDefinitionList = faultDefinitionDao.listByFaultTypeAndElevatorCode(faultType, uncivilizedBehaviorFlag,
                elevatorType, eventType);

        // 构建Y轴数据
        List<Map<String, Object>> seriesList = new ArrayList<>();

        if ("1".equals(faultStatisticsModule.getGroupByElevator())) {
            // 按照电梯展示 - X轴为电梯编号
            return getFaultStatisticsByElevatorCode(faultStatisticsModule, resultMap, faultDefinitionList, seriesList);
        } else {
            // 按照时间展示 - X轴为时间
            return getFaultStatisticsByDate(faultStatisticsModule, resultMap, faultDefinitionList, seriesList);
        }

    }

    @Override
    public PageListResultEntity searchSensorFaultSList(SearchSensorFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizFaultDao.searchSensorFaultList(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        var mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(),
                mapPageInfo.getList());
        return mapPageListResultEntity;
    }


    /**
     * 获取故障详情
     */
    @Override
    public Map<String, Object> getSensorFaultSDetail(String faultId) {

        Map<String, Object> result = new HashMap<>();
        result.put("detail", bizFaultDao.getSensorFaultDetail(faultId));
        return result;
    }

    @Override
    public void cancelSensorFault(String faultId) {

        String elevatorCode = bizFaultDao.getElevatorCodeBySensorFaultId(faultId);

        if (!StringUtils.hasText(elevatorCode)) {
            log.info("cancelSensorFault-故障不存在-faultId:{}", faultId);
            return;
        }

        // 已确认 - 非故障
        TblFault tblFault = new TblFault();
        tblFault.setVFaultId(faultId);
        tblFault.setDtModifyTime(new Date());
        tblFault.setIStatus(1); // 状态（0:故障中、1:已恢复）
        tblFault.setIManualClear(-2); // 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
        bizFaultDao.cancelSensorFault(tblFault);

        //清除对应的传感器故障屏蔽
        disappearShieldCache(elevatorCode);

        // 通知设备取消故障
        /*String message = "{\"TY\":\"Fault\",\"ST\":\"clear\",\"fault_type\":" + tblFault.getIFaultType()
                + ",\"sensorType\":\"CarRoof\"}";*/
        var message = """
                {"TY":"Fault","ST":"clear","fault_type":"%s","sensorType":"CarRoof"}"""
                .formatted(tblFault.getIFaultType());
        kafkaProducer.sendMessageToKafka(KafkaTopicConstants.SOCKET_TOPIC, message);

        //openfeign调用socket模块对外接口
        socketClient.sendMessageToCube(message);
    }

    @Override
    public Map<String, Object> getSensorFaultRate(String projectId) {

        //获取所有按日期和状态展示的故障
        List<Map<String, Object>> res = bizFaultDao.getSensorFaultBYDataAndStatus(projectId);

        Map<String, Map<String, Map<String, Object>>> result = new HashMap<>();

        //获取日期列表
        Date end = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date start = calendar.getTime();
        List<String> categoriesList = DateUtils.getDatesBetweenTwoDate(sdf.format(start), sdf.format(end));

        //构建查询结果数据
        for (Map<String, Object> data : res) {
            String createTime = (String) data.get("time");

            if (result.containsKey(createTime)) {
                Map<String, Map<String, Object>> temp = result.get(createTime);
                temp.put(String.valueOf(data.get("i_status")), data);
            } else {
                Map<String, Map<String, Object>> temp = new HashMap<>();
                temp.put(String.valueOf(data.get("i_status")), data);
                result.put(createTime, temp);
            }
        }


        List<List<Integer>> typeDataList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Integer> dataList = new ArrayList<>();
            typeDataList.add(dataList);
        }

        for (String dateStr : categoriesList) {

            if (result.containsKey(dateStr)) {

                Map<String, Map<String, Object>> temp = result.get(dateStr);

                for (int i = 0; i < 2; i++) {

                    if (temp.containsKey(String.valueOf(i))) {
                        Map<String, Object> map = temp.get(String.valueOf(i));
                        long y = (Long) map.get("num");
                        typeDataList.get(i).add((int) y);
                    } else {
                        typeDataList.get(i).add(0);
                    }
                }

            } else {
                for (int i = 0; i < 2; i++) {
                    typeDataList.get(i).add(0);
                }
            }
        }


        // 构建Y轴数据
        List<Map<String, Object>> seriesList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> seriesMap = new HashMap<>();
            seriesMap.put("name", i);
            seriesMap.put("data", typeDataList.get(i));
            seriesList.add(seriesMap);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("categories", categoriesList);
        resultMap.put("series", seriesList);
        return resultMap;

    }

    @Override
    public Map<String, Object> tiringStatistics(String userId, boolean isAdmin, String projectId) {

        List<Map<String, Object>> res = bizFaultDao.tiringStatistics(userId, isAdmin, projectId);
        HashMap<String, Object> responseResult = getStatisticsByTime(res);

        return responseResult;
    }

    @Override
    public Map<String, Object> maintenanceStatistics(String userId, boolean isAdmin, String projectId) {

        List<Map<String, Object>> res = bizFaultDao.maintenanceStatistics(userId, isAdmin, projectId);
        HashMap<String, Object> responseResult = getStatisticsByTime(res);

        return responseResult;
    }

    @Override
    public Map<String, Object> faultStatistics(String userId, boolean isAdmin, String projectId) {

        List<Map<String, Object>> res = bizFaultDao.faultStatistics(userId, isAdmin, projectId);
        HashMap<String, Object> responseResult = getStatisticsByTime(res);

        return responseResult;
    }

    @Override
    public Map<String, Object> bicycleStatistics(String userId, boolean isAdmin, String projectId) {

        List<Map<String, Object>> res = bizFaultDao.bicycleStatistics(userId, isAdmin, projectId);
        HashMap<String, Object> responseResult = getStatisticsByTime(res);

        return responseResult;
    }

    @Override
    public Map<String, Object> stopTheDoorStatistics(String userId, boolean isAdmin, String projectId) {

        List<Map<String, Object>> res = bizFaultDao.stopTheDoorStatistics(userId, isAdmin, projectId);
        HashMap<String, Object> responseResult = getStatisticsByTime(res);

        return responseResult;
    }

    @Override
    public Map<String, Object> runStatistics(String userId, boolean isAdmin, String projectId) {

        //获取日统计运行次数
        List<Map<Object, Object>> res = bizFaultDao.runStatistics(userId, isAdmin, projectId);
        //日期
        ArrayList<Date> time = new ArrayList<>();
        //故障数
        ArrayList<BigDecimal> num = new ArrayList<>();

        res.stream().forEach(it -> {
            time.add((Date) it.get("time"));
            num.add((BigDecimal) it.get("num"));
        });
        HashMap<String, Object> responseResult = new HashMap<>();
        responseResult.put("time", time);
        responseResult.put("num", num);
        return responseResult;
    }

    /**
     * 根据日期统计故障结果
     *
     * @param res map结果为time(日期) num(数量) 结构
     */
    private HashMap<String, Object> getStatisticsByTime(List<Map<String, Object>> res) {
        //日期
        ArrayList<String> time = new ArrayList<>();
        //故障数
        ArrayList<Long> num = new ArrayList<>();

        res.stream().forEach(it -> {
            time.add((String) it.get("time"));
            num.add((long) it.get("num"));
        });
        HashMap<String, Object> responseResult = new HashMap<>();
        responseResult.put("time", time);
        responseResult.put("num", num);
        return responseResult;
    }

    @Override
    public ResponseResult getElevatorEventStatistics(EventStatisticsReqVO eventStatisticsReqVO) {

        if ("1".equals(eventStatisticsReqVO.getGroupByElevator())) {
            // 按照电梯展示 - X轴为电梯编号
            return getElevatorEventStatisticsByElevatorCode(eventStatisticsReqVO);
        } else {
            // 按照时间展示 - X轴为时间
            return getElevatorEventStatisticsByDate(eventStatisticsReqVO);
        }

    }

    private ResponseResult getElevatorEventStatisticsByElevatorCode(EventStatisticsReqVO eventStatisticsReqVO) {

        Map<String, Object> resultMap = new HashMap<>();
        // 构建Y轴数据
        List<Map<String, Object>> seriesList = new ArrayList<>();

        //获取符合条件的电梯列表
        List<String> eleCodeList = tblElevatorDao.searchEleCodes(eventStatisticsReqVO);
        if (CollectionUtils.isEmpty(eleCodeList)) {
            resultMap.put("categories", Collections.emptyList());
            resultMap.put("series", seriesList);
            return ResponseResult.successObj(resultMap);
        }

        //获取上下线记录
        List<ElevatorEventStatistics> eventStatisticsList = tblDeviceDao
                .getElevatorEventStatisticsListByCode(eventStatisticsReqVO, eleCodeList);

        eventStatisticsList.forEach(e -> {
            if (!StringUtils.hasText(e.getType())) {
                e.setType("null");
            }
        });

        // 获取上下线数前30的电梯
        Map<String, Integer> collect = eventStatisticsList.stream()
                .collect(Collectors.groupingBy(ElevatorEventStatistics::getElevatorCode,
                        Collectors.reducing(0, ElevatorEventStatistics::getEventNumCount, Integer::sum)));

        List<String> elevatorCodeList = collect.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(30).map(Map.Entry::getKey)
                .collect(Collectors.toList());

        //获取上下线原因
        List<String> eventTypes = eventStatisticsList.stream().map(e -> e.getType())
                .distinct().collect(Collectors.toList());

        // 上下线原因列表循环
        for (String eventType : eventTypes) {

            Map<String, Object> seriesMap = new HashMap<>();
            List<Integer> listNum = new ArrayList<>();

            // 电梯循环
            for (String code : elevatorCodeList) {

                int countNum = 0;
                try {
                    for (ElevatorEventStatistics event : eventStatisticsList) {
                        String elevatorCode = event.getElevatorCode();
                        String type = event.getType();
                        if (eventType.equals(type) && code.equals(elevatorCode)) {
                            countNum += event.getEventNumCount();
                        }
                    }
                } catch (Exception e) {
                    log.error("上下线按电梯统计失败，error：{}", ExceptionUtils.getStackTrace(e));
                }

                listNum.add(countNum);
            }

            seriesMap.put("name", eventType);
            seriesMap.put("data", listNum);
            seriesList.add(seriesMap);
        }

        resultMap.put("categories", elevatorCodeList);
        resultMap.put("series", seriesList);
        return ResponseResult.successObj(resultMap);
    }

    private ResponseResult getElevatorEventStatisticsByDate(EventStatisticsReqVO eventStatisticsReqVO) {

        // 构建Y轴数据
        List<Map<String, Object>> seriesList = new ArrayList<>();

        Map<String, Map<String, ElevatorEventStatistics>> result = new HashMap<>();

        // 获取开始时间 - 结束时间的数组
        List<String> categoriesList = DateUtils.getDatesBetweenTwoDate(eventStatisticsReqVO.getStartTime(),
                eventStatisticsReqVO.getEndTime());

        //获取符合条件的电梯列表
        List<String> eleCodeList = tblElevatorDao.searchEleCodes(eventStatisticsReqVO);

        //获取上下线记录
        List<ElevatorEventStatistics> eventStatisticsList = tblDeviceDao
                .getElevatorEventStatisticsListByTime(eventStatisticsReqVO, eleCodeList);

        eventStatisticsList.forEach(e -> {
            if (!StringUtils.hasText(e.getType())) {
                e.setType("null");
            }
        });

        //获取上下线原因
        List<String> eventTypes = eventStatisticsList.stream().map(e -> e.getType())
                .distinct().collect(Collectors.toList());

        //根据时间构建查询数据
        for (ElevatorEventStatistics data : eventStatisticsList) {

            String createTime = data.getHappenTime();
            String type = data.getType();

            if (result.containsKey(type)) {
                Map<String, ElevatorEventStatistics> temp = result.get(type);
                temp.put(createTime, data);
            } else {
                Map<String, ElevatorEventStatistics> temp = new HashMap<>();
                temp.put(createTime, data);
                result.put(type, temp);
            }
        }

        //上下线原因遍历
        for (String type : eventTypes) {

            HashMap<String, Object> seriesMap = new HashMap<>();
            ArrayList<Integer> data = new ArrayList<>();

            Map<String, ElevatorEventStatistics> eventStatisticsMap = result.get(type);

            //时间列表遍历
            for (String dateStr : categoriesList) {

                if (eventStatisticsMap.containsKey(dateStr)) {

                    ElevatorEventStatistics temp = eventStatisticsMap.get(dateStr);
                    data.add(temp.getEventNumCount());
                } else {
                    //该时间点没有记录-类型列表补0
                    data.add(0);
                }
            }

            seriesMap.put("name", type);
            seriesMap.put("data", data);
            seriesList.add(seriesMap);
        }

        Map<String, Object> resultMap = Map.of("categories", categoriesList,
                "series", seriesList);
        return ResponseResult.successObj(resultMap);
    }


    /**
     * 获取昨日故障占比
     */
    @Override
    public HashMap<String, Object> getYesterdayFaultStatistics(boolean isAdmin, String userId, List<String> projectIds,
                                                               List<String> villageIds) {
        List<String> elevatorIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(villageIds)) {
            elevatorIds = tblElevatorDao.getElevatorIdsByVillageIds(villageIds);
            if (CollectionUtils.isEmpty(elevatorIds)) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("total", 0);
                result.put("series", new ArrayList<>());
                return result;
            }
        } else if (!CollectionUtils.isEmpty(projectIds)) {
            elevatorIds = tblElevatorDao.getElevatorIdsByProjectIds(projectIds);
            if (CollectionUtils.isEmpty(elevatorIds)) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("total", 0);
                result.put("series", new ArrayList<>());
                return result;
            }
        }

        List<Map> faultStatics = bizFaultDao.getYesterdayFaultStatics(isAdmin, userId, elevatorIds);
        ArrayList<Object> series = new ArrayList<>();

        BigDecimal finalSum = new BigDecimal("0");
        for (Map faultStatic : faultStatics) {
            BigDecimal number = (BigDecimal) faultStatic.get("number");
            finalSum = finalSum.add(number);
        }
        for (Map item : faultStatics) {
            JSONObject seriesData = new JSONObject();
            seriesData.put("name", item.get("v_fault_name"));
            BigDecimal number = (BigDecimal) item.get("number");
            BigDecimal divide = number.divide(finalSum, 2, BigDecimal.ROUND_UP);
            seriesData.put("y", divide.multiply(BigDecimal.valueOf(100)));
            series.add(seriesData);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("total", finalSum);
        result.put("series", series);

        return result;

    }


    /**
     * 单体历史故障占比统计
     */
    @Override
    public List<Object> getHisFaultStatistics(boolean isAdmin, String userId, String elevatorId) {
        List<Map> faultStatics = bizFaultDao.getHisFaultStatics(isAdmin, userId, elevatorId);
        ArrayList<Object> result = new ArrayList<>();
        BigDecimal finalSum = new BigDecimal("0");
        for (Map faultStatic : faultStatics) {
            BigDecimal number = (BigDecimal) faultStatic.get("number");
            finalSum = finalSum.add(number);
        }
        for (Map item : faultStatics) {
            JSONObject seriesData = new JSONObject();
            seriesData.put("name", item.get("v_fault_name"));
            BigDecimal number = (BigDecimal) item.get("number");
            BigDecimal divide = number.divide(finalSum, 2, BigDecimal.ROUND_UP);
            seriesData.put("y", divide.multiply(BigDecimal.valueOf(100)));
            result.add(seriesData);
        }

        return result;
    }


    /**
     * 获取单梯的不文明行为
     */
    @Override
    public List<Object> getHospitalizationIonFaultStatistics(boolean isAdmin, String userId, String elevatorId) {
        // 1.单梯不文明行为统计
        List<Map> statistics = bizFaultDao.getHospitalizationIonFaultStatistics(isAdmin, userId, elevatorId);

        ArrayList<Object> result = new ArrayList<>();

        for (int i = 0; i < statistics.size(); i++) {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(statistics.get(i).get("v_fault_name"));
            objects.add(statistics.get(i).get("number"));
            result.add(objects);
        }
        return result;
    }

    @Override
    public List<FaultDetailDownloadModuleMap> searchFaultListDownload(SearchFaultsReqVO searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var mapPageInfo = new PageInfo<>(bizFaultDao.searchFaultListDownload(searchFaultModule), pageSize);
        return mapPageInfo.getList();
    }

    @Override
    public List<UncivilizedBehaviorDownloadModuleMap> searchUncivilizedBehaviorListDownload(
            SearchFaultsReqVO searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var mapPageInfo = new PageInfo<>(bizFaultDao.searchUncivilizedBehaviorListDownload(searchFaultModule),
                pageSize);

        return mapPageInfo.getList();
    }

    @Override
    public TblFault getById(String faultId) {
        return bizFaultDao.getById(faultId);
    }

    @Override
    public void update(TblFault fault) {
        fault.setDtModifyTime(new Date());
        bizFaultDao.update(fault);
    }

    @Override
    public void taskReloadFaultConfirm() {
        List<Map<String, String>> result = bizFaultDao.taskReloadFaultConfirm();
        for (Map<String, String> stringStringMap : result) {
            // imageIdentifyService.restTemplateSendMessage(stringStringMap.get("v_business_id"),
            //      stringStringMap.get("v_url"), "motorcycle");
            ImageIdentifyUtils.identifyImage(stringStringMap.get("v_business_id"), stringStringMap.get("v_url"),
                    ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE, aliOssProperties.getUseInternal());

        }
    }

    @Override
    public void taskReloadFaultDisappear() {
        //恢复故障
        List<TblFault> faults = bizFaultDao.getOnFualtBy37();
        for (TblFault tblFault : faults) {

            tblFault.setDtModifyTime(new Date());
            tblFault.setDtEndTime(new Date());
            tblFault.setIStatus(1);
            tblFault.setIManualClear(-2);
            bizFaultDao.update(tblFault);

            // 更新电梯表中状态
            elevatorService.cancelFaultByElevatorId(tblFault.getVElevatorId());

        }
    }


    @Override
    public void cancelFault(String faultId) {
        // 已确认 - 非故障
        TblFault tblFault = bizFaultDao.getByFaultId(faultId);
        tblFault.setDtModifyTime(new Date());
        tblFault.setIStatus(1); // 状态（0:故障中、1:已恢复）
        tblFault.setIManualClear(-2); // 手动恢复故障 -2 无任何操作，-1 手动恢复故障，需通知设备，0 失败，1成功，2故障不存在（后面三个都是通知终端后返回结果 ）
        bizFaultDao.update(tblFault);

        // 更新电梯表中状态
        elevatorService.cancelFaultByElevatorId(tblFault.getVElevatorId());

        //获取设备列表
        List<TblDevice> tblDevices = tblDeviceDao.deviceListByElevatorCode(tblFault.getVElevatorCode());

        tblDevices.forEach(d -> {
            // 通知设备取消故障
            String message = JSON.toJSONString(Map.of("TY", "Fault",
                    "ST", "clear",
                    "fault_type", Integer.valueOf(tblFault.getIFaultType()),
                    "sensorType", d.getVSensorType(),
                    "eid", tblFault.getVElevatorCode()));

            //openfeign调用socket模块对外接口
            socketClient.sendMessageToCube(message);
        });

    }

    /**
     * 导出上下线记录
     */
    @Override
    public List<EventElevatorDownloadModuleMap> searchEventElevatorDownload(FaultStatisticsModule module) {
        return tblDeviceDao.searchDeviceEventRecordBatch(module);
    }


    /**
     * 按照日期获取故障统计
     */
    private Map<String, Object> getFaultStatisticsByDate(FaultStatisticsModule faultStatisticsModule,
                                                         Map<String, Object> resultMap,
                                                         List<TblFaultDefinition0902> faultDefinitionList,
                                                         List<Map<String, Object>> seriesList) {
        List<String> categoriesList;
        Map<String, Map<String, FaultResponseModule>> result = new HashMap<>();

        // 获取开始时间 - 结束时间的数组
        categoriesList = DateUtils.getDatesBetweenTwoDate(faultStatisticsModule.getStartTime(),
                faultStatisticsModule.getEndTime());

        // 查询符合条件的所有故障
        List<FaultResponseModule> faultList = bizFaultDao.getStatisticsFaultList(faultStatisticsModule);

        for (FaultResponseModule fault : faultList) {
            Date createTime = fault.getCreateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (result.containsKey(dateFormat.format(createTime))) {
                Map<String, FaultResponseModule> temp = result.get(dateFormat.format(createTime));
                temp.put(fault.getFaultType(), fault);
            } else {
                Map<String, FaultResponseModule> temp = new HashMap<>();
                temp.put(fault.getFaultType(), fault);
                result.put(dateFormat.format(createTime), temp);
            }
        }

        List<List<Integer>> typeDataList = new ArrayList<>();
        for (TblFaultDefinition0902 faultDefinition : faultDefinitionList) {
            List<Integer> dataList = new ArrayList<>();
            typeDataList.add(dataList);
        }

        for (String dateStr : categoriesList) {
            if (result.containsKey(dateStr)) {
                Map<String, FaultResponseModule> temp = result.get(dateStr);
                for (int i = 0; i < faultDefinitionList.size(); i++) {
                    String faultType = faultDefinitionList.get(i).getFaultType();
                    if (temp.containsKey(faultType)) {
                        typeDataList.get(i).add(temp.get(faultType).getFaultNumCount());
                    } else {
                        typeDataList.get(i).add(0);
                    }
                }
            } else {
                for (int i = 0; i < faultDefinitionList.size(); i++) {
                    typeDataList.get(i).add(0);
                }
            }
        }

        for (int i = 0; i < faultDefinitionList.size(); i++) {
            Map<String, Object> seriesMap = new HashMap<>();
            seriesMap.put("name", faultDefinitionList.get(i).getFaultName());
            seriesMap.put("data", typeDataList.get(i));
            seriesList.add(seriesMap);
        }
        resultMap.put("categories", categoriesList);
        resultMap.put("series", seriesList);
        return resultMap;
    }


    /**
     * 按照电梯获取故障统计
     */
    private Map<String, Object> getFaultStatisticsByElevatorCode(FaultStatisticsModule faultStatisticsModule,
                                                                 Map<String, Object> resultMap,
                                                                 List<TblFaultDefinition0902> faultDefinitionList,
                                                                 List<Map<String, Object>> seriesList) {

        List<FaultResponseModule> faultList = bizFaultDao.getStatisticsFaultListByElevatorCode(faultStatisticsModule);
        // 获取故障数前50的电梯
        List<String> elevatorCodeList = bizFaultDao.getTopHundredElevatorCode(faultStatisticsModule);

        // 获取电梯对应的详细地址
        HashMap<String, String> address = new HashMap<>();
        if (elevatorCodeList != null && elevatorCodeList.size() > 0) {
            List<HashMap<String, String>> elevatorAddress = bizFaultDao.getElevatorAddressByEleCodes(elevatorCodeList);
            elevatorAddress.forEach(it -> address.put(it.get("v_elevator_code"), it.get("v_address")));
        }


        // 故障字典列表循环
        for (TblFaultDefinition0902 faultDefinition : faultDefinitionList) {
            Map<String, Object> seriesMap = new HashMap<>();
            List<Integer> listNum = new ArrayList<>();
            // 电梯循环
            for (String code : elevatorCodeList) {
                int failureCountNum = 0;
                for (FaultResponseModule fault : faultList) {
                    if (fault.getFaultType().equals(faultDefinition.getFaultType())
                            && code.equals(fault.getElevatorCode())) {
                        failureCountNum += fault.getFaultNumCount();
                    }
                }
                listNum.add(failureCountNum);
            }
            seriesMap.put("name", faultDefinition.getFaultName());
            seriesMap.put("data", listNum);
            seriesList.add(seriesMap);
        }

        resultMap.put("categories", elevatorCodeList);
        resultMap.put("series", seriesList);
        resultMap.put("addressInfo", address);
        return resultMap;
    }

    @Override
    public PageListResultEntity searchTimeoutFaultList(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(bizFaultDao.searchTimeoutFaultList(searchFaultModule), pageSize);
        // 封装分页数据结构
        var mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(),
                mapPageInfo.getList());
        return mapPageListResultEntity;
    }

    @Override
    public void cancelTimeOutFault(String id) {
        bizFaultDao.cancelTimeOutFault(id);
    }

    @Override
    public ResponseResult getFaultMediaFile(String faultId) {

        // 故障录制视频
        String videoUrl = fileDao.getVideoUrl(faultId, 2, 1);
        // 故障截取图片
        List<String> imageList = fileDao.getByBusinessIdAndBusinessTypeAndFileType(faultId, 2, 0);

        Map<String, Object> result = new HashMap<>();

        result.put("videoUrl", videoUrl);
        result.put("imageList", imageList);
        return ResponseResult.successObj(result);
    }

    @Override
    public ResponseResult delFaultByElevatorCode(String elevatorCode) {

        /**
         * 逻辑删除
         */
        return ResponseResult.successObj(bizFaultDao.delFaultByElevatorCode(elevatorCode));
    }

    @Override
    public ResponseResult delFaultById(String faultId) {
        return ResponseResult.successObj(bizFaultDao.delFaultById(faultId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearInFaultingFault() {
        //更新电梯故障中为恢复
        tblElevatorDao.clearInFaultingStatus();
        //清理故障中故障
        bizFaultDao.clearInFaultingFault();

    }

    @Override
    public void exportFaultStatistics(FaultStatisticsModule faultStatisticsModule, HttpServletResponse response) {

        // 查询故障统计列表
        List<FaultStatisticsExportModule> data = bizFaultDao.getStatisticsFaultExportList(faultStatisticsModule);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String name = faultStatisticsModule.getUncivilizedBehaviorFlag() == 0 ? "每日故障统计" : "每日不文明行为统计";
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), FaultStatisticsExportModule.class).sheet(name).doWrite(data);

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
    public void exportSensorFaultList(SearchSensorFaultModule searchSensorFaultModule, HttpServletResponse response) {
        // 查询故障统计列表
        List<SensorFaultExportModule> data = bizFaultDao.searchSensorFaultDownload(searchSensorFaultModule);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String name = "设备故障统计";
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(name, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), SensorFaultExportModule.class).sheet(name).doWrite(data);

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
    public ElevatorStopStatisticsVO getElevatorStopStatistics(String elevatorCode, Date startTime, Date endTime) {
        //获取电梯故障列表
        List<TblFault> faultList = bizFaultDao.getByElevatorCodeAndReportTime(elevatorCode, startTime, endTime);
        //计算运行时间
        TblElevator elevator = elevatorService.getElevatorByCode(elevatorCode);
        Date dtInstallTime = DateUtil.date((LocalDateTime) elevator.getDtInstallTime());
        if (dtInstallTime != null && startTime.before(dtInstallTime)) {
            startTime = dtInstallTime;
        }
        long runningTime = DateUtil.betweenMs(startTime, endTime);
        //故障停梯时间（包含困人）
        long stoppingTime = faultList.stream().mapToInt(TblFault::getIDurationTime).sum();
        //运行次数
        int runningNum = elevatorService.getRunNumByElevatorCodeAndTime(elevatorCode, startTime, endTime);
        //困人次数
        long peopleTrappedNum = faultList.stream().filter(f -> "7".equals(f.getIFaultType())
                || "8".equals(f.getIFaultType())).count();
        //故障次数
        long faultNum = faultList.size() - peopleTrappedNum;

        return ElevatorStopStatisticsVO.builder().elevatorCode(elevatorCode)
                .stoppingTime(stoppingTime).runningTime(runningTime).runningNum(runningNum)
                .peopleTrappedNum(peopleTrappedNum).faultNum(faultNum).build();
    }

    // CHECKSTYLE:OFF
    @Override
    public ResponseResult searchShieldedFaultList(String elevatorCode) {

        //故障
        ArrayList<String> southShields = new ArrayList<>();
        ArrayList<String> northShields = new ArrayList<>();

        //获取电梯信息
        TblElevator elevator = tblElevatorDao.getByElevatorCode(elevatorCode);

        //1. 获取手动屏蔽故障列表
        List<Map<String, Object>> shieldInfo =
                tblFaultShieldDao.getShieldInfo(elevatorCode, elevator.getIElevatorType(), elevator.getPlatformType());

        shieldInfo.stream().forEach(info -> {
            if (info.get("user_visible") != null && 0 == (Integer) info.get("user_visible")) {
                southShields.add((String) info.get("fault_name"));
            }
            if (info.get("user_visible") != null && 0 == (Integer) info.get("is_report")) {
                northShields.add((String) info.get("fault_name"));
            }
        });

        JSONObject respObj = new JSONObject();
        respObj.put("手动配置屏蔽-平台", southShields.stream().distinct().collect(Collectors.toList()));
        respObj.put("手动配置屏蔽-北向", northShields.stream().distinct().collect(Collectors.toList()));

        // 2. 获取电梯模式状态
        String statusKey = RedisKeyUtils.getElevatorStatus(elevatorCode);
        String status = redisUtils.hget(statusKey, "mode_status") == null
                ? "0" : (String) redisUtils.hget(statusKey, "mode_status");

        switch (status) {
            case "1" -> respObj.put("电梯模式状态屏蔽:", "电梯处于检修状态-屏蔽所有故障");
            case "2" -> respObj.put("电梯模式状态屏蔽:", "电梯处于停止服务-屏蔽所有故障");
            default -> respObj.put("电梯模式状态屏蔽:", "正常模式-无屏蔽故障");
        }

        //北向推送屏蔽故障
        ArrayList<String> sensorFaultShields = new ArrayList<>();
        //传感器配置关联故障屏蔽
        ArrayList<String> sensorShields = new ArrayList<>();
        //3. 传感器关联配置故障屏蔽
        List<String> sensorShieldFaults = tblDeviceDao.searchShieldedFaultList(elevatorCode);
        if (sensorShieldFaults != null && sensorShieldFaults.size() > 0) {

            List<TblFaultDefinition0902> tblFaultDefinition =
                    faultDefinitionDao.listByFaultTypeAndElevatorCode(null, null,
                            elevator.getIElevatorType(), elevator.getPlatformType().toString());

            HashMap<String, String> faultDefinitionMap = new HashMap<>();
            tblFaultDefinition.forEach(d -> faultDefinitionMap.put(d.getFaultType(), d.getFaultName()));

            sensorShieldFaults.stream().forEach(s -> {
                String[] split = s.split(",");
                for (String s1 : split) {
                    sensorShields.add(faultDefinitionMap.get(s1));
                }
            });
        }
        //传感器故障关联的故障屏蔽
        respObj.put("传感器配置关联的故障屏蔽", sensorShields.stream().distinct().collect(Collectors.toList()));

        //4. 传感器关联故障屏蔽
        List<Object> sensorFaultShieldFaults = redisUtils.getCacheList(SENSOR_FAULT_SHIELD_CACHE + elevatorCode);
        if (sensorFaultShieldFaults != null && sensorFaultShieldFaults.size() > 0) {
            List<TblFaultDefinition0902> tblFaultDefinition =
                    faultDefinitionDao.listByFaultTypeAndElevatorCode(null, null,
                            elevator.getIElevatorType(), elevator.getPlatformType().toString());

            HashMap<String, String> faultDefinitionMap = new HashMap<>();
            tblFaultDefinition.forEach(d -> faultDefinitionMap.put(d.getFaultType(), d.getFaultName()));

            sensorFaultShieldFaults.forEach(s -> sensorFaultShields.add(faultDefinitionMap.get(s)));
        }
        respObj.put("传感器故障关联的故障屏蔽", sensorFaultShields.stream().distinct().collect(Collectors.toList()));


        //5. 故障上报次数限制屏蔽
        String south = "ELEVATOR:FAULT:SOUTH_COUNT:" + elevatorCode + ":*";
        Set<String> southKey = redisUtils.keys(south);
        ArrayList<String> faultCountSouthShields = new ArrayList<>();
        if (southKey != null && southKey.size() > 0) {

            List<TblFaultDefinition0902> tblFaultDefinition =
                    faultDefinitionDao.listByFaultTypeAndElevatorCode(null, null,
                            elevator.getIElevatorType(), elevator.getPlatformType().toString());

            HashMap<String, String> faultDefinitionMap = new HashMap<>();
            tblFaultDefinition.forEach(d -> faultDefinitionMap.put(d.getFaultType(), d.getFaultName()));

            southKey.forEach(sk -> {

                Integer southCount = (Integer) redisUtils.getCacheObject(sk);
                if (southCount >= faultMaskingProperties.getPlatformFaultCountHour()) {
                    // 提取后缀部分
                    String suffix = sk.substring(south.length() - 1);
                    faultCountSouthShields.add(faultDefinitionMap.get(suffix));
                }
            });
        }
        respObj.put("每小时故障上报次数大于" + faultMaskingProperties.getPlatformFaultCountHour() + "次-平台屏蔽",
                faultCountSouthShields.stream().distinct().collect(Collectors.toList()));

        String north = "ELEVATOR:FAULT:NORTH_COUNT:" + elevatorCode + ":*";
        Set<String> northKey = redisUtils.keys(north);
        ArrayList<String> faultCountNorthShields = new ArrayList<>();
        if (northKey != null && northKey.size() > 0) {

            List<TblFaultDefinition0902> tblFaultDefinition =
                    faultDefinitionDao.listByFaultTypeAndElevatorCode(null, null,
                            elevator.getIElevatorType(), elevator.getPlatformType().toString());

            HashMap<String, String> faultDefinitionMap = new HashMap<>();
            tblFaultDefinition.forEach(d -> faultDefinitionMap.put(d.getFaultType(), d.getFaultName()));

            northKey.forEach(sk -> {

                Integer northCount = (Integer) redisUtils.getCacheObject(sk);
                if (northCount > faultMaskingProperties.getNorthFaultCountDay()) {
                    // 提取后缀部分
                    String suffix = sk.substring(south.length() - 1);
                    faultCountNorthShields.add(faultDefinitionMap.get(suffix));
                }

            });
        }
        respObj.put("每天故障上报次数大于" + faultMaskingProperties.getNorthFaultCountDay() + "次-北向屏蔽",
                faultCountNorthShields.stream().distinct().collect(Collectors.toList()));

        return ResponseResult.successObj(respObj);
    }
    // CHECKSTYLE:ON

    /**
     * 传感器故障消除 清除屏蔽缓存
     */
    public void disappearShieldCache(String elevator) {

        redisUtils.del(SENSOR_FAULT_SHIELD_CACHE + elevator);

        List<HashMap<String, String>> sensorFaults = bizFaultDao.getAllSensorFaultOnFaulting(2, elevator);

        //加载故障屏蔽缓存
        if (sensorFaults != null && sensorFaults.size() > 0) {

            List<String> linkFaults = new ArrayList<>();
            for (HashMap<String, String> sensorFault : sensorFaults) {
                linkFaults.addAll(sensorLinkFault.get(sensorFault.get("i_fault_type")));
            }

            redisUtils.lSet(SENSOR_FAULT_SHIELD_CACHE + elevator,
                    linkFaults.stream().distinct().collect(Collectors.toList()));
        }

    }

}
