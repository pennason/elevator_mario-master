package com.shmashine.api.service.verifyCode.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblVerifyCodeDao;
import com.shmashine.api.entity.TblVerifyCode;
import com.shmashine.api.service.verifyCode.ITblVerifyCodeService;
import com.shmashine.common.constants.ServiceConstants;
import com.shmashine.common.utils.TimeMillisUtil;

@Service
public class TblVerifyCodeServiceImpl implements ITblVerifyCodeService {

    @Autowired
    private TblVerifyCodeDao tblVerifyCodeDao;

    @Override
    public int insertRecord(TblVerifyCode tblVerifyCode) {
        Date now = new Date();

        tblVerifyCode.setDtCreateTime(now);
        tblVerifyCode.setDtExpiredTime(TimeMillisUtil.exchangeStampToDate(now.getTime() + ServiceConstants.LOGIN_VERIFY_CODE_EXPIRED_PEROID));
        return tblVerifyCodeDao.insertRecord(tblVerifyCode);
    }

    @Override
    public int updateRecord(TblVerifyCode tblVerifyCode) {
        Date now = new Date();

        tblVerifyCode.setDtModifyTime(now);
        return tblVerifyCodeDao.updateRecord(tblVerifyCode);
    }

    @Override
    public TblVerifyCode findRecord(String mobileNum, int type) {

        return tblVerifyCodeDao.findRecord(mobileNum, type);
    }
}
