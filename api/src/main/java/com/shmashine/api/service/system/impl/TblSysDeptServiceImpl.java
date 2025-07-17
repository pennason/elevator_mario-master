package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shmashine.api.dao.TblSysDeptDao;
import com.shmashine.api.service.system.TblSysDeptServiceI;
import com.shmashine.common.entity.TblSysDept;

@Service
public class TblSysDeptServiceImpl implements TblSysDeptServiceI {

    @Resource(type = TblSysDeptDao.class)
    private TblSysDeptDao tblSysDeptDao;

    @Override
    public TblSysDeptDao getTblSysDeptDao() {
        return tblSysDeptDao;
    }

    public TblSysDept getById(String vDeptId) {
        return tblSysDeptDao.getById(vDeptId);
    }

    public List<TblSysDept> getByEntity(TblSysDept tblSysDept) {
        return tblSysDeptDao.getByEntity(tblSysDept);
    }

    public List<TblSysDept> listByEntity(TblSysDept tblSysDept) {
        return tblSysDeptDao.listByEntity(tblSysDept);
    }

    public List<TblSysDept> listByIds(List<String> ids) {
        return tblSysDeptDao.listByIds(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "dept", allEntries = true)
    public int insert(TblSysDept tblSysDept) {
        Date date = new Date();
        tblSysDept.setDtCreatetime(date);
        tblSysDept.setDtModifytime(date);
        return tblSysDeptDao.insert(tblSysDept);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public int insertBatch(List<TblSysDept> list) {
        return tblSysDeptDao.insertBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "dept", allEntries = true)
    public int update(TblSysDept tblSysDept) {
        tblSysDept.setDtModifytime(new Date());
        return tblSysDeptDao.update(tblSysDept);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public int updateBatch(List<TblSysDept> list) {
        return tblSysDeptDao.updateBatch(list);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public int deleteById(String vDeptId) {
        return tblSysDeptDao.deleteById(vDeptId);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public int deleteByEntity(TblSysDept tblSysDept) {
        return tblSysDeptDao.deleteByEntity(tblSysDept);
    }

    @CacheEvict(value = "dept", allEntries = true)
    public int deleteByIds(List<String> list) {
        return tblSysDeptDao.deleteByIds(list);
    }

    public int countAll() {
        return tblSysDeptDao.countAll();
    }

    public int countByEntity(TblSysDept tblSysDept) {
        return tblSysDeptDao.countByEntity(tblSysDept);
    }

    @Override
    public int existsByName(String vDeptName) {
        return tblSysDeptDao.existsByName(vDeptName);
    }

    @Override
    @Cacheable(value = "DEPT_INFO", key = "#deptTypeId+#deptName", unless = "#result == null")
    public TblSysDept getDeptInfoByNameAndType(String deptName, Integer deptTypeId) {
        return tblSysDeptDao.getByTypeAndName(deptTypeId, deptName);
    }
}