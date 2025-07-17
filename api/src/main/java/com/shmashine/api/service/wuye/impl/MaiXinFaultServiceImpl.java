package com.shmashine.api.service.wuye.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizFaultDao;
import com.shmashine.api.dao.BizVillageDao;
import com.shmashine.api.dao.MaiXinWuyeFaultDao;
import com.shmashine.api.dao.TblFaultDefinitionDao;
import com.shmashine.api.dao.WuyeFaultDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.output.FaultResponseModule;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.api.service.wuye.ElevatorService;
import com.shmashine.api.service.wuye.MaiXinEventService;
import com.shmashine.api.service.wuye.MaiXinFaultService;
import com.shmashine.api.service.wuye.MaiXinMaintenanceService;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblFaultDefinition0902;

@Service
public class MaiXinFaultServiceImpl implements MaiXinFaultService {

    @Autowired
    private MaiXinWuyeFaultDao faultDao;

    @Autowired
    private TblVillageServiceI tblVillageServiceI;

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private WuyeFaultDao wuyeFaultDao;

    @Autowired
    private BizDeptService bizDeptService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private MaiXinEventService eventService;

    @Autowired
    private MaiXinMaintenanceService maintenanceService;

    @Autowired
    private BizVillageDao bizVillageDao;

    @Autowired
    private BizElevatorDao bizElevatorDao;
    @Autowired
    private  TblFaultDefinitionDao faultDefinitionDao;
    @Autowired
    private BizFaultDao bizFaultDao;

