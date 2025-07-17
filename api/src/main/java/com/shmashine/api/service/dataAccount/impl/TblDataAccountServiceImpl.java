package com.shmashine.api.service.dataAccount.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblDataAccountDao;
import com.shmashine.api.entity.TblDataAccount;
import com.shmashine.api.service.dataAccount.TblDataAccountService;

@Service
public class TblDataAccountServiceImpl implements TblDataAccountService {

    @Autowired
    private TblDataAccountDao tblDataAccountDao;

    @Override
    public TblDataAccount getById(String vDataAccountId) {
        return tblDataAccountDao.getById(vDataAccountId);
    }

    @Override
    public TblDataAccount getByEntity(TblDataAccount tblDataAccount) {
        return tblDataAccountDao.getByEntity(tblDataAccount);
    }

    @Override
    public int insert(TblDataAccount tblDataAccount) {
        Date now = new Date();
        tblDataAccount.setDtCreateTime(now);
        tblDataAccount.setDtModifyTime(now);
        tblDataAccount.setiDelFlag(0);
        return tblDataAccountDao.insert(tblDataAccount);
    }

    @Override
    public int update(TblDataAccount tblDataAccount) {
        tblDataAccount.setDtModifyTime(new Date());
        return tblDataAccountDao.update(tblDataAccount);
    }

    @Override
    public int deleteById(String vDateAccountId) {
        return tblDataAccountDao.deleteById(vDateAccountId);
    }
}
