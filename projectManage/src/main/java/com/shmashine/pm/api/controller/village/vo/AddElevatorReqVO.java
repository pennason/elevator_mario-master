package com.shmashine.pm.api.controller.village.vo;

import com.shmashine.common.entity.TblVillage;

import lombok.Data;

/**
 * 添加电梯请求参数
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/12/3 15:19
 * @Since: 1.0.0
 */
@Data
public class AddElevatorReqVO extends TblVillage {

    /**
     * 添加电梯的数量
     */
    private Integer elevatorNumber = 1;

}
