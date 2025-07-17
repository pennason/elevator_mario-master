package com.shmashine.api.controller.wuye;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.ElevatorService;
import com.shmashine.api.service.wuye.EventService;
import com.shmashine.api.service.wuye.FaultService;
import com.shmashine.api.service.wuye.MaintenanceService;

@RestController
@RequestMapping("/wuye")
public class WuyeController extends BaseRequestEntity {

    @Autowired
    private FaultService faultService;

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ElevatorService elevatorService;

    @PostMapping("/villagesCount")
    public Object getVillageList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(super.getUserId());
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(faultService.getFaultCountByVillage(searchFaultModule));
    }

    @PostMapping("/villagesCountRate")
    public Object getVillageCountRate(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(super.getUserId());
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(faultService.getVillageCountRate(searchFaultModule));
    }

    /**
     * 获取维保记录
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/queryMaintenanceList")
    public ResponseEntity queryMaintenanceList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(maintenanceService.searchMaintenanceList(searchFaultModule));
    }

    /**
     * 获取电梯基本信息
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorInfo")
    public Object getElevatorInfo(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(elevatorService.getElevatorInfo(faultStatisticalQuantitySearchModule));
    }

    /**
     * 智能监管
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/IntelligentSupervision")
    public Object intelligentSupervision(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(eventService.intelligentSupervision(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取电梯每日运行数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getElevatorRunDataStatistics")
    public Object getElevatorEventStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, Object> result = elevatorService.getElevatorRunDataStatistics(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取电梯每日运行数据统计 电梯分组
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getElevatorEventStatisticsGroupByElevator")
    public Object getElevatorEventStatisticsGroupByElevator(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, Object> result = elevatorService.getElevatorRunDataStatisticsGroupByElevator(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取故障数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getFaultDataStatistics")
    public Object getFaultDataStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getFaultStatistics(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取故障数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getFaultDataStatisticsGroupByElevator")
    public Object getFaultDataStatisticsGroupByElevator(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getFaultStatisticsGroupByElevator(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取不文明数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getUnCivilizedDataStatistics")
    public Object getUnCivilizedDataStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getUncivilizedStatistics(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取不文明数据统计
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getUnCivilizedDataStatisticsGroupByElevator")
    public Object getUnCivilizedDataStatisticsGroupByElevator(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getUncivilizedStatisticsGroupByElevator(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取电梯总数
     *
     * @param
     * @return
     */
    @PostMapping("/getUserElevatorCount")
    public Object getUserElevatorCount(SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());

        HashMap<String, Integer> result = new HashMap<>();
        result.put("total", elevatorService.getElevatorCount(searchElevatorModule));
        return ResponseResult.successObj(result);
    }

    /**
     * 急修工单趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getRepairTrend")
    public Object getRepairTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getRepairTrend(faultStatisticsModule));
    }

    /**
     * 维保工单趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getMaintenanceTrend")
    public Object getMaintenanceTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getMaintenanceTrend(faultStatisticsModule));
    }


    /**
     * 不文明单趋势
     *
     * @param faultStatisticsModule
     * @return
     */
    @PostMapping("/getUncivilizedTrend")
    public Object getUncivilizedTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getUncivilizedTrend(faultStatisticsModule));
    }
}
