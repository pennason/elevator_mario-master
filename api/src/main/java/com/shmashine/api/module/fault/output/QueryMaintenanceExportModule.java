package com.shmashine.api.module.fault.output;


import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 导出维保信息
 */
@Data
public class QueryMaintenanceExportModule implements Serializable {

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
     * 开始时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "开始时间", index = 4)
    @ColumnWidth(20)
    private Date shouldCompleteDate;

    /**
     * 完成时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "完成时间", index = 5)
    @ColumnWidth(20)
    private Date completeTime;

    /**
     * 维保状态
     */
    @ExcelProperty(value = "维保状态", index = 6)
    @ColumnWidth(20)
    private String vMaintenanceStatus;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态", index = 7)
    @ColumnWidth(20)
    private String vStatusName;

    /**
     * 维保人员
     */
    @ExcelProperty(value = "维保人员", index = 8)
    @ColumnWidth(20)
    private String employeeName;
}
