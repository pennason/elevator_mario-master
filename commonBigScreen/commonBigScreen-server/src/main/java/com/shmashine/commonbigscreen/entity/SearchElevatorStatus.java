package com.shmashine.commonbigscreen.entity;

import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.base.PageListParams;

import lombok.Data;

/**
 * 查询电梯状态参数列表
 */
@Data
public class SearchElevatorStatus extends PageListParams {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 小区id
     */
    private String villageId;

    /**
     * 0：不正常，1：正常
     */
    @NotNull(message = "请输入正常状态")
    private Integer normal;

    /**
     * 0 ：在线，1：离线
     */
    @NotNull(message = "请输入在线状态")
    private Integer isOnLine;

    /**
     * 0：无故障，1：故障中
     */
    @NotNull(message = "请输入故障状态")
    private Integer faultStatus;

    /**
     * 0 正常运行，1 检修模式，2 停止服务
     */
    @NotNull(message = "请输入模式状态")
    private Integer modeStatus;

    /**
     * 0：无困人，1：困人中
     */
    @NotNull(message = "请输入困人状态")
    private Integer peopleTrappedStatus;
}
