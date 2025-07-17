package com.shmashine.pm.api.service.testingBill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblTestingBillDao;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.service.testingBill.TblTestingBillService;

@Service
public class TblTestingBillServiceImpl implements TblTestingBillService {

    @Resource(type = TblTestingBillDao.class)
    private TblTestingBillDao tblTestingBillDao;

    @Override
    public TblTestingBillDao getTblTestingBillDao() {
        return tblTestingBillDao;
    }

    @Override
    public TblTestingBill getById(String vInstallingBillId) {
        return tblTestingBillDao.getById(vInstallingBillId);
    }

    @Override
    public TblTestingBill getByEntity(TblTestingBill tblTestingBill) {
        return tblTestingBillDao.getByEntity(tblTestingBill);
    }

    @Override
    public int insert(TblTestingBill tblInstallingBill) {
        Date date = new Date();
        tblInstallingBill.setDtCreateTime(date);
        tblInstallingBill.setDtModifyTime(date);
        return tblTestingBillDao.insert(tblInstallingBill);
    }

    @Override
    public int update(TblTestingBill tblInstallingBill) {
        tblInstallingBill.setDtModifyTime(new Date());
        return tblTestingBillDao.update(tblInstallingBill);
    }

    @Override
    public int deleteById(String vVillageDeviceBillId) {
        return tblTestingBillDao.deleteById(vVillageDeviceBillId);
    }

    @Override
    public int insertBatch(List<TblTestingBill> list) {
        list.forEach(bill -> {
            bill.setDtCreateTime(new Date());
            bill.setDtModifyTime(new Date());
            bill.setiDelFlag(0);
        });
        return tblTestingBillDao.insertBatch(list);
    }
}
