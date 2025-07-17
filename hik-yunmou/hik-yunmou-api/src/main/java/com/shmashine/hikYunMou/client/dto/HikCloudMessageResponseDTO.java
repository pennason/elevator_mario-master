// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hikYunMou.client.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/16 18:05
 * @since v1.0
 */

@Data
@ToString
public class HikCloudMessageResponseDTO
        extends HikCloudBaseResponseDTO<List<HikCloudMessageResponseDTO.HikMessageInfo>> {

    @Data
    @ToString
    public static class HikMessageInfo implements Serializable {
        /**
         * 消息ID,每条消息的唯一ID,消费者可根据此ID进行去重处理
         */
        private String msgId;
        /**
         * 消息类型
         */
        private String msgType;
        /**
         * 消息内容
         */
        private String content;
        /**
         * 消息发送到消息通道时的时间戳
         */
        private Long timestamp;
    }

    @Data
    @ToString
    public static class DeviceOnOffline implements Serializable {
        /**
         * 组ID
         */
        private String groupId;
        /**
         * 设备ID
         */
        private String deviceId;
        /**
         * 设备序列号
         */
        private String deviceSerial;
        /**
         * 设备类型
         */
        private String devType;
        /**
         * 注册时间
         */
        private String regTime;
        /**
         * 设备IP
         */
        private String natIp;
        /**
         * 消息类型：ONLINE-上线，OFFLINE-下线
         */
        private String msgType;
        /**
         * 子序列号
         */
        private String subSerial;
        /**
         * 事件发生时间
         */
        private String occurTime;
        /**
         * 设备名称
         */
        private String deviceName;
    }

    @Data
    @ToString
    public static class CloudVideoRecord implements Serializable {
        /**
         * 项目ID
         */
        private String projectId;
        /**
         * 任务ID
         */
        private String taskId;
        /**
         * 设备序列号
         */
        private String deviceSerial;
        /**
         * 通道号
         */
        private String channel;
        /**
         * 文件数
         */
        private Long fileNum;
        /**
         * 文件大小（B）
         */
        private Long totalSize;
        /**
         * 错误码（当错误码为0且其文件数大于0时表示结果正常，否则异常）
         */
        private String errorCode;
        /**
         * 错误信息
         */
        private String errorMsg;
        /**
         * 消息类型（video_replay_rec-回放，video_preview_rec-预览，video_instant_rec-实时预览）
         */
        private String messageType;
        /**
         * 实际结束时间（日期格式yyyyMMddHHmmss）
         */
        private String actualEndTime;
    }
}
