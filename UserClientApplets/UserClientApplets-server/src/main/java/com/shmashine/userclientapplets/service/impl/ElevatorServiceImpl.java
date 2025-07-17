package com.shmashine.userclientapplets.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.exceptions.ExceptionUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.userclientapplets.client.RemoteApiClient;
import com.shmashine.userclientapplets.dao.ElevatorDao;
import com.shmashine.userclientapplets.entity.Elevator;
import com.shmashine.userclientapplets.entity.Maintenance;
import com.shmashine.userclientapplets.entity.PageListResultEntity;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.entity.SearchElevatorModule;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.service.FaultService;
import com.shmashine.userclientapplets.service.MaintenanceService;
import com.shmashine.userclientapplets.utils.BeanHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * ElevatorServiceImpl
 *
 * @author jiangheng
 * @version v1.0.0 - 2022/2/8 14:36
 */

@Slf4j
@Service
public class ElevatorServiceImpl extends ServiceImpl<ElevatorDao, Elevator> implements ElevatorService {

    @Autowired
    private RemoteApiClient remoteApiClient;

    @Autowired
    private FaultService faultService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Override
    public Map queryElevatorStatus(SearchFaultModule searchFaultModule) {

        HashMap<String, Integer> responseResult = new HashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(7);

        List<String> ids = Arrays.asList("elevatorNumber", "faultNumber",
                "maintenanceOverdueNumber", "repairNumber", "monthFaultNumber",
                "trappedPeopleNumber", "yearCheckNumber");

        try {
            var completableFutures = ids.stream()
                    .map(id -> CompletableFuture.supplyAsync(()
                            -> getElevatorStatus(id, SerializationUtils.clone(searchFaultModule)), executorService)
                            .whenComplete((num, e) -> responseResult.put(id, num)))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(completableFutures)
                    .get(20, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("查询设备状态异常 {}", ExceptionUtil.stacktraceToString(e));
        } finally {
            executorService.shutdown();
        }

        return responseResult;
    }

    @Override
    public HashMap insertCollectElevator(String elevatorId, String userId) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("elevatorId", elevatorId);
        return remoteApiClient.insertCollectElevator(jsonObject, userId);
    }

    @Override
    public HashMap cancelCollectElevator(String elevatorId, String userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("elevatorId", elevatorId);
        return remoteApiClient.cancelCollectElevator(jsonObject, userId);
    }

    @Override
    public HashMap searchElevatorCollectList(String userId) {
        return remoteApiClient.searchElevatorCollectList(new JSONObject(), userId);
    }

    @Override
    public List<String> getWarningByDay(String userId, boolean admin) {

        //获取今日故障列表
        List<String> faultList = faultService.getFaultByDay(userId, admin);

        //获取今日需维保列表
        String maintenance = maintenanceService.queryMaintenanceByDay(userId, admin);
        faultList.add(maintenance);

        return faultList;
    }

    @Override
    public PageListResultEntity queryElevatorAndCollectList(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var elevators = new PageInfo<>(baseMapper.queryElevatorAndCollectList(searchElevatorModule), pageSize);
        return new PageListResultEntity(pageIndex, pageSize, elevators.getTotal(), elevators.getList());
    }

    @Override
    public JSONObject getElevatorInfoById(String elevatorId) {

        JSONObject elevatorObj = remoteApiClient.getElevatorInfoById(elevatorId);

        JSONObject elevatorInfo = elevatorObj.getJSONObject("info");

        if (elevatorInfo != null) {

            //获取设备信息
            List<JSONObject> devices = baseMapper.getDeviceByEleId(elevatorId);

            elevatorInfo.put("devices", devices);
        }

        return elevatorInfo;
    }

