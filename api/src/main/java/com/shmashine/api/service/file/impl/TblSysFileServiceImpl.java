package com.shmashine.api.service.file.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblSysFileDao;
import com.shmashine.api.service.file.TblSysFileServiceI;
import com.shmashine.common.entity.TblSysFile;
import com.shmashine.common.utils.OSSUtil;
import com.shmashine.common.utils.SnowFlakeUtils;

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
    public void insertWorkOrderBatch(List<String> list, String workOrderDetailId) {

        for (String fileName : list) {
            TblSysFile file = new TblSysFile();
            file.setVFileId(SnowFlakeUtils.nextStrId());
            file.setVFileType(String.valueOf(0));
            file.setVFileName(fileName);
            file.setVUrl(OSSUtil.OSS_URL + fileName);
            file.setDtCreateTime(new Date());
            file.setDtModifyTime(new Date());
            file.setIBusinessType(1);
            file.setVBusinessId(workOrderDetailId);
            tblSysFileDao.insert(file);
        }
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

    @Override
    public String getVideoUrl(String businessId, int businessType, int fileType) {
        return tblSysFileDao.getVideoUrl(businessId, businessType, fileType);
    }

    @Override
    public List<TblSysFile> getByBusinessId(String businessId) {
        return tblSysFileDao.getByBusinessId(businessId);
    }

}