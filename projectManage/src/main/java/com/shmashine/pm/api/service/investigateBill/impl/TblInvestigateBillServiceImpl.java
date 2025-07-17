package com.shmashine.pm.api.service.investigateBill.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblInvestigateBillDao;
import com.shmashine.pm.api.entity.TblInvestigateBill;
import com.shmashine.pm.api.service.investigateBill.TblInvestigateBillService;

@Service
public class TblInvestigateBillServiceImpl implements TblInvestigateBillService {


    @Resource(type = TblInvestigateBillDao.class)
    private TblInvestigateBillDao tblInvestigateBillDao;


    @Override
    public TblInvestigateBill getById(String vVillageDeviceBillId) {
        return tblInvestigateBillDao.getById(vVillageDeviceBillId);
    }

    @Override
    public TblInvestigateBill getByElevatorId(String vElevatorId) {
        return tblInvestigateBillDao.getByElevatorId(vElevatorId);
    }


    @Override
    public int insert(TblInvestigateBill tblInvestigateBill) {
        Date date = new Date();
        tblInvestigateBill.setDtCreateTime(date);
        tblInvestigateBill.setDtModifyTime(date);
        return tblInvestigateBillDao.insert(tblInvestigateBill);
    }

    @Override
    public int insertBatch(List<TblInvestigateBill> list) {
        return tblInvestigateBillDao.insertBatch(list);
    }

    @Override
    public int update(TblInvestigateBill tblInvestigateBill) {
        tblInvestigateBill.setDtModifyTime(new Date());
        return tblInvestigateBillDao.update(tblInvestigateBill);
    }

    @Override
    public int deleteById(String vVillageDeviceBillId) {
        return tblInvestigateBillDao.deleteById(vVillageDeviceBillId);
    }

    @Override
    public List<TblInvestigateBill> listByEntity(TblInvestigateBill tblInvestigateBill) {
        return tblInvestigateBillDao.listByEntity(tblInvestigateBill);
    }


}
