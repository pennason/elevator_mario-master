package com.shmashine.pm.api.service.installingBill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblDeviceDao;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.dao.TblInstallingBillDao;
import com.shmashine.pm.api.dao.TblInvestigateBillDao;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.service.installingBill.TblInstallingBillService;

@Service
public class TblInstallingBillServiceImpl implements TblInstallingBillService {

    @Resource(type = TblInstallingBillDao.class)
    private TblInstallingBillDao tblInstallingBillDao;

    @Resource
    private TblElevatorDao tblElevatorDao;

    @Resource
    private TblInvestigateBillDao tblInvestigateBillDao;

    @Resource
    private TblDeviceDao tblDeviceDao;

    @Override
    public TblInstallingBillDao getTblInstallingBillDao() {
        return tblInstallingBillDao;
    }

    @Override
    public TblInstallingBill getById(String vInstallingBillId) {
        return tblInstallingBillDao.getById(vInstallingBillId);
    }

    @Override
    public TblInstallingBill getByEntity(TblInstallingBill tblInstallingBill) {
        return tblInstallingBillDao.getByEntity(tblInstallingBill);
    }

    @Override
    public int insert(TblInstallingBill tblInstallingBill) {
        Date date = new Date();
        tblInstallingBill.setDtCreateTime(date);
        tblInstallingBill.setDtModifyTime(date);
        return tblInstallingBillDao.insert(tblInstallingBill);
    }

    @Override
    public int update(TblInstallingBill tblInstallingBill) {
        tblInstallingBill.setDtModifyTime(new Date());
        return tblInstallingBillDao.update(tblInstallingBill);
    }

    @Override
    public int deleteById(String vVillageDeviceBillId) {
        return tblInstallingBillDao.deleteById(vVillageDeviceBillId);
    }

    @Override
    public int insertBatch(List<TblInstallingBill> list) {
        list.forEach(bill -> {
            bill.setDtCreateTime(new Date());
            bill.setDtModifyTime(new Date());
            bill.setiDelFlag(0);
        });
        return tblInstallingBillDao.insertBatch(list);
    }

    @Override
    public List<TblInstallingBill> selectByTaskId(String vInstallingTaskId) {
        return tblInstallingBillDao.selectByTaskId(vInstallingTaskId);
    }
}
