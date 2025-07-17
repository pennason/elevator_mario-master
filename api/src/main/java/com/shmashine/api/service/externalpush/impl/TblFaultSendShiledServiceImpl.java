package com.shmashine.api.service.externalpush.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblFaultSendShiledDao;
import com.shmashine.api.service.externalpush.TblFaultSendShiledServiceI;
import com.shmashine.common.entity.TblFaultSendShiled;

@Service
public class TblFaultSendShiledServiceImpl implements TblFaultSendShiledServiceI {

    @Resource(type = TblFaultSendShiledDao.class)
    private TblFaultSendShiledDao tblFaultSendShiledDao;

    @Override
    public TblFaultSendShiledDao getTblFaultSendShiledDao() {
        return tblFaultSendShiledDao;
    }

    public TblFaultSendShiled getById(String vFaultSendShiledId) {
        return tblFaultSendShiledDao.getById(vFaultSendShiledId);
    }

    public List<TblFaultSendShiled> getByEntity(TblFaultSendShiled tblFaultSendShiled) {
        return tblFaultSendShiledDao.getByEntity(tblFaultSendShiled);
    }

    public List<TblFaultSendShiled> listByEntity(TblFaultSendShiled tblFaultSendShiled) {
        return tblFaultSendShiledDao.listByEntity(tblFaultSendShiled);
    }

    public List<TblFaultSendShiled> listByIds(List<String> ids) {
        return tblFaultSendShiledDao.listByIds(ids);
    }

    public int insert(TblFaultSendShiled tblFaultSendShiled) {
        Date date = new Date();
        tblFaultSendShiled.setDtCreateTime(date);
        tblFaultSendShiled.setDtModifyTime(date);
        return tblFaultSendShiledDao.insert(tblFaultSendShiled);
    }

    public int insertBatch(List<TblFaultSendShiled> list) {
        return tblFaultSendShiledDao.insertBatch(list);
    }

    public int update(TblFaultSendShiled tblFaultSendShiled) {
        tblFaultSendShiled.setDtModifyTime(new Date());
        return tblFaultSendShiledDao.update(tblFaultSendShiled);
    }

    public int updateBatch(List<TblFaultSendShiled> list) {
        return tblFaultSendShiledDao.updateBatch(list);
    }

    public int deleteById(String vFaultSendShiledId) {
        return tblFaultSendShiledDao.deleteById(vFaultSendShiledId);
    }

    public int deleteByEntity(TblFaultSendShiled tblFaultSendShiled) {
        return tblFaultSendShiledDao.deleteByEntity(tblFaultSendShiled);
    }

    public int deleteByIds(List<String> list) {
        return tblFaultSendShiledDao.deleteByIds(list);
    }

    public int countAll() {
        return tblFaultSendShiledDao.countAll();
    }

    public int countByEntity(TblFaultSendShiled tblFaultSendShiled) {
        return tblFaultSendShiledDao.countByEntity(tblFaultSendShiled);
    }

}