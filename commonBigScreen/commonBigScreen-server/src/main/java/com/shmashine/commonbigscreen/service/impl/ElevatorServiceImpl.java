package com.shmashine.commonbigscreen.service.impl;

import java.text.SimpleDateFormat;
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

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.commonbigscreen.client.RemoteApiClient;
import com.shmashine.commonbigscreen.dao.ElevatorDao;
import com.shmashine.commonbigscreen.entity.Elevator;
import com.shmashine.commonbigscreen.entity.PageListResultEntity;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchElevatorStatus;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.ElevatorService;
import com.shmashine.commonbigscreen.service.FaultService;
import com.shmashine.commonbigscreen.service.MaintenanceService;
import com.shmashine.commonbigscreen.service.UserService;

/**
 * 电梯服务实现
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/3 16:14
 */
@Service
public class ElevatorServiceImpl extends ServiceImpl<ElevatorDao, Elevator> implements ElevatorService {

    @Autowired
    private UserService userService;

    @Autowired
    private FaultService faultService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Resource
    private RemoteApiClient apiClient;

    @Override
    public Integer getElevatorCount(SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(userService.isAdmin(searchElevatorModule.userId));
        return baseMapper.getAllElevatorCount(searchElevatorModule);
    }

    @Override
    public List<HashMap<String, Integer>> getElevatorCountByVillage(SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(userService.isAdmin(searchElevatorModule.userId));
        return baseMapper.getElevatorCountByVillage(searchElevatorModule);
    }

    @Override
    public List<HashMap<String, Object>> intelligentSupervision(SearchElevatorModule searchElevatorModule) {

        searchElevatorModule.setAdminFlag(userService.isAdmin(searchElevatorModule.userId));

        ArrayList<HashMap<String, Object>> responseResult = new ArrayList<>();

        //维保单位

        //当日困人电梯
        HashMap<String, Object> peopleTrappedElevator =
                faultService.getTodayPeopleTrappedElevator(searchElevatorModule);

        responseResult.add(peopleTrappedElevator);

        //当日故障电梯
        HashMap<String, Object> faultElevator = faultService.getTodayFaultElevator(searchElevatorModule);
        responseResult.add(faultElevator);

        //维保逾期电梯
        HashMap<String, Object> maintenanceOverdueElevator =
                maintenanceService.getMaintenanceOverdueElevator(searchElevatorModule);

        responseResult.add(maintenanceOverdueElevator);

        //年检逾期电梯
        HashMap<String, Object> yearCheckOverdueElevator =
                maintenanceService.getYearCheckOverdueElevator(searchElevatorModule);

        responseResult.add(yearCheckOverdueElevator);

        return responseResult;
    }

    @Override
    public HashMap<String, Object> getElevatorBaseInfo(String elevatorCode) {

        //获取基础信息
        HashMap<String, Object> baseInfo = (HashMap<String, Object>) getMap(new QueryWrapper<Elevator>()
                .select("v_elevator_id AS elevatorId",
                        "v_equipment_code AS equipmentCode",
                        "v_elevator_brand_id AS elevatorBrandId",
                        "i_elevator_type AS elevatorType",
                        "d_last_maintain_date AS lastMaintainDate",
                        "d_next_inspect_date AS nextInspectDate")
                .eq("v_elevator_code", elevatorCode));

        //获取电梯品牌
        String elevatorBrandName = baseMapper.getElevatorBrandById((String) baseInfo.get("elevatorBrandId"));
        baseInfo.put("elevatorBrandName", elevatorBrandName);

        //获取当前故障
        HashMap<String, Object> fault = faultService.getThisMomentFault(elevatorCode);
        if (fault != null) {
            baseInfo.putAll(fault);
        }

        //获取昨日运行统计信息
        HashMap<String, Integer> yesterdayRunCount = baseMapper.getYesterdayRunCount(elevatorCode);
        if (yesterdayRunCount != null) {
            baseInfo.putAll(yesterdayRunCount);
        }

        return baseInfo;
    }

