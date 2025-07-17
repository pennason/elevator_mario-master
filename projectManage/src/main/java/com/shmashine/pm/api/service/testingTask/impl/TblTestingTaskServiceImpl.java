package com.shmashine.pm.api.service.testingTask.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.dao.TblTestingBillDao;
import com.shmashine.pm.api.dao.TblTestingTaskDao;
import com.shmashine.pm.api.entity.TblTestingBill;
import com.shmashine.pm.api.entity.TblTestingTask;
import com.shmashine.pm.api.enums.TblTestingBillStatusEnum;
import com.shmashine.pm.api.enums.TblTestingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.service.testingTask.TblTestingTaskService;

@Service
public class TblTestingTaskServiceImpl implements TblTestingTaskService {

    @Resource(type = TblTestingTaskDao.class)
    private TblTestingTaskDao tblTestingTaskDao;

    @Resource(type = TblTestingBillDao.class)
    private TblTestingBillDao tblTestingBillDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

    @Override
    public TblTestingTaskDao getTblTestingTaskDao() {
        return tblTestingTaskDao;
    }

    public TblTestingTask getById(String vTestingTaskId) {
        return tblTestingTaskDao.getById(vTestingTaskId);
    }

    public TblTestingTask getByEntity(TblTestingTask tblTestingTask) {
        return tblTestingTaskDao.getByEntity(tblTestingTask);
    }

    public List<TblTestingTask> listByEntity(TblTestingTask tblTestingTask) {
        return tblTestingTaskDao.listByEntity(tblTestingTask);
    }

    @CacheEvict(value = "testingTask", allEntries = true)
    public int insert(TblTestingTask tblTestingTask) {
        Date date = new Date();
        tblTestingTask.setDtCreateTime(date);
        tblTestingTask.setDtModifyTime(date);
        return tblTestingTaskDao.insert(tblTestingTask);
    }

    @CacheEvict(value = "testingTask", allEntries = true)
    public int update(TblTestingTask tblTestingTask) {
        tblTestingTask.setDtModifyTime(new Date());
        int result = tblTestingTaskDao.update(tblTestingTask);

        if (tblTestingTask.getiStatus() == TblTestingTaskStatusEnum.Tested.getValue()) {
            TblTestingBill module = new TblTestingBill();
            module.setvTestingTaskId(tblTestingTask.getvTestingTaskId());
            List<TblTestingBill> list = tblTestingBillDao.listByEntity(module);

            for (TblTestingBill bill : list) {
                if (bill.getiStatus() == TblTestingBillStatusEnum.Canceled.getValue()) {
                    continue;
                }

                TblElevator tblElevator = tblElevatorDao.getByElevatorCode(bill.getvElevatorCode());
                if (tblElevator != null) {
                    tblElevator.setiPmStatus(TblVillageStatusEnum.CheckLess.getValue());
                    tblElevatorDao.update(tblElevator);
                }
            }
        }

        return result;
    }

    @CacheEvict(value = "testingTask", allEntries = true)
    public int deleteById(String vTestingTaskId) {
        return tblTestingTaskDao.deleteById(vTestingTaskId);
    }

    public int countAll() {
        return tblTestingTaskDao.countAll();
    }

    @Override
    public List<Map> countByStatus() {
        return tblTestingTaskDao.countByStatus();
    }

}
