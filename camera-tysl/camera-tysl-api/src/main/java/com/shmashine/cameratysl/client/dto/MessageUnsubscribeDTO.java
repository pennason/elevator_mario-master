// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/22 18:16
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "删除消息订阅MessageUnsubscribeDTO", description = "删除消息订阅")
public class MessageUnsubscribeDTO {
    /**
     * 订阅id
     */
    @Schema(title = "订阅id", description = "订阅id", required = true)
    private String subscriptionId;
}
