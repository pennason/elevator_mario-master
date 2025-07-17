package com.shmashine.pm.api.service.distributionBill.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblDistributionBillDao;
import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.module.distributionBill.DistributionBillModule;
import com.shmashine.pm.api.service.distributionBill.TblDistributionBillService;

@Service
public class TblDistributionBillImpl implements TblDistributionBillService {

    @Autowired
    private TblDistributionBillDao tblDistributionBillDao;

    @Override
    public int insert(TblDistributionBill record) {
        record.setDtCreateTime(new Date());
        return tblDistributionBillDao.insert(record);
    }

    @Override
    public TblDistributionBill selectByBillId(String vDistributionBillId) {
        return tblDistributionBillDao.selectByBillId(vDistributionBillId);
    }

    @Override
    public TblDistributionBill selectByElevatorId(String vElevatorId) {
        return tblDistributionBillDao.selectByElevatorId(vElevatorId);
    }

    @Override
    public List<TblDistributionBill> selectByTaskId(String vDistributionTaskId) {
        return tblDistributionBillDao.selectByTaskId(vDistributionTaskId);
    }

    @Override
    public List<Map> selectByBillModule(DistributionBillModule module) {
        return tblDistributionBillDao.selectByBillModule(module);
    }

    @Override
    public Map getRelatedInfo(String vDistributionBillId) {
        return tblDistributionBillDao.getRelatedInfo(vDistributionBillId);
    }

    @Override
    public List<Integer> getAllStatus(DistributionBillModule module) {
        return tblDistributionBillDao.getAllStatus(module);
    }

    @Override
    public int existVerifyCode(String vVerifyCode, String vDistributionTaskId) {
        return tblDistributionBillDao.existVerifyCode(vVerifyCode, vDistributionTaskId);
    }

    @Override
    public int updateByTaskId(String vDistributionTaskId) {
        return tblDistributionBillDao.updateByTaskId(vDistributionTaskId);
    }

    @Override
    public int update(TblDistributionBill record) {
        record.setDtModifyTime(new Date());
        return tblDistributionBillDao.update(record);
    }


    @Override
    public Integer getFloorSensorRemark(String elevatorId) {
        TblDistributionBill tblDistributionBill = tblDistributionBillDao.selectByElevatorId(elevatorId);
        return tblDistributionBill == null ? null : tblDistributionBill.getFloorSensorRemark();
    }


}
