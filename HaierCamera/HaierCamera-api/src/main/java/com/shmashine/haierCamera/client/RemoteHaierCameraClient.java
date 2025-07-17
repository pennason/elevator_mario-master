package com.shmashine.haierCamera.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/19 14:23
 */
@Component
@FeignClient(value = "HaierCamera-service")
public interface RemoteHaierCameraClient {

    /**
     * 获取海尔摄像头直播流
     *
     * @param elevatorId
     * @return
     */
    @RequestMapping(value = {"/haierCamera/getCameraHlsUrlByElevatorId/{elevatorId}"}, method = {RequestMethod.GET})
    String getCameraHlsUrlByElevatorId(@PathVariable String elevatorId);

    @RequestMapping(value = {"/camera/pushFault"}, method = {RequestMethod.POST})
    HaierCameraResponseResult pushFault(@RequestBody HaierCameraFault haierCameraFault);
}
