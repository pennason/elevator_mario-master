package com.shmashine.api.entity;

import java.io.Serializable;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/15 16:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportRunCountInfo implements Serializable {

    private static final long serialVersionUID = 65667698321535L;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯Id", index = 0)
    private String v_elevator_id;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯Name", index = 1)
    private String v_elevator_name;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯Code", index = 2)
    private String v_elevator_code;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯注册码", index = 3)
    private String v_equipment_code;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯品牌", index = 4)
    private String brandName;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯使用类型", index = 5)
    private String i_elevator_use_type_name;

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯类型", index = 6)
    private String i_elevator_type_name;

    @ColumnWidth(24)
    @ExcelProperty(value = "上次维保日期", index = 7)
    private String d_last_maintain_date;

    @ColumnWidth(24)
    @ExcelProperty(value = "下次年检日期", index = 8)
    private String d_next_inspect_date;

    @ColumnWidth(24)
    @ExcelProperty(value = "困人次数", index = 9)
    private String rescue = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "故障数", index = 10)
    private String fault = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "维保次数", index = 11)
    private String workOrderNum = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "运行总次数", index = 12)
    private String run_count = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "开关门次数", index = 13)
    private String door_count = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "关门受阻挡次数", index = 14)
    private String doorStopNumber = "0";

    @ColumnWidth(24)
    @ExcelProperty(value = "电动车乘梯次数", index = 15)
    private String electro_mobile_count = "0";

}
