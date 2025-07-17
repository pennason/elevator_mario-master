package com.shmashine.pm.api.service.dept.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblSysDeptLogoDao;
import com.shmashine.pm.api.entity.TblSysDeptLogo;
import com.shmashine.pm.api.service.dept.TblSysDeptLogoServiceI;

@Service
public class TblSysDeptLogoServiceImpl implements TblSysDeptLogoServiceI {

    @Resource(type = TblSysDeptLogoDao.class)
    private TblSysDeptLogoDao tblSysDeptLogoDao;

    @Override
    public TblSysDeptLogoDao getTblSysDeptLogoDao() {
        return tblSysDeptLogoDao;
    }

    public TblSysDeptLogo getById(String vLogoId) {
        return tblSysDeptLogoDao.getById(vLogoId);
    }

    public List<TblSysDeptLogo> getByEntity(TblSysDeptLogo tblSysDeptLogo) {
        return tblSysDeptLogoDao.getByEntity(tblSysDeptLogo);
    }

    public List<TblSysDeptLogo> listByEntity(TblSysDeptLogo tblSysDeptLogo) {
        return tblSysDeptLogoDao.listByEntity(tblSysDeptLogo);
    }

    public List<TblSysDeptLogo> listByIds(List<String> ids) {
        return tblSysDeptLogoDao.listByIds(ids);
    }

    public int insert(TblSysDeptLogo tblSysDeptLogo) {
        Date date = new Date();
        tblSysDeptLogo.setDtCreateTime(date);
        tblSysDeptLogo.setDtModifyTime(date);
        return tblSysDeptLogoDao.insert(tblSysDeptLogo);
    }

    public int insertBatch(List<TblSysDeptLogo> list) {
        return tblSysDeptLogoDao.insertBatch(list);
    }

    public int update(TblSysDeptLogo tblSysDeptLogo) {
        tblSysDeptLogo.setDtModifyTime(new Date());
        return tblSysDeptLogoDao.update(tblSysDeptLogo);
    }

    public int updateBatch(List<TblSysDeptLogo> list) {
        return tblSysDeptLogoDao.updateBatch(list);
    }

    public int deleteById(String vLogoId, String vDeptId) {
        return tblSysDeptLogoDao.deleteById(vLogoId, vDeptId);
    }

    public int deleteByEntity(TblSysDeptLogo tblSysDeptLogo) {
        return tblSysDeptLogoDao.deleteByEntity(tblSysDeptLogo);
    }

    public int deleteByIds(List<String> list) {
        return tblSysDeptLogoDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysDeptLogoDao.countAll();
    }

    public int countByEntity(TblSysDeptLogo tblSysDeptLogo) {
        return tblSysDeptLogoDao.countByEntity(tblSysDeptLogo);
    }
}
