package com.shmashine.pm.api.service.checkingTask.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblCheckingBillDao;
import com.shmashine.pm.api.dao.TblCheckingTaskDao;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.entity.TblCheckingBill;
import com.shmashine.pm.api.entity.TblCheckingTask;
import com.shmashine.pm.api.enums.TblCheckingBillEnum;
import com.shmashine.pm.api.enums.TblCheckingTaskEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.service.checkingTask.TblCheckingTaskService;

@Service
public class TblCheckingTaskServiceImpl implements TblCheckingTaskService {


    @Resource(type = TblCheckingTaskDao.class)
    private TblCheckingTaskDao tblCheckingTaskDao;

    @Resource(type = TblCheckingBillDao.class)
    private TblCheckingBillDao tblCheckingBillDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

    @Override
    public TblCheckingTaskDao getTblCheckingTaskDao() {
        return tblCheckingTaskDao;
    }

    public TblCheckingTask getById(String vCheckingTaskId) {
        return tblCheckingTaskDao.getById(vCheckingTaskId);
    }

    public TblCheckingTask getByEntity(TblCheckingTask tblCheckingTask) {
        return tblCheckingTaskDao.getByEntity(tblCheckingTask);
    }

    public List<TblCheckingTask> listByEntity(TblCheckingTask tblCheckingTask) {
        return tblCheckingTaskDao.listByEntity(tblCheckingTask);
    }

    @CacheEvict(value = "checkingTask", allEntries = true)
    public int insert(TblCheckingTask tblCheckingTask) {
        Date date = new Date();
        tblCheckingTask.setDtCreateTime(date);
        tblCheckingTask.setDtModifyTime(date);
        return tblCheckingTaskDao.insert(tblCheckingTask);
    }

    @CacheEvict(value = "checkingTask", allEntries = true)
    public int update(TblCheckingTask tblCheckingTask) {
        tblCheckingTask.setDtModifyTime(new Date());
        int result = tblCheckingTaskDao.update(tblCheckingTask);

        if (tblCheckingTask.getiStatus() == TblCheckingTaskEnum.Checked.getValue()) {
            List<TblCheckingBill> list = tblCheckingBillDao.selectByCheckingTaskId(tblCheckingTask.getvCheckingTaskId());

            for (TblCheckingBill bill : list) {
                if (bill.getiStatus() == TblCheckingBillEnum.Canceled.getValue()) {
                    continue;
                }

                TblElevator tblElevator = tblElevatorDao.getByElevatorCode(bill.getvElevatorCode());
                if (tblElevator != null) {
                    tblElevator.setiPmStatus(TblVillageStatusEnum.Runing.getValue());
                    tblElevator.setIInstallStatus(1);
                    tblElevatorDao.update(tblElevator);
                }
            }
        }

        return result;
    }

    @CacheEvict(value = "checkingTask", allEntries = true)
    public int deleteById(String vCheckingTaskId) {
        return tblCheckingTaskDao.deleteById(vCheckingTaskId);
    }

    public int countAll() {
        return tblCheckingTaskDao.countAll();
    }

    public Map countByStatus() {
        return tblCheckingTaskDao.countByStatus();
    }

    ;
}
