// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.entity;

import java.io.Serializable;
import java.sql.Date;
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
 * @version v1.0  -  2023/2/17 13:53
 * @since v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TblGroupLeasingElevatorCoefficientEntity implements Serializable {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 电梯编号
     */
    private String elevatorCode;
    /**
     * 日期 yyyy-MM-dd
     */
    private Date day;
    /**
     * 楼层总数（最低层至少从2楼开始算）
     */
    private Integer floorCount;
    /**
     * 当日总和（小时停靠次数*时间系数）
     */
    private Double dayTotalQuantity;
    /**
     * 统计次数
     */
    private Double coefficient;
    /**
     * 记录生成时间
     */
    private LocalDateTime createTime;
    /**
     * 记录修改时间
     */
    private LocalDateTime modifyTime;
}