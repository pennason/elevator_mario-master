package com.shmashine.api.module.fault.output;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.Data;

/**
 * 设备故障导出
 */
@Data
public class SensorFaultExportModule implements Serializable {

    /**
     * 电梯编号
     */
    @ExcelProperty(value = "电梯编号", index = 0)
    @ColumnWidth(20)
    private String vElevatorCode;

    /**
     * 所属项目
     */
    @ExcelProperty(value = "所属项目", index = 1)
    @ColumnWidth(20)
    private String vProjectName;

    /**
     * 终端类型
     */
    @ExcelProperty(value = "终端类型", index = 3)
    @ColumnWidth(20)
    private String eType;

    /**
     * 所属小区
     */
    @ExcelProperty(value = "所属小区", index = 2)
    @ColumnWidth(20)
    private String vVillageName;

    /**
     * 故障级别
     */
    @ExcelProperty(value = "故障级别", index = 4)
    @ColumnWidth(20)
    private String iLevelName;

    /**
     * 故障类型
     */
    @ExcelProperty(value = "故障类型", index = 5)
    @ColumnWidth(20)
    private String vFaultName;

    /**
     * 安装地址
     */
    @ExcelProperty(value = "安装地址", index = 6)
    @ColumnWidth(20)
    private String vAddress;

    /**
     * 故障上报时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "上报时间", index = 7)
    @ColumnWidth(20)
    private Date dtReportTime;

    /**
     * 最新上报时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "最新上报时间", index = 8)
    @ColumnWidth(20)
    private Date dtModifyTime;

    @ExcelProperty(value = "故障状态", index = 9)
    @ColumnWidth(20)
    private String iStatusName;

    @ExcelProperty(value = "上报次数", index = 10)
    @ColumnWidth(20)
    private Integer iFaultNum;

    @ExcelProperty(value = "服务模式状态名称", index = 11)
    @ColumnWidth(20)
    private String iModeStatusName;
}
