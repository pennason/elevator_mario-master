// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.server.fault;

import org.springframework.http.ResponseEntity;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/7/12 17:05
 * @since v1.0
 */

public interface SendFaultService {

    /**
     * 检查并发送故障
     *
     * @param faultId    故障ID
     * @param cameraType 摄像头类型 1：海康萤石平台，2：雄迈平台，3：海尔平台，4：海康云眸
     * @return 发送结果
     */
    ResponseEntity<String> checkAndSendFault(String faultId, Integer cameraType);
}