    @Override
    public HashMap<String, Object> searchVillageMap(String userId) {
        return apiClient.searchVillageMap(userId);
    }

    @Override
    public JSONObject getElevatorHeatMap(SearchFaultModule searchFaultModule) {
        JSONObject request = new JSONObject();
        request.put("elevatorCode", searchFaultModule.getvElevatorCode());
        request.put("startDate", searchFaultModule.getDtReportTime());
        request.put("endDate", searchFaultModule.getDtEndTime());
        return apiClient.getElevatorHeatMap(request);
    }

    @Override
    public HashMap<String, String> getElevatorSafetyAdministratorAndMaintainer(String registerNumber) {
        return baseMapper.getElevatorSafetyAdministratorAndMaintainer(registerNumber);
    }

    @Override
    public JSONObject getElevatorVideoUrl(String elevatorId) {
        JSONObject cameraObj = apiClient.getCameraUrl(elevatorId);
        return cameraObj;
    }

    @Override
    public List<Map> getElevatorsStatusWithMxData(SearchElevatorStatus searchElevatorStatus) {
        searchElevatorStatus.setAdminFlag(userService.isAdmin(searchElevatorStatus.getUserId()));
        return baseMapper.getElevatorsStatusWithMxData(searchElevatorStatus);
    }

    //CHECKSTYLE:OFF
    @Override
    public PageListResultEntity getElevatorsStatus(SearchElevatorStatus searchElevatorStatus) {

        HashMap<String, HashMap<String, Object>> allStatus = new HashMap();

        searchElevatorStatus.setAdminFlag(userService.isAdmin(searchElevatorStatus.getUserId()));

        //获取所有电梯状态
        List<HashMap<String, Object>> allStatusList = baseMapper.queryAllStatus(searchElevatorStatus);
        allStatusList.forEach(item -> allStatus.put((String) item.get("elevatorCode"), item));

        //困人、检修、故障、正常、离线

        //1:正常 0:非正常
        if (searchElevatorStatus.getNormal() == 1) {
            //困人
            if (searchElevatorStatus.getPeopleTrappedStatus() == 0) {
                List<String> elevatorCodes = baseMapper.getPeopleTrappedStatus(searchElevatorStatus);
                elevatorCodes.stream().forEach(item -> allStatus.remove(item));
            }

            //检修
            if (searchElevatorStatus.getModeStatus() == 0) {
                List<String> elevatorCodes = baseMapper.getModeStatus(searchElevatorStatus);
                elevatorCodes.stream().forEach(item -> allStatus.remove(item));
            }

            //故障
            if (searchElevatorStatus.getFaultStatus() == 0) {
                List<String> elevatorCodes = baseMapper.getFaultStatus(searchElevatorStatus);
                elevatorCodes.stream().forEach(item -> allStatus.remove(item));
            }

            //离线
            if (searchElevatorStatus.getIsOnLine() == 1) {
                List<String> elevatorCodes = baseMapper.getIsOnLineStatus(searchElevatorStatus);
                elevatorCodes.stream().forEach(item -> allStatus.remove(item));
            }
        }

        if (searchElevatorStatus.getNormal() == 0) {

            HashMap<String, HashMap<String, Object>> normalStatus = new HashMap();

            //困人
            if (searchElevatorStatus.getPeopleTrappedStatus() == 1) {
                List<String> elevatorCodes = baseMapper.getPeopleTrappedStatus(searchElevatorStatus);
                elevatorCodes.forEach(item -> normalStatus.put(item, allStatus.get(item)));
            }

            //检修
            if (searchElevatorStatus.getModeStatus() == 1) {
                List<String> elevatorCodes = baseMapper.getModeStatus(searchElevatorStatus);
                elevatorCodes.forEach(item -> normalStatus.put(item, allStatus.get(item)));
            }

            //故障
            if (searchElevatorStatus.getFaultStatus() == 1) {
                List<String> elevatorCodes = baseMapper.getFaultStatus(searchElevatorStatus);
                elevatorCodes.forEach(item -> normalStatus.put(item, allStatus.get(item)));
            }

            //离线
            if (searchElevatorStatus.getIsOnLine() == 0) {
                List<String> elevatorCodes = baseMapper.getIsOnLineStatus(searchElevatorStatus);
                elevatorCodes.forEach(item -> normalStatus.put(item, allStatus.get(item)));
            }

            allStatus.clear();
            allStatus.putAll(normalStatus);

        }

        //分页
        Integer pageIndex = searchElevatorStatus.getPageIndex();
        Integer pageSize = searchElevatorStatus.getPageSize();

        List<HashMap<String, Object>> rest = new ArrayList<>();
        for (HashMap<String, Object> value : allStatus.values()) {
            rest.add(value);
        }
        int s = pageSize * (pageIndex - 1);
        int e = pageSize * pageIndex;

        if (rest.size() > pageSize) {
            rest = rest.subList(s, Math.min(e, rest.size()));
        }

        return new PageListResultEntity<>(pageIndex, pageSize, allStatus.size(), rest);

    }    //CHECKSTYLE:ON

