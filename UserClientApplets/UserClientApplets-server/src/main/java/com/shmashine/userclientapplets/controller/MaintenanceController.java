package com.shmashine.userclientapplets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.annotation.SaIgnore;

import com.shmashine.userclientapplets.entity.BaseRequestEntity;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.service.MaintenanceService;
import com.shmashine.userclientapplets.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 维保接口
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/15 16:55
 */

@RestController
@RequestMapping("/maintenance")
@Tag(name = "小程序维保接口", description = "小程序维保接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class MaintenanceController extends BaseRequestEntity {

    @Autowired
    private UserService userService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private ElevatorService elevatorService;

    /**
     * 维保列表
     */
    @Operation(summary = "维保列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/queryMaintenanceList")
    public ResponseEntity queryMaintenanceList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(maintenanceService.queryMaintenanceList(searchFaultModule));
    }

    /**
     * 获取维保详情
     *
     * @param workOrderNumber 工单号
     */
    @SaIgnore
    @Operation(summary = "获取维保详情", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getMaintenanceById/{workOrderNumber}")
    public ResponseEntity getMaintenanceById(@PathVariable("workOrderNumber") String workOrderNumber) {

        var maintenance = maintenanceService.getMaintenanceById(workOrderNumber);
        elevatorService.extendElevatorNameAndAddress(maintenance);

        return ResponseEntity.ok(maintenance);
    }

    /**
     * 获取年检信息记录
     */
    @Operation(summary = "获取年检信息记录", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/queryAnnualInspectionList")
    public ResponseEntity queryAnnualInspectionList(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(maintenanceService.queryAnnualInspectionList(searchFaultModule));
    }
}
