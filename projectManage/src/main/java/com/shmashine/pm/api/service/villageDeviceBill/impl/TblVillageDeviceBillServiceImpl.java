package com.shmashine.pm.api.service.villageDeviceBill.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblVillageDeviceBill;
import com.shmashine.pm.api.dao.TblVillageDeviceBillDao;
import com.shmashine.pm.api.service.villageDeviceBill.TblVillageDeviceBillService;

@Service
public class TblVillageDeviceBillServiceImpl implements TblVillageDeviceBillService {

    @Resource(type = TblVillageDeviceBillDao.class)
    private TblVillageDeviceBillDao tblVillageDeviceBillDao;

    @Override
    public TblVillageDeviceBillDao getTblVillageDeviceBillDao() {
        return tblVillageDeviceBillDao;
    }

    @Override
    public TblVillageDeviceBill getById(String vVillageDeviceBillId) {
        return tblVillageDeviceBillDao.getById(vVillageDeviceBillId);
    }

    @Override
    public TblVillageDeviceBill getByVillageId(String vVillageId) {
        return tblVillageDeviceBillDao.getByVillageId(vVillageId);
    }

//    @Override
//    public List<TblVillageDeviceBill> listByEntity(TblVillageDeviceBill tblVillageDeviceBill) {
//        return tblVillageDeviceBillDao.listByEntity(tblVillageDeviceBill);
//    }

    @Override
    public int insert(TblVillageDeviceBill tblVillageDeviceBill) {
        Date date = new Date();
        tblVillageDeviceBill.setDtCreateTime(date);
        tblVillageDeviceBill.setDtModifyTime(date);
        return tblVillageDeviceBillDao.insert(tblVillageDeviceBill);
    }

    @Override
    public int update(TblVillageDeviceBill tblVillageDeviceBill) {
        tblVillageDeviceBill.setDtModifyTime(new Date());
        return tblVillageDeviceBillDao.update(tblVillageDeviceBill);
    }

    @Override
    public int deleteById(String vVillageDeviceBillId) {
        return tblVillageDeviceBillDao.deleteById(vVillageDeviceBillId);
    }


}
