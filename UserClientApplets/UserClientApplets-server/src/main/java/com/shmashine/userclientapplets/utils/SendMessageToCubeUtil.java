// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.userclientapplets.utils;

import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DateTime;

import com.alibaba.fastjson2.JSON;
import com.shmashine.userclient.dto.DeviceMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/6 9:11
 * @since v1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SendMessageToCubeUtil {
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param elevatorCode 电梯编号 MXxxx
     * @param sensorType   传感器类型 CarRoof
     * @param message      消息体
     * @return 结果
     */
    public String restTemplateSendMessageToCube(String elevatorCode, String sensorType, String message) {

        String key = "DEVICE:STATUS:" + elevatorCode + ":" + sensorType;
        Map<Object, Object> deviceStatus = redisTemplate.opsForHash().entries(key);
        String nodeIp = String.valueOf(deviceStatus.get("server_ip"));
        String nodePort = String.valueOf(deviceStatus.get("server_port"));

        // 请求路径：
        String url = String.format("http://%s:%d/cube/sendMessage", nodeIp, Integer.valueOf(nodePort));

        // 发出一个post请求
        try {
            var res = restTemplate.postForObject(url, message, String.class);
            log.info("[{}] ---升级指令下发成功，下发地址：[{}]，下发消息：[{}]，返回信息：[{}]\n",
                    DateTime.now().setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")), url, message, res);
            return res;
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info("接口调用失败,入参为{}", message);
        }
        return null;
    }

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param map 含 eid/elevatorCode，sensorType等
     * @return 结果
     */
    public String restTemplateSendMessageToCube(Map<String, String> map) {
        String elevatorCode = StringUtils.hasText(map.get("eid")) ? map.get("eid") : map.get("elevatorCode");
        String sensorType = map.get("sensorType");
        return restTemplateSendMessageToCube(elevatorCode, sensorType, JSON.toJSONString(map));
    }

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param messageDTO 消息体
     * @param <T>        消息体结构
     * @return 结果
     */
    public <T extends DeviceMessageDTO> String restTemplateSendMessageToCube(T messageDTO) {
        var elevatorCode = messageDTO.getElevatorCode();
        var sensorType = messageDTO.getSensorType();
        return restTemplateSendMessageToCube(elevatorCode, sensorType, JSON.toJSONString(messageDTO));
    }

    /**
     * 调用socket服务向盒子下发消息
     *
     * @param jsonString 消息体
     * @param <T>        消息体结构
     * @return 结果
     */
    public <T extends DeviceMessageDTO> String restTemplateSendMessageToCube(String jsonString) {
        var messageDTO = JSON.parseObject(jsonString, DeviceMessageDTO.class);
        var elevatorCode = messageDTO.getElevatorCode();
        var sensorType = messageDTO.getSensorType();
        return restTemplateSendMessageToCube(elevatorCode, sensorType, jsonString);
    }

    public Object getResultFromRedis(String redisKey) {
        var res = redisTemplate.opsForValue().get(redisKey);
        if (res != null) {
            return res;
        }
        return null;
    }
}

