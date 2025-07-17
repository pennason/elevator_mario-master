package com.shmashine.pm.api.service.investigateTask.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblInvestigateTaskDao;
import com.shmashine.pm.api.entity.TblInvestigateTask;
import com.shmashine.pm.api.service.investigateTask.TblInvestigateTaskService;

@Service
public class TblInvestigateTaskServiceImpl implements TblInvestigateTaskService {

    @Resource(type = TblInvestigateTaskDao.class)
    private TblInvestigateTaskDao tblInvestigateTaskDao;

    @Override
    public TblInvestigateTaskDao getTblInvestigateTaskDao() {
        return tblInvestigateTaskDao;
    }

    public TblInvestigateTask getById(String vInvestigateTaskId) {
        return tblInvestigateTaskDao.getById(vInvestigateTaskId);
    }

    public List<TblInvestigateTask> getByEntity(TblInvestigateTask tblInvestigateTask) {
        return tblInvestigateTaskDao.getByEntity(tblInvestigateTask);
    }

    public List<TblInvestigateTask> listByEntity(TblInvestigateTask tblInvestigateTask) {
        return tblInvestigateTaskDao.listByEntity(tblInvestigateTask);
    }

    @CacheEvict(value = "investigateTask", allEntries = true)
    public int insert(TblInvestigateTask tblInvestigateTask) {
        Date date = new Date();
        tblInvestigateTask.setDtCreateTime(date);
        tblInvestigateTask.setDtModifyTime(date);
        return tblInvestigateTaskDao.insert(tblInvestigateTask);
    }

    @CacheEvict(value = "investigateTask", allEntries = true)
    public int update(TblInvestigateTask tblInvestigateTask) {
        tblInvestigateTask.setDtModifyTime(new Date());
        int result = tblInvestigateTaskDao.update(tblInvestigateTask);

        return result;
    }

    @CacheEvict(value = "investigateTask", allEntries = true)
    public int deleteById(String vInvestigateTaskId) {
        return tblInvestigateTaskDao.deleteById(vInvestigateTaskId);
    }

    public int countAll() {
        return tblInvestigateTaskDao.countAll();
    }

    public List<Map> countByStatus(String principalId) {
        return tblInvestigateTaskDao.countByStatus(principalId);
    }

    ;
}
