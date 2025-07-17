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
 * @version v1.0  -  2023/8/22 10:24
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "通用回调返回体", description = "通用回调返回体")
public class TyslCallbackResponseDTO implements Serializable {
    /**
     * 第三方返回体， 200表示收到推送
     */
    @Schema(title = "第三方返回体", description = "第三方返回体， 200表示收到推送", example = "200", required = true)
    private Integer code;
    /**
     * 结果说明
     */
    @Schema(title = "结果说明", description = "结果说明", example = "success")
    private String msg;
    /**
     * 返回数据
     */
    @Schema(title = "返回数据", description = "返回数据, 目前不需要")
    private Object data;

}
