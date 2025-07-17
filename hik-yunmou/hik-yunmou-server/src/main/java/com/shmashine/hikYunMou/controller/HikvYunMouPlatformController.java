package com.shmashine.hikYunMou.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.hikYunMou.handle.HiKPlatformHandle;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  jiangheng
 * @version 2023/3/23 14:57
 * @description: com.shmashine.hikYunMou.controller
 */
@Slf4j
@RestController
@RequestMapping("/hikvYunMouPlatform")
public class HikvYunMouPlatformController {

    @Resource
    private HiKPlatformHandle hiKPlatformHandle;

    @Resource
    private HttpServletRequest request;

    /**
     * 下载图片
     *
     * @param deviceSerial 摄像头序列号
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     * @return
     */
    @PostMapping("/downloadPictureFile")
    public void downloadPictureFile(@RequestParam("deviceSerial") String deviceSerial,
                                    @RequestParam("elevatorCode") String elevatorCode,
                                    @RequestParam("faultType") String faultType,
                                    @RequestParam("faultId") String faultId) {
        log.info("downloadPictureFile-Controller {}, {}, {}, {}", deviceSerial, elevatorCode, faultType, faultId);
        log.debug("downloadPictureFile_Controller, requestIP[{}],requestUrl[{}],realIp[{}]", request.getRemoteAddr(), request.getRequestURL(), request.getHeader("X-Real-IP"));
        hiKPlatformHandle.downloadPictureFile(elevatorCode, faultId, faultType, deviceSerial);

    }

    /**
     * 下载故障视频文件
     *
     * @param deviceSerial 摄像头序列号
     * @param elevatorCode 电梯编号
     * @param faultId      故障id
     * @param faultType    故障类型
     * @param occurTime    故障发生时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    @PostMapping("/downloadFaultVideoFile")
    public void downloadFaultVideoFile(@RequestParam("deviceSerial") String deviceSerial,
                                       @RequestParam("elevatorCode") String elevatorCode,
                                       @RequestParam("faultType") String faultType,
                                       @RequestParam("faultId") String faultId,
                                       @RequestParam("occurTime") String occurTime) {
        log.info("downloadFaultVideoFile-Controller {}, {}, {}, {}, {}", deviceSerial, elevatorCode, faultType, faultId, occurTime);

        hiKPlatformHandle.downloadFaultVideoFile(deviceSerial, elevatorCode, faultId, faultType, occurTime);

    }

    /**
     * 获取图片url
     *
     * @param deviceSerial 摄像头序列号
     * @return
     */
    @PostMapping("/pictureURL")
    public String pictureURL(@RequestParam("deviceSerial") String deviceSerial) {

        return hiKPlatformHandle.getPictureURL(deviceSerial);

    }

    /**
     * 获取回放地址（获取监控点回放取流URL）
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param startTime    开始时间
     * @param stopTime     结束时间
     * @param expireTime   过期时间
     */
    @PostMapping("/playbackURLs")
    public String playbackURLs(@RequestParam("deviceSerial") String deviceSerial,
                               @RequestParam(value = "protocol", required = false) String protocol,
                               @RequestParam(value = "quality", required = false) Integer quality,
                               @RequestParam("startTime") String startTime,
                               @RequestParam("stopTime") String stopTime,
                               @RequestParam("expireTime") String expireTime) {

        return hiKPlatformHandle.playbackURLs(deviceSerial, protocol, quality, startTime, stopTime, expireTime);

    }

    /**
     * 获取监控点预览取流URL
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param expireTime   过期时间 ，单位秒
     */
    @PostMapping("/previewURLs")
    public String previewURLs(@RequestParam("deviceSerial") String deviceSerial,
                              @RequestParam(value = "protocol", required = false) String protocol,
                              @RequestParam(value = "quality", required = false) Integer quality,
                              @RequestParam(value = "expireTime", required = false) String expireTime) {

        return hiKPlatformHandle.previewURLs(deviceSerial, protocol, quality, expireTime);

    }

    /**
     * 获取取流token
     */
    @PostMapping("/getStreamToken")
    public String getStreamToken() {

        return hiKPlatformHandle.getStreamToken();

    }
}
