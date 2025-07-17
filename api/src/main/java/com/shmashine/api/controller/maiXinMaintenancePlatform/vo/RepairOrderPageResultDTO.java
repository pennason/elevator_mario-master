package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 11:54
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class RepairOrderPageResultDTO {

    //数据
    private List<RepairOrderResultDTO> list;

    //总量
    private Long total;

}
