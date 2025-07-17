package com.shmashine.api.entity.yidian;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/4/27 14:00
 * @description: com.shmashine.api.entity.yidian
 * <p>
 * 仪电获取维保工单excel
 */
@Data
public class MaintenanceOrderExcel {

    private static final long serialVersionUID = 6566738854285899034L;

    @ExcelProperty("电梯注册代码")
    private String registerNumber;

    @ExcelProperty("工单编号")
    private String orderNumber;

    @ExcelProperty("工单状态")
    private String orderStatus;

    @ExcelProperty("发现的主要问题")
    private String majorProblem;

}
