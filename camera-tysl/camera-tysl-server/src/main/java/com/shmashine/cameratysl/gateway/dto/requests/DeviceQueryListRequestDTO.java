// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl.gateway.dto.requests;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量查询指定条件设备信息
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/23 15:38
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceQueryListRequestDTO implements Serializable {


    /**
     * 每⻚数量，默认为10，最⼤为50
     */
    @Schema(title = "每页大小", description = "每⻚数量，默认为10，最⼤为50", minimum = "1", maximum = "50", defaultValue = "10")
    private Integer pageSize;
    /**
     * 当前⻚，不传默认1
     */
    @Schema(title = "当前页", description = "当前⻚，不传默认1", example = "1", defaultValue = "1")
    private Integer page;
    /**
     * 设备列表，最⼤为50条
     */
    @Schema(title = "设备列表", description = "设备列表，最⼤为50条")
    private List<String> guidList;
    /**
     * 排序字段，key-value，key为字段名，value为正序倒序，⽬前仅⽀持status字段排序
     */
    @Schema(title = "排序字段", description = "排序字段，key-value，key为字段名，value为正序倒序，⽬前仅⽀持status字段排序 status: desc")
    private Map<String, String> sortFiled;
    /**
     * 设备名称，模糊查询
     */
    @Schema(title = "设备名称", description = "设备名称，模糊查询")
    private String name;
    /**
     * 汇聚平台编号，精确查询,天翼云眼-TYYY，中兴-TYBD，⼤华-QQY
     */
    @Schema(title = "汇聚平台编号", description = "汇聚平台编号，精确查询,天翼云眼-TYYY，中兴-TYBD，⼤华-QQY")
    private List<String> vendorCode;
}
