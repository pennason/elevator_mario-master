// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 按项目北向推送相关KPI数据
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
public class KpiProjectNorthPushEntity implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 日期 yyyy-MM-dd
     */
    private String day;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 电梯总数
     */
    private Integer elevatorTotal;
    /**
     * 电梯当日最大离线数
     */
    private Integer elevatorOfflineMax;
    /**
     * 电梯当日实时离线数
     */
    private Integer elevatorOfflineRealtime;
    /**
     * 记录生成时间
     */
    private LocalDateTime createTime;
    /**
     * 记录修改时间
     */
    private LocalDateTime modifyTime;
}
