package com.shmashine.api.module.elevatorCollect.input;

import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.base.PageListParams;

/**
 * @PackgeName: com.shmashine.api.module.elevatorCollect.input
 * @ClassName: OperationElevatorCollectModule
 * @Date: 2020/7/915:18
 * @Author: LiuLiFu
 * @Description: 操作收藏电梯所需参数
 */
public class OperationElevatorCollectModule extends PageListParams {
    @NotNull(message = "请输入电梯ID")
    private String elevatorId;

    public String getElevatorId() {
        return elevatorId;
    }

    public void setElevatorId(String elevatorId) {
        this.elevatorId = elevatorId;
    }
}