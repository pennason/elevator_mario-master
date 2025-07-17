// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.service;

import com.shmashine.cameratysl.client.dto.callback.CallbackDeviceImage1400NotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackHeartbeatEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackPlaybackCutVideoNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackStatusChangeEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.CallbackZxPlatformStatusEventNotifyDTO;
import com.shmashine.cameratysl.client.dto.callback.TyslCallbackResponseDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 10:28
 * @since v1.0
 */

public interface CallbackNotifyServiceI {
    /**
     * 设备事件通知
     *
     * @param request 请求体
     * @return 结果
     */
    TyslCallbackResponseDTO deviceEventNotify(CallbackStatusChangeEventNotifyDTO request);

    /**
     * 心跳存活通知
     *
     * @param request 请求体
     * @return 结果
     */
    TyslCallbackResponseDTO heartbeatNotify(CallbackHeartbeatEventNotifyDTO request);

    /**
     * 中兴上下级平台状态通知
     *
     * @param request 请求体
     * @return 结果
     */
    TyslCallbackResponseDTO zxPlatformStatusNotify(CallbackZxPlatformStatusEventNotifyDTO request);

    /**
     * 设备1400图⽚通知
     *
     * @param request 请求体
     * @return 结果
     */
    TyslCallbackResponseDTO device1400ImageNotify(CallbackDeviceImage1400NotifyDTO request);

    /**
     * 视频剪切下载地址回调
     *
     * @param request 请求体
     * @return 结果
     */
    TyslCallbackResponseDTO cuttingVideoDownloadUrl(CallbackPlaybackCutVideoNotifyDTO request);
}
