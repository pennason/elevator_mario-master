package com.shmashine.api.controller.wuye;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenanceOrderPageReqVO;
import com.shmashine.api.controller.maiXinMaintenancePlatform.vo.MaintenancePlatformElevatorRespDTO;
import com.shmashine.api.controller.wuye.vo.WuyeElevatorRespVO;
import com.shmashine.api.controller.wuye.vo.WuyeMenuListRespVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.service.dept.BizDeptService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.village.BizVillageService;
import com.shmashine.api.service.wuye.MaiXinElevatorService;
import com.shmashine.api.service.wuye.MaiXinEventService;
import com.shmashine.api.service.wuye.MaiXinFaultService;
import com.shmashine.api.service.wuye.MaiXinMaintenanceService;
import com.shmashine.api.util.MaiXinMaintenancePlatformUtil;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblVillage;

import lombok.extern.slf4j.Slf4j;

/**
 * 物业小程序
 */

@Validated
@Slf4j
@RestController
@RequestMapping("/maixin/wuye")
public class MaiXinWuyeController extends BaseRequestEntity {


    @Autowired
    private MaiXinFaultService faultService;

    @Autowired
    private BizUserService bizUserService;

    @Autowired
    private MaiXinMaintenanceService maintenanceService;

    @Autowired
    private MaiXinEventService eventService;

    @Autowired
    private MaiXinElevatorService elevatorService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BizDeptService bizDeptService;

    @Autowired
    private BizVillageService bizVillageService;

    @Autowired
    private MaiXinMaintenancePlatformUtil maiXinMaintenancePlatformUtil;

