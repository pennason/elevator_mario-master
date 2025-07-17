// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service;

import com.shmashine.cameratysl.client.dto.MessageSubscribeDTO;
import com.shmashine.cameratysl.client.dto.MessageUnsubscribeDTO;
import com.shmashine.cameratysl.client.dto.ResponseCustom;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 17:56
 * @since v1.0
 */

public interface MessageServiceI {

    /**
     * 订阅消息
     *
     * @param request 订阅消息请求
     * @return 订阅结果
     */
    ResponseCustom<String> subscribeEventMessage(MessageSubscribeDTO request);

    /**
     * 取消订阅消息
     *
     * @param request 取消订阅消息请求
     * @return 取消订阅结果
     */
    ResponseCustom<String> unsubscribeEventMessage(MessageUnsubscribeDTO request);

    /**
     * 查询订阅列表
     *
     * @return 订阅列表
     */
    ResponseCustom<Object> querySubscribeEventMessage();
}
