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
 * @version v1.0  -  2023/8/21 15:18
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceCloudFileListRequestDTO implements Serializable {
    /**
     * 统⼀设备管理平台中设备通道唯⼀标识
     */
    @Schema(title = "统⼀设备管理平台中设备通道唯⼀标识", description = "统⼀设备管理平台中设备通道唯⼀标识", required = true)
    private String guid;
    /**
     * 需查询的录像⽂件的开始时间，时间格式yyyyMMddHHmmss
     */
    @Schema(title = "需查询的录像⽂件的开始时间", description = "需查询的录像⽂件的开始时间，时间格式yyyyMMddHHmmss", required = true)
    private String beginTime;
    /**
     * 需查询的录像⽂件的结束时间，时间格式yyyyMMddHHmmss
     */
    @Schema(title = "需查询的录像⽂件的结束时间", description = "需查询的录像⽂件的结束时间，时间格式yyyyMMddHHmmss", required = true)
    private String endTime;
    /**
     * 录像⽂件存放的位置。 中兴：0-- pu前端 ，1--pl平台 天翼云眼：0--云上，1--平台 ⼤华：0--cloud，1--device
     */
    @Schema(title = "录像⽂件存放的位置", description = "录像⽂件存放的位置。中兴：0-- pu前端 ，1--pl平台天翼云眼：0--云上，1--平台⼤华：0--cloud，1--device",
            required = true)
    private String location;
    /**
     * 当前⻚的⻚号,从1开始
     */
    @Schema(title = "当前⻚的⻚号", description = "当前⻚的⻚号,从1开始", defaultValue = "1")
    private Integer pageNo;
    /**
     * 每⻚条数，默认为10，最⼤50
     */
    @Schema(title = "每⻚条数", description = "每⻚条数，默认为10，最⼤50", defaultValue = "10")
    private Integer pageSize;
    /**
     * 天翼云眼：⽂件排序规则，1--时间倒序、2--时间顺序，默认时间倒序
     */
    @Schema(title = "天翼云眼：⽂件排序规则", description = "天翼云眼：⽂件排序规则，1--时间倒序、2--时间顺序，默认时间倒序")
    private Integer orderBy;
    /**
     * 天翼云眼：查询类型，1--按天搜索（path）、2--按时间段搜索（默认时间段）
     */
    @Schema(title = "天翼云眼：查询类型", description = "天翼云眼：查询类型，1--按天搜索（path）、2--按时间段搜索（默认时间段）")
    private Integer type;
    /**
     * 中兴：不填为所有流号录像
     */
    @Schema(title = "中兴：不填为所有流号录像", description = "中兴：不填为所有流号录像")
    private String streamId;
    /**
     * 天翼云眼：默认按天查询云存列表， 格式：yyyyMMdd， 例如 20180203。查询天翼云眼的录像⽂件时此字段必填
     */
    @Schema(title = "天翼云眼：默认按天查询云存列表", description = "天翼云眼：默认按天查询云存列表， 格式：yyyyMMdd， 例如 20180203。查询天翼云眼的录像⽂件时此字段必填")
    private String path;
}
