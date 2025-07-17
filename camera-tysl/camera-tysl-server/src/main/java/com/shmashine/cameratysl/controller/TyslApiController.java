// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shmashine.cameratysl.gateway.TyslGateway;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceCloudFileListRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceInfoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackCutVideoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePtzControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceStreamUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageDataRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageGroupRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageImageDetailRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.PageRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceIntercomRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceWssRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 8:35
 * @since v1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/tysl-api")
@Tag(name = "天翼视联API相关", description = "天翼视联API相关 开发者：chenxue")
public class TyslApiController {
    private final TyslGateway gateway;

    @Operation(summary = "1.批量获取设备信息")
    @PostMapping("/device/listDevice")
    public ResponseEntity<Object> listDeviceInfo(@RequestBody PageRequestDTO request) {
        log.info("listDeviceInfo uri:{}, request:{}", "/tysl-api/device/listDevice", request);
        return ResponseEntity.ok().body(gateway.listDeviceInfo(request));
    }

    @Operation(summary = "2.获取设备信息")
    @PostMapping("/device/getDeviceInfo")
    public ResponseEntity<Object> getDeviceInfo(@RequestBody DeviceInfoRequestDTO request) {
        log.info("getDeviceInfo uri:{}, request:{}", "/tysl-api/device/getDeviceInfo", request);
        return ResponseEntity.ok().body(gateway.getDeviceInfo(request));
    }

    @Operation(summary = "3.获取设备直播取流URL")
    @PostMapping("/device/getStreamUrl")
    public ResponseEntity<Object> getDeviceStreamUrl(@RequestBody DeviceStreamUrlRequestDTO request) {
        log.info("getDeviceStreamUrl uri:{}, request:{}", "/tysl-api/device/getStreamUrl", request);
        return ResponseEntity.ok().body(gateway.getDeviceStreamUrl(request));
    }

    @Operation(summary = "4.获取设备回看流信息")
    @PostMapping("/device/getPlaybackUrl")
    public ResponseEntity<Object> listDevicePlaybackUrl(@RequestBody DevicePlaybackUrlRequestDTO request) {
        log.info("listDevicePlaybackUrl uri:{}, request:{}", "/tysl-api/device/getPlaybackUrl", request);
        return ResponseEntity.ok().body(gateway.listDevicePlaybackUrl(request));
    }

    @Operation(summary = "16.获取云回看剪辑文件下载地址")
    @PostMapping("/device/getCuttingVideoDownloadUrl")
    public ResponseEntity<Object> getCuttingVideoDownloadUrl(@RequestBody DevicePlaybackCutVideoRequestDTO request) {
        log.info("getCuttingVideoDownloadUrl uri:{}, request:{}", "/tysl-api/device/getCuttingVideoDownloadUrl",
                request);
        return ResponseEntity.ok().body(gateway.getCuttingVideoDownloadUrl(request));
    }

    @Operation(summary = "5.获取录像⽂件列表")
    @PostMapping("/device/getCloudFileList")
    public ResponseEntity<Object> listDeviceCloudFile(@RequestBody DeviceCloudFileListRequestDTO request) {
        log.info("listDeviceCloudFile uri:{}, request:{}", "/tysl-api/device/getCloudFileList", request);
        return ResponseEntity.ok().body(gateway.listDeviceCloudFile(request));
    }

    @Operation(summary = "6.查询设备绑定⽤户")
    @PostMapping("/device/getDeviceBindUser")
    public ResponseEntity<Object> getDeviceBindUser(@RequestBody DeviceInfoRequestDTO request) {
        return ResponseEntity.ok().body(gateway.getDeviceBindUser(request));
    }

    @Operation(summary = "7.获取设备来源")
    @PostMapping("/device/getDeviceResource")
    public ResponseEntity<Object> getDeviceResource(@RequestBody DeviceInfoRequestDTO request) {
        log.info("getDeviceResource uri:{}, request:{}", "/tysl-api/device/getDeviceResource", request);
        return ResponseEntity.ok().body(gateway.getDeviceResource(request));
    }

    @Operation(summary = "8.获取设备上报的⽹络地址信息")
    @PostMapping("/device/getDeviceAddressInformation")
    public ResponseEntity<Object> getDeviceAddressInformation(@RequestBody DeviceInfoRequestDTO request) {
        log.info("getDeviceAddressInformation uri:{}, request:{}", "/tysl-api/device/getDeviceAddressInformation",
                request);
        return ResponseEntity.ok().body(gateway.getDeviceAddressInformation(request));
    }

