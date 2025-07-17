package com.shmashine.api.service.elevatorbrand.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblElevatorBrandDao;
import com.shmashine.api.service.elevatorbrand.TblElevatorBrandServiceI;
import com.shmashine.common.entity.TblElevatorBrand;

@Service
public class TblElevatorBrandServiceImpl implements TblElevatorBrandServiceI {

    @Resource(type = TblElevatorBrandDao.class)
    private TblElevatorBrandDao tblElevatorBrandDao;

    @Override
    public TblElevatorBrandDao getTblElevatorBrandDao() {
        return tblElevatorBrandDao;
    }

    public TblElevatorBrand getById(String vElevatorBrandId) {
        return tblElevatorBrandDao.getById(vElevatorBrandId);
    }

    public List<TblElevatorBrand> getByEntity(TblElevatorBrand tblElevatorBrand) {
        return tblElevatorBrandDao.getByEntity(tblElevatorBrand);
    }

    public List<TblElevatorBrand> listByEntity(TblElevatorBrand tblElevatorBrand) {
        return tblElevatorBrandDao.listByEntity(tblElevatorBrand);
    }

    public List<TblElevatorBrand> listByIds(List<String> ids) {
        return tblElevatorBrandDao.listByIds(ids);
    }

    public int insert(TblElevatorBrand tblElevatorBrand) {
        Date date = new Date();
        tblElevatorBrand.setDtCreateTime(date);
        tblElevatorBrand.setDtModifyTime(date);
        return tblElevatorBrandDao.insert(tblElevatorBrand);
    }

    public int insertBatch(List<TblElevatorBrand> list) {
        return tblElevatorBrandDao.insertBatch(list);
    }

    public int update(TblElevatorBrand tblElevatorBrand) {
        tblElevatorBrand.setDtModifyTime(new Date());
        return tblElevatorBrandDao.update(tblElevatorBrand);
    }

    public int updateBatch(List<TblElevatorBrand> list) {
        return tblElevatorBrandDao.updateBatch(list);
    }

    public int deleteById(String vElevatorBrandId) {
        return tblElevatorBrandDao.deleteById(vElevatorBrandId);
    }

    public int deleteByEntity(TblElevatorBrand tblElevatorBrand) {
        return tblElevatorBrandDao.deleteByEntity(tblElevatorBrand);
    }

    public int deleteByIds(List<String> list) {
        return tblElevatorBrandDao.deleteByIds(list);
    }

    public int countAll() {
        return tblElevatorBrandDao.countAll();
    }

    public int countByEntity(TblElevatorBrand tblElevatorBrand) {
        return tblElevatorBrandDao.countByEntity(tblElevatorBrand);
    }

    /**
     * 查询所有电梯品牌，批量添梯时校验
     *
     * @return
     */
    @Override
    public List<Map<String, String>> getElevatorBrands() {
        return tblElevatorBrandDao.getElevatorBrands();
    }

    @Override
    @Cacheable(value = "ELEVATOR_BRAND_INFO", key = "#elevatorBrandName", unless = "#result == null")
    public TblElevatorBrand getBrandByBrandName(String elevatorBrandName) {
        return tblElevatorBrandDao.getBrandByName(elevatorBrandName);
    }
}