    @Override
    public Map<String, Integer> getHealthRadarChart(String elevatorId) {

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        HashMap<String, Integer> responseResult = new HashMap<>();

        List<String> ids = Arrays.asList("maintenanceOverdueNumber", "uncivilizedBehavior",
                "faultNumber", "trappedPeopleNumber", "trappedPeopleTime", "runCount");


        try {
            var completableFutures = ids.stream()
                    .map(id -> CompletableFuture.supplyAsync(() -> getHealthRadarChart(id, elevatorId),
                                    executorService)
                    .whenComplete((num, e) -> responseResult.put(id, num)))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(completableFutures).get(20, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("getHealthRadarChart 异常 {}", ExceptionUtil.stacktraceToString(e));
        } finally {
            executorService.shutdown();
        }

        return responseResult;
    }

    @Override
    public Map<String, Object> getElevatorCountInfo(String elevatorId) {

        Map<String, Object> elevatorCountInfo = getMap(new QueryWrapper<Elevator>()
                .select("v_elevator_code AS elevatorCode",
                        "dt_install_time AS installTime",
                        "IFNULL(bi_run_count,0) AS runCount",
                        "IFNULL(bi_door_count,0) AS doorCount",
                        "IFNULL(bi_run_distance_count,0) AS runDistanceCount",
                        "IFNULL(bi_bend_count,0) AS bendCount")
                .eq("v_elevator_id", elevatorId));


        //累计运行时间天
        Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime installTime = (LocalDateTime) elevatorCountInfo.get("installTime");
        long installTimes = installTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long time = nowTimestamp.getTime() - installTimes;
        elevatorCountInfo.put("runTimeCount", time / (24 * 60 * 60 * 1000));

        //获取上一天统计信息
        HashMap<String, Object> yesterdayRunCount =
                baseMapper.getYesterdayRunCount((String) elevatorCountInfo.get("elevatorCode"));

        if (yesterdayRunCount != null) {
            elevatorCountInfo.putAll(yesterdayRunCount);
        }

        return elevatorCountInfo;
    }

    @Override
    public JSONObject getElevatorHeatMap(SearchElevatorModule searchElevatorModule) {
        JSONObject request = new JSONObject();
        request.put("elevatorCode", searchElevatorModule.getvElevatorCode());
        request.put("startDate", searchElevatorModule.getStartTime());
        request.put("endDate", searchElevatorModule.getEndTime());
        return remoteApiClient.getElevatorHeatMap(request);
    }

    @Override
    public ResponseResult getPermissionElevators(String requestUserId, boolean admin, String userId,
                                                 Integer permission, String villageId, String vProjectId) {

        if (permission == null) {
            permission = 2;
        }

        //未授权
        if (permission == 0) {

            List<HashMap<String, Object>> allElevators =
                    baseMapper.getPermissionElevators(requestUserId, admin, userId, 0, villageId, vProjectId);

            List<HashMap<String, Object>> permissionElevators =
                    baseMapper.getPermissionElevators(requestUserId, admin, userId, 1, villageId, vProjectId);

            List<String> permissionKeys = permissionElevators.stream()
                    .map(it -> (String) it.get("v_elevator_code")).collect(Collectors.toList());

            List<HashMap<String, Object>> elevators = allElevators.stream().filter(item -> {
                item.put("permission", 0);
                return !permissionKeys.contains(item.get("v_elevator_code"));
            }).collect(Collectors.toList());

            return ResponseResult.successObj(elevators);
        }

        //已授权
        if (permission == 1) {
            List<HashMap<String, Object>> elevators =
                    baseMapper.getPermissionElevators(requestUserId, admin, userId, permission, villageId, vProjectId);

            elevators.stream().forEach(item -> item.put("permission", 1));
            return ResponseResult.successObj(elevators);
        }

        //全部
        if (permission == 2) {

            List<HashMap<String, Object>> allElevators =
                    baseMapper.getPermissionElevators(requestUserId, admin, userId, 0, villageId, vProjectId);

            List<HashMap<String, Object>> permissionElevators =
                    baseMapper.getPermissionElevators(requestUserId, admin, userId, 1, villageId, vProjectId);

            List<Object> permissionKeys = permissionElevators.stream()
                    .map(v -> v.get("v_elevator_code")).collect(Collectors.toList());

            allElevators.stream().forEach(item -> {
                if (permissionKeys.contains(item.get("v_elevator_code"))) {
                    item.put("permission", 1);
                } else {
                    item.put("permission", 0);
                }
            });
            return ResponseResult.successObj(allElevators);
        }

        return ResponseResult.successObj(new ArrayList<>());

    }

    @Override
    public void extendElevatorNameAndAddress(Maintenance maintenance) {
        Elevator elevator = getOne(new QueryWrapper<Elevator>()
                .select("v_elevator_name", "v_address")
                .eq("v_equipment_code", maintenance.getRegisterNumber()));

        maintenance.setElevatorName(elevator.getVElevatorName());
        maintenance.setAddress(elevator.getVAddress());
    }

    @Override
    public PageListResultEntity getFaultElevatorByPage(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var elevators = new PageInfo<>(baseMapper.getFaultElevatorByPage(searchElevatorModule), pageSize);

        return new PageListResultEntity(pageIndex, pageSize, elevators.getTotal(), elevators.getList());

    }

    @Override
    public PageListResultEntity getUnMaintenanceElevatorByPage(SearchElevatorModule searchElevatorModule) {
        Integer pageIndex = searchElevatorModule.getPageIndex();
        Integer pageSize = searchElevatorModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var elevators = new PageInfo<>(baseMapper.getUnMaintenanceElevatorByPage(searchElevatorModule), pageSize);

        return new PageListResultEntity(pageIndex, pageSize, elevators.getTotal(), elevators.getList());
    }

    @Override
    public List<Elevator> getUserElevatorList(String userId) {
        return baseMapper.getUserElevatorList(userId);
    }

    private Integer getElevatorStatus(String id, SearchFaultModule searchFaultModule) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = format.format(c.getTime());
        searchFaultModule.setStartTime(first);
        JSONObject jsonObject;

        switch (id) {
            /**
             * 获取用户电梯列表
             */
            case "elevatorNumber":
                SearchElevatorModule searchElevatorModule =
                        BeanHelper.copyProperties(searchFaultModule, SearchElevatorModule.class);

                List<Elevator> elevatorList = baseMapper.queryElevatorList(searchElevatorModule);
                return elevatorList.size();
            /**
             * 故障（当日）
             */
            case "faultNumber":
                String today = format.format(new Date());
                searchFaultModule.setStartTime(today);
                searchFaultModule.setiUncivilizedBehaviorFlag(0);
                return faultService.queryFaultNumber(searchFaultModule);
            /**
             * 故障（本月）
             */
            case "monthFaultNumber":
                searchFaultModule.setiUncivilizedBehaviorFlag(0);
                return faultService.queryFaultNumber(searchFaultModule);
            /**
             * 维保超期（当月）
             */
            case "maintenanceOverdueNumber":
                jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
                jsonObject.put("dtReportTime", searchFaultModule.getStartTime().substring(0, 10));
                //获取超期记录
                jsonObject.put("overdue", 1);
                HashMap responseMaintenanceList =
                        remoteApiClient.searchMaintenanceList(jsonObject, searchFaultModule.getUserId());

                return (Integer) responseMaintenanceList.get("totalRecordCnt");
            /**
             * 急修列表（当日）
             */
            case "repairNumber":
                searchFaultModule.setStartTime(format.format(new Date()));
                jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
                jsonObject.put("dtReportTime", searchFaultModule.getStartTime().substring(0, 10));
                jsonObject.put("iStatus", 6);
                HashMap responseFaultList = remoteApiClient.queryFaultList(jsonObject, searchFaultModule.getUserId());
                return (Integer) responseFaultList.get("totalRecordCnt");
            /**
             * 困人(当月)
             */
            case "trappedPeopleNumber":
                return faultService.queryTrappedPeopleNumber(searchFaultModule);
            /**
             * 年检（当年）
             */
            case "yearCheckNumber":
                //设置为1号,当前日期既为本年第一天
                c.set(Calendar.DAY_OF_YEAR, 1);
                jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
                jsonObject.put("dtReportTime", format.format(c.getTime()).substring(0, 10));
                HashMap responseYearCheckList =
                        remoteApiClient.queryRuiJinAnnualCheckList(jsonObject, searchFaultModule.getUserId());

                return (Integer) responseYearCheckList.get("totalRecordCnt");
            default:
                return null;
        }
    }

