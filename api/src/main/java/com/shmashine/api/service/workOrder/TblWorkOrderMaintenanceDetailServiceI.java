package com.shmashine.api.service.workOrder;

import java.util.List;

import com.shmashine.api.module.workOrder.ResponseMaintenanceDetailModule;
import com.shmashine.common.entity.TblWorkOrderMaintenanceDetail;

public interface TblWorkOrderMaintenanceDetailServiceI {

    int insert(TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail);

    List<ResponseMaintenanceDetailModule> getMaintenanceDetail(String workOrderId);
}