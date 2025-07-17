package com.shmashine.hkcamerabyys.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.shmashine.hkcamerabyys.client.entity.DownLoadByHKYSRequestBody;
import com.shmashine.hkcamerabyys.service.HKCameraByYSService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * HKCameraByYSController
 *
 * @author jiangheng
 * @version v1.0 - 2021/11/5 15:07
 */

@RestController
@RequestMapping("/hkCameraByYSController")
@Slf4j
public class HKCameraByYSController {

    @Autowired
    private HKCameraByYSService hkCameraByYSService;

    @PostMapping("/downloadVideoFile")
    public ResponseEntity downloadVideoFile(@RequestBody DownLoadByHKYSRequestBody downLoadByHKYSRequestBody) {
        return hkCameraByYSService.downloadVideoFile(downLoadByHKYSRequestBody);
    }

    @PostMapping("/downloadPictureFile")
    public ResponseEntity downloadPictureFile(@RequestBody DownLoadByHKYSRequestBody downLoadByHKYSRequestBody) {
        return hkCameraByYSService.downloadPictureFile(downLoadByHKYSRequestBody);
    }

    /**
     * 根据电梯编号批量获取当前图像
     */
    @Operation(summary = "根据电梯编号批量获取当前图像")
    @PostMapping("/getElevatorPic")
    public ResponseEntity<HashMap<String, String>> getElevatorPicByElevators(@RequestParam("elevatorCodes")
                                                                             List<String> elevatorCodes) {
        return hkCameraByYSService.getElevatorPicByElevators(elevatorCodes);
    }

    /**
     * 根据电梯编号获取当前图像
     */
    @Operation(summary = "根据电梯编号批量获取当前图像")
    @PostMapping("/getLivePictureByElevatorCode")
    public ResponseEntity<String> getLivePictureByElevatorCode(@RequestParam("elevatorCode")
                                                               String elevatorCode) {
        return hkCameraByYSService.getLivePictureByElevatorCode(elevatorCode);
    }

    /**
     * 摄像头语音下发
     *
     * @param vCloudNumber 摄像头序列号
     * @param faultType    故障类型
     */
    @Operation(summary = "摄像头语音下发")
    @PostMapping("/pushCameraVoice")
    public ResponseEntity<String> pushCameraVoice(@RequestParam("vCloudNumber") String vCloudNumber,
                                                  @RequestParam("faultType") String faultType) {
        return hkCameraByYSService.pushCameraVoice(vCloudNumber, faultType);
    }

    /**
     * 手动重新下载视频文件
     *
     * @param faultId   故障id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return string
     */
    @PostMapping("/videoFileReDownload")
    public String videoFileReDownload(@RequestParam("faultId") String faultId,
                                      @RequestParam("startTime") String startTime,
                                      @RequestParam("endTime") String endTime) {
        return hkCameraByYSService.videoFileReDownload(faultId, startTime, endTime);
    }


    /**
     * 萤石消息推送
     * 参考文档 https://open.ys7.com/help/571
     *
     * @param body 萤石回调请求体
     * @return messageId
     */
    @PostMapping("/ysPlatform/notify")
    public String ysPlatformNotify(@RequestBody String body) {
        log.info("萤石平台推送-告警消息，body：{}", body);
        return hkCameraByYSService.ysPlatformNotify(JSON.parseObject(body));
    }
}
