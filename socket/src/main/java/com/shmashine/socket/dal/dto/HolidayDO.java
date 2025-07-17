package com.shmashine.socket.dal.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节假日信息
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/11 10:05
 * @Since: 1.0.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HolidayDO implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 是否为节假日
     */
    private Boolean holiday;

    /**
     * 节假日类型(0, 1, 2, 3)，分别表示 工作日、周末、节日、调休
     */
    private Integer type;

    /**
     * 节假日名称
     */
    private String name;

    /**
     * 日期
     */
    private Date date;
}
