package com.shmashine.hikYunMou.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;

/**
 * @author  jiangheng
 * @version 2023/3/28 9:36
 * @description: com.shmashine.hikYunMou.client
 */

@FeignClient(url = "${endpoint.shmashine-hk-camera-ym:shmashine-hk-camera-ym:8080}", name = "shmashine-hk-camera-ym")
public interface RemoteHikCloudClient {

    /**
     * 下载图片
     *
     * @param deviceSerial 摄像头序列号
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @param faultId      故障id
     * @return 结果
     */
    @PostMapping("/hikvYunMouPlatform/downloadPictureFile")
    void downloadPictureFile(@RequestParam("deviceSerial") String deviceSerial,
                             @RequestParam("elevatorCode") String elevatorCode,
                             @RequestParam("faultType") String faultType,
                             @RequestParam("faultId") String faultId);


    /**
     * 下载故障视频文件
     *
     * @param deviceSerial 摄像头序列号
     * @param elevatorCode 电梯编号
     * @param faultId      故障id
     * @param faultType    故障类型
     * @param occurTime    故障发生时间 yyyy-MM-dd HH:mm:ss
     * @return 结果
     */
    @PostMapping("/hikvYunMouPlatform/downloadFaultVideoFile")
    void downloadFaultVideoFile(@RequestParam("deviceSerial") String deviceSerial,
                                @RequestParam("elevatorCode") String elevatorCode,
                                @RequestParam("faultType") String faultType,
                                @RequestParam("faultId") String faultId,
                                @RequestParam("occurTime") String occurTime);


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
    @PostMapping("/hikvYunMouPlatform/playbackURLs")
    String playbackURLs(@RequestParam("deviceSerial") String deviceSerial,
                        @RequestParam(value = "protocol", required = false) String protocol,
                        @RequestParam(value = "quality", required = false) Integer quality,
                        @RequestParam("startTime") String startTime,
                        @RequestParam("stopTime") String stopTime,
                        @RequestParam(value = "expireTime", required = false) String expireTime);

    /**
     * 获取监控点预览取流URL
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param expireTime   过期时间，单位秒
     */
    @PostMapping("/hikvYunMouPlatform/previewURLs")
    String previewURLs(@RequestParam("deviceSerial") String deviceSerial,
                       @RequestParam(value = "protocol", required = false) String protocol,
                       @RequestParam(value = "quality", required = false) Integer quality,
                       @RequestParam(value = "expireTime", required = false) String expireTime);

    @PostMapping("/hikvYunMouPlatform/pictureURL")
    String pictureURL(@RequestParam("deviceSerial") String deviceSerial);

    /**
     * 获取取流token
     *
     * @return 结果
     */
    @PostMapping("/hikvYunMouPlatform/getStreamToken")
    String getStreamToken();


    /**
     * 下载摄像头指定时间段的视频或图像
     *
     * @param request 请求参数
     * @return 结果
     */
    @PostMapping("/camera/download-camera-file-by-elevator-code")
    ResponseEntity<String> downloadCameraFileByElevatorCode(@RequestBody CamareMediaDownloadRequestDTO request);

    /**
     * 获取成功的下载记录
     *
     * @param request 请求参数
     * @return 结果
     */
    @PostMapping("/camera/list-success-download-records")
    ResponseEntity<List<TblCameraDownloadTaskEntity>> downloadCameraFileByElevatorCode(
            @RequestBody CamaraMediaDownloadBaseRequestDTO request);

}
