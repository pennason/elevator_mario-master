// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 16:24
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDeviceImageGroupRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识,仅⽀持单个设备编码查询
     */
    private String guid;
    /**
     * 查询开始时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天
     */
    private String startTime;
    /**
     * 查询结束时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天
     */
    private String endTime;
    /**
     * 查询数量，默认100
     */
    private Integer size;
    /**
     * ⾸次查询为空，⾮⾸次查询填上⼀次返回结果中sort值数组
     */
    private String sortArr;
    /**
     * 抓拍类型，1：⼈脸， 2：机动⻋
     */
    private Integer shotType;
}
