package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/10/20 14:07
 * @description: 维保工单分页请求实体
 */
@Data
public class MaintenanceOrderPageReqVO {

    /**
     * 开始时间
     */
    @NotBlank(message = "开始时间不为空")
    private String startTime;

    /**
     * 结束时间
     */
    @NotBlank(message = "结束时间不为空")
    private String endTime;

    /**
     * 工单状态 [0：正常，1：超期]
     */
    private Integer orderStatus;

    /**
     * 小区id
     */
    private String communityId;

    /**
     * 页码
     */
    @NotNull(message = "页码不为空")
    private Integer pageNo;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不为空")
    private Integer pageSize;

}
