package com.shmashine.api.module.workOrder;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 工单request实体类
 *
 * @author little.li
 */
public class RequestWorkOrderModule {


    /**
     * 工单id
     */
    @NotNull(message = "请指定工单编号")
    @Length(min = 1, message = "请指定工单编号")
    private String workOrderId;

    /**
     * 上一处理人
     */
    private String lastUserId;

    /**
     * 当前处理人（被指派的、被转交的）
     */
    private String currentUserId;

    /**
     * 当前处理人（被指派的、被转交的）
     */
    private String currentUserName;

    /**
     * 处理备注
     */
    @NotNull(message = "请填写备注")
    @Length(min = 1, message = "请填写备注")
    private String remarks;

    /**
     * 跨部门转交标识 true：跨部门，false：不跨部门
     */
    private boolean isCrossDept;

    /**
     * 维保项处理结果
     */
    private List<ResponseMaintenanceDetailModule> maintenanceMap;


    public List<ResponseMaintenanceDetailModule> getMaintenanceMap() {
        return maintenanceMap;
    }

    public void setMaintenanceMap(List<ResponseMaintenanceDetailModule> maintenanceMap) {
        this.maintenanceMap = maintenanceMap;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(String lastUserId) {
        this.lastUserId = lastUserId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isCrossDept() {
        return isCrossDept;
    }

    public void setCrossDept(boolean crossDept) {
        isCrossDept = crossDept;
    }
}
