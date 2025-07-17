package com.shmashine.pm.api.service.checkingBill.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblCheckingBillDao;
import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.module.checkingBill.CheckingBillModule;
import com.shmashine.pm.api.service.checkingBill.TblCheckingBillService;

@Service
public class TblCheckBillServiceImpl implements TblCheckingBillService {

    @Autowired
    private TblCheckingBillDao tblCheckingBillDao;


    @Override
    public int insert(TblCheckingBill record) {
        return tblCheckingBillDao.insert(record);
    }

    @Override
    public int batchInsert(List<TblCheckingBill> list) {
        return tblCheckingBillDao.batchInsert(list);
    }

    @Override
    public int update(TblCheckingBill record) {
        return tblCheckingBillDao.update(record);
    }

    @Override
    public List<TblCheckingBill> selectListByEntity(TblCheckingBill record) {
        return tblCheckingBillDao.selectListByEntity(record);
    }

    @Override
    public TblCheckingBill selectByCheckingTaskBillId(String vCheckingTaskBillId) {
        return tblCheckingBillDao.selectByBillId(vCheckingTaskBillId);
    }

    @Override
    public TblCheckingBill selectByElevatorCode(String vElevatorCode) {
        return tblCheckingBillDao.selectByElevatorCode(vElevatorCode);
    }

    @Override
    public List<Integer> getAllStatus(CheckingBillModule checkingBillModule) {
        return tblCheckingBillDao.getAllStatus(checkingBillModule);
    }

//    @Override
//    public List<Integer> getAllStatusByVillageId(String vVillageId) {
//        return tblCheckingBillDao.getAllStatusByVillageId(vVillageId);
//    }

    @Override
    public List<Map> selectByBillModule(CheckingBillModule checkingBillModule) {
        return tblCheckingBillDao.selectByBillModule(checkingBillModule);
    }

    @Override
    public TblCheckingBill selectByBillId(String tblCheckingBillId) {
        return tblCheckingBillDao.selectByBillId(tblCheckingBillId);
    }
}
