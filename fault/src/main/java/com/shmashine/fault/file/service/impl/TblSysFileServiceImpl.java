package com.shmashine.fault.file.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.fault.file.dao.TblSysFileDao;
import com.shmashine.fault.file.entity.TblSysFile;
import com.shmashine.fault.file.service.TblSysFileServiceI;

/**
 * TblSysFileServiceImpl
 */
@Service
public class TblSysFileServiceImpl implements TblSysFileServiceI {

    @Resource(type = TblSysFileDao.class)
    private TblSysFileDao tblSysFileDao;

    @Override
    public TblSysFileDao getTblSysFileDao() {
        return tblSysFileDao;
    }

    @Override
    public TblSysFile getById(String vFileId) {
        return tblSysFileDao.getById(vFileId);
    }

    @Override
    public List<TblSysFile> getFilesById(String vFaultId) {
        return tblSysFileDao.getFilesById(vFaultId);
    }

    @Override
    public List<TblSysFile> getByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.getByEntity(tblSysFile);
    }

    @Override
    public List<TblSysFile> listByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.listByEntity(tblSysFile);
    }

    @Override
    public List<TblSysFile> listByIds(List<String> ids) {
        return tblSysFileDao.listByIds(ids);
    }

    @Override
    public int insert(TblSysFile tblSysFile) {
        Date date = new Date();
        tblSysFile.setDtCreateTime(date);
        tblSysFile.setDtModifyTime(date);
        return tblSysFileDao.insert(tblSysFile);
    }

    @Override
    public void insertBatch(List<TblSysFile> fileList) {
        tblSysFileDao.insertBatch(fileList);
    }

    @Override
    public int update(TblSysFile tblSysFile) {
        tblSysFile.setDtModifyTime(new Date());
        return tblSysFileDao.update(tblSysFile);
    }

    @Override
    public int updateBatch(List<TblSysFile> list) {
        return tblSysFileDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vFileId) {
        return tblSysFileDao.deleteById(vFileId);
    }

    @Override
    public int deleteByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.deleteByEntity(tblSysFile);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSysFileDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSysFileDao.countAll();
    }

    @Override
    public int countByEntity(TblSysFile tblSysFile) {
        return tblSysFileDao.countByEntity(tblSysFile);
    }

}