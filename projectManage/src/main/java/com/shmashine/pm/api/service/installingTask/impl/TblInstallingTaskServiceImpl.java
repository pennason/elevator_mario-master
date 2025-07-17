package com.shmashine.pm.api.service.installingTask.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.dao.TblInstallingBillDao;
import com.shmashine.pm.api.dao.TblInstallingTaskDao;
import com.shmashine.pm.api.entity.TblInstallingBill;
import com.shmashine.pm.api.entity.TblInstallingTask;
import com.shmashine.pm.api.enums.TblInstallingBillStatusEnum;
import com.shmashine.pm.api.enums.TblInstallingTaskStatusEnum;
import com.shmashine.pm.api.enums.TblVillageStatusEnum;
import com.shmashine.pm.api.service.installingTask.TblInstallingTaskService;

@Service
public class TblInstallingTaskServiceImpl implements TblInstallingTaskService {

    @Resource(type = TblInstallingTaskDao.class)
    private TblInstallingTaskDao tblInstallingTaskDao;

    @Resource(type = TblInstallingBillDao.class)
    private TblInstallingBillDao tblInstallingBillDao;

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

    @Override
    public TblInstallingTaskDao getTblInstallingTaskDao() {
        return tblInstallingTaskDao;
    }

    public TblInstallingTask getById(String vInstallingTaskId) {
        return tblInstallingTaskDao.getById(vInstallingTaskId);
    }

    public List<TblInstallingTask> getByEntity(TblInstallingTask tblInstallingTask) {
        return tblInstallingTaskDao.getByEntity(tblInstallingTask);
    }

    public List<TblInstallingTask> listByEntity(TblInstallingTask tblInstallingTask) {
        return tblInstallingTaskDao.listByEntity(tblInstallingTask);
    }

    @CacheEvict(value = "installingTask", allEntries = true)
    public int insert(TblInstallingTask tblInstallingTask) {
        Date date = new Date();
        tblInstallingTask.setDtCreateTime(date);
        tblInstallingTask.setDtModifyTime(date);
        return tblInstallingTaskDao.insert(tblInstallingTask);
    }

    @CacheEvict(value = "installingTask", allEntries = true)
    public int update(TblInstallingTask tblInstallingTask) {
        tblInstallingTask.setDtModifyTime(new Date());
        int result = tblInstallingTaskDao.update(tblInstallingTask);

        if (tblInstallingTask.getiStatus() == TblInstallingTaskStatusEnum.Installed.getValue()) {
            List<TblInstallingBill> list = tblInstallingBillDao.selectByTaskId(tblInstallingTask.getvInstallingTaskId());

            for (TblInstallingBill bill : list) {
                if (bill.getiStatus() == TblInstallingBillStatusEnum.Canceled.getValue()) {
                    continue;
                }
                TblElevator elevator = tblElevatorDao.getByElevatorCode(bill.getvElevatorCode());
//                elevator.setVElevatorCode(bill.getvElevatorCode());
                elevator.setiPmStatus(TblVillageStatusEnum.TestLess.getValue());
                tblElevatorDao.update(elevator);
            }
        }
        return result;
    }

    @CacheEvict(value = "installingTask", allEntries = true)
    public int deleteById(String vInstallingTaskId) {
        return tblInstallingTaskDao.deleteById(vInstallingTaskId);
    }

    @Override
    public List<Map> countByStatus() {
        return tblInstallingTaskDao.countByStatus();
    }

    public int countAll() {
        return tblInstallingTaskDao.countAll();
    }
}
