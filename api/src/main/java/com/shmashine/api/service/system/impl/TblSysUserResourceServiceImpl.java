package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.shmashine.api.dao.TblSysUserResourceDao;
import com.shmashine.api.entity.SysUserResourceForExcel;
import com.shmashine.api.service.system.TblSysUserResourceServiceI;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblSysUserResource;

@Service
public class TblSysUserResourceServiceImpl implements TblSysUserResourceServiceI {

    @Resource(type = TblSysUserResourceDao.class)
    private TblSysUserResourceDao tblSysUserResourceDao;

    @Override
    public TblSysUserResourceDao getTblSysUserResourceDao() {
        return tblSysUserResourceDao;
    }

    @Override
    public TblSysUserResource getById(String vUserId) {
        return tblSysUserResourceDao.getById(vUserId);
    }

    @Override
    public List<String> getElevatorCodesByUserId(String userId) {
        return tblSysUserResourceDao.getElevatorCodesByUserId(userId);
    }

    @Override
    public List<String> getUserIdListByResourceIdList(List<String> resourceIdList) {
        return tblSysUserResourceDao.getUserIdListByResourceIdList(resourceIdList);
    }

    @Override
    public List<TblSysUserResource> getByEntity(TblSysUserResource tblSysUserResource) {
        return tblSysUserResourceDao.listByEntity(tblSysUserResource);
    }

    @Override
    public List<TblSysUserResource> listByEntity(TblSysUserResource tblSysUserResource) {
        return tblSysUserResourceDao.listByEntity(tblSysUserResource);
    }

    @Override
    public List<TblSysUserResource> listByIds(List<String> ids) {
        return tblSysUserResourceDao.listByIds(ids);
    }

    @Override
    public List<String> getElevatorIdListByUserId(String userId) {
        return tblSysUserResourceDao.getResourceIdListByUserId(userId);
    }

    @Override
    public int insert(TblSysUserResource tblSysUserResource) {
        Date date = new Date();
        tblSysUserResource.setDtCreateTime(date);
        tblSysUserResource.setDtModifyTime(date);
        return tblSysUserResourceDao.insert(tblSysUserResource);
    }

    @Override
    public int insertBatch(List<TblSysUserResource> list) {
        return tblSysUserResourceDao.insertBatch(list);
    }

    @Override
    public int update(TblSysUserResource tblSysUserResource) {
        tblSysUserResource.setDtModifyTime(new Date());
        return tblSysUserResourceDao.update(tblSysUserResource);
    }

    @Override
    public int updateBatch(List<TblSysUserResource> list) {
        return tblSysUserResourceDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vUserId, String vResourceId) {
        return tblSysUserResourceDao.deleteById(vUserId, vResourceId);
    }

    @Override
    public int deleteByEntity(TblSysUserResource tblSysUserResource) {
        return tblSysUserResourceDao.deleteByEntity(tblSysUserResource);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblSysUserResourceDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblSysUserResourceDao.countAll();
    }

    @Override
    public int countByEntity(TblSysUserResource tblSysUserResource) {
        return tblSysUserResourceDao.countByEntity(tblSysUserResource);
    }

    @Override
    @Transactional
    public int batchUpdateByUserId(String userId, List<TblElevator> elevators, String createUserId) {

        // 删除用户下已有授权电梯
        int num = tblSysUserResourceDao.deleteByUserId(userId);
        // 批量新增
        if (null != elevators && elevators.size() > 0) {
            List<TblSysUserResource> list = Lists.newArrayList();
            for (TblElevator elevator : elevators) {
                TblSysUserResource resource = new TblSysUserResource();
                resource.setVUserId(userId);
                resource.setVResourceId(elevator.getVElevatorId());
                resource.setVResourceCode(elevator.getVElevatorCode());
                resource.setVCreateUserId(createUserId);
                list.add(resource);
            }
            num = tblSysUserResourceDao.insertBatch(list);
        }
        return num;
    }

    /**
     * 对添加成功的电梯进行授权
     *
     * @param sysUserResourceList
     */
    @Override
    public void addElevatorByExcel(List<SysUserResourceForExcel> sysUserResourceList) {
        tblSysUserResourceDao.addElevatorByExcel(sysUserResourceList);
    }

    /**
     * 对添加成功的电梯进行绑定
     *
     * @param userId
     * @param tblElevator
     */
    @Override
    public void batchByUserId(String userId, TblElevator tblElevator) {
        tblSysUserResourceDao.batchByUserId(userId, tblElevator);
    }

    /**
     * 对添加成功的电梯进行批量授权
     *
     * @param userId
     * @param elevators
     */
    @Override
    public void batchaddElevatorsByUserId(String userId, List<TblElevator> elevators) {

        // 批量新增
        if (null != elevators && elevators.size() > 0) {
            List<TblSysUserResource> list = Lists.newArrayList();
            for (TblElevator elevator : elevators) {
                TblSysUserResource resource = new TblSysUserResource();
                resource.setVUserId(userId);
                resource.setVResourceId(elevator.getVElevatorId());
                resource.setVResourceCode(elevator.getVElevatorCode());
                resource.setVCreateUserId(userId);
                list.add(resource);
            }
            tblSysUserResourceDao.insertBatch(list);
        }
    }

    @Override
    public void deleteByUserAndResourcesId(String userId, List<String> removedData) {
        tblSysUserResourceDao.deleteByUserAndResourcesId(userId, removedData);
    }

}