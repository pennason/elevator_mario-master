package com.shmashine.api.controller.maiXinMaintenancePlatform.vo;

import java.util.List;

import lombok.Data;

/**
 * @author  jiangheng
 * @version 2024/1/17 14:05
 * @description: com.shmashine.api.controller.maiXinMaintenancePlatform.vo
 */
@Data
public class EmergencyRescueOrderPageResultDTO {

    //数据
    private List<EmergencyRescueOrderResultDTO> list;

    //总量
    private Long total;

}
