// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.elevator;

import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.module.elevator.GroupLeasingFloorEvidenceModule;
import com.shmashine.api.service.cameradownloadtask.CameraDownloadTaskServiceI;
import com.shmashine.api.service.elevator.ElevatorGroupLeasingServiceI;
import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/elevator/group-leasing")
@Tag(name = "群租识别相关接口", description = "群租识别相关接口-开发者：（chenxue）")
public class ElevatorGroupLeasingController {
    private final CameraDownloadTaskServiceI cameraDownloadTaskService;
    private final ElevatorGroupLeasingServiceI elevatorGroupLeasingService;

    @Operation(summary = "根据电梯，楼层，类型获取对应成功的记录")
    @PostMapping("/list-success-download-records")
    public ResponseResult listSuccessDownloadRecords(@RequestBody CamaraMediaDownloadBaseRequestDTO request) {
        var list = cameraDownloadTaskService.listSuccessDownloadRecords(request);
        if (CollectionUtils.isEmpty(list)) {
            return ResponseResult.successObj(Collections.emptyList());
        }
        var res = list.stream().map(item -> {
            var itemMap = new HashMap<String, String>();
            itemMap.put("elevatorCode", item.getElevatorCode());
            itemMap.put("floor", item.getFloor());
            itemMap.put("extendInfo", item.getExtendInfo());
            itemMap.put("collectTime", item.getCollectTime() == null
                    ? elevatorGroupLeasingService.dateShortToNormal(item.getStartTime()) : item.getCollectTime());
            itemMap.put("ossUrl", item.getOssUrl() == null ? item.getSourceUrl() : item.getOssUrl());
            return itemMap;
        }).collect(Collectors.toList());

        return ResponseResult.successObj(res);
    }

    @Operation(summary = "根据电梯编号，获取群租楼层信息，取证信息")
    @GetMapping("/list-group-leasing-floors/{elevatorCode}")
    public ResponseResult getElevatorInfo(@PathVariable("elevatorCode") @Valid @NotEmpty(message = "请输入电梯编号") String elevatorCode) {
        var floorList = elevatorGroupLeasingService.getFloorWithEvidenceByElevatorCode(elevatorCode);
        return ResponseResult.successObj(floorList);
    }

    @Operation(summary = "根据电梯编号，获取群租可疑楼层")
    @GetMapping("/list-floor-suspicious/{elevatorCode}")
    public ResponseResult listFloorSuspiciousByElevatorCode(@Valid @PathVariable("elevatorCode") String elevatorCode) {
        var floorSuspiciousList = elevatorGroupLeasingService.getFloorSuspiciousByElevatorCode(elevatorCode);
        return ResponseResult.successObj(floorSuspiciousList);
    }

    @Operation(summary = "根据电梯编号，获取群租取证配置信息")
    @GetMapping("/list-floor-evidence/{elevatorCode}")
    public ResponseResult listFloorEvidenceByElevatorCode(@Valid @PathVariable("elevatorCode") String elevatorCode) {
        var floorEvidenceList = elevatorGroupLeasingService.getFloorEvidenceConfigByElevatorCode(elevatorCode);
        return ResponseResult.successObj(floorEvidenceList);
    }

    @Operation(summary = "保存电梯取证配置信息")
    @PostMapping("/save-evidence")
    public ResponseResult saveFloorEvidences(@Valid @RequestBody GroupLeasingFloorEvidenceModule module) {
        return elevatorGroupLeasingService.saveFloorEvidences(module);
    }
}
