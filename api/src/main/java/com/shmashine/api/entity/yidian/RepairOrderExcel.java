package com.shmashine.api.entity.yidian;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/4/27 14:00
 * @description: com.shmashine.api.entity.yidian
 * <p>
 * 仪电获取急修工单excel
 */
@Data
public class RepairOrderExcel {

    private static final long serialVersionUID = 656673885428575835L;

    @ExcelProperty("电梯注册代码")
    private String registerNumber;

    @ExcelProperty("告警时间")
    private Date reportTime;

    @ExcelProperty("工单状态")
    private String orderStatus;

    @ExcelProperty("完工描述")
    private String description;

}
