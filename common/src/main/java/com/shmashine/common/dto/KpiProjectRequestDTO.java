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
 * 项目KPI，北向推送KPI 查询参数
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/30 15:10
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(name = "KpiProjectRequestDTO", description = "项目KPI，北向推送KPI 查询参数")
public class KpiProjectRequestDTO implements Serializable {
    /**
     * 项目ID
     */
    private String projectId;

    private List<String> projectIds;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 开始日期 yyyy-MM-dd
     */
    private String startDate;
    /**
     * 结束日期 yyyy-MM-dd
     */
    private String endDate;
}
