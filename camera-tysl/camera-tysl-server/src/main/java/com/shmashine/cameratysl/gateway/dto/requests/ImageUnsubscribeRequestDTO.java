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
 * @version v1.0  -  2023/8/21 16:51
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUnsubscribeRequestDTO implements Serializable {
    /**
     * （覆盖⾮追加）设备信息列表, 多个⽤分号隔开。最多10个
     * 格式为guid,shotType,imageGroupType; guid为统⼀设备管理平台中设备通道唯⼀标识
     * shotType为抓拍类型 - 0:⼈脸和⻋牌 - 1:⼈脸 - 2:⻋牌 imageGroupType为图⽚组类型 - 0: 特征和背景组类型 - 1: 特征组类型 - 2: 背景组类型
     * 格式示例：123,0,0;123,0,0;
     */
    @Schema(title = "设备信息列表", description = "格式为guid,shotType,imageGroupType;"
            + " guid为统⼀设备管理平台中设备通道唯⼀标识 shotType为抓拍类型 - 0:⼈脸和⻋牌 - 1:⼈脸 - 2:⻋牌 "
            + "imageGroupType为图⽚组类型 - 0: 特征和背景组类型 - 1: 特征组类型 - 2: 背景组类型",
            required = true, example = "123,0,0;123,0,0;")
    private String guidList;
}
