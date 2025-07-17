// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.client.dto.requests.FaultForHistoryPhotoVideoRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;


/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 10:35
 * @since v1.0
 */

@FeignClient(url = "${endpoint.shmashine-camera-tysl:shmashine-camera-tysl:8080}", name = "shmashine-camera-tysl")
public interface RemoteCameraTyslClient {

    /**
     * 获取视频直播流URL, 协议支持 RTSP, HLS
     *
     * @param elevatorCode 电梯编号
     * @param proto        协议 RTSP, HLS
     */
    @GetMapping("/camera/get-camera-stream-url/{elevatorCode}/{proto}")
    ResponseCustom<String> getCameraStreamUrl(@PathVariable(value = "elevatorCode") String elevatorCode,
                                              @PathVariable(value = "proto") String proto);

    /**
     * 直播抓图
     */
    @GetMapping("/camera/live-snapshot/{elevatorCode}")
    ResponseCustom<String> liveSnapshot(@PathVariable(value = "elevatorCode") String elevatorCode);


    /**
     * 获取视频回放地址
     *
     * @param request 请求参数
     * @return 视频回放地址
     */
    @PostMapping("/camera/get-video-playback-url")
    ResponseCustom<String> getVideoPlaybackUrl(@RequestBody CamareMediaDownloadRequestDTO request);


    /**
     * 下载摄像头指定时间段的视频或图像
     *
     * @param request 请求参数
     * @return 结果
     */
    @PostMapping("/camera/download-camera-file-by-elevator-code")
    ResponseCustom<String> downloadCameraFileByElevatorCode(@RequestBody CamareMediaDownloadRequestDTO request);

    /**
     * 下载故障时的图片信息
     *
     * @param request 请求参数
     * @return 结果
     */
    @PostMapping("/camera/fault-download-picture-file")
    ResponseCustom<String> downloadPictureFile(@RequestBody FaultForHistoryPhotoVideoRequestDTO request);

    /**
     * 下载故障视频文件
     *
     * @param request 请求参数
     * @return 结果
     */
    @PostMapping("/camera/fault-download-video-file")
    ResponseCustom<String> downloadFaultVideoFile(@RequestBody FaultForHistoryPhotoVideoRequestDTO request);

    /**
     * 获取语音对讲WSS地址
     *
     * @param elevatorCode 电梯编号
     * @return 结果
     */
    @GetMapping("/camera/voice/wss/{elevatorCode}")
    ResponseCustom<Object> getVoiceWssInfo(@PathVariable(value = "elevatorCode") String elevatorCode);

    /**
     * 获取语音对讲WSS地址 带域名方式 1：域名，0：IP+端口
     *
     * @param elevatorCode 电梯编号
     * @param domain       1：域名，0：IP+端口
     * @return 结果
     */
    @GetMapping("/camera/voice/wss/{elevatorCode}/{domain}")
    ResponseCustom<Object> getVoiceWssInfo(@PathVariable(value = "elevatorCode") String elevatorCode, @PathVariable(value = "domain") Integer domain);
}
