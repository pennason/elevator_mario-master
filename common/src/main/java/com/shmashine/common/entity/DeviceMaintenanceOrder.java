package com.shmashine.common.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备运维单
 *
 * @author jiangheng
 * @since 2023/6/29 13:35
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DeviceMaintenanceOrder {

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 小区id
     */
    private Long villageId;

    /**
     * 小区名
     */
    private String villageName;

    /**
     * 地址
     */
    private String address;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 故障名称[上报时间]
     */
    private String faultName;

    /**
     * 工单生成时间
     */
    private Date orderGenerationTime;

    /**
     * 派单时间
     */
    private Date distributeOrdersTime;

    /**
     * 指派执行人
     */
    private String appointed;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 执行人
     */
    private String executor;

    /**
     * 工单状态【0:未派单、1:已派单、2:待审核、3:审核未通过、4:已完成】
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
