package com.shmashine.commonbigscreen.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.commonbigscreen.dao.FaultDao;
import com.shmashine.commonbigscreen.entity.Fault;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.EventService;
import com.shmashine.commonbigscreen.service.FaultService;
import com.shmashine.commonbigscreen.service.MaintenanceService;
import com.shmashine.commonbigscreen.service.UserService;
import com.shmashine.commonbigscreen.utils.EChartDateUtils;
import com.shmashine.commonbigscreen.client.RemoteApiClient;

/**
 * 故障服务实现
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/7 11:13
 */
@Service
public class FaultServiceImpl extends ServiceImpl<FaultDao, Fault> implements FaultService {

    @Autowired
    private EventService eventService;

    @Resource
    private RemoteApiClient apiClient;

    @Autowired
    private UserService userService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Override
    public HashMap<String, Integer> getFaultCount(SearchFaultModule searchFaultModule) {

        //困人数（仪电工单为准）
        Integer peopleTrappedCount = eventService.getPeopleTrappedCount(searchFaultModule);

        //电动车入梯数（平台故障统计）
        searchFaultModule.setiUncivilizedBehaviorFlag(1);
        searchFaultModule.setiFaultType("37");
        Integer electroMobileFaultCount = baseMapper.getFaultCount(searchFaultModule);

        HashMap<String, Integer> result = new HashMap<>();
        result.put("peopleTrappedCount", peopleTrappedCount);

        //故障数（仪电工单为准）
        Integer faultCount = eventService.getFaultCount(searchFaultModule);
        result.put("faultCount", faultCount);
        result.put("electroMobileFaultCount", electroMobileFaultCount);

        return result;
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
        List<HashMap<String, Object>> faultOrderByConfirmOrCompleted =
                eventService.getFaultOrderByConfirmOrCompleted(searchFaultModule);

        HashMap<String, Integer> repairOrder = new HashMap<>();

        faultOrderByConfirmOrCompleted.stream().forEach(it -> repairOrder.put((String) it.get("v_village_id"),
                Integer.valueOf(it.get("number").toString())));

        //隐患
        List<HashMap<String, Object>> getFaultOrderByFalsePositiveOrNew =
                eventService.getFaultOrderByFalsePositiveOrNew(searchFaultModule);

        HashMap<String, Integer> hiddenDangerOrder = new HashMap<>();
        getFaultOrderByFalsePositiveOrNew.stream().forEach(it -> hiddenDangerOrder.put((String) it.get("v_village_id"),
                (Integer) it.get("number")));

        //电瓶车入梯
        List<HashMap<String, Object>> electroMobileFaultCount =
                baseMapper.getElectroMobileFaultCount(searchFaultModule);

        HashMap<String, Integer> electroMobileCount = new HashMap<>();
        electroMobileFaultCount.stream().forEach(it -> electroMobileCount.put((String) it.get("v_village_id"),
                (Integer) it.get("number")));

        //获取所有小区列表
        List<HashMap<String, Object>> villageInfos = getVillageInfo(searchFaultModule);

        villageInfos.stream().forEach(it -> {
            it.put("repairOrder", repairOrder.get(it) == null ? 0 : repairOrder.get(it));
            it.put("hiddenDangerOrder", hiddenDangerOrder.get(it) == null ? 0 : hiddenDangerOrder.get(it));
            it.put("electroMobileCount", electroMobileCount.get(it) == null ? 0 : electroMobileCount.get(it));
        });

        return villageInfos;
    }

    @Override
    public HashMap<String, Object> getFaultChartByTime(SearchFaultModule searchFaultModule) {

        searchFaultModule.setAdminFlag(userService.isAdmin(searchFaultModule.getUserId()));
        List<HashMap<String, Object>> faultChartByTime = baseMapper.getFaultChartByTime(searchFaultModule);

        String timeFlag = searchFaultModule.getTimeFlag();

        if (timeFlag.equals("00")) {
            return EChartDateUtils.getDataOnWeek(faultChartByTime, null, null, null);
        }

        if (timeFlag.equals("11")) {
            return EChartDateUtils.getDataOnMonth(faultChartByTime, null, null, null);
        }

        return null;
    }

    @Override
    public HashMap<String, Object> getTodayPeopleTrappedElevator(SearchElevatorModule searchElevatorModule) {
        return baseMapper.getTodayPeopleTrappedElevator(searchElevatorModule);
    }

    @Override
    public HashMap<String, Object> getTodayFaultElevator(SearchElevatorModule searchElevatorModule) {
        return baseMapper.getTodayFaultElevator(searchElevatorModule);
    }

    @Override
    public HashMap<String, Object> getThisMomentFault(String elevatorCode) {

        return (HashMap<String, Object>) getMap(new QueryWrapper<Fault>()
                .select("v_fault_name AS faultName", "dt_report_time AS reportTime")
                .eq("i_status", 0)
                .eq("i_uncivilized_behavior_flag", 0)
                .eq("v_elevator_code", elevatorCode).orderByDesc("dt_report_time"));
    }

    @Override
    public HashMap<String, Integer> getHistoryRecordCount(SearchFaultModule searchFaultModule) {

        HashMap<String, Integer> responseResult = new HashMap<>();
        searchFaultModule.setAdminFlag(true);

        //维保数
        Integer maintenanceCount = maintenanceService.getMaintenanceCount(searchFaultModule);
        responseResult.put("maintenanceCount", maintenanceCount);

        //困人次数（仪电）
        //故障次数（仪电）
        //电动车入梯数
        HashMap<String, Integer> faultCount = getFaultCount(searchFaultModule);
        if (faultCount != null) {
            responseResult.putAll(faultCount);
        }

        //反复阻挡门数
        searchFaultModule.setiUncivilizedBehaviorFlag(1);
        searchFaultModule.setiFaultType("20");
        Integer doorStopedFaultCount = baseMapper.getFaultCount(searchFaultModule);
        responseResult.put("doorStopedFaultCount", doorStopedFaultCount);

        return responseResult;
    }

    /**
     * 获取小区列表
     * 排序规则 1：根据电梯数量排序，2：根据当日困人数排序
     */
    private List<HashMap<String, Object>> getVillageInfo(SearchFaultModule searchFaultModule) {

        //获取所有下级部门id
        List<String> deptIds = apiClient.getDeptIds(searchFaultModule.getUserId());

        //根据部门id获取所有小区

        //根据电梯数量排序
        if (1 == searchFaultModule.getiStatus()) {
            return baseMapper.getVillageInfoByDeptIdsAndElevatorCount(deptIds);
        }

        //根据当日困人数判断
        if (2 == searchFaultModule.getiStatus()) {
            return baseMapper.getVillageInfoByDeptIdsAndPeopleTrappedCount(deptIds);
        }
        return null;
    }

    @Override
    public Integer queryFaultNumber(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultCount(searchFaultModule);
    }

    @Override
    public Integer queryTrappedPeopleNumber(SearchFaultModule searchFaultModule) {
        return baseMapper.getTrappedPeopleTotal(searchFaultModule);
    }

    @Override
    public Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule) {
        return baseMapper.getTrappedPeopleTimeForMX(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getFaultFileById(String faultId) {
        return baseMapper.getFaultFileById(faultId);
    }

}