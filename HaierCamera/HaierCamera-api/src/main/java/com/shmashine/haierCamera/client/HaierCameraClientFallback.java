package com.shmashine.haierCamera.client;

import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/19 15:14
 */
public class HaierCameraClientFallback implements RemoteHaierCameraClient {

    @Override
    public String getCameraHlsUrlByElevatorId(String elevatorId) {
        return null;
    }

    @Override
    public HaierCameraResponseResult pushFault(HaierCameraFault haierCameraFault) {
        return HaierCameraResponseResult.error("timeOut");
    }
}
