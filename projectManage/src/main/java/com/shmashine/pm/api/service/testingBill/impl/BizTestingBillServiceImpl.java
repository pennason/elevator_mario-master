package com.shmashine.pm.api.service.testingBill.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.BizTestingBillDao;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.dto.TblTestingBillDto;
import com.shmashine.pm.api.module.testingBill.input.TestingBillModule;
import com.shmashine.pm.api.service.testingBill.BizTestingBillService;

@Service
public class BizTestingBillServiceImpl implements BizTestingBillService {

    @Autowired
    private BizTestingBillDao bizTestingBillDao;

    @Override
    public TblTestingBillDto getByBizInfo(TblTestingBill tblTestingBill) {
        return bizTestingBillDao.getByBizInfo(tblTestingBill);
    }

    @Override
    public List<Integer> getAllStatus(TestingBillModule module) {
        return bizTestingBillDao.getAllStatus(module);
    }

//    @Override
//    public List<Integer> getAllStatusByVillageId(String vVillageId) {
//        return bizTestingBillDao.getAllStatusByVillageId(vVillageId);
//    }

    @Override
    public List<Map> getStatusInfo(String vVillageId) {
        return bizTestingBillDao.getStatusInfo(vVillageId);
    }

    @Override
    public Map getInfoAndElevatorInfo(TblTestingBill tblTestingBill) {
        return bizTestingBillDao.getInfoAndElevatorInfo(tblTestingBill);
    }

    @Override
    public List<Map> selectByBillModule(TestingBillModule module) {
        return bizTestingBillDao.selectByBillModule(module);
    }
}
