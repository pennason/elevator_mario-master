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
import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.BizVillageDao;
import com.shmashine.api.dao.TblFaultDefinitionDao;
import com.shmashine.api.dao.WuyeFaultDao;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.ElevatorService;
import com.shmashine.api.service.wuye.EventService;
import com.shmashine.api.service.wuye.FaultService;
import com.shmashine.api.service.wuye.MaintenanceService;
import com.shmashine.common.constants.RedisConstants;

@Service
public class FaultServiceImpl implements FaultService {

    @Autowired
    private EventService eventService;

    @Autowired
    private WuyeFaultDao baseMapper;

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private BizDeptService bizDeptService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private TblFaultDefinitionDao faultDefinitionDao;

    @Autowired
    private BizVillageDao bizVillageDao;

    @Autowired
    private BizElevatorDao bizElevatorDao;

    @Override
    public HashMap<String, Integer> getFaultCount(SearchFaultModule searchFaultModule) {
        return null;
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

        //困人数（仪电工单为准）
        List<HashMap<String, Object>> peopleTrappedResult = eventService.getPeopleTrappedCountByVillage(searchFaultModule);
        HashMap<String, Integer> peopleTrappedCount = new HashMap<>();
        peopleTrappedResult.stream().forEach(it -> peopleTrappedCount.put((String) it.get("v_village_id"), Integer.valueOf(it.get("number").toString())));

        //电瓶车入梯
        List<HashMap<String, Object>> electroMobileFaultCount = baseMapper.getElectroMobileFaultCount(searchFaultModule);
        HashMap<String, Integer> electroMobileCount = new HashMap<>();
        electroMobileFaultCount.stream().forEach(it -> electroMobileCount.put((String) it.get("v_village_id"), Integer.valueOf(it.get("number").toString())));

        //获取所有小区列表
        List<HashMap<String, Object>> villageInfos = getVillageInfo(searchFaultModule);

        villageInfos.stream().forEach(it -> {
            Integer _repairOrder = repairOrder.get(it.get("v_village_id")) == null ? 0 : repairOrder.get(it.get("v_village_id"));
            Integer _peopleTrappedCount = peopleTrappedCount.get(it.get("v_village_id")) == null ? 0 : peopleTrappedCount.get(it.get("v_village_id"));
            Integer _electroMobileCount = electroMobileCount.get(it.get("v_village_id")) == null ? 0 : electroMobileCount.get(it.get("v_village_id"));
            it.put("repairOrder", _repairOrder);
            it.put("peopleTrappedCount", _peopleTrappedCount);
            it.put("electroMobileCount", _electroMobileCount);
        });

        return villageInfos;
    }

