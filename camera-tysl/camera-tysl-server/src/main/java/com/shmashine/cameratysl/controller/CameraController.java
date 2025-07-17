// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.client.dto.requests.FaultForHistoryPhotoVideoRequestDTO;
import com.shmashine.cameratysl.service.CameraServiceI;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/9 14:20
 * @since v1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/camera")
@Tag(name = "摄像头图像视频语音相关", description = "摄像头图像视频语音相关 开发者：chenxue")
public class CameraController {
    private final CameraServiceI cameraService;

    @Operation(summary = "根据电梯编号或故障ID下载对应时间段内视频或图像")
    @PostMapping("/download-camera-file-by-elevator-code")
    public ResponseCustom<String> downloadCameraFileByElevatorCode(@RequestBody CamareMediaDownloadRequestDTO request) {
        log.info("downloadCameraFileByElevatorCode uri:{}, request: {}",
                "/camera/download-camera-file-by-elevator-code", request);
        return cameraService.saveCameraDownloadTask(request);
    }

    @Operation(summary = "根据任务ID，删除对应任务记录和OSS文件")
    @DeleteMapping("/delete-task-by-id/{id}")
    public ResponseCustom<String> deleteCameraDownloadTaskById(@PathVariable(value = "id") Long id) {
        log.info("deleteCameraDownloadTaskById uri:{}{}", "/camera/delete-task-by-id/", id);
        return cameraService.removeTaskAndOssFile(id);
    }

    /*@Operation(summary = "根据电梯，楼层，类型获取对应成功的记录")
    @PostMapping("/list-success-download-records")
    public ResponseEntity<List<TblCameraDownloadTaskEntity>> listSuccessDownloadRecords(
            @RequestBody CamaraMediaDownloadBaseRequestDTO request) {
        return cameraService.listSuccessDownloadRecords(request);
    }*/

    @Operation(summary = "获取视频直播流URL, 协议支持 RTSP, HLS")
    @GetMapping("/get-camera-stream-url/{elevatorCode}/{proto}")
    public ResponseCustom<String> getCameraStreamUrl(@PathVariable(value = "elevatorCode") String elevatorCode,
                                                     @PathVariable(value = "proto") String proto) {
        log.info("getCameraStreamUrl uri:{}/{}/{}", "/camera/get-camera-stream-url", elevatorCode, proto);
        return cameraService.getCameraStreamUrl(elevatorCode, proto);
    }

    /**
     * 直播抓图
     */
    @Operation(summary = "获取视频直播流并截图图片")
    @GetMapping("/live-snapshot/{elevatorCode}")
    ResponseCustom<String> liveSnapshot(@PathVariable(value = "elevatorCode") String elevatorCode) {
        log.info("liveSnapshot uri:{}/{}", "/camera/live-snapshot", elevatorCode);
        return cameraService.liveSnapshot(elevatorCode);
    }

    /**
     * 直播录制
     */
    @Operation(summary = "获取视频直播流并录制指定时长视频")
    @GetMapping("/live-record/{elevatorCode}/{duringSeconds}")
    ResponseCustom<String> liveSnapshot(@PathVariable(value = "elevatorCode") String elevatorCode,
                                        @PathVariable(value = "duringSeconds") Long duringSeconds) {
        log.info("liveRecord uri:{}/{}/{}", "/camera/live-record", elevatorCode, duringSeconds);
        return cameraService.liveRecord(elevatorCode, duringSeconds);
    }

    @Operation(summary = "获取视频回放地址")
    @PostMapping("/get-video-playback-url")
    public ResponseCustom getVideoPlaybackUrl(@RequestBody CamareMediaDownloadRequestDTO request) {
        log.info("getVideoPlaybackUrl uri:{}, request: {}", "/camera/get-video-playback-url", request);
        return cameraService.getVideoPlaybackUrl(request);
    }


    @Operation(summary = "与天翼视联同步摄像头设备列表, 不需要手动发起，系统每小时同步一次")
    @PostMapping("/sync-all-camera-from-tysl")
    public ResponseCustom<String> syncAllCameraFromTysl() {
        log.info("syncAllCameraFromTysl uri:{}", "/camera/sync-all-camera-from-tysl");
        cameraService.syncAllCameraExtendInfo();
        return ResponseCustom.success("同步成功");
    }

    /**
     * 下载故障时的图片信息
     *
     * @param request 请求参数
     * @return 结果
     */
    @Operation(summary = "获取故障时的照片")
    @PostMapping("/fault-download-picture-file")
    ResponseCustom<String> downloadPictureFile(@RequestBody FaultForHistoryPhotoVideoRequestDTO request) {
        log.info("downloadPictureFile uri:{}, params:{}", "/camera/fault-download-picture-file",
                JSON.toJSONString(request));
        return cameraService.downloadPictureFile(request);
    }

    /**
     * 下载故障视频文件
     *
     * @param request 请求参数
     * @return 结果
     */
    @Operation(summary = "获取故障时的视频")
    @PostMapping("/fault-download-video-file")
    ResponseCustom<String> downloadFaultVideoFile(@RequestBody FaultForHistoryPhotoVideoRequestDTO request) {
        log.info("downloadFaultVideoFile uri:{}, params:{}", "/camera/fault-download-video-file",
                JSON.toJSONString(request));
        return cameraService.downloadFaultVideoFile(request);
    }

    @Operation(summary = "获取语音对讲WSS地址")
    @GetMapping("/voice/wss/{elevatorCode}")
    ResponseCustom<Object> getVoiceWssInfo(@PathVariable(value = "elevatorCode") String elevatorCode) {
        log.info("getVoiceWssInfo uri:{}/{}", "/camera/voice/wss", elevatorCode);
        return cameraService.getVoiceWssInfo(elevatorCode, 1);
    }

    @Operation(summary = "获取语音对讲WSS地址-domain：1:域名，0：IP+端口")
    @GetMapping("/voice/wss/{elevatorCode}/{domain}")
    ResponseCustom<Object> getVoiceWssInfo(@PathVariable(value = "elevatorCode") String elevatorCode,
                                           @PathVariable(value = "domain") Integer domain) {
        log.info("getVoiceWssInfo uri:{}/{}", "/camera/voice/wss", elevatorCode);
        return cameraService.getVoiceWssInfo(elevatorCode, domain);
    }

    @Operation(summary = "获取语音对讲鉴权Token")
    @GetMapping("/voice/stream-token/{elevatorCode}")
    ResponseCustom<String> getVoiceStreamToken(@PathVariable(value = "elevatorCode") String elevatorCode) {
        log.info("getVoiceStreamToken uri:{}/{}", "/camera/voice/stream-token", elevatorCode);
        return cameraService.getVoiceStreamToken(elevatorCode);
    }

}