    //CHECKSTYLE:OFF

    /**
     * 健康度雷达图
     *
     * @param id         查询类型
     * @param elevatorId 电梯id
     * @return 健康度
     */
    private Integer getHealthRadarChart(String id, String elevatorId) {

        /*本年度维保逾期次数-按期维保*/
        if ("maintenanceOverdueNumber".equals(id)) {

            List<Map> elevatorWorkOrder = maintenanceService.getOverdueOrders(elevatorId);

            if (elevatorWorkOrder.size() == 0) {
                return 4;
            }

            int overdueOrders = 0;

            // 统计每个电梯的维保记录中 是否存在超期
            for (int i = 1; i < elevatorWorkOrder.size(); i++) {
                Date nextMaintenanceDate = (Date) elevatorWorkOrder.get(i - 1).get("next_maintenance_date");
                Date completeTime = (Date) elevatorWorkOrder.get(i).get("complete_time");
                if (completeTime.after(nextMaintenanceDate)) {
                    // 超期维保记录
                    overdueOrders++;
                }
            }

            // 最后一条记录的下次维保时间与当前时间比较
            Map lastOrder = elevatorWorkOrder.get(elevatorWorkOrder.size() - 1);
            Date currentDate = new Date();
            // 此记录的完成时间与上一条记录的下次维保日期比较
            if (currentDate.after((Date) lastOrder.get("next_maintenance_date"))) {
                overdueOrders++;
            }

            return 4 - overdueOrders >= 0 ? 4 - overdueOrders : 0;
        }

        /*文明用梯*/
        if ("uncivilizedBehavior".equals(id)) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -30);
            String startTime = format.format(c.getTime());

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setiUncivilizedBehaviorFlag(1);
            searchFaultModule.setStartTime(startTime);
            searchFaultModule.setAdminFlag(true);

