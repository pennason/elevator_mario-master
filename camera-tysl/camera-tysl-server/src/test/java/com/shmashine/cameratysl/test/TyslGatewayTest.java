// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shmashine.cameratysl.enums.TyslEventTypeEnum;
import com.shmashine.cameratysl.enums.TyslNetWorkTypeEnum;
import com.shmashine.cameratysl.gateway.TyslGateway;
import com.shmashine.cameratysl.gateway.dto.requests.MessageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.PageRequestDTO;
import com.shmashine.cameratysl.videohandle.VideoStreamDownloadHandle;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 14:02
 * @since v1.0
 */

@ActiveProfiles("test")
@SpringBootTest
public class TyslGatewayTest {

    @Autowired
    private TyslGateway tyslGateway;
    @Autowired
    private VideoStreamDownloadHandle videoStreamDownloadHandle;

    @Test
    public void testGetAccessToken() {
        var accessToken = tyslGateway.getAccessToken();
        System.out.println(accessToken);
    }

    @Test
    public void testListDevice() {
        var pageInfo = PageRequestDTO.builder().pageNum(1).pageSize(10).build();
        var deviceList = tyslGateway.listDeviceInfo(pageInfo);
        System.out.println(deviceList);
    }

    @Test
    public void testMessageSubscribe() {
        var messageSubscribe = MessageSubscribeRequestDTO.builder()
                .eventType(TyslEventTypeEnum.STATUS_CHANGE.getCode())
                .notifyUrl("http://localhost")
                .networkType(TyslNetWorkTypeEnum.PUBLIC_NETWORK.getCode())
                .build();
        var res = tyslGateway.subscribeMessage(messageSubscribe);
        System.out.println(res);
    }

    @Test
    public void testMessageSubscribeList() {
        var res = tyslGateway.listMessageSubs();
        System.out.println(res);
    }

    @Test
    public void testVideoDownload() {
        var streamUrl = "http://180.163.93.220:30002/vod/15029161.m3u8";
        var filePath = "/data/";
        var workId = "test";
        var res = videoStreamDownloadHandle.run(streamUrl, filePath, workId, null);
        System.out.println(res);
    }

}
