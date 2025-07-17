package com.shmashine.api.service.building.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblBuildingDao;
import com.shmashine.api.service.building.TblBuildingServiceI;
import com.shmashine.common.entity.TblBuilding;

@Service
public class TblBuildingServiceImpl implements TblBuildingServiceI {

    @Resource(type = TblBuildingDao.class)
    private TblBuildingDao tblBuildingDao;

    @Override
    public TblBuildingDao getTblBuildingDao() {
        return tblBuildingDao;
    }

    public TblBuilding getById(String vVillageId) {
        return tblBuildingDao.getById(vVillageId);
    }

    public List<TblBuilding> getByEntity(TblBuilding tblBuilding) {
        return tblBuildingDao.getByEntity(tblBuilding);
    }

    public List<TblBuilding> listByEntity(TblBuilding tblBuilding) {
        return tblBuildingDao.listByEntity(tblBuilding);
    }

    public List<TblBuilding> listByIds(List<String> ids) {
        return tblBuildingDao.listByIds(ids);
    }

    public int insert(TblBuilding tblBuilding) {
        Date date = new Date();
        tblBuilding.setDtCreateTime(date);
        tblBuilding.setDtModifyTime(date);
        return tblBuildingDao.insert(tblBuilding);
    }

    public int insertBatch(List<TblBuilding> list) {
        return tblBuildingDao.insertBatch(list);
    }

    public int update(TblBuilding tblBuilding) {
        tblBuilding.setDtModifyTime(new Date());
        return tblBuildingDao.update(tblBuilding);
    }

    public int updateBatch(List<TblBuilding> list) {
        return tblBuildingDao.updateBatch(list);
    }

    public int deleteById(String vVillageId, String vBuildingId) {
        return tblBuildingDao.deleteById(vVillageId, vBuildingId);
    }

    public int deleteByEntity(TblBuilding tblBuilding) {
        return tblBuildingDao.deleteByEntity(tblBuilding);
    }

    public int deleteByIds(List<String> list) {
        return tblBuildingDao.deleteByIds(list);
    }

    public int countAll() {
        return tblBuildingDao.countAll();
    }

    public int countByEntity(TblBuilding tblBuilding) {
        return tblBuildingDao.countByEntity(tblBuilding);
    }

}