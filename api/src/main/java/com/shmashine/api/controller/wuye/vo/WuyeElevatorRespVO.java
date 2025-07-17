package com.shmashine.api.controller.wuye.vo;

import com.shmashine.common.entity.TblElevator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author  jiangheng
 * @version 2023/10/20 11:31
 * @description: com.shmashine.api.controller.wuye.vo
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WuyeElevatorRespVO extends TblElevator {

    /**
     * 电梯收藏标识（0：未收藏；1：收藏）
     */
    private Integer isCollect = 0;

}
