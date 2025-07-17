package com.shmashine.socketClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shmashine.config.FeignConfig;


/**
 * 远程调用client
 *
 * @author jiangheng
 * @version V1.0.0 2020/12/24 —— 20:09
 */
@FeignClient(url = "${endpoint.shmashine-socket:172.31.183.100:8002}", value = "shmashine-socket",
        fallback = SocketClientFallback.class, configuration = FeignConfig.class)
public interface SocketClient {


    /**
     * 获取所有channel名单
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   传感器类型
     */
    @GetMapping("/cube/status/{elevatorCode}/{sensorType}")
    String getStatus(@PathVariable("elevatorCode") String elevatorCode, @PathVariable("sensorType") String sensorType);

    /**
     * 消息推送到设备
     *
     * @param message 推送消息
     */
    @PostMapping("/cube/sendMessage")
    String sendMessageToCube(@RequestBody String message);
}
