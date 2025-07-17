// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway;

import com.shmashine.cameratysl.gateway.dto.TyslDeviceAddressInformationResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceBindUserResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceCloudFileListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceInfoResultResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDevicePlaybackUrlResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceResourceResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslDeviceStreamUrlResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageDeviceGroupListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageDeviceImageListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageImageDetailResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslImageSubscribeResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslMessageSubsListResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslMessageSubscribeResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslVoiceTokenResponseDTO;
import com.shmashine.cameratysl.gateway.dto.TyslVoiceWssResponseDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceCloudFileListRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceInfoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackCutVideoRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePlaybackUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DevicePtzControlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.DeviceStreamUrlRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageDataRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageDeviceImageGroupRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageImageDetailRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.ImageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.MessageSubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.MessageUnsubscribeRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.PageRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceIntercomRequestDTO;
import com.shmashine.cameratysl.gateway.dto.requests.VoiceWssRequestDTO;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 10:46
 * @since v1.0
 */

public interface TyslGateway {

    /**
     * 获取 access token
     *
     * @return 结果
     */
    String getAccessToken();

    /**
     * 批量获取设备信息
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceInfoResultResponseDTO listDeviceInfo(PageRequestDTO requestDTO);

    /**
     * 获取单个设备信息
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceInfoResultResponseDTO getDeviceInfo(DeviceInfoRequestDTO requestDTO);

    /**
     * 获取设备直播取流URL
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceStreamUrlResponseDTO getDeviceStreamUrl(DeviceStreamUrlRequestDTO requestDTO);

    /**
     * 获取设备回放取流URL
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDevicePlaybackUrlResponseDTO listDevicePlaybackUrl(DevicePlaybackUrlRequestDTO requestDTO);

    /**
     * 获取云回看剪辑⽂件下载地址
     *
     * @param request 请求参数
     * @return 结果
     */
    TyslResponseDTO<String> getCuttingVideoDownloadUrl(DevicePlaybackCutVideoRequestDTO request);

    /**
     * 控制设备前端录像
     *
     * @param request 请求参数
     * @return 结果
     */
    TyslResponseDTO<Boolean> playbackControl(DevicePlaybackControlRequestDTO request);

    /**
     * 获取录像文件列表
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceCloudFileListResponseDTO listDeviceCloudFile(DeviceCloudFileListRequestDTO requestDTO);

    /**
     * 查询设备绑定用户
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceBindUserResponseDTO getDeviceBindUser(DeviceInfoRequestDTO requestDTO);

    /**
     * 获取设备来源
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceResourceResponseDTO getDeviceResource(DeviceInfoRequestDTO requestDTO);

    /**
     * 获取设备上报的⽹络地址信息
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslDeviceAddressInformationResponseDTO getDeviceAddressInformation(DeviceInfoRequestDTO requestDTO);

    /**
     * 设备云台控制
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslResponseDTO<Object> devicePtzControl(DevicePtzControlRequestDTO requestDTO);

    /**
     * 消息订阅
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslMessageSubscribeResponseDTO subscribeMessage(MessageSubscribeRequestDTO requestDTO);

    /**
     * 删除订阅
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslResponseDTO<Object> unsubscribeMessage(MessageUnsubscribeRequestDTO requestDTO);

    /**
     * 查询订阅列表
     *
     * @return 结果
     */
    TyslMessageSubsListResponseDTO listMessageSubs();

    /**
     * 获取1400设备图片列表
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslImageDeviceImageListResponseDTO listImageDeviceImage(ImageDeviceImageDataRequestDTO requestDTO);

    /**
     * 分组获取1400设备图片列表
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslImageDeviceGroupListResponseDTO listImageDeviceGroup(ImageDeviceImageGroupRequestDTO requestDTO);

    /**
     * 获取1400设备图片详情
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslImageImageDetailResponseDTO getImageDetail(ImageImageDetailRequestDTO requestDTO);

    /**
     * 设备订阅1400图片
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslImageSubscribeResponseDTO subscribeImage(ImageSubscribeRequestDTO requestDTO);

    /**
     * 设备取消订阅1400图片
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslImageSubscribeResponseDTO unsubscribeImage(ImageUnsubscribeRequestDTO requestDTO);

    /**
     * 语音播报
     *
     * @param request 请求参数
     * @return 结果
     */
    TyslResponseDTO<Object> voiceIntercom(VoiceIntercomRequestDTO request);

    /**
     * 获取语⾳对讲wss地址
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    TyslVoiceWssResponseDTO voiceWss(VoiceWssRequestDTO requestDTO);

    /**
     * 获取对讲鉴权token
     *
     * @param guid 统⼀设备管理平台中设备通道唯⼀标识
     * @return 结果
     */
    TyslVoiceTokenResponseDTO voiceStreamToken(String guid);
}
