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
 * @version v1.0  -  2023/8/21 16:34
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageImageDetailRequestDTO implements Serializable {
    /**
     * ⼿机号码
     */
    @Schema(title = "手机号码", description = "手机号码", required = true)
    private String phone;
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识,仅⽀持单个设备编码查询
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识,仅⽀持单个设备编码查询", required = true)
    private String guid;
    /**
     * 抓拍时间yyyy-MM-dd HH:mm:ss.000毫秒
     */
    @Schema(title = "抓拍时间", description = "抓拍时间yyyy-MM-dd HH:mm:ss.000毫秒", required = true)
    private String time;
    /**
     * 抓拍类型，1：⼈脸， 2：机动⻋
     */
    @Schema(title = "抓拍类型", description = "抓拍类型，1：⼈脸， 2：机动⻋")
    private Integer shotType;
    /**
     * 图⽚组id, ⽐如: ⼈脸id/机动⻋id
     */
    @Schema(title = "图⽚组id", description = "图⽚组id, ⽐如: ⼈脸id/机动⻋id", required = true)
    private String imageGroupId;
}
