package com.shmashine.api.module.workOrder;

import lombok.Data;

/**
 * 维保工单详情
 *
 * @author little.li
 */
@Data
public class ResponseMaintenanceDetailModule {

    /**
     * 维保详情记录id
     */
    private String workOrderMaintenanceDetailId;

    /**
     * 维保详情记录id
     */
    private String maintenanceDetailId;

    /**
     * 维保项名称
     */
    private String maintenanceName;

    /**
     * 维保项说明
     */
    private String maintenanceClaim;

    /**
     * 维保结果 0 未维保，1 已维保
     */
    private Integer result;

}
