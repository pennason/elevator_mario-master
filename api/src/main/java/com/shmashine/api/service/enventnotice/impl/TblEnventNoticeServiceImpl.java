package com.shmashine.api.service.enventnotice.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblEnventNoticeDao;
import com.shmashine.api.service.enventnotice.TblEnventNoticeServiceI;
import com.shmashine.common.entity.TblEnventNotice;

@Service
public class TblEnventNoticeServiceImpl implements TblEnventNoticeServiceI {

    @Resource(type = TblEnventNoticeDao.class)
    private TblEnventNoticeDao tblEnventNoticeDao;

    @Override
    public TblEnventNoticeDao getTblEnventNoticeDao() {
        return tblEnventNoticeDao;
    }

    public TblEnventNotice getById(String vEnventNotifyId) {
        return tblEnventNoticeDao.getById(vEnventNotifyId);
    }

    public List<TblEnventNotice> getByEntity(TblEnventNotice tblEnventNotice) {
        return tblEnventNoticeDao.getByEntity(tblEnventNotice);
    }

    public List<TblEnventNotice> listByEntity(TblEnventNotice tblEnventNotice) {
        return tblEnventNoticeDao.listByEntity(tblEnventNotice);
    }

    public List<TblEnventNotice> listByIds(List<String> ids) {
        return tblEnventNoticeDao.listByIds(ids);
    }

    public int insert(TblEnventNotice tblEnventNotice) {
        Date date = new Date();
        return tblEnventNoticeDao.insert(tblEnventNotice);
    }

    public int insertBatch(List<TblEnventNotice> list) {
        return tblEnventNoticeDao.insertBatch(list);
    }

    public int update(TblEnventNotice tblEnventNotice) {
        return tblEnventNoticeDao.update(tblEnventNotice);
    }

    public int updateBatch(List<TblEnventNotice> list) {
        return tblEnventNoticeDao.updateBatch(list);
    }

    public int deleteById(String vEnventNotifyId) {
        return tblEnventNoticeDao.deleteById(vEnventNotifyId);
    }

    public int deleteByEntity(TblEnventNotice tblEnventNotice) {
        return tblEnventNoticeDao.deleteByEntity(tblEnventNotice);
    }

    public int deleteByIds(List<String> list) {
        return tblEnventNoticeDao.deleteByIds(list);
    }

    public int countAll() {
        return tblEnventNoticeDao.countAll();
    }

    public int countByEntity(TblEnventNotice tblEnventNotice) {
        return tblEnventNoticeDao.countByEntity(tblEnventNotice);
    }

}