    @Override
    public Map<String, Integer> getHealthRadarChart(String elevatorId) {

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        HashMap<String, Integer> responseResult = new HashMap<>();

        List<String> ids = Arrays.asList("maintenanceOverdueNumber", "uncivilizedBehavior",
                "faultNumber", "trappedPeopleNumber", "trappedPeopleTime", "runCount");


        try {
            CompletableFuture[] completableFutures = ids.stream().map(id -> CompletableFuture.supplyAsync(() ->
                            getHealthRadarChart(id, elevatorId),
                    executorService).whenComplete((num, e) -> responseResult.put(id, num)
            )).toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(20, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } finally {
            executorService.shutdown();
        }

        return responseResult;
    }

    @Override
    public List<String> getBuildId(String userId) {
        return baseMapper.getBuildIdByUserId(userService.isAdmin(userId), userId);
    }

    @Override
    public Object searchElevatorByBuildId(String buildId) {

        return baseMapper.selectList(new QueryWrapper<Elevator>()
                .select("v_elevator_id AS elevatorId",
                        "v_elevator_code AS elevatorCode",
                        "v_elevator_name AS elevatorName",
                        "v_equipment_code AS equipmentCode",
                        "v_address AS address",
                        "i_elevator_type AS elevatorType",
                        "v_project_id AS projectId",
                        "v_village_id AS villageId",
                        "v_building_id AS buildingId")
                .eq("v_building_id", buildId));
    }

    //CHECKSTYLE:OFF

    /**
     * 健康度雷达图
     *
     * @param id         类型
     * @param elevatorId 电梯id
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

        /*反复阻挡门次数-文明用梯*/
        if ("uncivilizedBehavior".equals(id)) {

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setiUncivilizedBehaviorFlag(1);
            searchFaultModule.setiFaultType("20");
            searchFaultModule.setTimeFlag("11");
            searchFaultModule.setAdminFlag(true);

            Integer stopCount = faultService.queryFaultNumber(searchFaultModule);
            return 4 - (stopCount / 4) >= 0 ? 4 - (stopCount / 4) : 0;
        }

        /*电梯故障次数-故障频次*/
        if ("faultNumber".equals(id)) {

            SearchFaultModule searchFaultModule = new SearchFaultModule();
            searchFaultModule.setElevatorId(elevatorId);
            searchFaultModule.setiUncivilizedBehaviorFlag(0);
            searchFaultModule.setTimeFlag("11");
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
            searchFaultModule.setDtReportTime(startTime);
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
            searchFaultModule.setDtReportTime(startTime);
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

            Integer runCount = baseMapper.getAVGRunCountByDay(searchElevatorModule);

            runCount = runCount / 24;

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
    }    //CHECKSTYLE:ON
}
