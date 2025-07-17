package com.shmashine.pm.api.service.distributionTask.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblDistributionBillDao;
import com.shmashine.pm.api.dao.TblDistributionTaskDao;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.entity.TblDistributionBill;
import com.shmashine.pm.api.entity.TblDistributionTask;
import com.shmashine.pm.api.enums.TblDistributionBillStatusEnum;
import com.shmashine.pm.api.enums.TblDistributionTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.service.distributionTask.TblDistributionTaskService;

@Service
public class TblDistributionTaskServiceImpl implements TblDistributionTaskService {

    @Resource(type = TblDistributionTaskDao.class)
    private TblDistributionTaskDao tblDistributionTaskDao;

    @Resource(type = TblDistributionBillDao.class)
    private TblDistributionBillDao tblDistributionBillDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

    @Override
    public TblDistributionTaskDao getTblDistributionTaskDao() {
        return tblDistributionTaskDao;
    }

    public TblDistributionTask getById(String vDistributionTaskId) {
        return tblDistributionTaskDao.getById(vDistributionTaskId);
    }

    public List<TblDistributionTask> getByEntity(TblDistributionTask tblDistributionTask) {
        return tblDistributionTaskDao.getByEntity(tblDistributionTask);
    }

    public List<TblDistributionTask> listByEntity(TblDistributionTask tblDistributionTask) {
        return tblDistributionTaskDao.listByEntity(tblDistributionTask);
    }

    @CacheEvict(value = "distributionTask", allEntries = true)
    public int insert(TblDistributionTask tblDistributionTask) {
        Date date = new Date();
        tblDistributionTask.setDtCreateTime(date);
        tblDistributionTask.setDtModifyTime(date);
        return tblDistributionTaskDao.insert(tblDistributionTask);
    }

    @CacheEvict(value = "distributionTask", allEntries = true)
    public int update(TblDistributionTask tblDistributionTask) {

        tblDistributionTask.setDtModifyTime(new Date());
        int result = tblDistributionTaskDao.update(tblDistributionTask);

        tblDistributionTask = tblDistributionTaskDao.getById(tblDistributionTask.getvDistributionTaskId());

        if (tblDistributionTask.getiStatus() == TblDistributionTaskStatusEnum.Completed.getValue()) {
            List<TblDistributionBill> list = tblDistributionBillDao.selectByTaskId(tblDistributionTask.getvDistributionTaskId());

            for (TblDistributionBill bill : list) {

                if (bill.getiStatus() == TblDistributionBillStatusEnum.Canceled.getValue()) {
                    continue;
                }

                TblElevator elevator = tblElevatorDao.getById(bill.getvElevatorId());

                elevator.setVElevatorCode(bill.getvElevatorCode());
                elevator.setiPmStatus(TblVillageStatusEnum.InstallLess.getValue());
                tblElevatorDao.update(elevator);
            }
        }

        return result;
    }

    @CacheEvict(value = "distributionTask", allEntries = true)
    public int deleteById(String vDistributionTaskId) {
        return tblDistributionTaskDao.deleteById(vDistributionTaskId);
    }

    public int countAll() {
        return tblDistributionTaskDao.countAll();
    }

    @Override
    public List<Map> countByStatus() {
        return tblDistributionTaskDao.countByStatus();
    }
}
