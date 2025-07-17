package com.shmashine.api.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/12/6 15:56
 * <p>
 * 急修excel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairExcel {

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

    @ExcelProperty(value = "维保单位(现)", index = 6)
    private String maintenanceUnitNow;

    @ExcelProperty(value = "维保单位(工单)", index = 7)
    private String maintenanceUnitWorkOrder;

    @ExcelProperty(value = "告警时间", index = 8)
    private String reportTime;

    @ExcelProperty(value = "告警渠道", index = 9)
    private String reportChannel;

    @ExcelProperty(value = "上报人", index = 10)
    private String reportPeople;

    @ExcelProperty(value = "上报电话", index = 11)
    private String reportPhoneNumber;

    @ExcelProperty(value = "上报故障类型", index = 12)
    private String reportFaultType;

    @ExcelProperty(value = "事件内容", index = 13)
    private String eventContent;

    @ExcelProperty(value = "工单状态", index = 14)
    private String workOrderStatus;

    @ExcelProperty(value = "签到时间", index = 15)
    private String signInTime;

    @ExcelProperty(value = "签到单位", index = 16)
    private String signInUnit;

    @ExcelProperty(value = "签到人", index = 17)
    private String signInPeople;

    @ExcelProperty(value = "签到电话", index = 18)
    private String signInPhoneNumber;

    @ExcelProperty(value = "误报操作时间", index = 19)
    private String falseAlarmOperationTime;

    @ExcelProperty(value = "误报操作单位", index = 20)
    private String falseAlarmOperationUnit;

    @ExcelProperty(value = "误报操作人", index = 21)
    private String falseAlarmOperationPeople;

    @ExcelProperty(value = "误报操作人电话", index = 22)
    private String falseAlarmOperationPeoplePhoneNumber;

    @ExcelProperty(value = "完工时间", index = 23)
    private String completionTime;

    @ExcelProperty(value = "完工单位", index = 24)
    private String completionUnit;

    @ExcelProperty(value = "完工人", index = 25)
    private String completionPeople;

    @ExcelProperty(value = "完工人电话", index = 26)
    private String completionPeoplePhoneNumber;

    @ExcelProperty(value = "完工故障类型", index = 27)
    private String completionFaultType;

    @ExcelProperty(value = "完工描述", index = 28)
    private String completionDescription;

    @ExcelProperty(value = "确认时间", index = 29)
    private String confirmTime;

    @ExcelProperty(value = "确认类型", index = 30)
    private String confirmType;

}
