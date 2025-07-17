package com.shmashine.api.module.workOrder;

import lombok.Data;

/**
 * 创建工单请求实体类
 *
 * @author little.li
 */
@Data
public class InsertWorkOrderModule {

    /**
     * 工单类型 1：故障工单 2：困人工单 3：维保工单 4: 设备工单
     */
    private Integer workOrderType;
    /**
     * 电梯id
     */
    private String elevatorId;

    /**
     * 电梯code
     */
    private String elevatorCode;

    /**
     * 故障ID（创建维保工单时该字段为0）
     */
    private String faultId;

    /**
     * 故障类型（创建维保工单时该字段为0）
     */
    private String faultType;
    /**
     * 维保类型（创建故障、困人工单时该字段为0）
     */
    private Integer maintenanceType;
    /**
     * 当前处理人（创建工单时指定的处理人）
     */
    private String handleUserId;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 资源类型
     */
    private Integer iSourceType;
}
