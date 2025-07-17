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
import com.shmashine.userclientapplets.service.EventService;
import com.shmashine.userclientapplets.service.FaultService;
import com.shmashine.userclientapplets.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * FaultController
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/8 13:55
 */

@RestController
@RequestMapping("/fault")
@Tag(name = "小程序故障接口", description = "小程序故障接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FaultController extends BaseRequestEntity {

    private final UserService userService;
    private final FaultService faultService;
    private final EventService eventService;

    /**
     * 困人报警
     */
    @Operation(summary = "困人报警", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getTrappedPeopleStatus")
    public ResponseEntity getTrappedPeopleStatus() {
        return ResponseEntity.ok(faultService.getTrappedPeopleStatus(getUserId(),
                userService.isAdmin(super.getUserId())));
    }

    /**
     * 获取困人列表
     *
     * @param searchFaultModule 请求体
     */
    @Operation(summary = "获取困人列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/queryTrappedPeopleByPage")
    public ResponseEntity queryTrappedPeopleByPage(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(faultService.queryTrappedPeopleByPage(searchFaultModule));
    }

    /**
     * 获取故障详情
     *
     * @param faultId 故障id
     */
    @SaIgnore
    @Operation(summary = "获取故障详情", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getFaultById/{faultId}")
    public ResponseEntity getFaultById(@PathVariable("faultId") String faultId) {
        var event = eventService.getEventByFaultId(faultId);
        return ResponseEntity.ok(faultService.getFaultById(faultId, event));
    }

    /**
     * 获取故障列表
     *
     * @param searchFaultModule 查询条件
     */
    @Operation(summary = "获取故障列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getFaultByPage")
    public ResponseEntity getFaultByPage(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(faultService.getFaultByPage(searchFaultModule));
    }

    /**
     * 获取故障类型列表
     *
     * @param elevatorType 电梯类型
     * @param eventType    事件类型
     */
    @SaIgnore
    @Operation(summary = "获取故障类型列表", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getFaultType/{elevatorType}/{eventType}")
    public ResponseEntity getFaultType(@PathVariable("elevatorType") Integer elevatorType,
                                       @PathVariable("eventType") Integer eventType) {
        return ResponseEntity.ok(faultService.getFaultType(elevatorType, eventType));
    }
}
