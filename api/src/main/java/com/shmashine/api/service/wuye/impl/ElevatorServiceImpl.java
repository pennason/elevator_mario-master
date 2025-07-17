package com.shmashine.api.service.wuye.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.dao.WuyeElevatorDao;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.ElevatorService;
import com.shmashine.common.utils.DateUtils;

/**
 * 电梯服务实现类
 */

@Service
public class ElevatorServiceImpl implements ElevatorService {

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private WuyeElevatorDao baseMapper;

    @Override
    public Integer getElevatorCount(SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(searchElevatorModule.userId));
        return baseMapper.getAllElevatorCount(searchElevatorModule);
    }

    @Override
    public List<HashMap<String, Object>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(searchElevatorModule.userId));
        return baseMapper.getElevatorCountByVillage(searchElevatorModule);
    }

    @Override
    public HashMap<String, Object> getElevatorBaseInfo(String elevatorCode) {
        return null;
    }

    @Override
    public HashMap<String, Object> searchVillageMap(String userId) {
        return null;
    }

    @Override
    public JSONObject getElevatorHeatMap(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public HashMap<String, String> getElevatorSafetyAdministratorAndMaintainer(String registerNumber) {
        return null;
    }

    @Override
    public JSONObject getElevatorVideoUrl(String elevatorId) {
        return null;
    }

    @Override
    public Map<String, Integer> getHealthRadarChart(String elevatorId) {
        return null;
    }

    @Override
    public List<String> getBuildId(String userId) {
        return null;
    }

    @Override
    public Object searchElevatorByBuildId(String buildId) {
        return null;
    }

    @Override
    public Map<String, Object> getElevatorRunDataStatistics(FaultStatisticsModule faultStatisticsModule) {

        //构建时间对象
        ArrayList<String> villageName = new ArrayList<>();
        ArrayList<HashMap> series = new ArrayList<>();

        List<Map<Object, Object>> res = baseMapper.getElevatorRunDataStatistics(faultStatisticsModule);

        if (!StringUtils.hasText(faultStatisticsModule.getEventType())) {

            ArrayList<Object> runCount = new ArrayList<>();
            ArrayList<Object> doorCount = new ArrayList<>();
            ArrayList<Object> bendCount = new ArrayList<>();
            ArrayList<Object> triggerCount = new ArrayList<>();
            ArrayList<Object> distanceCount = new ArrayList<>();

            res.stream().forEach(it -> {
                villageName.add((String) it.get("villageName"));
                runCount.add(it.get("runCount"));
                doorCount.add(it.get("doorCount"));
                bendCount.add(it.get("bendCount"));
                triggerCount.add(it.get("triggerCount"));
                distanceCount.add(it.get("distanceCount"));
            });

            HashMap<String, Object> runCountMap = new HashMap<>();
            runCountMap.put("data", runCount);
            runCountMap.put("name", "运行次数");
            runCountMap.put("type", "column");
            HashMap<String, Object> doorCountMap = new HashMap<>();
            doorCountMap.put("data", doorCount);
            doorCountMap.put("name", "开关门次数");
            doorCountMap.put("type", "column");
            HashMap<String, Object> bendCountMap = new HashMap<>();
            bendCountMap.put("data", bendCount);
            bendCountMap.put("name", "弯折次数");
            bendCountMap.put("type", "column");
            HashMap<String, Object> triggerCountMap = new HashMap<>();
            triggerCountMap.put("data", triggerCount);
            triggerCountMap.put("name", "平层次数");
            triggerCountMap.put("type", "column");
            HashMap<String, Object> distanceCountMap = new HashMap<>();
            distanceCountMap.put("data", distanceCount);
            distanceCountMap.put("name", "运行距离");
            distanceCountMap.put("type", "column");

            series.add(runCountMap);
            series.add(doorCountMap);
            series.add(bendCountMap);
            series.add(triggerCountMap);
            series.add(distanceCountMap);


        } else {

            ArrayList<BigDecimal> count = new ArrayList<>();

            res.stream().forEach(it -> {
                villageName.add((String) it.get("villageName"));
                count.add((BigDecimal) it.get(faultStatisticsModule.getEventType()));
            });

            HashMap<String, Object> countMap = new HashMap<>();
            countMap.put("data", count);
            countMap.put("name", faultStatisticsModule.getEventType());
            series.add(countMap);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("series", series);
        result.put("categories", villageName);

        return result;
    }

    @Override
    public Map<String, Object> getElevatorRunDataStatisticsGroupByElevator(
            FaultStatisticsModule faultStatisticsModule) {

        //构建时间对象
        ArrayList<String> elevatorCode = new ArrayList<>();
        ArrayList<HashMap> series = new ArrayList<>();

        List<Map<Object, Object>> res = baseMapper.getElevatorRunDataStatisticsGroupByElevator(faultStatisticsModule);

        if (!StringUtils.hasText(faultStatisticsModule.getEventType())) {

            ArrayList<Object> runCount = new ArrayList<>();
            ArrayList<Object> doorCount = new ArrayList<>();
            ArrayList<Object> bendCount = new ArrayList<>();
            ArrayList<Object> triggerCount = new ArrayList<>();
            ArrayList<Object> distanceCount = new ArrayList<>();

            res.stream().forEach(it -> {
                elevatorCode.add((String) it.get("elevatorName"));
                runCount.add(it.get("runCount"));
                doorCount.add(it.get("doorCount"));
                bendCount.add(it.get("bendCount"));
                triggerCount.add(it.get("triggerCount"));
                distanceCount.add(it.get("distanceCount"));
            });

            HashMap<String, Object> runCountMap = new HashMap<>();
            runCountMap.put("data", runCount);
            runCountMap.put("name", "运行次数");
            runCountMap.put("type", "column");
            HashMap<String, Object> doorCountMap = new HashMap<>();
            doorCountMap.put("data", doorCount);
            doorCountMap.put("name", "开关门次数");
            doorCountMap.put("type", "column");
            HashMap<String, Object> bendCountMap = new HashMap<>();
            bendCountMap.put("data", bendCount);
            bendCountMap.put("name", "弯折次数");
            bendCountMap.put("type", "column");
            HashMap<String, Object> triggerCountMap = new HashMap<>();
            triggerCountMap.put("data", triggerCount);
            triggerCountMap.put("name", "平层次数");
            triggerCountMap.put("type", "column");
            HashMap<String, Object> distanceCountMap = new HashMap<>();
            distanceCountMap.put("data", distanceCount);
            distanceCountMap.put("name", "运行距离");
            distanceCountMap.put("type", "column");

            series.add(runCountMap);
            series.add(doorCountMap);
            series.add(bendCountMap);
            series.add(triggerCountMap);
            series.add(distanceCountMap);


        } else {

            ArrayList<BigDecimal> count = new ArrayList<>();

            res.stream().forEach(it -> {
                elevatorCode.add((String) it.get("elevatorName"));
                count.add((BigDecimal) it.get(faultStatisticsModule.getEventType()));
            });

            HashMap<String, Object> countMap = new HashMap<>();
            countMap.put("data", count);
            countMap.put("name", faultStatisticsModule.getEventType());
            series.add(countMap);
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("series", series);
        result.put("categories", elevatorCode);

        return result;
    }

    /**
     * 获取电梯基本信息
     */
    @Override
    public Map getElevatorInfo(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {

        Map elevatorInfo = baseMapper.getElevatorInfo(faultStatisticalQuantitySearchModule.getRegister_number());

        return getelevatorInfo(elevatorInfo);
    }

    private Map getelevatorInfo(Map elevatorInfo) {
        if (elevatorInfo == null) {
            return null;
        }

        LocalDateTime installTime = (LocalDateTime) elevatorInfo.get("dt_install_time");

        if (installTime != null) {
            // 获取日期
            String now = DateUtils.dateFormate(new Date(), "yyyy-MM-dd HH:mm:ss");
            Date date1 = DateUtils.parseDate(installTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    "yyyy-MM-dd HH:mm:ss");
            Date date2 = DateUtils.parseDate(now, "yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            long timeInMillis1 = calendar.getTimeInMillis();
            calendar.setTime(date2);
            long timeInMillis2 = calendar.getTimeInMillis();

            long betweenDays = (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
            elevatorInfo.put("cumulative_running_time", betweenDays * 24);
            elevatorInfo.put("cumulative_days", betweenDays);
        } else {
            elevatorInfo.put("cumulative_running_time", "--");
            elevatorInfo.put("cumulative_days", "--");
        }
        return elevatorInfo;
    }
}
