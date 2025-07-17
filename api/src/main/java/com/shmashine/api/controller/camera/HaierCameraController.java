package com.shmashine.api.controller.camera;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.haierCamera.client.RemoteHaierCameraClient;
import com.shmashine.haierCamera.entity.HaierCameraFault;
import com.shmashine.haierCamera.entity.HaierCameraResponseResult;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/21 16:51
 */
@RestController
@RequestMapping("/haier")
public class HaierCameraController {

    @Autowired
    private RemoteHaierCameraClient haierCameraClient;

    @PostMapping("/camera/pushFault")
    public HaierCameraResponseResult pushFault(@RequestBody HaierCameraFault haierCameraFault) {

        try {
            return haierCameraClient.pushFault(haierCameraFault);
        } catch (Exception e) {
            return HaierCameraResponseResult.error("timeout");
        }
    }
}