    /**
     * 获取故障列表
     *
     * @param searchFaultModule 查询条件
     * @return 分页信息
     */
    @Override
    public PageListResultEntity searchFaultsListWithPage(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(faultDao.searchFaultList(searchFaultModule), pageSize);

        // 扩展小区信息
        tblVillageServiceI.extendVillageInfo(mapPageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
    }

    @Override
    public List<HashMap<String, Object>> getFaultCountByVillage(SearchFaultModule searchFaultModule) {

        //根据小区统计急修、隐患、电瓶车入梯
        /**
         * 根据上方周月切换
         *
         * 进行数字同步刷新，急修次数为仪电反推且状态为已确认和已完成的工单，
         *
         * 隐患问题为仪电反推且状态为误报或新建工单两种状态的工单
         */

        //急修
        List<HashMap<String, Object>> faultOrderByConfirmOrCompleted = eventService.getFaultOrderByConfirmOrCompleted(searchFaultModule);
        HashMap<String, Integer> repairOrder = new HashMap<>();
        faultOrderByConfirmOrCompleted.stream().forEach(it -> repairOrder.put((String) it.get("v_village_id"), Integer.valueOf(it.get("number").toString())));

        //困人数
        List<HashMap<String, Object>> peopleTrappedResult = eventService.getPeopleTrappedCountByVillage(searchFaultModule);
        HashMap<String, Integer> peopleTrappedCount = new HashMap<>();
        peopleTrappedResult.stream().forEach(it -> peopleTrappedCount.put((String) it.get("v_village_id"), Integer.valueOf(it.get("number").toString())));

        //反复阻挡门
        List<HashMap<String, Object>> blockDoorFaultCount = eventService.getBlockDoorByVillage(searchFaultModule);
        HashMap<String, Integer> blockDoorCount = new HashMap<>();
        blockDoorFaultCount.stream().forEach(it -> blockDoorCount.put((String) it.get("v_village_id"), Integer.valueOf(it.get("number").toString())));

        //获取所有小区列表
        List<HashMap<String, Object>> villageInfos = getVillageInfo(searchFaultModule);

        villageInfos.stream().forEach(it -> {
            Integer _repairOrder = repairOrder.get(it.get("v_village_id")) == null ? 0 : repairOrder.get(it.get("v_village_id"));
            Integer _peopleTrappedCount = peopleTrappedCount.get(it.get("v_village_id")) == null ? 0 : peopleTrappedCount.get(it.get("v_village_id"));
            Integer _blockDoorCount = blockDoorCount.get(it.get("v_village_id")) == null ? 0 : blockDoorCount.get(it.get("v_village_id"));
            it.put("repairOrder", _repairOrder);
            it.put("peopleTrappedCount", _peopleTrappedCount);
            it.put("blockDoorCount", _blockDoorCount);
        });

        return villageInfos;
    }

    /**
     * 故障统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getFaultStatistics(FaultStatisticsModule faultStatisticsModule) {

        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);

        List<HashMap<String, Object>> result = null;
        switch (orderKey) {
            case "repairs":
                result = faultDao.getFaultStatisticsRepairs(faultStatisticsModule);
                break;
            case "personTrapped":
                result = faultDao.getFaultStatisticsPersonTrapped(faultStatisticsModule);
                break;
            default:
                result = faultDao.getFaultStatistics(faultStatisticsModule);
        }

        ArrayList<String> deptIds = getDeptIds(faultStatisticsModule.getUserId());

        SearchVillaListModule searchVillaListModule = new SearchVillaListModule();
        searchVillaListModule.setPermissionDeptIds(deptIds);
        searchVillaListModule.setAdminFlag(bizUserService.isAdmin(faultStatisticsModule.getUserId()));
        searchVillaListModule.setUserId(faultStatisticsModule.getUserId());

        List<Map> villageList = bizVillageDao.searchVillageList(searchVillaListModule);
        List<String> allVillages = villageList.stream().map(village -> (String) village.get("v_village_name")).collect(Collectors.toList());

        List<String> villageNames = result.stream().filter(item -> ((String) item.get("key")).equals(orderKey)).map(item -> (String) item.get("villageName")).collect(Collectors.toList());
        List<String> subVillages = allVillages.stream().filter(item -> !villageNames.contains(item)).collect(Collectors.toList());
        villageNames.addAll(subVillages);

        List<Long> totalCount = new ArrayList<>(villageNames.size());
        List<Long> repairsCount = new ArrayList<>(villageNames.size());
        List<Long> personTrappedCount = new ArrayList<>(villageNames.size());
        List<BigDecimal> totalRate = new ArrayList<>(villageNames.size());
        List<BigDecimal> repairsRate = new ArrayList<>(villageNames.size());
        List<BigDecimal> personTrappedRate = new ArrayList<>(villageNames.size());

        for (String strName : villageNames) {
            Optional<HashMap<String, Object>> mapTotal = result.stream().filter(item -> ((String) item.get("key")).equals("total") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapTotal.isPresent()) {
                totalCount.add((Long) mapTotal.get().get("number"));
                totalRate.add((BigDecimal) mapTotal.get().get("rate"));
            } else {
                totalCount.add(0L);
                totalRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapRepairs = result.stream().filter(item -> ((String) item.get("key")).equals("repairs") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapRepairs.isPresent()) {
                repairsCount.add((Long) mapRepairs.get().get("number"));
                repairsRate.add((BigDecimal) mapRepairs.get().get("rate"));
            } else {
                repairsCount.add(0L);
                repairsRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapPersonTrapped = result.stream().filter(item -> ((String) item.get("key")).equals("personTrapped") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapPersonTrapped.isPresent()) {
                personTrappedCount.add((Long) mapRepairs.get().get("number"));
                personTrappedRate.add((BigDecimal) mapRepairs.get().get("rate"));
            } else {
                personTrappedCount.add(0L);
                personTrappedRate.add(BigDecimal.valueOf(0.0));
            }
        }

        HashMap<String, List> resMap = new HashMap<>();

        resMap.put("categories", villageNames);
        List<HashMap<String, Object>> series = new ArrayList<>();

        HashMap<String, Object> totalSeries = new HashMap<>();
        totalSeries.put("data", totalCount);
        totalSeries.put("name", "全部");
        totalSeries.put("type", "column");
        series.add(totalSeries);

        HashMap<String, Object> repairsSeries = new HashMap<>();
        repairsSeries.put("data", repairsCount);
        repairsSeries.put("name", "急修");
        repairsSeries.put("type", "column");
        series.add(repairsSeries);

        HashMap<String, Object> personTrappedSeries = new HashMap<>();
        personTrappedSeries.put("data", personTrappedCount);
        personTrappedSeries.put("name", "困人");
        personTrappedSeries.put("type", "column");
        series.add(personTrappedSeries);

        HashMap<String, Object> totalRateSeries = new HashMap<>();
        totalRateSeries.put("data", totalRate);
        totalRateSeries.put("name", "全部(比率)");
        totalRateSeries.put("type", "spline");
        series.add(totalRateSeries);

        HashMap<String, Object> repairsRateSeries = new HashMap<>();
        repairsRateSeries.put("data", repairsRate);
        repairsRateSeries.put("name", "急修(比率)");
        repairsRateSeries.put("type", "spline");
        series.add(repairsRateSeries);

        HashMap<String, Object> personTrappedRateSeries = new HashMap<>();
        personTrappedRateSeries.put("data", personTrappedRate);
        personTrappedRateSeries.put("name", "困人(比率)");
        personTrappedRateSeries.put("type", "spline");
        series.add(personTrappedRateSeries);

        resMap.put("series", series);

        return resMap;
    }

    /**
     * 故障统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getFaultDataStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule) {

        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);

        List<HashMap<String, Object>> result = null;
        switch (orderKey) {
            case "repairs":
                result = faultDao.getFaultStatisticsRepairsGroupByElevator(faultStatisticsModule);
                break;
            case "personTrapped":
                result = faultDao.getFaultStatisticsPersonTrappedGroupByElevator(faultStatisticsModule);
                break;
            default:
                result = faultDao.getFaultStatisticsGroupByElevator(faultStatisticsModule);
        }

        ArrayList<String> deptIds = getDeptIds(faultStatisticsModule.getUserId());

        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setPermissionDeptIds(deptIds);
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(faultStatisticsModule.getUserId()));
        searchElevatorModule.setUserId(faultStatisticsModule.getUserId());

        List<Map> elevatorList = bizElevatorDao.searchElevatorList(searchElevatorModule);
        List<String> allElevators = elevatorList.stream()
                .filter(e -> faultStatisticsModule.getVillageId().equals(e.get("villageId")))
                .map(elevator -> (String) elevator.get("v_elevator_name"))
                .collect(Collectors.toList());

        List<String> elevatorCodes = result.stream().filter(item -> ((String) item.get("key")).equals(orderKey)).map(item -> (String) item.get("elevatorName")).collect(Collectors.toList());
        List<String> subElevatorCodes = allElevators.stream().filter(item -> !elevatorCodes.contains(item)).collect(Collectors.toList());
        elevatorCodes.addAll(subElevatorCodes);

        List<Long> totalCount = new ArrayList<>(elevatorCodes.size());
        List<Long> repairsCount = new ArrayList<>(elevatorCodes.size());
        List<Long> personTrappedCount = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> totalRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> repairsRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> personTrappedRate = new ArrayList<>(elevatorCodes.size());

        for (String strCode : elevatorCodes) {
            Optional<HashMap<String, Object>> mapTotal = result.stream().filter(item -> ((String) item.get("key")).equals("total") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapTotal.isPresent() && !mapTotal.isEmpty()) {
                totalCount.add(mapTotal.get().get("number") == null ? 0 : (Long) mapTotal.get().get("number"));
                totalRate.add((BigDecimal) mapTotal.get().get("rate"));
            } else {
                totalCount.add(0L);
                totalRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapRepairs = result.stream().filter(item -> ((String) item.get("key")).equals("repairs") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapRepairs.isPresent() && !mapRepairs.isEmpty()) {
                repairsCount.add(mapRepairs.get().get("number") == null ? 0L : (Long) mapRepairs.get().get("number"));
                repairsRate.add((BigDecimal) mapRepairs.get().get("rate"));
            } else {
                repairsCount.add(0L);
                repairsRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapPersonTrapped = result.stream().filter(item -> ((String) item.get("key")).equals("personTrapped") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapPersonTrapped.isPresent() && !mapPersonTrapped.isEmpty()) {
                personTrappedCount.add(mapPersonTrapped.get().get("number") == null ? 0L : (Long) mapPersonTrapped.get().get("number"));
                personTrappedRate.add((BigDecimal) mapPersonTrapped.get().get("rate"));
            } else {
                personTrappedCount.add(0L);
                personTrappedRate.add(BigDecimal.valueOf(0.0));
            }
        }

        HashMap<String, List> resMap = new HashMap<>();

        resMap.put("categories", elevatorCodes);
        List<HashMap<String, Object>> series = new ArrayList<>();

        HashMap<String, Object> totalSeries = new HashMap<>();
        totalSeries.put("data", totalCount);
        totalSeries.put("name", "全部");
        totalSeries.put("type", "column");
        series.add(totalSeries);

        HashMap<String, Object> repairsSeries = new HashMap<>();
        repairsSeries.put("data", repairsCount);
        repairsSeries.put("name", "急修");
        repairsSeries.put("type", "column");
        series.add(repairsSeries);

        HashMap<String, Object> personTrappedSeries = new HashMap<>();
        personTrappedSeries.put("data", personTrappedCount);
        personTrappedSeries.put("name", "困人");
        personTrappedSeries.put("type", "column");
        series.add(personTrappedSeries);

        HashMap<String, Object> totalRateSeries = new HashMap<>();
        totalRateSeries.put("data", totalRate);
        totalRateSeries.put("name", "全部(比率)");
        totalRateSeries.put("type", "spline");
        series.add(totalRateSeries);

        HashMap<String, Object> repairsRateSeries = new HashMap<>();
        repairsRateSeries.put("data", repairsRate);
        repairsRateSeries.put("name", "急修(比率)");
        repairsRateSeries.put("type", "spline");
        series.add(repairsRateSeries);

        HashMap<String, Object> personTrappedRateSeries = new HashMap<>();
        personTrappedRateSeries.put("data", personTrappedRate);
        personTrappedRateSeries.put("name", "困人(比率)");
        personTrappedRateSeries.put("type", "spline");
        series.add(personTrappedRateSeries);

        resMap.put("series", series);

        return resMap;
    }

    @Override
    public Map<String, List> getFaultDataStatisticsGroupByType(FaultStatisticsModule faultStatisticsModule) {
        // 查询故障分类
        faultStatisticsModule.setElevatorType(1);
        faultStatisticsModule.setEventType("1");
        faultStatisticsModule.setUncivilizedBehaviorFlag(0);
        var faultDefinitionList = faultDefinitionDao.listByFaultTypeAndElevatorCode(faultStatisticsModule.getFaultType(), faultStatisticsModule.getUncivilizedBehaviorFlag(),
                faultStatisticsModule.getElevatorType(), faultStatisticsModule.getEventType());

        List<HashMap<String, Object>> series = new ArrayList<>();
        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);

        List<HashMap<String, Object>> result = null;
        switch (orderKey) {
            case "repairs":
                result = faultDao.getFaultStatisticsRepairsGroupByElevator(faultStatisticsModule);
                break;
            case "personTrapped":
                result = faultDao.getFaultStatisticsPersonTrappedGroupByElevator(faultStatisticsModule);
                break;
            default:
                result = faultDao.getFaultStatisticsGroupByElevator(faultStatisticsModule);
        }

        ArrayList<String> deptIds = getDeptIds(faultStatisticsModule.getUserId());
        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setPermissionDeptIds(deptIds);
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(faultStatisticsModule.getUserId()));
        searchElevatorModule.setUserId(faultStatisticsModule.getUserId());
        List<Map> elevatorList = bizElevatorDao.searchElevatorList(searchElevatorModule);
        List<String> allElevators =new ArrayList<>();
        if(faultStatisticsModule.getVillageId()!=null) {
            allElevators = elevatorList.stream()
                    .filter(e -> faultStatisticsModule.getVillageId().equals(e.get("villageId")))
                    .map(elevator -> (String) elevator.get("v_elevator_name"))
                    .collect(Collectors.toList());
        } else {
            // 查小区
            SearchVillaListModule searchVillaListModule = new SearchVillaListModule();
            searchVillaListModule.setPermissionDeptIds(deptIds);
            searchVillaListModule.setAdminFlag(bizUserService.isAdmin(faultStatisticsModule.getUserId()));
            searchVillaListModule.setUserId(faultStatisticsModule.getUserId());
            List<Map> villageList = bizVillageDao.searchVillageList(searchVillaListModule);
            allElevators = villageList.stream().map(village -> (String) village.get("v_village_name")).collect(Collectors.toList());
        }

        List<String> elevatorCodes = result.stream().filter(item -> ((String) item.get("key")).equals(orderKey)).map(item -> (String) item.get("elevatorName")).collect(Collectors.toList());
        List<String> subElevatorCodes = allElevators.stream().filter(item -> !elevatorCodes.contains(item)).collect(Collectors.toList());
        elevatorCodes.addAll(subElevatorCodes);
        List<String> elevator = result.stream().filter(item -> ((String) item.get("key")).equals(orderKey)).map(item -> (String) item.get("vElevatorCode")).collect(Collectors.toList());
        faultStatisticsModule.setElevator(elevator);
        List<FaultResponseModule> faultList = bizFaultDao.getFultList(faultStatisticsModule);
        for (TblFaultDefinition0902 faultDefinition : faultDefinitionList) {
            HashMap<String, Object> seriesMap = new HashMap<>();
            List<Integer> listNum = new ArrayList<>();
            // 电梯循环
            for (String code : elevator) {
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
            seriesMap.put("type", "column");
            series.add(seriesMap);
        }

        HashMap<String, List> resMap = new HashMap<>();

        resMap.put("categories", elevatorCodes);

        resMap.put("series", series);

        return resMap;
    }

    /**
     * 城桥大屏，项目比率
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public HashMap<String, HashMap<String, Object>> getVillageCountRateCQ(SearchFaultModule searchFaultModule) {
        HashMap<String, HashMap<String, Object>> responseResult = new HashMap<>();
        List<String> elevator = wuyeFaultDao.getElevator(searchFaultModule);
        searchFaultModule.setElevatorCode(elevator);
        // 困人数（仪电工单为准）
        HashMap<String, Object> peopleTrappedCount = eventService.getPeopleTrappedCount(searchFaultModule);

        // 故障
        HashMap<String, Object> faultOrderByConfirmOrCompletedCount = eventService.getFaultOrderByConfirmOrCompletedTotal(searchFaultModule);

        // 不文明行为
        HashMap<String, Object> uncivilizedBehaviorCount = eventService.getUncivilizedBehaviorTotal(searchFaultModule);

        // 电动车入梯
        HashMap<String, Object> electronBikeCount = eventService.getElectronBikeTotal(searchFaultModule);

        HashMap<String, Object> peopleTrappedCountMap = new HashMap<>();
        HashMap<String, Object> uncivilizedBehaviorCountMap = new HashMap<>();
        HashMap<String, Object> faultOrderByConfirmOrCompletedMap = new HashMap<>();
        HashMap<String, Object> electronBikeCountMap = new HashMap<>();
        HashMap<String, Object> faultPerMillionMap = new HashMap<>();

        peopleTrappedCountMap.put("count", peopleTrappedCount.get("number"));
        peopleTrappedCountMap.put("key", "困人数");
        faultOrderByConfirmOrCompletedMap.put("count", faultOrderByConfirmOrCompletedCount.get("number"));
        faultOrderByConfirmOrCompletedMap.put("key", "故障报警数");
        uncivilizedBehaviorCountMap.put("count", uncivilizedBehaviorCount.get("number"));
        uncivilizedBehaviorCountMap.put("key", "不文明行为数");
        electronBikeCountMap.put("count", electronBikeCount.get("number"));
        electronBikeCountMap.put("key", "电瓶车入梯数");
        faultPerMillionMap.put("key", "每百万次运行困人次");

        DecimalFormat df = new DecimalFormat("0.0000");

        Integer _peopleTrappedCount = Integer.parseInt(((BigDecimal) peopleTrappedCount.get("total")).toString());
        if (_peopleTrappedCount == 0) {
            peopleTrappedCountMap.put("rate", 0);
            faultPerMillionMap.put("count", 0);
        } else {
            peopleTrappedCountMap.put("rate", df.format(Float.parseFloat(((Long) peopleTrappedCount.get("number")).toString()) / _peopleTrappedCount));
            faultPerMillionMap.put("count", df.format(Float.parseFloat(((Long) peopleTrappedCount.get("number")).toString()) / _peopleTrappedCount * 1000000));
        }

        Integer _faultOrderByConfirmOrCompletedCount = Integer.parseInt(((BigDecimal) faultOrderByConfirmOrCompletedCount.get("total")).toString());
        if (_faultOrderByConfirmOrCompletedCount == 0) {
            faultOrderByConfirmOrCompletedMap.put("rate", 0);
        } else {
            faultOrderByConfirmOrCompletedMap.put("rate", df.format((Float.parseFloat(((Long) faultOrderByConfirmOrCompletedCount.get("number")).toString()) / _faultOrderByConfirmOrCompletedCount)));
        }

        Integer _uncivilizedBehaviorCount = Integer.parseInt(((BigDecimal) uncivilizedBehaviorCount.get("total")).toString());
        if (_uncivilizedBehaviorCount == 0) {
            uncivilizedBehaviorCountMap.put("rate", 0);
        } else {
            uncivilizedBehaviorCountMap.put("rate", df.format(Float.parseFloat(((Long) uncivilizedBehaviorCount.get("number")).toString()) / _uncivilizedBehaviorCount));
        }

        Integer _electronBikeCount = Integer.parseInt(((BigDecimal) electronBikeCount.get("total")).toString());
        if (_electronBikeCount == 0) {
            electronBikeCountMap.put("rate", 0);
        } else {
            electronBikeCountMap.put("rate", df.format(Float.parseFloat(((Long) electronBikeCount.get("number")).toString()) / _electronBikeCount));
        }

        responseResult.put("peopleTrappedCount", peopleTrappedCountMap);
        responseResult.put("uncivilizedBehaviorCount", uncivilizedBehaviorCountMap);
        responseResult.put("faultOrderByConfirmOrCompleted", faultOrderByConfirmOrCompletedMap);
        responseResult.put("electronBikeCount", electronBikeCountMap);
        responseResult.put("faultPerMillion", faultPerMillionMap);

        return responseResult;
    }

    /**
     * 项目比率
     *
     * @param searchFaultModule
     * @return
     */
    @Override
    public HashMap<String, HashMap<String, Object>> getVillageCountRate(SearchFaultModule searchFaultModule) {
        HashMap<String, HashMap<String, Object>> responseResult = new HashMap<>();

//        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
//        searchElevatorModule.setUserId(searchFaultModule.getUserId());
//        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(searchFaultModule.getUserId()));
//        Integer elevatorCount = elevatorService.getElevatorCount(searchElevatorModule);
//
//        if(elevatorCount == null)
//            elevatorCount = 0;
        List<String> elevator = wuyeFaultDao.getElevator(searchFaultModule);
        searchFaultModule.setElevatorCode(elevator);
        // 保养
        Integer totalMaintenanceCount = maintenanceService.getMaintenanceCount(searchFaultModule);
        // 超期维保
        Integer totalOverdueMaintenanceCount = maintenanceService.getOverdueMaintenanceCount(searchFaultModule);
        // 困人数（仪电工单为准）
        HashMap<String, Object> peopleTrappedCount = eventService.getPeopleTrappedCount(searchFaultModule);
        // 急修
        HashMap<String, Object> faultOrderByConfirmOrCompletedCount = eventService.getFaultOrderByConfirmOrCompletedTotal(searchFaultModule);
        // 反复阻挡门
        HashMap<String, Object> blockDoorCount = eventService.getBlockDoorTotal(searchFaultModule);
        // 不文明行为
        HashMap<String, Object> uncivilizedBehaviorCount = eventService.getUncivilizedBehaviorTotal(searchFaultModule);
        // 电动车入梯
        HashMap<String, Object> electronBikeCount = eventService.getElectronBikeTotal(searchFaultModule);

        HashMap<String, Object> totalMaintenanceCountMap = new HashMap<>();
        HashMap<String, Object> peopleTrappedCountMap = new HashMap<>();
        HashMap<String, Object> blockDoorCountMap = new HashMap<>();
        HashMap<String, Object> uncivilizedBehaviorCountMap = new HashMap<>();
        HashMap<String, Object> faultOrderByConfirmOrCompletedMap = new HashMap<>();
        HashMap<String, Object> electronBikeMap = new HashMap<>();


        totalMaintenanceCountMap.put("count", totalMaintenanceCount - totalOverdueMaintenanceCount);
        totalMaintenanceCountMap.put("key", "保养数");
        peopleTrappedCountMap.put("count", peopleTrappedCount.get("number"));
        peopleTrappedCountMap.put("key", "困人数");
        faultOrderByConfirmOrCompletedMap.put("count", faultOrderByConfirmOrCompletedCount.get("number"));
        faultOrderByConfirmOrCompletedMap.put("key", "急修数");
        blockDoorCountMap.put("count", blockDoorCount.get("number"));
        blockDoorCountMap.put("key", "反复阻挡门");
        uncivilizedBehaviorCountMap.put("count", uncivilizedBehaviorCount.get("number"));
        uncivilizedBehaviorCountMap.put("key", "不文明行为");
        electronBikeMap.put("count", electronBikeCount.get("number"));
        electronBikeMap.put("key", "电动车乘梯");

        DecimalFormat df = new DecimalFormat("0.0000");

        if (totalMaintenanceCount == 0) {
            totalMaintenanceCountMap.put("rate", 0);
        } else {
            totalMaintenanceCountMap.put("rate", df.format((float) (totalMaintenanceCount - totalOverdueMaintenanceCount) / totalMaintenanceCount * 100));
        }

        Integer _blockDoorCount = Integer.parseInt(((BigDecimal) blockDoorCount.get("total")).toString());
        if (_blockDoorCount == 0) {
            blockDoorCountMap.put("rate", 0);
        } else {
            blockDoorCountMap.put("rate", df.format(Float.parseFloat(((Long) blockDoorCount.get("number")).toString()) / _blockDoorCount * 100));
        }

        Integer _peopleTrappedCount = Integer.parseInt(((BigDecimal) peopleTrappedCount.get("total")).toString());
        if (_peopleTrappedCount == 0) {
            peopleTrappedCountMap.put("rate", 0);
        } else {
            peopleTrappedCountMap.put("rate", df.format(Float.parseFloat(((Long) peopleTrappedCount.get("number")).toString()) / _peopleTrappedCount * 100));
        }

        Integer _faultOrderByConfirmOrCompletedCount = Integer.parseInt(((BigDecimal) faultOrderByConfirmOrCompletedCount.get("total")).toString());
        if (_faultOrderByConfirmOrCompletedCount == 0) {
            faultOrderByConfirmOrCompletedMap.put("rate", 0);
        } else {
            faultOrderByConfirmOrCompletedMap.put("rate", df.format((Float.parseFloat(((Long) faultOrderByConfirmOrCompletedCount.get("number")).toString()) / _faultOrderByConfirmOrCompletedCount) * 100));
        }

        Integer _uncivilizedBehaviorCount = Integer.parseInt(((BigDecimal) uncivilizedBehaviorCount.get("total")).toString());
        if (_uncivilizedBehaviorCount == 0) {
            uncivilizedBehaviorCountMap.put("rate", 0);
        } else {
            uncivilizedBehaviorCountMap.put("rate", df.format(Float.parseFloat(((Long) uncivilizedBehaviorCount.get("number")).toString()) / _uncivilizedBehaviorCount * 100));
        }

        Integer _electronBikeCount = Integer.parseInt(((BigDecimal) electronBikeCount.get("total")).toString());
        if (_electronBikeCount == 0) {
            electronBikeMap.put("rate", 0);
        } else {
            electronBikeMap.put("rate", df.format(Float.parseFloat(((Long) electronBikeCount.get("number")).toString()) / _electronBikeCount * 100));
        }

        responseResult.put("totalMaintenanceCount", totalMaintenanceCountMap);
        responseResult.put("peopleTrappedCount", peopleTrappedCountMap);
        responseResult.put("blockDoorCount", blockDoorCountMap);
        responseResult.put("uncivilizedBehaviorCount", uncivilizedBehaviorCountMap);
        responseResult.put("faultOrderByConfirmOrCompleted", faultOrderByConfirmOrCompletedMap);
        responseResult.put("electronBikeCount", electronBikeMap);

        return responseResult;
    }

    /**
     * 急修趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getRepairTrend(FaultStatisticsModule faultStatisticsModule) {
        List<HashMap<String, Object>> trends = faultDao.getRepairTrend(faultStatisticsModule);
        HashMap<String, List> resMap = new HashMap<>();

        List<String> times = getDateTimesBetweenTwoDate(faultStatisticsModule.getStartTime(), faultStatisticsModule.getEndTime(), faultStatisticsModule.getTimeFlag());
        resMap.put("categories", times);

        List<Long> totalCount = new ArrayList<>();
        List<Long> trappedCount = new ArrayList<>();
        List<HashMap<String, Object>> series = new ArrayList<>();

        for (String time : times) {
            Long total = trends.stream().filter(item -> !((String) item.get("failureCode")).equals("09") && ((String) item.get("occurTime")).equals(time)).map(item -> (Long) item.get("number")).mapToLong(Long::longValue).sum();
            totalCount.add(total == null ? 0 : total);

            Long trapped = trends.stream().filter(item -> ((String) item.get("failureCode")).equals("09") && ((String) item.get("occurTime")).equals(time)).map(item -> (Long) item.get("number")).mapToLong(Long::longValue).sum();
            trappedCount.add(trapped == null ? 0 : trapped);
        }

        HashMap<String, Object> totalSeries = new HashMap<>();
        totalSeries.put("data", totalCount);
        totalSeries.put("name", "急修工单趋势");
        totalSeries.put("type", "column");

        series.add(totalSeries);

        HashMap<String, Object> trappedSeries = new HashMap<>();
        trappedSeries.put("data", trappedCount);
        trappedSeries.put("name", "困人工单趋势");
        trappedSeries.put("type", "spline");
        series.add(trappedSeries);

        resMap.put("series", series);
        return resMap;
    }


    /**
     * 维保趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getMaintenanceTrend(FaultStatisticsModule faultStatisticsModule) {
        List<HashMap<String, Object>> trends = faultDao.getMaintenanceTrend(faultStatisticsModule);
        HashMap<String, List> resMap = new HashMap<>();

        List<HashMap<String, Object>> series = new ArrayList<>();
        HashMap<String, Object> totalSeries = new HashMap<>();

        List<String> times = getDateTimesBetweenTwoDate(faultStatisticsModule.getStartTime(), faultStatisticsModule.getEndTime(), faultStatisticsModule.getTimeFlag());

        resMap.put("categories", times);

        totalSeries.put("data", trends.stream().map(item -> (Long) item.get("number")).collect(Collectors.toList()));
        totalSeries.put("name", "维保工单趋势");
        totalSeries.put("type", "spline");
        series.add(totalSeries);

        resMap.put("series", series);
        return resMap;
    }


    /**
     * 获取两个日期之间的时间列表
     *
     * @param selectStartTime 开始时间
     * @param selectEndTime   结束时间
     */
    public static List<String> getDateTimesBetweenTwoDate(String selectStartTime, String selectEndTime,
                                                          String timeFlag) {

        List<String> resultDateTimeList = new ArrayList<>();

        DateTime start = DateUtil.parse(selectStartTime);
        DateTime end = DateUtil.parse(selectEndTime);

        String beginDate;
        //把开始时间加入集合
        switch (timeFlag) {
            case "11" -> {
                int i = start.weekOfYear();
                beginDate = DateUtil.format(start, "yyyy") + "-" + i + "(周)";
            }
            case "22" -> {
                beginDate = DateUtil.format(start, "yyyy-MM(月)");
            }
            default -> {
                beginDate = DateUtil.format(start, "yyyy-MM-dd");
            }
        }

        resultDateTimeList.add(beginDate);

        while (true) {

            switch (timeFlag) {
                case "11" -> {
                    start = DateUtil.offsetWeek(start, 1);
                    int i = start.weekOfYear();
                    resultDateTimeList.add(DateUtil.format(start, "yyyy") + "-" + i + "(周)");
                }
                case "22" -> {
                    start = DateUtil.offsetMonth(start, 1);
                    resultDateTimeList.add(DateUtil.format(start, "yyyy-MM(月)"));
                }
                default -> {
                    start = DateUtil.offsetDay(start, 1);
                    resultDateTimeList.add(DateUtil.format(start, "yyyy-MM-dd"));
                }
            }

            if (start.after(end)) {
                break;
            }

        }

        return resultDateTimeList;
    }

    /**
     * 物联报警趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getFaultTrend(FaultStatisticsModule faultStatisticsModule) {
        List<HashMap<String, Object>> trends = faultDao.getFaultTrend(faultStatisticsModule);

        HashMap<String, List> resMap = new HashMap<>();

        List<HashMap<String, Object>> series = new ArrayList<>();
        HashMap<String, Object> totalSeries = new HashMap<>();

        List<String> times = trends.stream().map(item -> (String) item.get("occurTime")).collect(Collectors.toList());
        resMap.put("categories", times);

        totalSeries.put("data", trends.stream().map(item -> (Long) item.get("number")).collect(Collectors.toList()));
        totalSeries.put("name", "物联报警趋势");
        totalSeries.put("type", "spline");
        series.add(totalSeries);

        resMap.put("series", series);
        return resMap;
    }


    private List<HashMap<String, Object>> getVillageInfo(SearchFaultModule searchFaultModule) {
        JSONObject userDept = bizDeptService.getUserDept(searchFaultModule.getUserId());
        String dept_id = userDept.getString("dept_id");
        // 1. 找到当前用户所属部门编号 查询 所有子部门

        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + dept_id;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);
        if (results == null) {
            results = new ArrayList<>();
            recursion(dept_id, results);
            if (!results.contains(dept_id))
                results.add(dept_id);
            redisTemplate.opsForValue().set(key, results);

        }
        results.add(dept_id);

        if (searchFaultModule.getiStatus() == null)
            searchFaultModule.setiStatus(1);

        //根据部门id获取所有小区
        //根据电梯数量排序
        if (1 == searchFaultModule.getiStatus()) {
            return wuyeFaultDao.getVillageInfoByDeptIdsAndElevatorCount(results);
        }

        return null;
    }

    /**
     * 递归查询 下级部门的用户
     *
     * @param dept_id
     * @param strings
     */
    private void recursion(String dept_id, List<String> strings) {

        if (null != dept_id) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(dept_id);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }

    private ArrayList<String> getDeptIds(String userId) {
        JSONObject userDept = bizDeptService.getUserDept(userId);
        String dept_id = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + dept_id;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(dept_id, results);
            if (!results.contains(dept_id))
                results.add(dept_id);
            redisTemplate.opsForValue().set(key, results);
        }

        return (ArrayList<String>) results;
    }
}
