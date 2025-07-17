package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblThirdPartyRuijinEnventDetailDao;
import com.shmashine.api.service.system.TblThirdPartyRuijinEnventDetailServiceI;
import com.shmashine.common.entity.TblThirdPartyRuijinEnventDetail;

@Service
public class TblThirdPartyRuijinEnventDetailServiceImpl implements TblThirdPartyRuijinEnventDetailServiceI {

    @Resource(type = TblThirdPartyRuijinEnventDetailDao.class)
    private TblThirdPartyRuijinEnventDetailDao tblThirdPartyRuijinEnventDetailDao;

    @Override
    public TblThirdPartyRuijinEnventDetailDao getTblThirdPartyRuijinEnventDetailDao() {
        return tblThirdPartyRuijinEnventDetailDao;
    }

    public TblThirdPartyRuijinEnventDetail getById(String eventid) {
        return tblThirdPartyRuijinEnventDetailDao.getById(eventid);
    }

    public List<TblThirdPartyRuijinEnventDetail> getByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        return tblThirdPartyRuijinEnventDetailDao.getByEntity(tblThirdPartyRuijinEnventDetail);
    }

    public List<TblThirdPartyRuijinEnventDetail> listByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        return tblThirdPartyRuijinEnventDetailDao.listByEntity(tblThirdPartyRuijinEnventDetail);
    }

    public List<TblThirdPartyRuijinEnventDetail> listByIds(List<String> ids) {
        return tblThirdPartyRuijinEnventDetailDao.listByIds(ids);
    }

    public int insert(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        Date date = new Date();
        tblThirdPartyRuijinEnventDetail.setDtCreateTime(date);
        tblThirdPartyRuijinEnventDetail.setDtModifyTime(date);
        return tblThirdPartyRuijinEnventDetailDao.insert(tblThirdPartyRuijinEnventDetail);
    }

    public int insertBatch(List<TblThirdPartyRuijinEnventDetail> list) {
        return tblThirdPartyRuijinEnventDetailDao.insertBatch(list);
    }

    public int update(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        tblThirdPartyRuijinEnventDetail.setDtModifyTime(new Date());
        return tblThirdPartyRuijinEnventDetailDao.update(tblThirdPartyRuijinEnventDetail);
    }

    public int updateBatch(List<TblThirdPartyRuijinEnventDetail> list) {
        return tblThirdPartyRuijinEnventDetailDao.updateBatch(list);
    }

    public int deleteById(String eventid, String eventdetailid) {
        return tblThirdPartyRuijinEnventDetailDao.deleteById(eventid, eventdetailid);
    }

    public int deleteByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        return tblThirdPartyRuijinEnventDetailDao.deleteByEntity(tblThirdPartyRuijinEnventDetail);
    }

    public int deleteByIds(List<String> list) {
        return tblThirdPartyRuijinEnventDetailDao.deleteByIds(list);
    }

    public int countAll() {
        return tblThirdPartyRuijinEnventDetailDao.countAll();
    }

    public int countByEntity(TblThirdPartyRuijinEnventDetail tblThirdPartyRuijinEnventDetail) {
        return tblThirdPartyRuijinEnventDetailDao.countByEntity(tblThirdPartyRuijinEnventDetail);
    }

}