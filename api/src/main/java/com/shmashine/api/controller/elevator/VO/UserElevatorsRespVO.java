package com.shmashine.api.controller.elevator.VO;

import java.util.List;

import com.shmashine.api.service.elevator.DO.UserElevatorsDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  jiangheng
 * @version 2023/5/25 14:53
 * @description: 用户授权电梯 resp VO
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserElevatorsRespVO {

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 项目总电梯数
     */
    @Builder.Default()
    private Integer totalElevators = 0;

    /**
     * 用户电梯数
     */
    @Builder.Default()
    private Integer userElevators = 0;

    /**
     * 电梯列表
     */
    private List<UserElevatorsDO> elevators;

}