            Integer stopCount = faultService.queryFaultNumber(searchFaultModule);
            return 4 - (stopCount / 8) >= 0 ? 4 - (stopCount / 8) : 0;
        }

        /*电梯故障次数-故障频次*/
        if ("faultNumber".equals(id)) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -30);
            String startTime = format.format(c.getTime());

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setiUncivilizedBehaviorFlag(0);
            searchFaultModule.setStartTime(startTime);
            searchFaultModule.setAdminFlag(true);

            Integer faultCount = faultService.queryFaultNumber(searchFaultModule);

            return 4 - (faultCount / 4) >= 0 ? 4 - (faultCount / 4) : 0;
        }

        /*电梯困人故障次数（非误报，非系统测试）-困人频次*/
        if ("trappedPeopleNumber".equals(id)) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -30);
            String startTime = format.format(c.getTime());

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setStartTime(startTime);
            searchFaultModule.setAdminFlag(true);

            Integer trappedPeopleCount = faultService.queryTrappedPeopleNumber(searchFaultModule);

            return 4 - trappedPeopleCount >= 0 ? 4 - trappedPeopleCount : 0;

        }

        /*单梯所有困人时长取平均-救援时长*/
        if ("trappedPeopleTime".equals(id)) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            String startTime = format.format(c.getTime());

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setStartTime(startTime);
            searchFaultModule.setAdminFlag(true);

            //儿童医院项目——救援时长统计麦信平台故障时长
            Integer num = faultService.getTrappedPeopleTimeForMX(searchFaultModule) / 1000;

            int trappedPeopleGrade;
            if (num <= 600) {
                trappedPeopleGrade = 4;
            } else if (600 < num && num <= 900) {
                trappedPeopleGrade = 3;
            } else if (900 < num && num <= 1200) {
                trappedPeopleGrade = 2;
            } else if (1200 < num && num <= 1500) {
                trappedPeopleGrade = 1;
            } else {
                trappedPeopleGrade = 0;
            }
            return trappedPeopleGrade;
        }

        //每月平均每小时运行次数-运行频度
        if ("runCount".equals(id)) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -30);
            String startTime = format.format(c.getTime());
            SearchElevatorModule searchElevatorModule = new SearchElevatorModule();
            searchElevatorModule.setElevatorId(elevatorId);
            searchElevatorModule.setStartTime(startTime);
            searchElevatorModule.setAdminFlag(true);

            Integer runCount = baseMapper.getAVGRunCountByDay(searchElevatorModule) / 24;

            int count;

            if (runCount < 5) {
                count = 0;
            } else if (5 <= runCount && runCount < 20) {
                count = 1;
            } else if (20 <= runCount && runCount < 35) {
                count = 2;
            } else if (35 <= runCount && runCount < 50) {
                count = 3;
            } else {
                count = 4;
            }
            return count;
        }

        return null;
    }       //CHECKSTYLE:ON


}
