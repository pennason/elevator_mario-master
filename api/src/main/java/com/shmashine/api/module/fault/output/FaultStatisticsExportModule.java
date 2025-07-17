package com.shmashine.api.module.fault.output;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 故障统计导出实体类
 *
 * @author little.li
 * @since 2020-06-17 17:45:48
 */
@Data
public class FaultStatisticsExportModule implements Serializable {

    /**
     * 电梯编号
     */
    @ExcelProperty("电梯编号")
    @ColumnWidth(20)
    private String elevatorCode;

    /**
     * 故障类型
     */
    @ExcelProperty("故障类型")
    @ColumnWidth(20)
    private String faultType;


    /**
     * 故障名称
     */
    @ExcelProperty("故障名称")
    @ColumnWidth(20)
    private String faultName;


    /**
     * 故障次数
     */
    @ExcelProperty("故障次数")
    @ColumnWidth(20)
    private Integer faultNumCount;

    /**
     * 故障上报时间
     */
    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty("故障上报时间")
    @ColumnWidth(20)
    private Date reportDate;

}