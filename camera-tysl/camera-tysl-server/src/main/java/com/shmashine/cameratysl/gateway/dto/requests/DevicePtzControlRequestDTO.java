// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

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
 * @version v1.0  -  2023/8/21 15:50
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePtzControlRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;

    /**
     * 设备控制动作：001云台上；002云台下；003云台左；004云台右；005镜头拉近；006镜头拉远
     */
    @Schema(title = "设备控制动作", description = "设备控制动作 001云台上；002云台下；003云台左；004云台右；005镜头拉近；006镜头拉远", required = true)
    private String action;
}
