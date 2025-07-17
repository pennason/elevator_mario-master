package com.shmashine.api.controller.litterScreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultService;
import com.shmashine.api.service.user.BizUserService;

import lombok.RequiredArgsConstructor;

/**
 * 监控小屏接口
 *
 * @author jiangheng
 * @version 1.0 2021/7/29 10:24
 */
@RestController
@RequestMapping("/litterScreen")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LitterScreen extends BaseRequestEntity {

    private final BizElevatorService elevatorService;

    private final BizFaultService faultService;

    private final BizUserService bizUserService;

    /**
     * 获取电梯在线率统计
     */
    @SaIgnore
    @GetMapping("/getElevatorOnlineRate")
    public ResponseEntity getElevatorOnlineRate(@RequestParam("projectId") String projectId) {
        return ResponseEntity.ok(elevatorService.getElevatorOnlineRate(projectId));
    }

    /**
     * 获取传感器故障统计
     */
    @SaIgnore
    @GetMapping("/getSensorFaultRate")
    public ResponseEntity getSensorFaultRate(@RequestParam("projectId") String projectId) {
        return ResponseEntity.ok(faultService.getSensorFaultRate(projectId));
    }

    /**
     * 获取流量卡统计
     */
    @SaIgnore
    @GetMapping("/getIotCardRate")
    public ResponseEntity getIotCardRate(@RequestParam("projectId") String projectId) {
        return ResponseEntity.ok(elevatorService.getIotCardRate(projectId));
    }

    /**
     * 周困人次数统计
     */
    @GetMapping("/tiringStatistics")
    public ResponseEntity tiringStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.tiringStatistics(userId, isAdmin, projectId));
    }

    /**
     * 周检修次数（每天最多一次（非平层停梯））
     */
    @GetMapping("/maintenanceStatistics")
    public ResponseEntity maintenanceStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.maintenanceStatistics(userId, isAdmin, projectId));
    }

    /**
     * 周故障次数（周柱状图）
     */
    @GetMapping("/faultStatistics")
    public ResponseEntity faultStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.faultStatistics(userId, isAdmin, projectId));
    }

    /**
     * 日运行频次
     * （用每天运行次数来表达  日柱状图）
     */
    @GetMapping("/runStatistics")
    public ResponseEntity runStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.runStatistics(userId, isAdmin, projectId));
    }

    /**
     * 周电瓶车入梯次数（周柱状图）
     */
    @GetMapping("/bicycleStatistics")
    public ResponseEntity bicycleStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.bicycleStatistics(userId, isAdmin, projectId));
    }

    /**
     * 反复阻挡门次数（周柱状图）
     */
    @GetMapping("/stopTheDoorStatistics")
    public ResponseEntity stopTheDoorStatistics(@RequestParam("projectId") String projectId) {
        String userId = getUserId();
        boolean isAdmin = bizUserService.isAdmin(super.getUserId());
        return ResponseEntity.ok(faultService.stopTheDoorStatistics(userId, isAdmin, projectId));
    }

    /**
     * 雷达图：
     * 在线率：在线率低于90%预警，最大值100%  每一阶梯为95%  90%  85% 80%（含低于）
     * 设备完好率：传感器无故障电梯/总电梯  低于90%告警  最大值100%  每一阶梯为95%  90%  85% 80%（含低于）
     * 流量池正常率：卡数量-流量卡超80%数量/卡数 低于90%告警  最大值100%  每一阶梯为95%  90%  85% 80%（含低于）
     * 北向完好率：在线数/总对接设备数   低于90%预警，最高100%，每一阶梯为90%，80%，70%，低于60%
     * 视频在线率：在线数/总对接设备数   低于90%预警，最高100%，每一阶梯为90%，80%，70%，低于60%
     * 电梯正常率：运行总次数 - 当日故障数/当日电梯运行总次数  低于90%预警，最高100%，每一阶梯为90%，80%，70%，低于60%
     */
    @SaIgnore
    @GetMapping("/radarChart")
    public ResponseEntity radarChart(@RequestParam("projectId") String projectId) {
        return ResponseEntity.ok(elevatorService.radarChart(projectId));
    }

}
