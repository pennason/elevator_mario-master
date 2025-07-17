package com.shmashine.api.service.user.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysUserAliyunDao;
import com.shmashine.api.service.user.TblSysUserAliyunServiceI;
import com.shmashine.common.entity.TblSysUserAliyun;

@Service
public class TblSysUserAliyunServiceImpl implements TblSysUserAliyunServiceI {

    @Resource(type = TblSysUserAliyunDao.class)
    private TblSysUserAliyunDao tblSysUserAliyunDao;

    @Override
    public TblSysUserAliyunDao getTblSysUserAliyunDao() {
        return tblSysUserAliyunDao;
    }

    @Override
    public TblSysUserAliyun getById(String vUserAliyunId) {
        return tblSysUserAliyunDao.getById(vUserAliyunId);
    }

    @Override
    public List<TblSysUserAliyun> getByEntity(TblSysUserAliyun tblSysUserAliyun) {
        return tblSysUserAliyunDao.getByEntity(tblSysUserAliyun);
    }

    @Override
    public List<TblSysUserAliyun> listByEntity(TblSysUserAliyun tblSysUserAliyun) {
        return tblSysUserAliyunDao.listByEntity(tblSysUserAliyun);
    }

    @Override
    public List<TblSysUserAliyun> listByIds(List<String> ids) {
        return tblSysUserAliyunDao.listByIds(ids);
    }

    @Override
    public int insert(TblSysUserAliyun tblSysUserAliyun) {
        Date date = new Date();
        tblSysUserAliyun.setDtCreateTime(date);
        tblSysUserAliyun.setDtModifyTime(date);
        return tblSysUserAliyunDao.insert(tblSysUserAliyun);
    }

    @Override
    public int insertBatch(List<TblSysUserAliyun> list) {
        return tblSysUserAliyunDao.insertBatch(list);
    }

    @Override
    public int update(TblSysUserAliyun tblSysUserAliyun) {
        tblSysUserAliyun.setDtModifyTime(new Date());
        return tblSysUserAliyunDao.update(tblSysUserAliyun);
    }

    @Override
    public int updateBatch(List<TblSysUserAliyun> list) {
        return tblSysUserAliyunDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vUserAliyunId) {
        return tblSysUserAliyunDao.deleteById(vUserAliyunId);
    }

    @Override
    public int deleteByEntity(TblSysUserAliyun tblSysUserAliyun) {
        return tblSysUserAliyunDao.deleteByEntity(tblSysUserAliyun);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSysUserAliyunDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSysUserAliyunDao.countAll();
    }

    @Override
    public int countByEntity(TblSysUserAliyun tblSysUserAliyun) {
        return tblSysUserAliyunDao.countByEntity(tblSysUserAliyun);
    }

    @Override
    public TblSysUserAliyun getByUserName(String userName) {
        return tblSysUserAliyunDao.getByUserName(userName);
    }

}