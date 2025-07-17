package com.shmashine.api.controller.fault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.controller.fault.vo.EventStatisticsReqVO;
import com.shmashine.api.controller.fault.vo.SearchFaultsReqVO;
import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.fault.input.FaultStatisticsModule;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.fault.input.SearchSensorFaultModule;
import com.shmashine.api.service.fault.BizFaultService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.util.ExceExport2;

/**
 * 故障接口
 */
@RestController
@RequestMapping("/fault")
public class FaultController extends BaseRequestEntity {


    private final BizFaultService faultService;

    private final BizUserService bizUserService;


    @Autowired
    public FaultController(BizFaultService faultService, BizUserService bizUserService) {
        this.faultService = faultService;
        this.bizUserService = bizUserService;
    }


    /**
     * 获取故障列表 或 不文明行为列表
     */
    @PostMapping("/searchFaultList")
    public Object searchFaultList(@RequestBody SearchFaultsReqVO searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        var pageListResultEntity = faultService.searchFaultSList(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }


    /**
     * 获取电梯被屏蔽的故障列表
     *
     * @param elevatorCode 电梯编号
     * @return 屏蔽列表
     */
    @GetMapping("/searchShieldedFaultList/{elevatorCode}")
    public ResponseResult searchShieldedFaultList(@PathVariable("elevatorCode") String elevatorCode) {
        return faultService.searchShieldedFaultList(elevatorCode);
    }


    /**
     * 根据参数，导出Excel文件
     */
    @RequestMapping("/excel")
    public void exportExcel(@RequestBody SearchFaultsReqVO searchFaultModule, HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        //创建一个数组用于设置表头
        String[] arr = new String[]{"故障编号", "电梯编号", "电梯名称", "故障级别", "安装地址", "上报时间", "故障类型", "故障名称",
                "上报次数", "服务模式", "故障状态", "终端类型"};
        //调用Excel导出工具类
        var faultDetailDownloadModuleMaps = faultService.searchFaultListDownload(searchFaultModule);
        ExceExport2.export(response, faultDetailDownloadModuleMaps, arr);

    }

    /**
     * 根据参数，导出不文明行为Excel文件
     */
    @RequestMapping("/exportUncivilizedBehavior")
    public void exportUncivilizedBehaviorExcel(@RequestBody SearchFaultsReqVO searchFaultModule,
                                               HttpServletResponse response) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        //创建一个数组用于设置表头
        String[] arr = new String[]{"电梯编号", "级别", "安装地址", "上报时间", "故障类型", "上报次数", "服务模式", "不文明行为状态"};
        //调用Excel导出工具类
        var faultDetailDownloadModuleMaps = faultService.searchUncivilizedBehaviorListDownload(searchFaultModule);
        ExceExport2.export(response, faultDetailDownloadModuleMaps, arr);

    }

