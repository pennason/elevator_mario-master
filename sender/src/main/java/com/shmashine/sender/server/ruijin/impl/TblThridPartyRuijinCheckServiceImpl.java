package com.shmashine.sender.server.ruijin.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblThridPartyRuijinCheck;
import com.shmashine.sender.dao.TblThridPartyRuijinCheckDao;
import com.shmashine.sender.server.ruijin.TblThridPartyRuijinCheckService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblThridPartyRuijinCheckServiceImpl implements TblThridPartyRuijinCheckService {

    @Autowired
    private TblThridPartyRuijinCheckDao tblThridPartyRuijinCheckDao;


    @Override
    public int insert(TblThridPartyRuijinCheck tblThridPartyRuijinCheck) {
        return tblThridPartyRuijinCheckDao.insert(tblThridPartyRuijinCheck);
    }

    @Override
    public int insertBatch(List<TblThridPartyRuijinCheck> list) {
        return tblThridPartyRuijinCheckDao.insertBatch(list);
    }

    @Override
    public int deleteByReportNumber(String reportNumber) {
        return tblThridPartyRuijinCheckDao.deleteByReportNumber(reportNumber);
    }

    @Override
    public int deleteAll() {
        return tblThridPartyRuijinCheckDao.deleteAll();
    }

    @Override
    public TblThridPartyRuijinCheck getElevatorCheckInfo(String reportNumer) {
        return tblThridPartyRuijinCheckDao.getElevatorCheckInfo(reportNumer);
    }

    @Override
    public TblThridPartyRuijinCheck getElevatorLastCheckInfoByRegisterNumer(String registerNumber) {
        return tblThridPartyRuijinCheckDao.getElevatorLastCheckInfoByRegisterNumer(registerNumber);
    }
}