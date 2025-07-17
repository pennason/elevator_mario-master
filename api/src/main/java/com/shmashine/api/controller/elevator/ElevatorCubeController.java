// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.controller.elevator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.entity.base.BaseRequestEntity;
import com.shmashine.api.entity.base.ResponseResult;
import com.shmashine.api.util.SendMessageToCubeUtil;
import com.shmashine.userclient.dto.DeviceMessageDTO;

import lombok.RequiredArgsConstructor;

/**
 * 电梯与设备发送消息
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/1/5 18:09
 * @since v1.0
 */

@RestController
@RequestMapping("/elevator-cube")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorCubeController extends BaseRequestEntity {
    private final SendMessageToCubeUtil sendMessageToCubeUtil;

    /**
     * 向设备发送消息
     *
     * @param deviceMessageDTO 消息体
     * @return 结果
     */
    @PostMapping("/send-message-to-cube")
    public Object sendMessageToCube(@RequestBody @Valid DeviceMessageDTO deviceMessageDTO) {
        var res = sendMessageToCubeUtil.restTemplateSendMessageToCube(deviceMessageDTO);
        return ResponseResult.successObj(res);
    }

    /**
     * 向设备发送消息
     *
     * @param jsonString 基于 DeviceMessageDTO 的 JSON
     * @return 结果
     */
    @PostMapping("/send-message-to-cube-json")
    public Object deviceDlogStartByJson(@RequestBody @Valid String jsonString) {
        var res = sendMessageToCubeUtil.restTemplateSendMessageToCube(jsonString);
        return ResponseResult.successObj(res);
    }


}