    @Operation(summary = "9.云台控制")
    @PostMapping("/device/devicePTZControl")
    public ResponseEntity<Object> devicePtzControl(@RequestBody DevicePtzControlRequestDTO request) {
        log.info("devicePtzControl uri:{}, request:{}", "/tysl-api/device/devicePTZControl", request);
        return ResponseEntity.ok().body(gateway.devicePtzControl(request));
    }

    @Operation(summary = "10.获取1400设备图⽚列表")
    @PostMapping("/image/queryDeviceImageData")
    public ResponseEntity<Object> listImageDeviceImage(@RequestBody ImageDeviceImageDataRequestDTO request) {
        log.info("listImageDeviceImage uri:{}, request:{}", "/tysl-api/image/queryDeviceImageData", request);
        return ResponseEntity.ok().body(gateway.listImageDeviceImage(request));
    }

    @Operation(summary = "11.分组获取1400设备图⽚列表")
    @PostMapping("/image/queryDeviceImageGroupData")
    public ResponseEntity<Object> listImageDeviceGroup(@RequestBody ImageDeviceImageGroupRequestDTO request) {
        log.info("listImageDeviceGroup uri:{}, request:{}", "/tysl-api/image/queryDeviceImageGroupData", request);
        return ResponseEntity.ok().body(gateway.listImageDeviceGroup(request));
    }

    @Operation(summary = "12.获取1400设备图⽚详情")
    @PostMapping("/image/queryImageDetail")
    public ResponseEntity<Object> getImageDetail(@RequestBody ImageImageDetailRequestDTO request) {
        log.info("getImageDetail uri:{}, request:{}", "/tysl-api/image/queryImageDetail", request);
        return ResponseEntity.ok().body(gateway.getImageDetail(request));
    }

    @Operation(summary = "13.设备订阅1400图⽚")
    @PostMapping("/image/subscribe")
    public ResponseEntity<Object> subscribeImage(@RequestBody ImageSubscribeRequestDTO request) {
        log.info("subscribeImage uri:{}, request:{}", "/tysl-api/image/subscribe", request);
        return ResponseEntity.ok().body(gateway.subscribeImage(request));
    }

    @Operation(summary = "14.设备退订1400图⽚")
    @PostMapping("/image/unsubscribe")
    public ResponseEntity<Object> unsubscribeImage(@RequestBody ImageUnsubscribeRequestDTO request) {
        log.info("unsubscribeImage uri:{}, request:{}", "/tysl-api/image/unsubscribe", request);
        return ResponseEntity.ok().body(gateway.unsubscribeImage(request));
    }

    @Deprecated
    @Operation(summary = "15.语⾳播报-有问题")
    @PostMapping(value = "/voice/intercom", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> voiceIntercom(@RequestBody VoiceIntercomRequestDTO request) {
        log.info("voiceIntercom uri:{}, request:{}", "/tysl-api/voice/intercom", request);
        return ResponseEntity.ok().body(gateway.voiceIntercom(request));
    }

    @Operation(summary = "15.语⾳播报-重写")
    @PostMapping(value = "/voice/intercom/{guid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> voiceIntercom(@PathVariable("guid") String guid,
                                                @RequestParam("file") MultipartFile file) {
        log.info("voiceIntercom uri:{}{}, request:{}", "/tysl-api/voice/intercom/", guid, file.getName());
        return ResponseEntity.ok()
                .body(gateway.voiceIntercom(VoiceIntercomRequestDTO.builder()
                        .guid(guid)
                        .voiceData(file)
                        .build()));
    }

    @Operation(summary = "16.获取语音对讲WSS")
    @PostMapping(value = "/voice/wss")
    public ResponseEntity<Object> voiceWss(@RequestBody VoiceWssRequestDTO request) {
        log.info("voiceGetWss uri:{}, request:{}", "/tysl-api/voice/wss", request);
        return ResponseEntity.ok().body(gateway.voiceWss(request));
    }

    @Operation(summary = "17.获取语音对讲鉴权Token")
    @GetMapping(value = "/voice/stream-token/{guid}")
    public ResponseEntity<Object> voiceStreamToken(@PathVariable("guid") String guid) {
        log.info("voiceStreamToken uri:{}, request:{}", "/tysl-api/voice/stream-token/{guid}", guid);
        return ResponseEntity.ok().body(gateway.voiceStreamToken(guid));
    }
}
