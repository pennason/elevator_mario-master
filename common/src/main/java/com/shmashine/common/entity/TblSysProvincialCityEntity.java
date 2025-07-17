// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/23 11:00
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblSysProvincialCityEntity implements Serializable {
    /**
     * 地区ID
     */
    private Integer areaId;
    /**
     * 地区编码
     */
    private String areaCode;
    /**
     * 地区名称
     */
    private String areaName;
    /**
     * 地区级别 1省 2市 3区县 4乡镇接到
     */
    private Integer level;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市中心点，经纬度坐标
     */
    private String center;
    /**
     * 地区父级ID
     */
    private Integer parentId;
}
