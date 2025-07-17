// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.cameratysl.client.dto.MessageSubscribeDTO;
import com.shmashine.cameratysl.client.dto.MessageUnsubscribeDTO;
import com.shmashine.cameratysl.client.dto.ResponseCustom;
import com.shmashine.cameratysl.service.MessageServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 17:47
 * @since v1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/tysl-message")
@Tag(name = "消息订阅与取消订阅", description = "消息订阅相关 开发者：chenxue")
public class MessageSubscribeController {
    private final MessageServiceI service;

    @Operation(summary = "订阅消息")
    @PostMapping("/subscribe-event")
    public ResponseCustom<String> subscribeEventMessage(@RequestBody MessageSubscribeDTO request) {
        log.info("subscribeEventMessage url {} request:{}", "/tysl-message/subscribe-event", request);
        return service.subscribeEventMessage(request);
    }

    @Operation(summary = "取消订阅消息")
    @PostMapping("/unsubscribe-event")
    public ResponseCustom<String> unsubscribeEventMessage(@RequestBody MessageUnsubscribeDTO request) {
        log.info("unsubscribeEventMessage url {} request:{}", "/tysl-message/unsubscribe-event", request);
        return service.unsubscribeEventMessage(request);
    }

    @Operation(summary = "查询订阅列表")
    @PostMapping("/query-subscribe-list")
    public ResponseCustom<Object> querySubscribeList() {
        log.info("querySubscribeList url {} request:{}", "/tysl-message/query-subscribe-list", "");
        return service.querySubscribeEventMessage();
    }
}
