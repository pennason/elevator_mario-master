// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.sender.server.fault.SendFaultService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 17:01
 * @since v1.0
 */

@Slf4j
@RestController
@RequestMapping("/sender/fault")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FaultResendController {
    private final SendFaultService sendFaultService;

    /**
     * 重发故障
     *
     * @param faultId    故障ID
     * @param cameraType 摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸， 5：天翼云眼，6：中兴
     * @return 发送结果
     */
    @GetMapping("/resend/{faultId}/{cameraType}")
    public ResponseEntity<String> resendFault(@PathVariable(value = "faultId") String faultId,
                                              @PathVariable(value = "cameraType") Integer cameraType) {
        log.info("/sender/fault/resend/{}/{} 因获取到视频或图片，重发故障到城市平台", faultId, cameraType);
        return sendFaultService.checkAndSendFault(faultId, cameraType);
    }
}
