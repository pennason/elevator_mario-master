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
 * @version v1.0  -  2023/8/22 9:54
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "⼼跳存活通知", description = "⼼跳存活通知")
public class CallbackHeartbeatEventNotifyDTO implements Serializable {
    /**
     * 消息类型。 deviceEvent ：设备事件通知
     */
    @Schema(title = "消息类型", description = "消息类型。 heartbeatEvent ：⼼跳存活通知", example = "heartbeat", required = true)
    private String msgType;
    /**
     * 消息流⽔号，便于后续⽇志回溯
     */
    @Schema(title = "消息流⽔号", description = "消息流⽔号，便于后续⽇志回溯", required = true)
    private String bizId;
}
