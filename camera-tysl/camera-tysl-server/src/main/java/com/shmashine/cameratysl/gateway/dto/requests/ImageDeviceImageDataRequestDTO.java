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
 * @version v1.0  -  2023/8/21 16:08
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDeviceImageDataRequestDTO implements Serializable {
    /**
     * ⼿机号码，允许查询多个⼿机号码，最多20个⼿机号码【设备拍照时的绑定⼿机】，eg: 13900139000,13900139001
     */
    @Schema(title = "手机号码", description = "手机号码，允许查询多个⼿机号码，最多20个⼿机号码【设备拍照时的绑定⼿机】", required = true)
    private String phone;
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识,仅⽀持单个设备编码查询
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识,仅⽀持单个设备编码查询", required = true)
    private String guid;
    /**
     * 查询开始时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天
     */
    @Schema(title = "查询开始时间", description = "查询开始时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天", required = true)
    private String startTime;
    /**
     * 查询结束时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天
     */
    @Schema(title = "查询结束时间", description = "查询结束时间, 格式: yyyy-MM-dd HH:mm:ss，时间间隔⼩于7天", required = true)
    private String endTime;
    /**
     * 图⽚类型: 11:⼈脸图, 14:背景图, 9:机动⻋; shotType=1时0:背景图和⼈脸图; shotType=2时0:背景图和机动⻋图
     */
    @Schema(title = "图⽚类型", description = "图⽚类型: 11:⼈脸图, 14:背景图, 9:机动⻋; shotType=1时0:背景图和⼈脸图; shotType=2时0:背景图和机动⻋图",
            required = true)
    private Integer imageType;
    /**
     * 图⽚分组标识 -1忽略这个条件 0:⼀般特征 14:场景图
     */
    @Schema(title = "图⽚分组标识", description = "图⽚分组标识 -1忽略这个条件 0:⼀般特征 14:场景图", required = true)
    private Integer imageTypeGroup;
    /**
     * 查询数量， 默认100
     */
    @Schema(title = "查询数量", description = "查询数量， 默认100", defaultValue = "100")
    private Integer size;
    /**
     * ⾸次查询为空，⾮⾸次查询填上⼀次返回结果中sort值数组
     * 2022-01-26 10:18:39,0000000080132700052302202201261017581841711,000000008013270005230220220126101758184150618416
     */
    @Schema(title = "⾸次查询为空，⾮⾸次查询填上⼀次返回结果中sort值数组", description = "⾸次查询为空，⾮⾸次查询填上⼀次返回结果中sort值数组", required = true,
            example = "")
    private String sortArr;
    /**
     * 抓拍类型，1：⼈脸， 2：机动⻋
     */
    @Schema(title = "抓拍类型", description = "抓拍类型，1：⼈脸， 2：机动⻋", required = true)
    private Integer shotType;
}
