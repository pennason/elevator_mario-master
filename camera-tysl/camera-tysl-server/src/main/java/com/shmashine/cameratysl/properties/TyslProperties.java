// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 电信属性配置
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 10:48
 * @since v1.0
 */

@Data
@Configuration
@ConfigurationProperties("camera.tysl")
public class TyslProperties {

    private String appId;
    private String appKey;
    private String appSecret;
    private String callbackBaseUrl;


    public static final String BASE_URL = "https://aiv.sh.189.cn:8002/vdmp";
    public static final String URI_ACCESS_TOKEN_GET = "/v1/token/{appId}";
    /**
     * 批量获取设备信息
     */
    public static final String URI_DEVICE_LIST_POST = "/api/device/listDevice";
    /**
     * 获取设备信息
     */
    public static final String URI_DEVICE_INFO_POST = "/api/device/getDeviceInfo";
    /**
     * 批量查询指定条件设备信息
     */
    public static final String URI_DEVICE_LIST_BY_CONDITION_POST = "/api/device/getDeviceInfoList";
    /**
     * 获取设备直播流URL
     */
    public static final String URI_DEVICE_STREAM_URL_POST = "/api/device/getStreamUrl";
    /**
     * 获取设备回看流信息
     */
    public static final String URI_DEVICE_PLAYBACK_URL_POST = "/api/device/getPlaybackUrl";
    /**
     * 获取云回看剪辑⽂件下载地址
     */
    public static final String URI_DEVICE_CUTTING_VIDEO_DOWNLOAD_URL_POST = "/api/device/getCuttingVideoDownloadUrl";
    /**
     * 控制设备前端录像
     */
    public static final String URI_PLAYBACK_CONTROL_POST = "/api/device/playbackControl";
    /**
     * 获取录像⽂件列表
     */
    public static final String URI_DEVICE_CLOUD_FILE_LIST_POST = "/api/device/getCloudFileList";
    /**
     * 查询设备绑定用户
     */
    public static final String URI_DEVICE_BIND_USER_POST = "/api/device/getDeviceBindUser";
    /**
     * 获取设备来源
     */
    public static final String URI_DEVICE_SOURCE_POST = "/api/device/getDeviceSource";
    /**
     * 获取设备上报的⽹络地址信息
     */
    public static final String URI_DEVICE_ADDRESS_INFO_POST = "/api/device/getDeviceAddressInformation";
    /**
     * 云台控制
     */
    public static final String URI_DEVICE_PTZ_CONTROL_POST = "/api/device/devicePTZControl";
    /**
     * 消息订阅
     */
    public static final String URI_MESSAGE_SUBSCRIBE_POST = "/api/sub/subscribe";
    /**
     * 消息取消订阅
     */
    public static final String URI_MESSAGE_UNSUBSCRIBE_POST = "/api/sub/unsubscribe";
    /**
     * 查询订阅列表
     */
    public static final String URI_MESSAGE_SUBSCRIBE_LIST_POST = "/api/sub/listSubs";

    // 图⽚流能⼒

    /**
     * 获取1400设备图⽚列表
     */
    public static final String URI_IMAGE_DEVICE_IMAGE_DATA_LIST_POST = "/api/image/queryDeviceImageData";
    /**
     * 分组获取1400设备图⽚列表
     */
    public static final String URI_IMAGE_DEVICE_IMAGE_GROUP_LIST_POST = "/api/image/queryDeviceImageGroupData";
    /**
     * 获取1400设备图⽚详情
     */
    public static final String URI_IMAGE_IMAGE_DETAIL_POST = "/api/image/queryImageDetail";
    /**
     * 设备订阅1400图片
     */
    public static final String URI_IMAGE_SUBSCRIBE_POST = "/api/image/subscribe";
    /**
     * 设备取消订阅1400图片
     */
    public static final String URI_IMAGE_UNSUBSCRIBE_POST = "/api/image/unsubscribe";

    // 语音能力

    /**
     * 语音能力
     */
    public static final String URI_VOICE_INTERCOM_POST = "/api/voice/intercom";

    /**
     * 语音对讲 获取 WSS地址
     */
    public static final String URI_VOICE_TALKBACK_TOKEN_POST = "/api/voice/getWss";
    /**
     * 获取对讲鉴权token
     */
    public static final String URI_VOICE_STREAM_TOKEN_GET = "/api/device/stream-token/{guid}";


    // 事件回调
    public static final String CALLBACK_URI_STATUS_CHANGE_NOTIFY = "/tysl-callback/device-event-notify";
    public static final String CALLBACK_URI_HEARTBEAT_NOTIFY = "/tysl-callback/heartbeat-notify";
    public static final String CALLBACK_URI_ZX_PLATFORM_STATUS_NOTIFY = "/tysl-callback/zx-platform-status-notify";
    public static final String CALLBACK_URI_DEVICE_IMAGE1400_NOTIFY = "/tysl-callback/device-1400-image-notify";
    public static final String CALLBACK_URI_CUTTING_VIDEO_DOWNLOAD_URL_NOTIFY
            = "/tysl-callback/cutting-video-download-url";


}
