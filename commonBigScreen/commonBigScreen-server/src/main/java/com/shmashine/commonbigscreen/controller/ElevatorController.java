package com.shmashine.commonbigscreen.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.commonbigscreen.entity.BaseRequestEntity;
import com.shmashine.commonbigscreen.entity.ResponseResult;
import com.shmashine.commonbigscreen.entity.SearchElevatorModule;
import com.shmashine.commonbigscreen.entity.SearchElevatorStatus;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.ElevatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 电梯接口
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/3 16:07
 */
@RestController
@RequestMapping("/elevator")
@Tag(name = "大屏电梯接口", description = "大屏电梯接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ElevatorController extends BaseRequestEntity {

    @Autowired
    private ElevatorService elevatorService;

    /**
     * 电梯统计
     */
    @Operation(summary = "电梯统计", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorCount")
    public ResponseEntity getElevatorCount(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setUserId(super.getUserId());
        return ResponseEntity.ok(elevatorService.getElevatorCount(searchElevatorModule));
    }

    /**
     * 根据小区统计电梯数
     *
     * @param searchElevatorModule 请求体
     */
    @Operation(summary = "根据小区统计电梯数", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorCountByVillage")
    public ResponseEntity getElevatorCountByVillage(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setUserId(super.getUserId());
        return ResponseEntity.ok(elevatorService.getElevatorCountByVillage(searchElevatorModule));
    }

    /**
     * 智能监管
     *
     * @param searchElevatorModule 请求体
     */
    @Operation(summary = "智能监管", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/intelligentSupervision")
    public ResponseEntity intelligentSupervision(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setUserId(super.getUserId());
        return ResponseEntity.ok(elevatorService.intelligentSupervision(searchElevatorModule));
    }

    /**
     * 获取电梯基础信息
     *
     * @param elevatorCode 电梯编号
     */
    @SaIgnore
    @PostMapping("/getElevatorBaseInfo")
    public ResponseEntity getElevatorBaseInfo(@RequestParam("elevatorCode") String elevatorCode) {
        return ResponseEntity.ok(elevatorService.getElevatorBaseInfo(elevatorCode));
    }

    /**
     * 获取楼宇地图
     */
    @Operation(summary = "获取楼宇地图", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/searchVillageMap")
    public ResponseEntity searchVillageMap() {
        return ResponseEntity.ok(elevatorService.searchVillageMap(super.getUserId()));
    }

    /**
     * 热力图
     *
     * @param searchFaultModule 请求体
     */
    @Operation(summary = "热力图", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorHeatMap")
    public ResponseEntity getElevatorHeatMap(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(super.getUserId());
        return ResponseEntity.ok(elevatorService.getElevatorHeatMap(searchFaultModule));
    }

    /**
     * 维保人员&安全管理员
     */
    @SaIgnore
    @PostMapping("/getElevatorSafetyAdministratorAndMaintainer")
    public ResponseEntity getElevatorSafetyAdministratorAndMaintainer(
            @RequestParam("registerNumber") String registerNumber) {
        return ResponseEntity.ok(elevatorService.getElevatorSafetyAdministratorAndMaintainer(registerNumber));
    }

    /**
     * 视频流地址
     *
     * @param elevatorId 电梯id
     */
    @SaIgnore
    @PostMapping("/getElevatorVideoUrl")
    public ResponseEntity getElevatorVideoUrl(@RequestParam("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.getElevatorVideoUrl(elevatorId));
    }

    /**
     * 电梯运行状态-仪电工单数据
     *
     * @param searchElevatorStatus 电梯运行状态
     */
    @Operation(summary = "电梯运行状态-仪电工单数据", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorsStatus")
    public ResponseEntity getElevatorsStatus(@RequestBody @Valid SearchElevatorStatus searchElevatorStatus) {
        searchElevatorStatus.setUserId(super.getUserId());
        return ResponseEntity.ok(elevatorService.getElevatorsStatus(searchElevatorStatus));
    }

    /**
     * 电梯运行状态-麦信数据
     *
     * @param searchElevatorStatus 电梯运行状态
     */
    @Operation(summary = "电梯运行状态-麦信数据", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorsStatusWithMxData")
    public ResponseResult getElevatorsStatusWithMxData(@RequestBody SearchElevatorStatus searchElevatorStatus) {
        searchElevatorStatus.setUserId(super.getUserId());
        return ResponseResult.successObj(elevatorService.getElevatorsStatusWithMxData(searchElevatorStatus));
    }

    /**
     * 获取电梯健康度
     *
     * @param elevatorId 电梯id
     */
    @SaIgnore
    @GetMapping("/getHealthRadarChart/{elevatorId}")
    public ResponseEntity getHealthRadarChart(@PathVariable("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.getHealthRadarChart(elevatorId));
    }

    /**
     * 获取楼宇id
     */
    @Operation(summary = "获取楼宇id", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getBuildId")
    public ResponseEntity getBuildId() {
        return ResponseEntity.ok(elevatorService.getBuildId(getUserId()));
    }

    /**
     * 根据楼宇id获取电梯
     *
     * @param buildId 楼宇id
     */
    @SaIgnore
    @PostMapping("/searchElevatorByBuildId")
    public ResponseEntity searchElevatorByBuildId(@RequestParam("buildId") String buildId) {
        return ResponseEntity.ok(elevatorService.searchElevatorByBuildId(buildId));
    }

}
