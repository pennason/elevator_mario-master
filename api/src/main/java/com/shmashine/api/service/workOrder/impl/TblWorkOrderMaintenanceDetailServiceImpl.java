package com.shmashine.api.service.workOrder.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblMaintenanceDefinitionDao;
import com.shmashine.api.dao.TblWorkOrderMaintenanceDetailDao;
import com.shmashine.api.module.workOrder.ResponseMaintenanceDetailModule;
import com.shmashine.api.service.workOrder.TblWorkOrderMaintenanceDetailServiceI;
import com.shmashine.common.entity.TblWorkOrderMaintenanceDetail;

@Service
public class TblWorkOrderMaintenanceDetailServiceImpl implements TblWorkOrderMaintenanceDetailServiceI {


    @Resource(type = TblWorkOrderMaintenanceDetailDao.class)
    private TblWorkOrderMaintenanceDetailDao maintenanceDetailDao;

    @Resource(type = TblMaintenanceDefinitionDao.class)
    private TblMaintenanceDefinitionDao maintenanceDefinitionDao;

    @Override
    public int insert(TblWorkOrderMaintenanceDetail tblWorkOrderMaintenanceDetail) {
        Date date = new Date();
        tblWorkOrderMaintenanceDetail.setDtCreateTime(date);
        tblWorkOrderMaintenanceDetail.setDtModifyTime(date);
        return maintenanceDetailDao.insert(tblWorkOrderMaintenanceDetail);
    }

    @Override
    public List<ResponseMaintenanceDetailModule> getMaintenanceDetail(String workOrderId) {
        return maintenanceDetailDao.getMaintenanceDetail(workOrderId);
    }


    /**
     * 更新维保记录标记
     *
     * @param workOrderId 工单id
     */
    private void updateHistoryFlagByWorkOrderId(String workOrderId) {
        maintenanceDetailDao.updateHistoryFlagByWorkOrderId(workOrderId);
    }


}