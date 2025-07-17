package com.shmashine.userclientapplets.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 维保记录表
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/9 11:31
 */

@TableName("tbl_third_party_ruijin_work_order")
@Data
@EqualsAndHashCode(callSuper = false)
public class Maintenance extends BaseEntity {

    /**
     * 电梯注册码
     */
    private String registerNumber;

    /**
     * 工单编号
     */
    private String workOrderNumber;

    /**
     * 电梯名
     */
    private String elevatorName;

    /**
     * 电梯安装地址
     */
    private String address;

    /**
     * 签到时间
     */
    private String signTime;

    /**
     * 完成时间
     */
    private String completeTime;

    /**
     * 应完成日期
     */
    private String shouldCompleteDate;

    /**
     * 处理人员姓名
     */
    private String dealEmployeeName;

    /**
     * 处理人电话
     */
    private String dealEmployeeTel;

    /**
     * 保养工单类型编号
     */
    private String orderTypeNumber;
}
