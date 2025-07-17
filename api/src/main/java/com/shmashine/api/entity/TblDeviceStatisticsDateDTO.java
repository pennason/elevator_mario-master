// Copyright (C) 2022 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2022/9/7 15:27
 * @since v1.0
 */

@Data
@ToString
public class TblDeviceStatisticsDateDTO implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dateTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dateTimeEnd;
}
