// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.module.kpi;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 按北向推送统计电梯设备相关KPI数据-导出excel
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/11 18:07
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "KpiProjectNorthPushExcelModule", description = "按北向推送统计电梯设备相关KPI数据-导出excel")
public class KpiProjectNorthPushExcelModule implements Serializable {

    /**
     * 日期 yyyy-MM-dd
     */
    @ExcelProperty(value = {"日期", "日期"}, index = 0)
    @ColumnWidth(15)
    private String day;
    /**
     * 电梯总数
     */
    @ExcelProperty(value = {"项目名", "电梯总数"}, index = 1)
    @ColumnWidth(12)
    private Integer elevatorTotal;
    /**
     * 电梯当日最大离线数
     */
    @ExcelProperty(value = {"项目名", "离线数"}, index = 2)
    @ColumnWidth(10)
    private Integer elevatorOfflineMax;
}