    /**
     * 获取物业小程序动态菜单
     */
    @GetMapping("/getMenuList")
    public ResponseResult getMenuList() {

        var elevatorStatusCount = WuyeMenuListRespVO.ElevatorStatusCount.builder().build();
        var historicalRecordQuery = WuyeMenuListRespVO.HistoricalRecordQuery.builder().build();

        try {
            String userId = super.getUserId();

            //获取用户手机号
            HashMap user = bizUserService.getUser(userId);

            //根据手机号获取客户信息
            String customerInfo = maiXinMaintenancePlatformUtil.getCustomerByPhone((String) user.get("vMobile"));


            if (StringUtils.hasText(customerInfo)) {
                elevatorStatusCount.setMaintenanceOverdue(true);
                elevatorStatusCount.setEmergencyRepair(true);
                elevatorStatusCount.setAnnualInspection(true);
                historicalRecordQuery.setMaintenanceOverdue(true);
                historicalRecordQuery.setEmergencyRepair(true);
            }
        } catch (Exception e) {
            log.error("根据用户手机号获取维保系统客户信息失败，e;{}", ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

        WuyeMenuListRespVO res = WuyeMenuListRespVO.builder()
                .elevatorStatusCount(elevatorStatusCount).historicalRecordQuery(historicalRecordQuery).build();

        return ResponseResult.successObj(res);
    }

    /**
     * 物业小程序-获取电梯数量
     *
     * @param needMaintenancePlatform 是否需要维保平台电梯 1：需要
     */
    @GetMapping("/getElevatorCount")
    public ResponseResult getElevatorCount(
            @RequestParam(value = "needMaintenancePlatform", required = false) Integer needMaintenancePlatform) {

        String userId = super.getUserId();

        //麦信电梯列表
        var elevatorList = elevatorService.getElevatorListByUser(userId, bizUserService.isAdmin(getUserId()));
        List<String> registrationCodes = elevatorList.stream().map(TblElevator::getVEquipmentCode).toList();
        long count = registrationCodes.size();

        //维保平台电梯列表
        if (needMaintenancePlatform != null && needMaintenancePlatform == 1) {

            //获取用户手机号
            HashMap user = bizUserService.getUser(userId);

            //根据手机号获取电梯列表
            var maintenanceElevators = maiXinMaintenancePlatformUtil.getElevatorList((String) user.get("vMobile"));

            if (maintenanceElevators != null && maintenanceElevators.size() > 0) {
                //唯一注册码去重
                long maintenanceElevatorCount = maintenanceElevators.stream()
                        .filter(e -> !registrationCodes.contains(e.getRegistrationCode()))
                        .count();

                count = count + maintenanceElevatorCount;
            }

        }

        return ResponseResult.successObj(count);
    }

    /**
     * 物业小程序-获取麦信维保平台维保单列表
     */
    @PostMapping("/getMaintenanceOrderPage")
    public ResponseResult getMaintenanceOrderPage(@Valid @RequestBody MaintenanceOrderPageReqVO reqVO) {

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        //根据手机号获取客户维保单列表
        var maintenanceOrders = maiXinMaintenancePlatformUtil.getMaintenanceOrderPage((String) user.get("vMobile"),
                reqVO.getStartTime(), reqVO.getEndTime(), reqVO.getOrderStatus(), reqVO.getCommunityId(),
                reqVO.getPageNo(), reqVO.getPageSize());

        return ResponseResult.successObj(maintenanceOrders);
    }

    /**
     * 物业小程序-获取小区列表
     */
    @GetMapping("/getVillageList")
    public ResponseResult getVillageList(
            @RequestParam(value = "villageName", required = false) String villageName,
            @RequestParam(value = "needMaintenancePlatform", required = false) Integer needMaintenancePlatform) {

        //麦信平台小区列表
        var villageList = bizVillageService.getVillageList(getUserId(), bizUserService.isAdmin(getUserId()),
                villageName);
        List<String> villageNames = villageList.stream().map(TblVillage::getVVillageName).toList();

        //维保平台小区列表
        if (needMaintenancePlatform != null && needMaintenancePlatform == 1) {

            //获取用户手机号
            var user = bizUserService.getUser(super.getUserId());

            //根据手机号获取客户小区列表
            var maintenanceVillages = maiXinMaintenancePlatformUtil.getVillageList((String) user.get("vMobile"),
                    villageName);

            if (maintenanceVillages != null) {
                //小区去重
                villageList.addAll(maintenanceVillages.stream()
                        .filter(v -> !villageNames.contains(v.getVVillageName()))
                        .toList());
            }

        }

        return ResponseResult.successObj(villageList);
    }

    /**
     * 物业小程序-获取电梯列表
     */
    @GetMapping("/getElevatorListByVillage")
    public ResponseResult getElevatorListByVillage(@RequestParam("villageName") String villageName,
                                                   @RequestParam("villageId") String villageId) {

        //todo:编号和地址搜索条件

        //获取用户收藏电梯
        String key = RedisConstants.MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR + getUserId();
        List<String> elevatorIds = redisTemplate.opsForList().range(key, 0, -1);

        //麦信平台电梯列表
        var elevatorList = elevatorService.getElevatorListByVillage(getUserId(), bizUserService.isAdmin(getUserId()),
                villageId);
        var registrationCodes = elevatorList.stream()
                .map(TblElevator::getVEquipmentCode)
                .toList();
        //是否收藏
        var mxElevators = elevatorList.stream().map(e -> {

            if (elevatorIds.contains(e.getVElevatorId())) {
                WuyeElevatorRespVO wuyeElevatorRespVO = BeanUtil.toBean(e, WuyeElevatorRespVO.class);
                wuyeElevatorRespVO.setIsCollect(1);
                return wuyeElevatorRespVO;
            } else {
                WuyeElevatorRespVO wuyeElevatorRespVO = BeanUtil.toBean(e, WuyeElevatorRespVO.class);
                return wuyeElevatorRespVO;
            }

        }).collect(Collectors.toList());

        //维保平台电梯列表

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        //根据手机号获取客户小区列表
        var maintenanceElevators = maiXinMaintenancePlatformUtil.getElevatorListByVillageName(
                (String) user.get("vMobile"), villageName);

        List<MaintenancePlatformElevatorRespDTO> maintenanceElevatorList = Collections.emptyList();
        if (maintenanceElevators != null) {
            //去重
            maintenanceElevatorList = maintenanceElevators.stream()
                    .filter(e -> !registrationCodes.contains(e.getRegistrationCode()))
                    .collect(Collectors.toList());
            if (!elevatorIds.isEmpty()) {
                maintenanceElevatorList = maintenanceElevatorList.stream().map(e -> {

                    if (elevatorIds.contains(e.getId())) {
                        e.setIsCollect(1);
                    }
                    return e;
                }).collect(Collectors.toList());
            }
        }
        var res = Map.of("maintenanceElevatorList", maintenanceElevatorList,
                "mashineElevatorList", mxElevators);
        return ResponseResult.successObj(res);

    }

    /**
     * 物业小程序-本月电梯维保逾期数量、年检数量
     */
    @GetMapping("/getMaintenanceOrderAndAnnualInspectionNum")
    public ResponseResult getMaintenanceOrderAndAnnualInspectionNum() {

        //获取用户手机号
        HashMap user = bizUserService.getUser(super.getUserId());

        Date date = new Date();
        String startTime = DateUtil.format(DateUtil.beginOfMonth(date), "yyyy-MM-dd HH:mm:ss");
        String endTime = DateUtil.format(DateUtil.endOfMonth(date), "yyyy-MM-dd HH:mm:ss");

        //根据手机号获取客户维保单列表
        var maintenanceOrders = maiXinMaintenancePlatformUtil.getMaintenanceOrderPage((String) user.get("vMobile"),
                startTime, endTime, 1, null, 1, 100000);

        int maintenanceOrderNum = 0;
        int annualInspectionNum = 0;
        if (maintenanceOrders != null) {
            maintenanceOrderNum = maintenanceOrders.getList().size();
        }
        var res = Map.of("maintenanceOrderNum", maintenanceOrderNum,
                "annualInspectionNum", annualInspectionNum);
        return ResponseResult.successObj(res);

    }

    /**
     * 添加收藏电梯
     */
    @PostMapping("/insertCollectElevator")
    public ResponseResult insertCollectElevator(@RequestParam("elevatorId") String elevatorId) {

        String key = RedisConstants.MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR + getUserId();

        redisTemplate.opsForList().rightPush(key, elevatorId);

        return ResponseResult.successObj("success");
    }

    /**
     * 取消收藏电梯
     */
    @PostMapping("/cancelCollectElevator")
    public ResponseResult cancelCollectElevator(@RequestParam("elevatorId") String elevatorId) {

        String key = RedisConstants.MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR + getUserId();

        redisTemplate.opsForList().remove(key, 0L, elevatorId);

        return ResponseResult.successObj("success");
    }

    /**
     * 获取收藏电梯列表
     */
    @GetMapping("/searchElevatorCollectList")
    public ResponseResult searchElevatorCollectList() {

        //获取电梯收藏ids
        String key = RedisConstants.MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR + getUserId();
        List<String> elevatorIds = redisTemplate.opsForList().range(key, 0, -1);

        if (elevatorIds.isEmpty()) {
            return ResponseResult.successObj(null);
        }

        //根据ids获取麦信平台电梯列表
        String userId = getUserId();
        var mxElevators = elevatorService.getElevatorListByIds(userId, bizUserService.isAdmin(userId), elevatorIds);
        var mxElevatorIds = mxElevators.stream().map(TblElevator::getVElevatorId).toList();

        //去重
        elevatorIds.removeAll(mxElevatorIds);
        //根据ids获取维保平台电梯列表
        var maintenanceElevators = maiXinMaintenancePlatformUtil.getElevatorListByIds(elevatorIds);
        var res = Map.of("maintenanceElevatorList", CollectionUtils.isEmpty(maintenanceElevators)
                        ? Collections.emptyList() : maintenanceElevators,
                "mashineElevatorList", mxElevators);
        return ResponseResult.successObj(res);
    }


    /**
     * 获取故障列表(仪电推送故障)
     */
    @PostMapping("/searchFaultList")
    public Object searchFaultList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = faultService.searchFaultsListWithPage(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    @PostMapping("/villagesCount")
    public Object getVillageCount(@RequestBody SearchFaultModule searchFaultModule) {
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
     * 城桥大屏接口
     */
    @PostMapping("/villagesCountRateCQ")
    public Object getVillageCountRateCQ(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(super.getUserId());
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        return ResponseResult.successObj(faultService.getVillageCountRateCQ(searchFaultModule));
    }

    /**
     * 智能监管
     */
    @PostMapping("/IntelligentSupervision")
    public Object intelligentSupervision(
            @RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(eventService.intelligentSupervision(faultStatisticalQuantitySearchModule));
    }

    /**
     * 智能监管, 城桥
     */
    @PostMapping("/IntelligentSupervisionCQ")
    public Object intelligentSupervisionCQ(
            @RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(eventService.intelligentSupervisionCQ(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取维保记录
     */
    @PostMapping("/queryMaintenanceList")
    public ResponseEntity queryMaintenanceList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(maintenanceService.searchMaintenanceList(searchFaultModule));
    }

    /**
     * 获取电梯基本信息
     */
    @PostMapping("/getElevatorInfo")
    public Object getElevatorInfo(
            @RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        return ResponseResult.successObj(elevatorService.getElevatorInfo(faultStatisticalQuantitySearchModule));
    }

    /**
     * 获取故障数据统计
     */
    @PostMapping("/getFaultDataStatistics")
    public Object getFaultDataStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getFaultStatistics(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 获取故障数据统计，电梯分组
     */
    @PostMapping("/getFaultDataStatisticsGroupByElevator")
    public Object getFaultDataStatisticsGroupByElevator(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getFaultDataStatisticsGroupByElevator(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }
    @PostMapping("/getFaultDataStatisticsGroupByType")
    public Object getFaultDataStatisticsGroupByType(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setIsAdmin(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        Map<String, List> result = faultService.getFaultDataStatisticsGroupByType(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }
    /**
     * 急修工单趋势
     */
    @PostMapping("/getRepairTrend")
    public Object getRepairTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getRepairTrend(faultStatisticsModule));
    }

    /**
     * 维保工单趋势
     */
    @PostMapping("/getMaintenanceTrend")
    public Object getMaintenanceTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getMaintenanceTrend(faultStatisticsModule));
    }

    /**
     * 急修工单趋势
     */
    @PostMapping("/getFaultTrend")
    public Object getFaultTrend(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        return ResponseResult.successObj(faultService.getFaultTrend(faultStatisticsModule));
    }

    /**
     * 柱状热力图
     */
    @PostMapping("/elevatorHeatMapNew")
    public Object elevatorHeatMapNew(
            @RequestBody FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        faultStatisticalQuantitySearchModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticalQuantitySearchModule.setUserId(getUserId());
        elevatorService.getElevatorHeatMapNew(faultStatisticalQuantitySearchModule);
        return ResponseResult.successObj(elevatorService.getElevatorHeatMapNew(faultStatisticalQuantitySearchModule));
    }

    /**
     * 天气预报
     */
    @GetMapping("/getWeatherInfo")
    public Object getWeatherInfo(@RequestParam(name = "location", required = false) String location) {
        return ResponseResult.successObj(elevatorService.getWeatherInfo(location));
    }

    /**
     * 获取小区列表
     */
    @PostMapping("/searchVillageList")
    public Object searchVillageList(@RequestBody SearchVillaListModule searchVillaListModule) {

        //1. 查找有权查看的项目
        // 1. 递归 查找 部门id list
        // 1.1 递归找到有权查看的的部门编号
        JSONObject userDept = bizDeptService.getUserDept(super.getUserId());
        String deptId = userDept.getString("dept_id");
        // 2. 找到当前用户所属部门编号 查询 所有子部门 及 当前部门 list 准备查找项目参数
        //获取所有部门缓存
        String key = RedisConstants.USER_DEPT_INFO + deptId;
        List<String> results = (List<String>) redisTemplate.opsForValue().get(key);

        if (results == null) {
            results = new ArrayList<>();
            recursion(deptId, results);
            if (!results.contains(deptId)) {
                results.add(deptId);
            }
            redisTemplate.opsForValue().set(key, results);

        }
        searchVillaListModule.setPermissionDeptIds((ArrayList<String>) results);
        searchVillaListModule.setAdminFlag(bizUserService.isAdmin(getUserId()));
        searchVillaListModule.setUserId(getUserId());
        return ResponseResult.successObj(bizVillageService.searchVillageListWithElevator(searchVillaListModule));
    }

    /**
     * 递归查询 下级部门的编号
     */
    private void recursion(String deptId, List<String> strings) {

        if (null != deptId) {
            List<String> userDeptIds = bizUserService.getUserDeptIds(deptId);
            if (null != userDeptIds && userDeptIds.size() > 0) {
                userDeptIds.forEach(id -> {
                    recursion(id, strings);
                });
            }
            strings.addAll(userDeptIds);
        }
    }
}
