package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2023/10/17 16:49
 * @description: 麦信维保平台维保工单分页数据
 */
@Data
public class MaintenancePageResultDTO implements Serializable {

    //数据
    private List<MaintenanceResultDTO> list;

    //总量
    private Long total;

}
