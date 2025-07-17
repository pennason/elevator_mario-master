package com.shmashine.api.module.elevator;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * @Author jiangheng
 * @create 2022/10/24 10:11
 * <p>
 * 电梯运行统计下载
 */
@Data
public class ElevatorRunDataStatisticsDownload implements Serializable {

    @ExcelProperty("电梯编号")
    @ColumnWidth(20)
    private String elevatorCode;

    @ExcelProperty("运行次数")
    @ColumnWidth(20)
    private Integer runCount;

    @ExcelProperty("开关门次数")
    @ColumnWidth(20)
    private Integer doorCount;

    @ExcelProperty("钢丝绳折弯次数")
    @ColumnWidth(20)
    private Integer bendCount;

    @ExcelProperty("平层触发次数")
    @ColumnWidth(20)
    private Integer levelTriggerCount;

    @ExcelProperty("累计运行距离（米）")
    @ColumnWidth(20)
    private Integer runDistanceCount;

    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty("记录日期")
    @ColumnWidth(20)
    private Date reportDate;

}
