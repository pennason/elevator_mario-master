package com.shmashine.api.controller.ruijin;


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
import com.shmashine.api.module.elevator.SearchElevatorModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.EventDownloadModuleMap;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.ruijin.BizThirdPartyRuijinEnventServiceI;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.ExceExport2;

/**
 * 瑞金医院大屏接口
 */
@RestController
@RequestMapping("RuiJin")
public class RuiJinBigScreenController extends BaseRequestEntity {

    private final BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI;
    private final BizUserService bizUserService;

    @Autowired
    public RuiJinBigScreenController(BizThirdPartyRuijinEnventServiceI bizThirdPartyRuijinEnventServiceI, BizUserService bizUserService) {
        this.bizThirdPartyRuijinEnventServiceI = bizThirdPartyRuijinEnventServiceI;
        this.bizUserService = bizUserService;
    }


    /**
     * 统计电梯各个状态的数量
     *
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/countElevator/{villageId}")
    public Object countElevator(@PathVariable String villageId) {
        String userId = super.getUserId();
        Map map = bizThirdPartyRuijinEnventServiceI.countElevator(villageId, userId, bizUserService.isAdmin(userId));
        return ResponseResult.successObj(map);
    }

    /**
     * 电梯列表（不分页）
     *
     * @param searchElevatorModule
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/searchElevatorNotPage")
    public Object searchElevatorNotPage(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        List<Map<String, Object>> maps = bizThirdPartyRuijinEnventServiceI.searchElevatorListNoPage(searchElevatorModule);
        return ResponseResult.successObj(maps);
    }

    /**
     * 电梯分类占比
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    @PostMapping("/ElevatorClassification")
    public Object elevatorClassification(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.elevatorClassification(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.elevatorClassificationQuantityList(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.statisticsOfFailureTimes(faultStatisticalQuantitySearchModule));
    }


    /**
     * 统计故障次数信息 骋隆
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/StatisticsOfFailureTimesForCl")
    public Object statisticsOfFailureTimesForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.statisticsOfFailureTimesForCl(faultStatisticalQuantitySearchModule));
    }

    /**
     * 统计故障次数信息 骋隆 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/StatisticsOfFailureTimesForClWithMXData")
    public Object statisticsOfFailureTimesForClWithMXData(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.statisticsOfFailureTimesForClWithMXData(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.rankingOfPoorPeople(faultStatisticalQuantitySearchModule));
    }

    /**
     * 困人来源排行 骋隆
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/RankingOfPoorPeopleForCl")
    public Object rankingOfPoorPeopleForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.rankingOfPoorPeopleForCl(faultStatisticalQuantitySearchModule));
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
     * 电梯故障占比 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return
     */
    @PostMapping("/ProportionOfElevatorFailuresWithMXData")
    public Object proportionOfElevatorFailuresWithMXData(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.proportionOfElevatorFailuresWithMXData(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.elevatorFailureRanking(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯故障排名 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/ElevatorFailureRankingWithMXData")
    public Object elevatorFailureRankingWithMXData(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.elevatorFailureRankingWithMXData(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.frequencyOfUncivilizedSpecies(faultStatisticalQuantitySearchModule));
    }

    /**
     * 不文明行为统计 不文明种类次数 骋隆
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/FrequencyOfUncivilizedSpeciesForCl")
    public Object frequencyOfUncivilizedSpeciesForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.frequencyOfUncivilizedSpeciesForCl(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.frequencyOfUncivilizedSpeciesTop3(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.trendOfUncivilizedBehavior(faultStatisticalQuantitySearchModule));
    }

    /**
     * 不文明行为走势图 骋隆
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/TrendOfUncivilizedBehaviorForCl")
    public Object trendOfUncivilizedBehaviorForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.trendOfUncivilizedBehaviorForCl(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.intelligentSupervision(faultStatisticalQuantitySearchModule));
    }

    /**
     * 智能监管 骋隆
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/IntelligentSupervisionForCl")
    public Object intelligentSupervisionForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.intelligentSupervisionForCl(faultStatisticalQuantitySearchModule));
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
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorInfo(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取电梯基本信息 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorInfoWithMXData")
    public Object getElevatorInfoWithMXData(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorInfoWithMXData(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯历史信息
     * 困人记录 故障记录 不文明行为
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorHlsInfo")
    public Object getElevatorHlsInfo(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorHlsInfo(faultStatisticalQuantitySearchModule));
    }

    /**
     * 电梯 困人记录 故障记录 不文明行为 麦信数据源
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorHlsInfoWithMXData")
    public Object getElevatorHlsInfoWithMXData(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorHlsInfoWithMXData(faultStatisticalQuantitySearchModule));
    }


    /**
     * 电梯历史信息 骋隆
     * 困人记录 故障记录 不文明行为
     *
     * @param faultStatisticalQuantitySearchModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult#
     */
    @PostMapping("/getElevatorHlsInfoForCl")
    public Object getElevatorHlsInfoForCl(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorHlsInfoForCl(faultStatisticalQuantitySearchModule));
    }

    /**
     * 困人救援实时信息
     */
    @GetMapping("/getElevatorFaultRealTime/{eventId}")
    public Object getElevatorFaultRealTime(@PathVariable("eventId") String eventId) {
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getEnventDetailById(eventId));
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
     * @param faultStatisticalQuantitySearchModule
     */
    @PostMapping("/trendYearOfHistoricalMaintenance")
    public Object trendYearOfHistoricalMaintenance(@RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.trendYearOfHistoricalMaintenance(faultStatisticalQuantitySearchModule));
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
     * 获取故障列表(麦信推送故障)
     *
     * @param searchFaultModule
     * @return #type: com.shmashine.api.entity.base.ResponseResult,com.shmashine.api.entity.base.PageListResultEntity#
     */
    @PostMapping("/searchFaultListWithMXData")
    public Object searchFaultListWithMXData(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = bizThirdPartyRuijinEnventServiceI.searchFaultsListWithMXDataByPage(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 获取维保记录列表(仪电推送故障)
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
        String[] arr = new String[]{"电梯名称", "地址", "类型", "工单状态", "应完成日期", "完成时间"};
        //调用Excel导出工具类
        List<Map> list = bizThirdPartyRuijinEnventServiceI.downLoadSearchMaintenanceList(searchFaultModule);
        ExceExport2.exprotMaintenance(response, list, arr);
    }

    /**
     * 瑞金大屏——获取运行统计信息
     *
     * @return
     */
    @PostMapping("/getRunCountInfo")
    public ResponseResult getRunCountInfo(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getRunCountInfo(searchFaultModule));
    }

    /**
     * 运行统计数据导出
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/exportRunCountInfo")
    public void exportRunCountInfo(@RequestBody SearchFaultModule searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        bizThirdPartyRuijinEnventServiceI.exportRunCountInfo(searchFaultModule, response);
    }

    /**
     * 瑞金大屏首页状态
     *
     * @param searchFaultModule
     * @return
     */
    @PostMapping("/getElevatorsStatus")
    public ResponseResult getElevatorsStatus(@RequestBody SearchFaultModule searchFaultModule) {
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.getElevatorsStatus(searchFaultModule));
    }

    @PostMapping("/queryBuilding")
    public ResponseResult queryBuilding(@RequestBody SearchFaultModule searchFaultModule) {
        return ResponseResult.successObj(bizThirdPartyRuijinEnventServiceI.queryBuilding(searchFaultModule));
    }


}
