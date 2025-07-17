package com.shmashine.pm.api.service.project.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.dao.TblProjectDao;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.service.project.TblProjectServiceI;

@Service
public class TblProjectServiceImpl implements TblProjectServiceI {
    @Resource(type = TblProjectDao.class)
    private TblProjectDao tblProjectDao;

    @Override
    public TblProjectDao getTblProjectDao() {
        return tblProjectDao;
    }

    public TblProject getById(String vProjectId) {
        return tblProjectDao.getById(vProjectId);
    }

    public List<TblProject> getByEntity(TblProject tblProject) {
        return tblProjectDao.getByEntity(tblProject);
    }

    public List<TblProject> listByEntity(TblProject tblProject) {
        return tblProjectDao.listByEntity(tblProject);
    }

    public List<TblProject> listByIds(List<String> ids) {
        return tblProjectDao.listByIds(ids);
    }

    @CacheEvict(value = "project", allEntries = true)
    @Override
    public int insert(TblProject tblProject) {
        Date date = new Date();
        tblProject.setDtCreateTime(date);
        tblProject.setDtModifyTime(date);
        return tblProjectDao.insert(tblProject);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int insertBatch(List<TblProject> list) {
        return tblProjectDao.insertBatch(list);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int update(TblProject tblProject) {
        tblProject.setDtModifyTime(new Date());
        return tblProjectDao.update(tblProject);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int updateBatch(List<TblProject> list) {
        return tblProjectDao.updateBatch(list);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int deleteById(String vProjectId) {
        return tblProjectDao.deleteById(vProjectId);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int deleteByEntity(TblProject tblProject) {
        return tblProjectDao.deleteByEntity(tblProject);
    }

    @CacheEvict(value = "project", allEntries = true)
    public int deleteByIds(List<String> list) {
        return tblProjectDao.deleteByIds(list);
    }

    public int countAll() {
        return tblProjectDao.countAll();
    }

    public int countByEntity(TblProject tblProject) {
        return tblProjectDao.countByEntity(tblProject);
    }

    @Override
    public String existsByName(String vProjectName) {
        return tblProjectDao.existsByName(vProjectName);
    }
}
