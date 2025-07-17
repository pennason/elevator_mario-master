package com.shmashine.sender.server.fault.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblFaultSendLog;
import com.shmashine.sender.dao.TblFaultSendLogDao;
import com.shmashine.sender.server.fault.TblFaultSendLogService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblFaultSendLogServiceImpl implements TblFaultSendLogService {

    @Resource(type = TblFaultSendLogDao.class)
    private TblFaultSendLogDao tblFaultSendLogDao;

    @Override
    public TblFaultSendLogDao getTblFaultSendLogDao() {
        return tblFaultSendLogDao;
    }

    @Override
    public TblFaultSendLog getById(Integer id) {
        return tblFaultSendLogDao.getById(id);
    }

    @Override
    public List<TblFaultSendLog> getByEntity(TblFaultSendLog tblFaultSendLog) {
        return tblFaultSendLogDao.getByEntity(tblFaultSendLog);
    }

    @Override
    public List<TblFaultSendLog> listByEntity(TblFaultSendLog tblFaultSendLog) {
        return tblFaultSendLogDao.listByEntity(tblFaultSendLog);
    }

    @Override
    public List<TblFaultSendLog> listByIds(List<Integer> ids) {
        return tblFaultSendLogDao.listByIds(ids);
    }

    @Override
    public int insert(TblFaultSendLog tblFaultSendLog) {
        return tblFaultSendLogDao.insert(tblFaultSendLog);
    }

    @Override
    public int insertIsNotEmpty(TblFaultSendLog tblFaultSendLog) {
        return tblFaultSendLogDao.insertIsNotEmpty(tblFaultSendLog);
    }

    @Override
    public int insertBatch(List<TblFaultSendLog> list) {
        return tblFaultSendLogDao.insertBatch(list);
    }

    @Override
    public int countAll() {
        return tblFaultSendLogDao.countAll();
    }

    @Override
    public int countByEntity(TblFaultSendLog tblFaultSendLog) {
        return tblFaultSendLogDao.countByEntity(tblFaultSendLog);
    }

    @Override
    public List<TblFaultSendLog> getNeedRetryPushFaults() {
        return tblFaultSendLogDao.getNeedRetryPushFaults();
    }

    @Override
    public void updateById(TblFaultSendLog fault) {
        tblFaultSendLogDao.updateById(fault);
    }

}