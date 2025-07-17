package com.shmashine.api.controller.bigScreen;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.EventDownloadModuleMap;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.bigScreen.BigScreenService;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.ruijin.BizThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.ExceExport2;

@RestController
@RequestMapping("bigScreen")
public class BigScreenController extends BaseRequestEntity {

    @Autowired
    private final BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI;
    @Autowired
    private final BigScreenService bigScreenService;
    @Autowired
    private final BizUserService bizUserService;
    @Autowired
    private final BizElevatorService elevatorService;

    public BigScreenController(BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI, BigScreenService bigScreenService, BizUserService bizUserService, BizElevatorService elevatorService) {
        this.bizThirdPartyRuijinEnventServiceI = bizThirdPartyRuijinEnventServiceI;
        this.bigScreenService = bigScreenService;
        this.bizUserService = bizUserService;
        this.elevatorService = elevatorService;
    }


    /**
     * 统计电梯各个状态的数量
     *
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/countElevator/{villageId}")
    public Object countElevator(@PathVariable String villageId) {
        String userId = super.getUserId();
        Map map = bigScreenService.countElevatorV1(villageId, userId, bizUserService.isAdmin(userId));
//        Map map = elevatorService.countElevator(userId, bizUserService.isAdmin(userId));
        return ResponseResult.successObj(map);
    }

    /**
     * 电梯分类数量列表
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/ElevatorClassificationQuantityList")
    public Object elevatorClassificationQuantityList(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.elevatorClassificationQuantityListV1(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bigScreenService.getElevatorInfoV1(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bigScreenService.intelligentSupervisionV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 不文明行为走势图
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/TrendOfUncivilizedBehavior")
    public Object trendOfUncivilizedBehavior(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.trendOfUncivilizedBehaviorV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 不文明行为前三电梯排行
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/FrequencyOfUncivilizedSpeciesTop3")
    public Object frequencyOfUncivilizedSpeciesTop3(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.frequencyOfUncivilizedSpeciesTop3V1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 不文明行为统计 不文明种类次数
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/FrequencyOfUncivilizedSpecies")
    public Object frequencyOfUncivilizedSpecies(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.frequencyOfUncivilizedSpeciesV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 统计故障次数信息
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/StatisticsOfFailureTimes")
    public Object statisticsOfFailureTimes(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.statisticsOfFailureTimesV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯故障排名
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/ElevatorFailureRanking")
    public Object elevatorFailureRanking(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.elevatorFailureRankingV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 困人来源排行
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/RankingOfPoorPeople")
    public Object rankingOfPoorPeople(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bigScreenService.rankingOfPoorPeopleV1(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯故障占比
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/ProportionOfElevatorFailures")
    public Object proportionOfElevatorFailures(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.proportionOfElevatorFailures(faultStatisticalQuantitySearchModule));
    }

    /**
     * 最新的上报困人(仪电推送故障， 困人中的记录)
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/personShuttingLately")
    public Object getPersonShuttingLately(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getPersonShuttingLately(faultStatisticalQuantitySearchModule));
    }

    /**
     * 维保工单次数统计
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/numberOfMaintenanceWorkOrders")
    public Object numberOfMaintenanceWorkOrders(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.numberOfMaintenanceWorkOrders(faultStatisticalQuantitySearchModule));
    }

    /**
     * 统计工单状态占比
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/statisticsWorkOrderStatusProportion")
    public Object statisticsWorkOrderStatusProportion(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.statisticsWorkOrderStatusProportion(faultStatisticalQuantitySearchModule));
    }

    /**
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/trendYearOfHistoricalMaintenance")
    public Object trendYearOfHistoricalMaintenance(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.trendYearOfHistoricalMaintenance(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯故障工单走势图
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/failureWorkOrderTrendChart")
    public Object failureWorkOrderTrendChart(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.failureWorkOrderTrendChart(faultStatisticalQuantitySearchModule));
    }

    /**
     * 困人救援统计
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/statisticsOfTrappedPeopleRescue")
    public Object statisticsOfTrappedPeopleRescue(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.statisticsOfTrappedPeopleRescue(faultStatisticalQuantitySearchModule));
    }

    /**
     * 困人工单表
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/theHistoryOfSingleLabor")
    public Object theHistoryOfSingleLabor(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.theHistoryOfSingleLabor(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取当日困人电梯
     *
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/getTodayTrappedPeopleEleName")
    public Object getTodayTrappedPeopleEleName(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getTodayTrappedPeopleEleName(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取故障列表(仪电推送故障)
     *
     * @param searchFaultModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchFaultList")
    public Object searchFaultList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = bizThirdPartyRuijinEnventServiceI.searchFaultsListWithPage(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 根据参数，导出Excel文件
     *
     * @param searchFaultModule
     * @param response
     */
    @RequestMapping("/exprotEvent")
    public void testExprotExcel(@RequestBody SearchFaultModule searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        //创建一个数组用于设置表头
        String[] arr = new String[]{"电梯编号", "安装地址", "最新上报时间", "类型", "状态"};
        //调用Excel导出工具类
        List<EventDownloadModuleMap> list = bizThirdPartyRuijinEnventServiceI.searchEventForDownload(searchFaultModule);
        ExceExport2.export(response, list, arr);
    }

    /**
     * 获取故障列表(仪电推送故障)
     *
     * @param searchFaultModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchMaintenanceList")
    public Object searchMaintenanceList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = bizThirdPartyRuijinEnventServiceI.searchMaintenanceList(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 导出维保记录
     *
     * @param searchFaultModule
     * @param response
     */
    @RequestMapping("/exprotMaintenance")
    public void exprotMaintenance(@RequestBody SearchFaultModule searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        //创建一个数组用于设置表头
        String[] arr = new String[]{"电梯编号", "安装地址", "类型", "状态", "开始时间", "完成时间"};
        //调用Excel导出工具类
        List<Map> list = bizThirdPartyRuijinEnventServiceI.downLoadSearchMaintenanceList(searchFaultModule);
        ExceExport2.exprotMaintenance(response, list, arr);
    }
}
