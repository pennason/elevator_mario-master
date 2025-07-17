// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto.callback;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 9:34
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "设备事件通知", description = "设备事件通知")
public class CallbackStatusChangeEventNotifyDTO implements Serializable {
    /**
     * 消息类型。 deviceEvent ：设备事件通知
     */
    @Schema(title = "消息类型", description = "消息类型。 deviceEvent ：设备事件通知", example = "statusChange", required = true)
    private String msgType;
    /**
     * 消息流⽔号，便于后续⽇志回溯
     */
    @Schema(title = "消息流⽔号", description = "消息流⽔号，便于后续⽇志回溯", required = true)
    private String bizId;
    /**
     * 设备事件信息
     */
    @Schema(title = "设备事件信息", description = "设备事件信息", required = true)
    private DeviceEventData deviceEvent;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeviceEventData implements Serializable {
        /**
         * 统⼀设备管理平台中设备通道唯⼀标识
         */
        @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
        private String guid;
        /**
         * 国标号
         */
        @Schema(title = "国标号", description = "国标号")
        private String gbCode;
        /**
         * 对应各平台的设备标识，映射关系如下： ⼤华：通道号 channelId 中兴平台：通道编号 guid 天翼云眼： deviceCode ⼿机看店：监控点唯⼀标识 cameraIndexCode 视觉智联：设备编号 deviceid
         */
        @Schema(title = "对应各平台的设备标识",
                description = "对应各平台的设备标识，映射关系如下： ⼤华：通道号 channelId 中兴平台：通道编号 guid 天翼云眼： deviceCode ⼿机看店：监控点唯⼀标识 cameraIndexCode 视觉智联：设备编号 deviceid",
                required = true)
        private String deviceCode;
        /**
         * IP地址
         */
        @Schema(title = "IP地址", description = "IP地址")
        private String ip;
        /**
         * 通道号
         */
        @Schema(title = "通道号", description = "通道号")
        private String channelNo;
        /**
         * 通道序号，统⼀从0开始
         */
        @Schema(title = "通道序号", description = "通道序号，统⼀从0开始")
        private Integer channelSeq;
        /**
         * 事件类型，对应消息订阅中的 eventType。 statusChange 状态变更
         */
        @Schema(title = "事件类型", description = "事件类型，对应消息订阅中的 eventType。 statusChange 状态变更; heartbeat:平台⼼跳推送", required = true)
        private String eventType;
        /**
         * 事件数据信息。结构体由事件类型 eventType 确定
         */
        @Schema(title = "事件数据信息", description = "事件数据信息。结构体由事件类型 eventType 确定", required = true)
        private Object data;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusChangeData implements Serializable {
        /**
         * 事件发⽣时间。格式：yyyyMMddHHmmss
         */
        @Schema(title = "事件发⽣时间", description = "事件发⽣时间。格式：yyyyMMddHHmmss", required = true)
        private String eventTime;
        /**
         * 设备状态。1：在线；0：离线；-1：删除；2：未知故障；3：新增
         */
        @Schema(title = "设备状态", description = "设备状态。1：在线；0：离线；-1：删除；2：未知故障；3：新增", required = true)
        private Integer status;
        /**
         * 描述
         */
        @Schema(title = "描述", description = "描述")
        private String msg;
    }
}
