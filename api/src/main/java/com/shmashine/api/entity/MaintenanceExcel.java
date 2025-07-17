package com.shmashine.api.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/7 10:55
 * <p>
 * 维保工单excel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceExcel {

    @ColumnWidth(24)
    @ExcelProperty(value = "电梯注册代码", index = 0)
    private String registerCode;

    @ExcelProperty(value = "电梯名", index = 1)
    private String elevatorName;

    @ExcelProperty(value = "所属区", index = 2)
    private String region;

    @ExcelProperty(value = "所属街道", index = 3)
    private String street;

    @ExcelProperty(value = "监管部门", index = 4)
    private String supervisionDepartment;

    @ExcelProperty(value = "安装地址", index = 5)
    private String installationAddress;

    @ExcelProperty(value = "使用场所", index = 6)
    private String useAddress;

    @ExcelProperty(value = "维保单位", index = 7)
    private String maintenanceUnit;

    @ExcelProperty(value = "工单编号", index = 8)
    private String workOrderNumber;

    @ExcelProperty(value = "维保类型", index = 9)
    private String maintenanceType;

    @ExcelProperty(value = "工单状态", index = 10)
    private String workOrderStatus;

    @ExcelProperty(value = "工单完成人员", index = 11)
    private String workOrderCompletionStaff;

    @ExcelProperty(value = "工单完成人员电话", index = 12)
    private String workOrderCompletionStaffPhone;

    @ExcelProperty(value = "工单完成时间", index = 13)
    private String workOrderCompletionTime;

    @ExcelProperty(value = "签到时间", index = 14)
    private String signInTime;

    @ExcelProperty(value = "签到人员", index = 15)
    private String signInPeople;

    @ExcelProperty(value = "签到人员电话", index = 16)
    private String signInPhoneNumber;

    @ExcelProperty(value = "确认时间", index = 17)
    private String confirmTime;

    @ExcelProperty(value = "确认状态", index = 18)
    private String confirmStatus;

    @ExcelProperty(value = "使用单位评价", index = 19)
    private String useUnitAppraise;

    @ExcelProperty(value = "使用单位评分", index = 20)
    private String useUnitScore;

    @ExcelProperty(value = "发现的主要问题", index = 21)
    private String mainProblemsFound;

}
