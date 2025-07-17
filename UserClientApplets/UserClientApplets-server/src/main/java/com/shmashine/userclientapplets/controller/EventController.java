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
import com.shmashine.userclientapplets.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * EventController
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/15 16:57
 */
@RestController
@RequestMapping("/event")
@Tag(name = "小程序事件接口", description = "小程序事件接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class EventController extends BaseRequestEntity {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    /**
     * 获取急修列表
     *
     * @param searchFaultModule 查询参数
     */
    @Operation(summary = "获取急修列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/queryRepairByPage")
    public ResponseEntity queryRepairByPage(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setAdminFlag(userService.isAdmin(super.getUserId()));
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(eventService.queryRepairByPage(searchFaultModule));
    }

    /**
     * 获取维修详情
     *
     * @param eventNumber 事件编号
     */
    @SaIgnore
    @Operation(summary = "获取维修详情", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getEventById/{eventNumber}")
    public ResponseEntity getEventById(@PathVariable(value = "eventNumber") String eventNumber) {
        return ResponseEntity.ok(eventService.getEventById(eventNumber));
    }

    /**
     * 获取故障工单状态
     *
     * @param faultId 故障id
     */
    @SaIgnore
    @Operation(summary = "获取故障工单状态", security = {@SecurityRequirement(name = "token")})
    @GetMapping("/getEventStatus/{faultId}")
    public ResponseEntity getEventStatus(@PathVariable String faultId) {
        return ResponseEntity.ok(eventService.getEventStatus(faultId));
    }
}