    @Override
    public HashMap<String, Object> getFaultChartByTime(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public HashMap<String, Object> getTodayPeopleTrappedElevator(SearchElevatorModule searchElevatorModule) {
        return null;
    }

    @Override
    public HashMap<String, Object> getTodayFaultElevator(SearchElevatorModule searchElevatorModule) {
        return null;
    }

    @Override
    public HashMap<String, Object> getThisMomentFault(String elevatorCode) {
        return null;
    }

    @Override
    public HashMap<String, Integer> getHistoryRecordCount(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public Integer queryFaultNumber(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public Integer queryTrappedPeopleNumber(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getFaultFileById(String faultId) {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, Object>> getVillageCountRate(SearchFaultModule searchFaultModule) {
        HashMap<String, HashMap<String, Object>> responseResult = new HashMap<>();

        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setUserId(searchFaultModule.getUserId());
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(searchFaultModule.getUserId()));
        Integer elevatorCount = elevatorService.getElevatorCount(searchElevatorModule);

        if (elevatorCount == null)
            elevatorCount = 0;

        // 保养
        Integer totalMaintenanceCount = maintenanceService.getMaintenanceCount(searchFaultModule);
        // 超期维保
        Integer totalOverdueMaintenanceCount = maintenanceService.getOverdueMaintenanceCount(searchFaultModule);
        // 困人数（仪电工单为准）
        HashMap<String, Object> peopleTrappedCount = eventService.getPeopleTrappedCount(searchFaultModule);
        // 急修
        HashMap<String, Object> faultOrderByConfirmOrCompletedCount = eventService.getFaultOrderByConfirmOrCompletedTotal(searchFaultModule);
        // 电动车入梯数（平台故障统计）
        searchFaultModule.setiUncivilizedBehaviorFlag(1);
        searchFaultModule.setiFaultType("37");
        Integer electroMobileFaultCount = baseMapper.getFaultCount(searchFaultModule);

        searchFaultModule.setDistinct(true);
        Integer electroMobileFaultDistinctCount = baseMapper.getFaultCount(searchFaultModule);

        HashMap<String, Object> totalMaintenanceCountMap = new HashMap<>();
        HashMap<String, Object> peopleTrappedCountMap = new HashMap<>();
        HashMap<String, Object> electroMobileFaultCountMap = new HashMap<>();
        HashMap<String, Object> faultOrderByConfirmOrCompletedMap = new HashMap<>();

        totalMaintenanceCountMap.put("count", totalMaintenanceCount - totalOverdueMaintenanceCount);
        totalMaintenanceCountMap.put("key", "保养数");
        peopleTrappedCountMap.put("count", peopleTrappedCount.get("number"));
        peopleTrappedCountMap.put("key", "困人数");
        faultOrderByConfirmOrCompletedMap.put("count", faultOrderByConfirmOrCompletedCount.get("number"));
        faultOrderByConfirmOrCompletedMap.put("key", "急修数");
        electroMobileFaultCountMap.put("count", electroMobileFaultCount);
        electroMobileFaultCountMap.put("key", "电动车入梯数");

        DecimalFormat df = new DecimalFormat("0.0000");
        if (totalMaintenanceCount == 0) {
            totalMaintenanceCountMap.put("rate", 0);
        } else {
            totalMaintenanceCountMap.put("rate", df.format((float) (totalMaintenanceCount - totalOverdueMaintenanceCount) / totalMaintenanceCount * 100));
        }

        if (elevatorCount == 0) {
            electroMobileFaultCountMap.put("rate", 0);
        } else {
            electroMobileFaultCountMap.put("rate", df.format((float) electroMobileFaultDistinctCount / elevatorCount * 100));
        }

        Integer _peopleTrappedCount = Integer.parseInt(((BigDecimal) peopleTrappedCount.get("total")).toString());
        if (_peopleTrappedCount == 0) {
            peopleTrappedCountMap.put("rate", 0);
        } else {
            peopleTrappedCountMap.put("rate", df.format(Float.parseFloat(((BigDecimal) peopleTrappedCount.get("number")).toString()) / _peopleTrappedCount * 100));
        }

        Integer _faultOrderByConfirmOrCompletedCount = Integer.parseInt(((BigDecimal) faultOrderByConfirmOrCompletedCount.get("total")).toString());
        if (_faultOrderByConfirmOrCompletedCount == 0) {
            faultOrderByConfirmOrCompletedMap.put("rate", 0);
        } else {
            faultOrderByConfirmOrCompletedMap.put("rate", df.format((Float.parseFloat(((BigDecimal) faultOrderByConfirmOrCompletedCount.get("number")).toString()) / _faultOrderByConfirmOrCompletedCount) * 100));
        }

        responseResult.put("totalMaintenanceCount", totalMaintenanceCountMap);
        responseResult.put("peopleTrappedCount", peopleTrappedCountMap);
        responseResult.put("electroMobileFaultCount", electroMobileFaultCountMap);
        responseResult.put("faultOrderByConfirmOrCompleted", faultOrderByConfirmOrCompletedMap);

        return responseResult;
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
                result = baseMapper.getFaultStatisticsRepairs(faultStatisticsModule);
                break;
            case "personTrapped":
                result = baseMapper.getFaultStatisticsPersonTrapped(faultStatisticsModule);
                break;
            default:
                result = baseMapper.getFaultStatistics(faultStatisticsModule);
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
     * 故障统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getFaultStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule) {

        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);

        List<HashMap<String, Object>> result = null;
        switch (orderKey) {
            case "repairs":
                result = baseMapper.getFaultStatisticsRepairsGroupByElevator(faultStatisticsModule);
                break;
            case "personTrapped":
                result = baseMapper.getFaultStatisticsPersonTrappedGroupByElevator(faultStatisticsModule);
                break;
            default:
                result = baseMapper.getFaultStatisticsGroupByElevator(faultStatisticsModule);
        }

        ArrayList<String> deptIds = getDeptIds(faultStatisticsModule.getUserId());

        SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
        searchElevatorModule.setPermissionDeptIds(deptIds);
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(faultStatisticsModule.getUserId()));
        searchElevatorModule.setUserId(faultStatisticsModule.getUserId());

        List<Map> elevatorList = bizElevatorDao.searchElevatorList(searchElevatorModule);
        List<String> allElevators = elevatorList.stream().map(elevator -> (String) elevator.get("v_elevator_name")).collect(Collectors.toList());

        List<String> elevatorCodes = result.stream().filter(item -> ((String) item.get("key")).equals(orderKey)).map(item -> (String) item.get("elevatorName")).collect(Collectors.toList());
        List<String> subElevatorCodes = allElevators.stream().filter(item -> !elevatorCodes.contains(item)).collect(Collectors.toList());
        elevatorCodes.addAll(subElevatorCodes);

        List<Long> totalCount = new ArrayList<>(elevatorCodes.size());
        List<Long> repairsCount = new ArrayList<>(elevatorCodes.size());
        List<Long> personTrappedCount = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> totalRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> repairsRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> personTrappedRate = new ArrayList<>(elevatorCodes.size());

        for (String strName : elevatorCodes) {
            Optional<HashMap<String, Object>> mapTotal = result.stream().filter(item -> ((String) item.get("key")).equals("total") && ((String) item.get("elevatorName")).equals(strName)).findFirst();
            if (mapTotal.isPresent()) {
                totalCount.add((Long) mapTotal.get().get("number"));
                totalRate.add((BigDecimal) mapTotal.get().get("rate"));
            } else {
                totalCount.add(0L);
                totalRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapRepairs = result.stream().filter(item -> ((String) item.get("key")).equals("repairs") && ((String) item.get("elevatorName")).equals(strName)).findFirst();
            if (mapRepairs.isPresent()) {
                repairsCount.add((Long) mapRepairs.get().get("number"));
                repairsRate.add((BigDecimal) mapRepairs.get().get("rate"));
            } else {
                repairsCount.add(0L);
                repairsRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapPersonTrapped = result.stream().filter(item -> ((String) item.get("key")).equals("personTrapped") && ((String) item.get("elevatorName")).equals(strName)).findFirst();
            if (mapPersonTrapped.isPresent()) {
                personTrappedCount.add((Long) mapRepairs.get().get("number"));
                personTrappedRate.add((BigDecimal) mapRepairs.get().get("rate"));
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

    /**
     * 不文明统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getUncivilizedStatistics(FaultStatisticsModule faultStatisticsModule) {
        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);
        List<HashMap<String, Object>> result = null;

        switch (orderKey) {
            case "electricBike":
                result = baseMapper.getUncivilizedStatisticsElectricBike(faultStatisticsModule);
                break;
            case "blockDoor":
                result = baseMapper.getUncivilizedStatisticsBlockDoor(faultStatisticsModule);
                break;
            default:
                result = baseMapper.getUncivilizedStatistics(faultStatisticsModule);
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
        List<Long> blockDoorCount = new ArrayList<>(villageNames.size());
        List<Long> electricBikeCount = new ArrayList<>(villageNames.size());
        List<BigDecimal> totalRate = new ArrayList<>(villageNames.size());
        List<BigDecimal> blockDoorRate = new ArrayList<>(villageNames.size());
        List<BigDecimal> electricBikeRate = new ArrayList<>(villageNames.size());

        for (String strName : villageNames) {
            Optional<HashMap<String, Object>> mapTotal = result.stream().filter(item -> ((String) item.get("key")).equals("total") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapTotal.isPresent()) {
                totalCount.add((Long) mapTotal.get().get("number"));
                totalRate.add((BigDecimal) mapTotal.get().get("rate"));
            } else {
                totalCount.add(0L);
                totalRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapBlockDoor = result.stream().filter(item -> ((String) item.get("key")).equals("blockDoor") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapBlockDoor.isPresent()) {
                blockDoorCount.add((Long) mapBlockDoor.get().get("number"));
                blockDoorRate.add((BigDecimal) mapBlockDoor.get().get("rate"));
            } else {
                blockDoorCount.add(0L);
                blockDoorRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapElectricBike = result.stream().filter(item -> ((String) item.get("key")).equals("electricBike") && ((String) item.get("villageName")).equals(strName)).findFirst();
            if (mapElectricBike.isPresent()) {
                electricBikeCount.add((Long) mapElectricBike.get().get("number"));
                electricBikeRate.add((BigDecimal) mapElectricBike.get().get("rate"));
            } else {
                electricBikeCount.add(0L);
                electricBikeRate.add(BigDecimal.valueOf(0.0));
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

        HashMap<String, Object> blockDoorSeries = new HashMap<>();
        blockDoorSeries.put("data", blockDoorCount);
        blockDoorSeries.put("name", "反复阻挡门");
        blockDoorSeries.put("type", "column");
        series.add(blockDoorSeries);

        HashMap<String, Object> electricBikeSeries = new HashMap<>();
        electricBikeSeries.put("data", electricBikeCount);
        electricBikeSeries.put("name", "电动车入梯");
        electricBikeSeries.put("type", "column");
        series.add(electricBikeSeries);

        HashMap<String, Object> totalRateSeries = new HashMap<>();
        totalRateSeries.put("data", totalRate);
        totalRateSeries.put("name", "全部(比率)");
        totalRateSeries.put("type", "spline");
        series.add(totalRateSeries);

        HashMap<String, Object> blockDoorRateSeries = new HashMap<>();
        blockDoorRateSeries.put("data", blockDoorRate);
        blockDoorRateSeries.put("name", "反复阻挡门(比率)");
        blockDoorRateSeries.put("type", "spline");
        series.add(blockDoorRateSeries);

        HashMap<String, Object> electricBikeRateSeries = new HashMap<>();
        electricBikeRateSeries.put("data", electricBikeRate);
        electricBikeRateSeries.put("name", "电动车入梯(比率)");
        electricBikeRateSeries.put("type", "spline");
        series.add(electricBikeRateSeries);

        resMap.put("series", series);

        return resMap;
    }

    /**
     * 不文明统计,电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getUncivilizedStatisticsGroupByElevator(FaultStatisticsModule faultStatisticsModule) {
        String orderType = faultStatisticsModule.getOrderType() == null ? "asc" : faultStatisticsModule.getOrderType();
        String orderKey = faultStatisticsModule.getOrderBy() == null ? "total" : faultStatisticsModule.getOrderBy();
        faultStatisticsModule.setOrderType(orderType);
        List<HashMap<String, Object>> result = null;

        switch (orderKey) {
            case "electricBike":
                result = baseMapper.getUncivilizedStatisticsElectricBikeGroupByElevator(faultStatisticsModule);
                break;
            case "blockDoor":
                result = baseMapper.getUncivilizedStatisticsBlockDoorGroupByElevator(faultStatisticsModule);
                break;
            default:
                result = baseMapper.getUncivilizedStatisticsGroupByElevator(faultStatisticsModule);
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
        List<Long> blockDoorCount = new ArrayList<>(elevatorCodes.size());
        List<Long> electricBikeCount = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> totalRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> blockDoorRate = new ArrayList<>(elevatorCodes.size());
        List<BigDecimal> electricBikeRate = new ArrayList<>(elevatorCodes.size());

        for (String strCode : elevatorCodes) {
            Optional<HashMap<String, Object>> mapTotal = result.stream().filter(item -> ((String) item.get("key")).equals("total") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapTotal.isPresent()) {
                totalCount.add((Long) mapTotal.get().get("number"));
                totalRate.add((BigDecimal) mapTotal.get().get("rate"));
            } else {
                totalCount.add(0L);
                totalRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapBlockDoor = result.stream().filter(item -> ((String) item.get("key")).equals("blockDoor") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapBlockDoor.isPresent()) {
                blockDoorCount.add((Long) mapBlockDoor.get().get("number"));
                blockDoorRate.add((BigDecimal) mapBlockDoor.get().get("rate"));
            } else {
                blockDoorCount.add(0L);
                blockDoorRate.add(BigDecimal.valueOf(0.0));
            }

            Optional<HashMap<String, Object>> mapElectricBike = result.stream().filter(item -> ((String) item.get("key")).equals("electricBike") && ((String) item.get("elevatorName")).equals(strCode)).findFirst();
            if (mapElectricBike.isPresent()) {
                electricBikeCount.add((Long) mapElectricBike.get().get("number"));
                electricBikeRate.add((BigDecimal) mapElectricBike.get().get("rate"));
            } else {
                electricBikeCount.add(0L);
                electricBikeRate.add(BigDecimal.valueOf(0.0));
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

        HashMap<String, Object> blockDoorSeries = new HashMap<>();
        blockDoorSeries.put("data", blockDoorCount);
        blockDoorSeries.put("name", "反复阻挡门");
        blockDoorSeries.put("type", "column");
        series.add(blockDoorSeries);

        HashMap<String, Object> electricBikeSeries = new HashMap<>();
        electricBikeSeries.put("data", electricBikeCount);
        electricBikeSeries.put("name", "电动车入梯");
        electricBikeSeries.put("type", "column");
        series.add(electricBikeSeries);

        HashMap<String, Object> totalRateSeries = new HashMap<>();
        totalRateSeries.put("data", totalRate);
        totalRateSeries.put("name", "全部(比率)");
        totalRateSeries.put("type", "spline");
        series.add(totalRateSeries);

        HashMap<String, Object> blockDoorRateSeries = new HashMap<>();
        blockDoorRateSeries.put("data", blockDoorRate);
        blockDoorRateSeries.put("name", "反复阻挡门(比率)");
        blockDoorRateSeries.put("type", "spline");
        series.add(blockDoorRateSeries);

        HashMap<String, Object> electricBikeRateSeries = new HashMap<>();
        electricBikeRateSeries.put("data", electricBikeRate);
        electricBikeRateSeries.put("name", "电动车入梯(比率)");
        electricBikeRateSeries.put("type", "spline");
        series.add(electricBikeRateSeries);

        resMap.put("series", series);

        return resMap;
    }

    /**
     * 急修趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @Override
    public Map<String, List> getRepairTrend(FaultStatisticsModule faultStatisticsModule) {
        List<HashMap<String, Object>> trends = baseMapper.getRepairTrend(faultStatisticsModule);
        HashMap<String, List> resMap = new HashMap<>();

        List<String> times = trends.stream().map(item -> (String) item.get("occurTime")).distinct().collect(Collectors.toList());
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
        List<HashMap<String, Object>> trends = baseMapper.getMaintenanceTrend(faultStatisticsModule);
        HashMap<String, List> resMap = new HashMap<>();

        List<HashMap<String, Object>> series = new ArrayList<>();
        HashMap<String, Object> totalSeries = new HashMap<>();

        List<String> times = trends.stream().map(item -> (String) item.get("occurTime")).collect(Collectors.toList());
        resMap.put("categories", times);

        totalSeries.put("data", trends.stream().map(item -> (Long) item.get("number")).collect(Collectors.toList()));
        totalSeries.put("name", "维保工单趋势");
        totalSeries.put("type", "spline");
        series.add(totalSeries);

        resMap.put("series", series);
        return resMap;
    }

    @Override
    public Map<String, List> getUncivilizedTrend(FaultStatisticsModule faultStatisticsModule) {
        List<HashMap<String, Object>> trends = baseMapper.getUncivilizedTrend(faultStatisticsModule);

        HashMap<String, List> resMap = new HashMap<>();
        List<Long> electronBikeCount = new ArrayList<>();
        List<Long> doorCount = new ArrayList<>();
        List<HashMap<String, Object>> series = new ArrayList<>();
        HashMap<String, Object> electronBikeSeries = new HashMap<>();
        HashMap<String, Object> doorSeries = new HashMap<>();

        //获取两个日期之间的时间列表
        List<String> times = getDateTimesBetweenTwoDate(faultStatisticsModule.getStartTime(), faultStatisticsModule.getEndTime(), faultStatisticsModule.getTimeFlag());

        resMap.put("categories", times);

        for (String time : times) {
            Optional<Long> _electronBikeCount = trends.stream().filter(item -> ((String) item.get("faultType")).equals("37") && ((String) item.get("occurTime")).equals(time)).map(item -> (Long) item.get("number")).findFirst();
            Optional<Long> _doorCount = trends.stream().filter(item -> ((String) item.get("faultType")).equals("20") && ((String) item.get("occurTime")).equals(time)).map(item -> (Long) item.get("number")).findFirst();

            if (_electronBikeCount.isPresent()) {
                electronBikeCount.add(_electronBikeCount.get());
            } else {
                electronBikeCount.add(0L);
            }

            if (_doorCount.isPresent()) {
                doorCount.add(_doorCount.get());
            } else {
                doorCount.add(0L);
            }
        }
//        electronBikeCount = trends.stream().filter(item -> ((String)item.get("faultType")).equals("37")).map(item ->(Long)item.get("number")).collect(Collectors.toList());
//        doorCount = trends.stream().filter(item -> ((String)item.get("faultType")).equals("20")).map(item ->(Long)item.get("number")).collect(Collectors.toList());

        electronBikeSeries.put("data", electronBikeCount);
        electronBikeSeries.put("name", "电动车入梯趋势");
        electronBikeSeries.put("type", "column");
        series.add(electronBikeSeries);

        doorSeries.put("data", doorCount);
        doorSeries.put("name", "反复阻挡门趋势");
        doorSeries.put("type", "spline");
        series.add(doorSeries);

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
            return baseMapper.getVillageInfoByDeptIdsAndElevatorCount(results);
        }

        //根据当日困人数判断
        if (2 == searchFaultModule.getiStatus()) {
            return baseMapper.getVillageInfoByDeptIdsAndPeopleTrappedCount(results);
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
}
