package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblThirdPartyRuijinEnventDao;
import com.shmashine.api.service.system.TblThirdPartyRuijinEnventServiceI;
import com.shmashine.common.entity.TblThirdPartyRuijinEnvent;

@Service
public class TblThirdPartyRuijinEnventServiceImpl implements TblThirdPartyRuijinEnventServiceI {

    @Resource(type = TblThirdPartyRuijinEnventDao.class)
    private TblThirdPartyRuijinEnventDao tblThirdPartyRuijinEnventDao;

    @Override
    public TblThirdPartyRuijinEnventDao getTblThirdPartyRuijinEnventDao() {
        return tblThirdPartyRuijinEnventDao;
    }

    public TblThirdPartyRuijinEnvent getById(String eventid) {
        return tblThirdPartyRuijinEnventDao.getById(eventid);
    }

    public List<TblThirdPartyRuijinEnvent> getByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        return tblThirdPartyRuijinEnventDao.getByEntity(tblThirdPartyRuijinEnvent);
    }

    public List<TblThirdPartyRuijinEnvent> listByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        return tblThirdPartyRuijinEnventDao.listByEntity(tblThirdPartyRuijinEnvent);
    }

    public List<TblThirdPartyRuijinEnvent> listByIds(List<String> ids) {
        return tblThirdPartyRuijinEnventDao.listByIds(ids);
    }

    public int insert(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        Date date = new Date();
        tblThirdPartyRuijinEnvent.setDtCreateTime(date);
        tblThirdPartyRuijinEnvent.setDtModifyTime(date);
        return tblThirdPartyRuijinEnventDao.insert(tblThirdPartyRuijinEnvent);
    }

    public int insertBatch(List<TblThirdPartyRuijinEnvent> list) {
        return tblThirdPartyRuijinEnventDao.insertBatch(list);
    }

    public int update(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        tblThirdPartyRuijinEnvent.setDtModifyTime(new Date());
        return tblThirdPartyRuijinEnventDao.update(tblThirdPartyRuijinEnvent);
    }

    public int updateBatch(List<TblThirdPartyRuijinEnvent> list) {
        return tblThirdPartyRuijinEnventDao.updateBatch(list);
    }

    public int deleteById(String eventid) {
        return tblThirdPartyRuijinEnventDao.deleteById(eventid);
    }

    public int deleteByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        return tblThirdPartyRuijinEnventDao.deleteByEntity(tblThirdPartyRuijinEnvent);
    }

    public int deleteByIds(List<String> list) {
        return tblThirdPartyRuijinEnventDao.deleteByIds(list);
    }

    public int countAll() {
        return tblThirdPartyRuijinEnventDao.countAll();
    }

    public int countByEntity(TblThirdPartyRuijinEnvent tblThirdPartyRuijinEnvent) {
        return tblThirdPartyRuijinEnventDao.countByEntity(tblThirdPartyRuijinEnvent);
    }

}