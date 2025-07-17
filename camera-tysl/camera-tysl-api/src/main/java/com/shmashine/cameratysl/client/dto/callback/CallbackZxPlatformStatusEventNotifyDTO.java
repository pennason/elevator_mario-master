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
 * @version v1.0  -  2023/8/22 9:56
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "中兴上下级平台状态通知", description = "中兴上下级平台状态通知")
public class CallbackZxPlatformStatusEventNotifyDTO implements Serializable {
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
     * 设备事件信息，参⻅ ZxPlatformStatusEventData 结构体
     */
    @Schema(title = "设备事件信息", description = "设备事件信息", required = true)
    private ZxPlatformStatusEventData data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZxPlatformStatusEventData implements Serializable {
        /**
         * 中兴平台编码
         */
        @Schema(title = "中兴平台编码", description = "中兴平台编码", required = true)
        private String platformCode;
        /**
         * 中兴平台名称
         */
        @Schema(title = "中兴平台名称", description = "中兴平台名称", required = true)
        private String platformName;
        /**
         * 在线状态，1：在线；0：离线
         */
        @Schema(title = "在线状态", description = "在线状态，1：在线；0：离线", required = true)
        private Integer status;
        /**
         * 事件发⽣时间（格式为yyyy-MM-dd HH:mm:ss）
         */
        @Schema(title = "事件发⽣时间", description = "事件发⽣时间（格式为yyyy-MM-dd HH:mm:ss）", required = true)
        private String time;
        /**
         * 离线原因码，status=0时有意义。1：上级平台注册失败；2：上级平台保活超时；34037：下级平台保活超时，34038：下级平台主动注销
         */
        @Schema(title = "离线原因码", description = "离线原因码，status=0时有意义。1：上级平台注册失败；2：上级平台保活超时；34037：下级平台保活超时，34038：下级平台主动注销")
        private Integer offReasonCode;
        /**
         * 离线原因描述，status=0时有意义
         */
        @Schema(title = "离线原因描述", description = "离线原因描述，status=0时有意义")
        private String offReasonMsg;
    }

}
