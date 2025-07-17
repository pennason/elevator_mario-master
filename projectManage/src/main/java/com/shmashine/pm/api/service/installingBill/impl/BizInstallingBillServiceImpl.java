package com.shmashine.pm.api.service.installingBill.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.BizInstallingBillDao;
import com.shmashine.pm.api.entity.dto.TblInstallingBillDto;
import com.shmashine.pm.api.module.installingBill.InstallingBillModule;
import com.shmashine.pm.api.service.installingBill.BizInstallingBillService;

@Service
public class BizInstallingBillServiceImpl implements BizInstallingBillService {

    @Autowired
    private BizInstallingBillDao bizInstallingBillDao;

    @Override
    public List<Map> getStatusInfo(String vVillageId) {
        return bizInstallingBillDao.getStatusInfo(vVillageId);
    }

    @Override
    public TblInstallingBillDto getBizInfoById(String vInstallingBillId) {
        return bizInstallingBillDao.getBizInfoById(vInstallingBillId);
    }

    @Override
    public List<Integer> getAllStatus(InstallingBillModule installingBillModule) {
        return bizInstallingBillDao.getAllStatus(installingBillModule);
    }

//    @Override
//    public List<Integer> getAllStatusByVillageId(String vVillageId) {
//        return bizInstallingBillDao.getAllStatusByVillageId(vVillageId);
//    }

    @Override
    public List<Map> selectByBillModule(InstallingBillModule module) {
        return bizInstallingBillDao.selectByBillModule(module);
    }
}
