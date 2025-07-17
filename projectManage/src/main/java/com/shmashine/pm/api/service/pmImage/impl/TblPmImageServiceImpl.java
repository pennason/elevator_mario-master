package com.shmashine.pm.api.service.pmImage.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblPmImageDao;
import com.shmashine.pm.api.entity.TblPmImage;
import com.shmashine.pm.api.service.pmImage.TblPmImageService;

@Service
public class TblPmImageServiceImpl implements TblPmImageService {

    @Resource(type = TblPmImageDao.class)
    private TblPmImageDao tblPmImageDao;

    @Override
    public TblPmImageDao getTblPmImageDao() {
        return tblPmImageDao;
    }

    @Override
    public TblPmImage getById(String vInvestigateBillImageId) {
        return tblPmImageDao.getById(vInvestigateBillImageId);
    }

    @Override
    public TblPmImage getByVInvestigateBillId(String vInvestigateBillId) {
        return tblPmImageDao.getByVTargetId(vInvestigateBillId);
    }

    @Override
    public int insert(TblPmImage tblPmImage) {
        Date date = new Date();
        tblPmImage.setDtCreateTime(date);
        tblPmImage.setDtModifyTime(date);
        return tblPmImageDao.insert(tblPmImage);
    }

    @Override
    public int insertBatch(List<TblPmImage> list) {
        return tblPmImageDao.insertBatch(list);
    }

    @Override
    public int update(TblPmImage tblPmImage) {
        tblPmImage.setDtModifyTime(new Date());
        return tblPmImageDao.update(tblPmImage);
    }

    @Override
    public int updateBatch(List<TblPmImage> list) {
        return tblPmImageDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vPmImageId) {
        return tblPmImageDao.deleteById(vPmImageId);
    }

    @Override
    public int deleteByTargetId(String vTargetId) {
        return tblPmImageDao.deleteByTargetId(vTargetId);
    }
}
