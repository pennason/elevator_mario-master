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
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/3 10:57
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblGroupLeasingStatisticsEntity implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 小区ID
     */
    private String villageId;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 统计级别，elevator, floor
     */
    private String statisticsType;
    /**
     * 楼层 如果是电梯级别，此值非空，或者0
     */
    private String floor;
    /**
     * 日群租系数
     */
    private Double dayCoefficient;
    /**
     * 平均的群租系数
     */
    private Double averageCoefficient;
    /**
     * 百分比，使用小数表示
     */
    private Double percent;
    /**
     * 等级，0：不可能（均值以下）， 1：可疑（均值~均值*1.5），2：很可疑（均值*1.5~均值*2.0），3：非常可疑（均值*2.0~均值*2.5），4：高度可疑（均值*2.5以上）
     */
    private Integer level;
    /**
     * 记录生成时间
     */
    private LocalDateTime createTime;
    /**
     * 记录修改时间
     */
    private LocalDateTime modifyTime;
}