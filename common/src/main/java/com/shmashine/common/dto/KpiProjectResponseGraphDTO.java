// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 显示图表数据，用于图形化展示
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/5 10:17
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "KpiProjectResponseGraphDTO", description = "显示图表数据，用于图形化展示")
public class KpiProjectResponseGraphDTO implements Serializable {
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 统计分类字段，一般X轴的字段定义
     */
    private List<String> categories;
    /**
     * 数据明细
     */
    private List<SeriesItem> series;

    /**
     * 每项数据明细
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SeriesItem implements Serializable {
        /**
         * 数据名称
         */
        private String name;
        /**
         * 数据值
         */
        private List<Integer> data;
    }

}
