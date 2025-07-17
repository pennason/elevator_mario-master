package com.shmashine.api.service.elevatorcollect.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorCollectDao;
import com.shmashine.api.service.elevatorcollect.TblElevatorCollectServiceI;
import com.shmashine.common.entity.TblElevatorCollect;

@Service
public class TblElevatorCollectServiceImpl implements TblElevatorCollectServiceI {

    @Resource(type = TblElevatorCollectDao.class)
    private TblElevatorCollectDao tblElevatorCollectDao;

    @Override
    public TblElevatorCollectDao getTblElevatorCollectDao() {
        return tblElevatorCollectDao;
    }

    public TblElevatorCollect getById(String vUserId) {
        return tblElevatorCollectDao.getById(vUserId);
    }

    public List<TblElevatorCollect> getByEntity(TblElevatorCollect tblElevatorCollect) {
        return tblElevatorCollectDao.getByEntity(tblElevatorCollect);
    }

    public List<TblElevatorCollect> listByEntity(TblElevatorCollect tblElevatorCollect) {
        return tblElevatorCollectDao.listByEntity(tblElevatorCollect);
    }

    public List<TblElevatorCollect> listByIds(List<String> ids) {
        return tblElevatorCollectDao.listByIds(ids);
    }

    public int insert(TblElevatorCollect tblElevatorCollect) {
        Date date = new Date();
        tblElevatorCollect.setDtCreateTime(date);
        tblElevatorCollect.setDtModifyTime(date);
        return tblElevatorCollectDao.insert(tblElevatorCollect);
    }

    public int insertBatch(List<TblElevatorCollect> list) {
        return tblElevatorCollectDao.insertBatch(list);
    }

    public int update(TblElevatorCollect tblElevatorCollect) {
        tblElevatorCollect.setDtModifyTime(new Date());
        return tblElevatorCollectDao.update(tblElevatorCollect);
    }

    public int updateBatch(List<TblElevatorCollect> list) {
        return tblElevatorCollectDao.updateBatch(list);
    }

    public int deleteById(String vUserId, String vElevatorId) {
        return tblElevatorCollectDao.deleteById(vUserId, vElevatorId);
    }

    public int deleteByEntity(TblElevatorCollect tblElevatorCollect) {
        return tblElevatorCollectDao.deleteByEntity(tblElevatorCollect);
    }

    public int deleteByIds(List<String> list) {
        return tblElevatorCollectDao.deleteByIds(list);
    }

    public int countAll() {
        return tblElevatorCollectDao.countAll();
    }

    public int countByEntity(TblElevatorCollect tblElevatorCollect) {
        return tblElevatorCollectDao.countByEntity(tblElevatorCollect);
    }

}