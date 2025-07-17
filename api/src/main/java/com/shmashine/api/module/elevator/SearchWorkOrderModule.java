package com.shmashine.api.module.elevator;

import com.shmashine.common.entity.base.PageListParams;


/**
 * 工单列表查询参数类
 *
 * @author little.li
 */

public class SearchWorkOrderModule extends PageListParams {

    /**
     * 工单id
     */
    private String workOrderId;

    /**
     * 工单类型 1：故障工单 2：困人工单 3：维保工单
     */
    private Integer workOrderType;

    /**
     * 电梯编号
     */
    private String elevatorCode;

    /**
     * 当前处理状态
     * 1：创建（系统生成），
     * 2：创建（用户生成），
     * 3：处理中（指派），
     * 4：处理中（驳回），
     * 5：处理中（转交），
     * 6：处理中（跨部门转交），
     * 7：结束（已取消），
     * 8：结束（已完成）,
     * 9：系统结束（故障消除后工单结束）
     */
    private String handleStatus;

    /**
     * 开始时间
     */
    private String createTime;

    /**
     * 结束时间
     */
    private String endTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getWorkOrderType() {
        return workOrderType;
    }

    public void setWorkOrderType(Integer workOrderType) {
        this.workOrderType = workOrderType;
    }

    public String getElevatorCode() {
        return elevatorCode;
    }

    public void setElevatorCode(String elevatorCode) {
        this.elevatorCode = elevatorCode;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }
}
