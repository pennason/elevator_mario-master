// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.pm.api.controller.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.pm.api.service.cache.CacheServiceI;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/9/26 14:11
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CacheController {

    private final CacheServiceI cacheService;

    @Operation(summary = "清除所有缓存")
    @GetMapping("/clear")
    public Object clear() {
        log.info("清除缓存");
        cacheService.clearCacheAll();
        return "success";
    }
}
