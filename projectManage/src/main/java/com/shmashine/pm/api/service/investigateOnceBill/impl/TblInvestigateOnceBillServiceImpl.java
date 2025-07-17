package com.shmashine.pm.api.service.investigateOnceBill.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblInvestigateOnceBillDao;
import com.shmashine.pm.api.entity.TblInvestigateOnceBill;
import com.shmashine.pm.api.service.investigateOnceBill.TblInvestigateOnceBillService;

@Service
public class TblInvestigateOnceBillServiceImpl implements TblInvestigateOnceBillService {

    @Autowired
    private TblInvestigateOnceBillDao tblInvestigateOnceBillDao;

    @Override
    public TblInvestigateOnceBill getById(String vInvestigateOnceBillId) {
        return tblInvestigateOnceBillDao.getById(vInvestigateOnceBillId);
    }

    @Override
    public TblInvestigateOnceBill getByTaskId(String vInvestigateTaskId) {
        return tblInvestigateOnceBillDao.getByTaskId(vInvestigateTaskId);
    }

    @Override
    public TblInvestigateOnceBill getByElevatorCode(String vElevatorCode) {
        return tblInvestigateOnceBillDao.getByElevatorCode(vElevatorCode);
    }

    @Override
    public int insert(TblInvestigateOnceBill tblInvestigateOnceBill) {
        tblInvestigateOnceBill.setDtCreateTime(new Date());
        tblInvestigateOnceBill.setDtModifyTime(new Date());
        return tblInvestigateOnceBillDao.insert(tblInvestigateOnceBill);
    }

    @Override
    public int update(TblInvestigateOnceBill tblInvestigateOnceBill) {
        tblInvestigateOnceBill.setDtModifyTime(new Date());
        return tblInvestigateOnceBillDao.update(tblInvestigateOnceBill);
    }

    @Override
    public int deleteById(String vInvestigateOnceBillId) {
        return tblInvestigateOnceBillDao.deleteById(vInvestigateOnceBillId);
    }
}
