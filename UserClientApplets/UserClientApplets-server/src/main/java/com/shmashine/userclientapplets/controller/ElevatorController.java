package com.shmashine.userclientapplets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.userclientapplets.entity.BaseRequestEntity;
import com.shmashine.userclientapplets.entity.ResponseResult;
import com.shmashine.userclientapplets.entity.SearchElevatorModule;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.service.EventService;
import com.shmashine.userclientapplets.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * 电梯接口
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/8 13:52
 */
@RestController
@RequestMapping("/elevator")
@Tag(name = "小程序电梯接口", description = "小程序电梯接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class ElevatorController extends BaseRequestEntity {

    @Autowired
    private ElevatorService elevatorService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    /**
     * 首页提示：根据每日消息进行更新，比如今日故障及今日需维保等信息，在提醒中进行信息滚动显示
     */
    @Operation(summary = "根据每日消息进行更新，比如今日故障及今日需维保等信息，在提醒中进行信息滚动显示", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getWarningByDay")
    public ResponseEntity getWarningByDay() {

        return ResponseEntity.ok(elevatorService.getWarningByDay(super.getUserId(),
                userService.isAdmin(super.getUserId())));
    }

    /**
     * 本月电梯状况：本月困人、维保是自然月，急修、隐患是当日；年检是自然年
     */
    @Operation(summary = "本月电梯状况：本月困人、维保是自然月，急修、隐患是当日；年检是自然年", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/queryElevatorStatus")
    public ResponseEntity queryElevatorStatus(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(elevatorService.queryElevatorStatus(searchFaultModule));
    }

    /**
     * 添加收藏电梯
     */
    @Operation(summary = "添加收藏电梯", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/insertCollectElevator")
    public ResponseEntity insertCollectElevator(@RequestParam("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.insertCollectElevator(elevatorId, super.getUserId()));
    }

    /**
     * 取消收藏电梯
     */
    @Operation(summary = "取消收藏电梯", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/cancelCollectElevator")
    public ResponseEntity cancelCollectElevator(@RequestParam("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.cancelCollectElevator(elevatorId, super.getUserId()));
    }

    /**
     * 获取收藏电梯列表
     */
    @Operation(summary = "获取收藏电梯列表", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/searchElevatorCollectList")
    public ResponseEntity searchElevatorCollectList() {
        return ResponseEntity.ok(elevatorService.searchElevatorCollectList(super.getUserId()));
    }

    /**
     * 获取电梯列表_分页
     */
    @Operation(summary = "获取电梯列表_分页", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorByPage")
    public ResponseEntity getElevatorByPage(@RequestBody SearchElevatorModule searchElevatorModule) {
        searchElevatorModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        return ResponseEntity.ok(elevatorService.queryElevatorAndCollectList(searchElevatorModule));
    }

    /**
     * 获取电梯详情
     *
     * @param elevatorId 电梯id
     */
    @SaIgnore
    @Operation(summary = "获取电梯详情", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getElevatorInfoById/{elevatorId}")
    public ResponseEntity getElevatorInfoById(@PathVariable("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.getElevatorInfoById(elevatorId));
    }

    /**
     * 获取健康雷达图
     */
    @SaIgnore
    @Operation(summary = "获取健康雷达图", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getHealthRadarChart/{elevatorId}")
    public ResponseEntity getHealthRadarChart(@PathVariable("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.getHealthRadarChart(elevatorId));
    }

    /**
     * 获取电梯统计信息
     *
     * @param elevatorId 电梯id
     */
    @SaIgnore
    @Operation(summary = "获取电梯统计信息", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getElevatorCountInfoById/{elevatorId}")
    public ResponseEntity getElevatorCountInfo(@PathVariable("elevatorId") String elevatorId) {
        return ResponseEntity.ok(elevatorService.getElevatorCountInfo(elevatorId));
    }

    /**
     * 获取电梯热力图
     *
     * @param searchElevatorModule 请求体
     */
    @SaIgnore
    @Operation(summary = "获取电梯热力图", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getElevatorHeatMap")
    public ResponseEntity getElevatorHeatMap(@RequestBody SearchElevatorModule searchElevatorModule) {
        return ResponseEntity.ok(elevatorService.getElevatorHeatMap(searchElevatorModule));
    }

    /**
     * 获取授权电梯
     *
     * @param permission 是否授权 0:未授权，1：已授权，2：全部
     */
    @Operation(summary = "获取授权电梯", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getPermissionElevators")
    public ResponseResult getPermissionElevators(
            @RequestParam(value = "permission", required = false) Integer permission,
            @RequestParam("userId") String userId,
            @RequestHeader(value = "villageId", required = false) String villageId,
            @RequestHeader(value = "vProjectId", required = false) String vProjectId) {

        return elevatorService.getPermissionElevators(getUserId(), userService.isAdmin(getUserId()), userId,
                permission, villageId, vProjectId);
    }

    /**
     * 获取故障电梯，按电梯故障数排序
     */
    @Operation(summary = "获取故障电梯，按电梯故障数排序", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getFaultElevatorByPage")
    public ResponseEntity getFaultElevatorByPage(@RequestBody SearchElevatorModule searchElevatorModule) {

        searchElevatorModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        return ResponseEntity.ok(elevatorService.getFaultElevatorByPage(searchElevatorModule));
    }

    /**
     * 获取年检电梯
     */
    @Operation(summary = "获取年检电梯", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getUnAnnualInspectionElevatorByPage")
    public ResponseEntity getUnAnnualInspectionElevatorByPage(@RequestBody SearchFaultModule searchFaultModule) {

        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(eventService.getUnAnnualInspectionElevatorByPage(searchFaultModule));
    }

    /**
     * 获取待维保电梯
     */
    @Operation(summary = "获取待维保电梯", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getUnMaintenanceElevatorByPage")
    public ResponseEntity getUnMaintenanceElevatorByPage(@RequestBody SearchElevatorModule searchElevatorModule) {

        searchElevatorModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchElevatorModule.setUserId(getUserId());
        return ResponseEntity.ok(elevatorService.getUnMaintenanceElevatorByPage(searchElevatorModule));
    }

}
