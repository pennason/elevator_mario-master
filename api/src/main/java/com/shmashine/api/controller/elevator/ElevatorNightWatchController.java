// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.elevator;

import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.service.cameradownloadtask.CameraDownloadTaskServiceI;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/14 10:47
 * @since v1.0
 */

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/elevator/night-watch")
@Tag(name = "夜间守护相关接口", description = "夜间守护相关接口-开发者：（chenxue）")
public class ElevatorNightWatchController {
    private final CameraDownloadTaskServiceI cameraDownloadTaskService;


    @GetMapping("/list-by-elevator-code/{elevatorCode}")
    public ResponseResult listByElevatorCode(@PathVariable String elevatorCode) {
        var list = cameraDownloadTaskService.listRecentNightWatchSuccess(elevatorCode);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseResult.successObj(Collections.emptyList());
        }
        var res = list.stream().map(item -> {
            var itemMap = new HashMap<String, String>();
            itemMap.put("collectTime", item.getCollectTime() == null ? dateShortToNormal(item.getStartTime()) : item.getCollectTime());
            itemMap.put("ossUrl", item.getOssUrl() == null ? item.getSourceUrl() : item.getOssUrl());
            itemMap.put("elevatorCode", item.getElevatorCode());
            return itemMap;
        }).collect(Collectors.toList());

        return ResponseResult.successObj(res);
    }

    private String dateShortToNormal(String date) {
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
                + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12);
    }
}
