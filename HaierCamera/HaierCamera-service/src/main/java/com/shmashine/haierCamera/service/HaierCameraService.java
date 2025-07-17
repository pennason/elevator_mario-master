package com.shmashine.haierCamera.service;

import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;


/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 14:58
 */
public interface HaierCameraService {

    /**
     * 获取海康摄像头实时播放hls流
     *
     * @param elevatorId
     * @return
     */
    String getCameraHlsUrlByElevatorId(String elevatorId);

    /**
     * 海康摄像头推送故障
     *
     * @param haierCameraFault
     * @return
     */
    HaierCameraResponseResult pushFault(HaierCameraFault haierCameraFault);
}
