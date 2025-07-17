package com.shmashine.api.module.fault.output;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 维修工单导出
 */
@Data
public class QueryFaultExportModule implements Serializable {

    /**
     * 电梯编号
     */
    @ExcelProperty(value = "电梯编号", index = 0)
    @ColumnWidth(20)
    private String vElevatorCode;

    /**
     * 电梯名称
     */
    @ExcelProperty(value = "电梯名称", index = 1)
    @ColumnWidth(20)
    private String vElevatorName;

    /**
     * 小区名称
     */
    @ExcelProperty(value = "小区名称", index = 2)
    @ColumnWidth(20)
    private String vVillageName;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称", index = 3)
    @ColumnWidth(20)
    private String vProjectName;

    /**
     * 当前状态
     */
    @ExcelProperty(value = "当前状态", index = 4)
    @ColumnWidth(20)
    private String vStatusName;

    /**
     * 地址
     */
    @ExcelProperty(value = "地址", index = 5)
    @ColumnWidth(20)
    private String vAddress;

    /**
     * 故障类型
     */
    @ExcelProperty(value = "故障类型", index = 6)
    @ColumnWidth(20)
    private String vFaultName;

    /**
     * 时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "时间", index = 7)
    @ColumnWidth(20)
    private Date dtReportTime;
}
