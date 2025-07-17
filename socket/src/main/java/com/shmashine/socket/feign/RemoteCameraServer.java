package com.shmashine.socket.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shmashine.common.model.request.FaceRecognitionRequest;

/**
 * 远程调用摄像头服务
 *
 * @author Dean Winchester
 */
@Component
@FeignClient(value = "SHMASHINE-CAMERA")
public interface RemoteCameraServer {

    /**
     * 截取摄像头当前一帧图片（困人），返回图片存储路径
     *
     * @param elevatorCode 电梯编号
     * @return 图片地址
     */
    @RequestMapping(path = "/cameras/getCurrentImagePathByElevatorCode/{elevatorCode}", method = {RequestMethod.GET})
    String getCurrentImagePathByElevatorCode(@PathVariable String elevatorCode);

    /**
     * 根据电梯编号 获取rtmp流地址
     *
     * @param elevatorCode 电梯编号
     */
    @RequestMapping(path = "/getRtmpUrlByElevatorCode/{elevatorCode}", method = {RequestMethod.GET})
    String getRtmpUrlByElevatorCode(@PathVariable String elevatorCode);

    /**
     * 通过电梯编号 获取hls流地址
     *
     * @param elevatorCode 电梯编号
     */
    @RequestMapping(path = "/getHlsUrlByElevatorCode/{elevatorCode}", method = {RequestMethod.GET})
    String getHlsUrlByElevatorCode(@PathVariable String elevatorCode);


    @RequestMapping(path = "/cameras/faceRecognition", method = {RequestMethod.POST})
    Integer faceRecognition(@RequestBody FaceRecognitionRequest faceRecognitionRequest);
}
