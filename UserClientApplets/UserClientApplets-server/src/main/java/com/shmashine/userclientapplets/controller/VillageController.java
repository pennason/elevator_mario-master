package com.shmashine.userclientapplets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.userclientapplets.entity.BaseRequestEntity;
import com.shmashine.userclientapplets.service.VillageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 小区接口
 *
 * @author jiangheng
 * @version V1.0.0 -2022/2/17 15:41
 */
@RestController
@RequestMapping("/village")
@Tag(name = "小程序小区接口", description = "小程序小区接口")
@SecurityScheme(name = "token", scheme = "bearer", type = SecuritySchemeType.HTTP)
public class VillageController extends BaseRequestEntity {

    @Autowired
    private VillageService villageService;

    /**
     * 获取小区列表
     */
    @Operation(summary = "获取小区列表", security = {@SecurityRequirement(name = "token")})
    @PostMapping("/getVillageList")
    public ResponseEntity getVillageList(@RequestParam(value = "villageName", required = false) String villageName) {
        return ResponseEntity.ok(villageService.getVillageList(getUserId(), villageName));
    }
}