    /**
     * 根据参数，导出不文明行为Excel文件
     */
    @RequestMapping("/exportOnOffLine")
    public void exportEventRecordExcel(@RequestBody FaultStatisticsModule faultStatisticsModule,
                                       HttpServletResponse response) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        //创建一个数组用于设置表头
        String[] arr = new String[]{"电梯编号", "设备类型", "事件类型", "原因", "时间"};
        //调用Excel导出工具类
        var eventElevatorDownloadModuleMap = faultService.searchEventElevatorDownload(faultStatisticsModule);
        ExceExport2.export(response, eventElevatorDownloadModuleMap, arr);

    }

    /**
     * 获取实时故障
     *
     * @param elevatorCode 电梯编号
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/searchRealTimeFault/{elevatorCode}")
    public Object searchRealTimeFault(@PathVariable("elevatorCode") String elevatorCode) {
        SearchFaultsReqVO searchFaultModule = new SearchFaultsReqVO();
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        searchFaultModule.setvElevatorCode(elevatorCode);
        searchFaultModule.setiStatus(0);

        List<Map> maps = faultService.searchRealTimeFault(searchFaultModule);
        return ResponseResult.successObj(maps);
    }


    /**
     * 获取故障详情
     *
     * @param faultId 故障id
     *                cameraType 摄像头类型 1：海康，2：雄迈
     *                前端展示故障视频，如果videoUrl和海康则使用privateUrl都存在，则对两个视频都进行播放
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @SaIgnore
    @GetMapping("/getFaultDetail/{faultId}")
    public Object getFaultDetail(@PathVariable("faultId") String faultId) {
        Map<String, Object> faultSDetail = faultService.getFaultSDetail(faultId);
        return ResponseResult.successObj(faultSDetail);
    }

    /**
     * 获取故障取证文件
     */
    @SaIgnore
    @GetMapping("/getFaultMediaFile/{faultId}")
    public ResponseResult getFaultMediaFile(@PathVariable("faultId") String faultId) {

        return faultService.getFaultMediaFile(faultId);
    }

    /**
     * 故障统计接口 或 不文明行为统计
     */
    @SaIgnore
    @PostMapping("/statistics")
    public Object getFaultStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule) {
        var userId = getUserId();
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(userId));
        faultStatisticsModule.setUserId(userId);
        Map<String, Object> result = faultService.getFaultStatistics(faultStatisticsModule);
        return ResponseResult.successObj(result);
    }

    /**
     * 故障统计接口 或 不文明行为统计 导出
     */
    @PostMapping("/exportFaultStatistics")
    public void exportFaultStatistics(@RequestBody FaultStatisticsModule faultStatisticsModule,
                                      HttpServletResponse response) {
        faultStatisticsModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        faultStatisticsModule.setUserId(getUserId());
        faultService.exportFaultStatistics(faultStatisticsModule, response);
    }

    /**
     * 获取电梯上下线统计数据
     *
     * @param eventStatisticsReqVO 请求参数
     * @return 统计结果
     */
    @PostMapping("/getElevatorEventStatistics")
    public Object getElevatorEventStatistics(@RequestBody EventStatisticsReqVO eventStatisticsReqVO) {
        eventStatisticsReqVO.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        eventStatisticsReqVO.setUserId(getUserId());
        return faultService.getElevatorEventStatistics(eventStatisticsReqVO);
    }

    /**
     * 昨日故障占比
     */
    @GetMapping("/yesterdayStatistics")
    public Object getYesterdayFaultStatistics(
            @RequestParam(value = "projectIds", required = false) String projectIdsString,
            @RequestParam(value = "villageIds", required = false) String villageIdsString) {
        // 不可使用 stream 的 collect, jdk 不兼容
        ArrayList<String> projectIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(projectIdsString)) {
            Arrays.stream(projectIdsString.split(",")).map(String::trim)
                    .forEach(projectIds::add);
        }
        ArrayList<String> villageIds = new ArrayList<>();
        if (org.springframework.util.StringUtils.hasText(villageIdsString)) {
            Arrays.stream(villageIdsString.split(",")).map(String::trim)
                    .forEach(villageIds::add);
        }
        var yesterdayFaultStatistics = faultService.getYesterdayFaultStatistics(
                bizUserService.isAdmin(super.getUserId()), super.getUserId(), projectIds, villageIds);
        return ResponseResult.successObj(yesterdayFaultStatistics);
    }

    /**
     * 单梯历史故障占比
     *
     * @param elevatorId ID
     * @return #type:com.shmashine.api.entity.base.ResponseResult#
     */
    @GetMapping("/getHisFaultStatistics/{elevatorId}")
    public Object getHisFaultStatistics(@PathVariable("elevatorId") String elevatorId) {
        var res = faultService.getHisFaultStatistics(bizUserService.isAdmin(super.getUserId()), super.getUserId(),
                elevatorId);
        return ResponseResult.successObj(res);
    }

    /**
     * 获取
     * 停梯率=故障（包含困人）停梯时间/运行时间
     * 困人率=困人次数/运行次数
     * 故障率=故障次数/运行次数
     *
     * @param elevatorCode 电梯编号
     * @param startTime    开始时间
     * @param endTime      结束时间
     * @return 统计结果
     */
    @SaIgnore
    @PostMapping("/getElevatorStopStatistics")
    public ResponseResult getElevatorStopStatistics(
            @RequestParam("elevatorCode") String elevatorCode,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return ResponseResult.successObj(faultService.getElevatorStopStatistics(elevatorCode, startTime, endTime));
    }

    /**
     * 单梯不文明行为柱状图统计
     */
    @GetMapping("/getHospitalizationIonFaultStatistics/{elevatorId}")
    public Object getHospitalizationIonFaultStatistics(@PathVariable("elevatorId") String elevatorId) {
        var res = faultService.getHospitalizationIonFaultStatistics(bizUserService.isAdmin(super.getUserId()),
                super.getUserId(), elevatorId);
        return ResponseResult.successObj(res);
    }

    /**
     * 取消故障(手动恢复故障)
     */
    @PostMapping("/cancel")
    public Object cancelFault(@RequestBody @Valid @NotNull(message = "缺少故障唯一标识") String faultId) {
        faultService.cancelFault(faultId);
        return ResponseResult.successObj("success!");
    }


    /**
     * 查询传感器故障列表
     */
    @PostMapping("/searchSensorFaultList")
    public Object searchSensorFaultList(@RequestBody SearchSensorFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        var pageListResultEntity = faultService.searchSensorFaultSList(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    /**
     * 查询传感器故障列表下载
     */
    @PostMapping("/searchSensorFaultDownload")
    public void searchSensorFaultDownload(@RequestBody SearchSensorFaultModule searchSensorFaultModule,
                                          HttpServletResponse response) {
        searchSensorFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchSensorFaultModule.setUserId(getUserId());
        faultService.exportSensorFaultList(searchSensorFaultModule, response);
    }

    /**
     * 获取传感器故障详情
     *
     * @param faultId 故障id
     */
    @SaIgnore
    @GetMapping("/getSensorFaultDetail/{faultId}")
    public Object getSensorFaultDetail(@PathVariable("faultId") String faultId) {
        Map<String, Object> faultSDetail = faultService.getSensorFaultSDetail(faultId);
        return ResponseResult.successObj(faultSDetail);
    }

    /**
     * 取消传感器故障(手动恢复故障)
     */
    @PostMapping("/cancelSensorFault")
    public Object cancelSensorFault(@RequestBody @Valid @NotNull(message = "缺少故障唯一标识") String faultId) {
        faultService.cancelSensorFault(faultId);
        return ResponseResult.successObj(null);
    }

    /**
     * 获取离线一小时告警故障列表
     */
    @PostMapping("/searchTimeoutFaultList")
    public Object searchTimeoutFaultList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(bizUserService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        PageListResultEntity pageListResultEntity = faultService.searchTimeoutFaultList(searchFaultModule);
        return ResponseResult.successObj(pageListResultEntity);
    }

    @GetMapping("/cancelTimeOutFault/{id}")
    public Object cancelTimeOutFault(@PathVariable("id") String id) {
        faultService.cancelTimeOutFault(id);
        return ResponseEntity.ok("success");
    }

    /**
     * 根据电梯code删除该梯所有故障
     */
    @PostMapping("/delFaultByElevatorCode")
    public ResponseResult delFaultByElevatorCode(@RequestParam("elevatorCode") String elevatorCode) {
        return faultService.delFaultByElevatorCode(elevatorCode);
    }

    /**
     * 根据故障id删除故障
     */
    @PostMapping("/delFaultById")
    public ResponseResult delFaultById(@RequestParam("faultId") String faultId) {
        return faultService.delFaultById(faultId);
    }

}
