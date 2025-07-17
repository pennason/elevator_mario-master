package com.shmashine.hkcamerabyys.client;

import java.util.HashMap;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.shmashine.common.dto.CamaraMediaDownloadBaseRequestDTO;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.hkcamerabyys.client.entity.DownLoadByHKYSRequestBody;

/**
 * 远程调用请求
 *
 * @author jiangheng
 * @version v1.0.0 - 2021/11/8 15:49
 */

@FeignClient(url = "${endpoint.shmashine-hk-camera-ys:shmashine-hk-camera-ys:8080}", value = "shmashine-hk-camera-ys")
public interface RemoteHikEzvizClient {

    @PostMapping("/hkCameraByYSController/downloadVideoFile")
    @Deprecated
    ResponseEntity downloadVideoFile(@RequestBody DownLoadByHKYSRequestBody downLoadByHKYSRequestBody);

    @PostMapping("/hkCameraByYSController/downloadPictureFile")
    @Deprecated
    ResponseEntity downloadPictureFile(@RequestBody DownLoadByHKYSRequestBody downLoadByHKYSRequestBody);

    @PostMapping("/hkCameraByYSController/getElevatorPic")
    ResponseEntity<HashMap<String, String>> getElevatorPicByElevators(@RequestParam("elevatorCodes")
                                                                      List<String> elevatorCodes);

    /**
     * 视频下载请求重试
     *
     * @param faultId   故障id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return string
     */
    @PostMapping("/hkCameraByYSController/videoFileReDownload")
    String videoFileReDownload(@RequestParam("faultId") String faultId,
                               @RequestParam("startTime") String startTime,
                               @RequestParam("endTime") String endTime);


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

    /**
     * 获取当前图像
     *
     * @param elevatorCode 电梯编号
     * @return 图像地址
     */
    @PostMapping("/hkCameraByYSController/getLivePictureByElevatorCode")
    ResponseEntity<String> getLivePictureByElevatorCode(@RequestParam("elevatorCode") String elevatorCode);

    /**
     * 摄像头语音下发
     *
     * @param vCloudNumber 摄像头序列号
     * @param faultType    故障类型
     */
    @PostMapping("/hkCameraByYSController/pushCameraVoice")
    ResponseEntity<String> pushCameraVoice(@RequestParam("vCloudNumber") String vCloudNumber,
                                           @RequestParam("faultType") String faultType);

}
