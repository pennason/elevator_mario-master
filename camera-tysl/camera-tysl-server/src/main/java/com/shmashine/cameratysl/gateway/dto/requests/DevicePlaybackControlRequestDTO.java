// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2025/6/27 16:01
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DevicePlaybackControlRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;
    /**
     * 流媒体标识，通过视频回看接⼝获取
     */
    @Schema(description = "流媒体标识，通过视频回看接⼝获取", required = true)
    private String ssrc;
    /**
     * 设备控制命令 播放：PLAY; 停⽌播放：STOP; 暂停播放：PAUSE; 随机拖动：SEEK; 快进/慢进/倍速/倒放：SPEED
     */
    @Schema(description = "设备控制命令 播放：PLAY; 停⽌播放：STOP; 暂停播放：PAUSE; 随机拖动：SEEK; 快进/慢进/倍速/倒放：SPEED")
    private String control;
    /**
     * 播放速度（0.25,0.5,1,2,4）。快 进/慢进/倍速(SPEED) 命令时有效 且不能为空。 为 1,正常播放;不等于 1,为正常播 放速率的倍数;负数为倒放
     */
    @Schema(description = "播放速度（0.25,0.5,1,2,4）。快 进/慢进/倍速(SPEED) 命令时有效 且不能为空。 为 1,正常播放;不等于 1,为正常播 放速率的倍数;负数为倒放")
    private String scale;
    /**
     * 随机拖动位置，表示从录像起点后 的 x 秒开始播放。随机拖动(SEEK) 命令时有效且不能为空， 必须⼤于等于 0。例如为 0,则表示 从起点开始播放;为 100, 则表示 从录像起点后的 100s 处开始播放
     */
    @Schema(description = "随机拖动位置，表示从录像起点后 的 x 秒开始播放。随机拖动(SEEK) 命令时有效且不能为空， 必须⼤于等于 0。"
            + "例如为 0,则表示 从起点开始播放;为 100, 则表示 从录像起点后的 100s 处开始播放")
    private Integer range;

}
