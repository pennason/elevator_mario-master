// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.hkcamerabyys.service.CameraServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:20
 * @since v1.0
 */

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/camera")
@Tag(name = "摄像头图像视频相关", description = "摄像头图像视频相关 开发者：chenxue")
public class CameraController {


    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(64, 128,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "CameraController");

    private final CameraServiceI cameraService;

    @Operation(summary = "根据电梯编号或故障ID下载对应时间段内视频或图像")
    @PostMapping("/download-camera-file-by-elevator-code")
    public ResponseEntity<String> downloadCameraFileByElevatorCode(@RequestBody CamareMediaDownloadRequestDTO request) {
        executorService.submit(() -> cameraService.saveCameraDownloadTask(request));
        return ResponseEntity.ok("已加入执行队列，请稍等...");
    }

    @Operation(summary = "根据任务ID，删除对应任务记录和OSS文件")
    @DeleteMapping("/delete-task-by-id/{id}")
    public ResponseEntity<String> deleteCameraDownloadTaskById(@PathVariable(value = "id") Long id) {
        return cameraService.removeTaskAndOssFile(id);
    }

    @Operation(summary = "根据电梯，楼层，类型获取对应成功的记录")
    @PostMapping("/list-success-download-records")
    public ResponseEntity<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            @RequestBody CamaraMediaDownloadBaseRequestDTO request) {
        return cameraService.listSuccessDownloadRecords(request);
    }

    @Operation(summary = "手动触发萤石摄像头在线状态")
    @GetMapping("/renew-camera-online-status")
    public ResponseEntity<String> renewCameraOnlineStatus() {
        return cameraService.renewCameraOnlineStatus();
    }
}
