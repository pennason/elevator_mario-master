package com.shmashine.api.service.camera.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.api.dao.TblCameraDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.camera.SearchCamerasModule;
import com.shmashine.api.service.camera.TblCameraServiceI;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.common.entity.TblCamera;

@Service
public class TblCameraServiceImpl implements TblCameraServiceI {

    @Resource(type = TblCameraDao.class)
    private TblCameraDao tblCameraDao;

    @Override
    public TblCameraDao getTblCameraDao() {
        return tblCameraDao;
    }

    public TblCamera getById(String vCameraId) {
        return tblCameraDao.getById(vCameraId);
    }

    public List<TblCamera> getByEntity(TblCamera tblCamera) {
        return tblCameraDao.getByEntity(tblCamera);
    }

    public List<TblCamera> listByEntity(TblCamera tblCamera) {
        return tblCameraDao.listByEntity(tblCamera);
    }

    public List<TblCamera> listByIds(List<String> ids) {
        return tblCameraDao.listByIds(ids);
    }

    public int insert(TblCamera tblCamera) {
        Date date = new Date();
        tblCamera.setDtCreateTime(date);
        tblCamera.setDtModifyTime(date);
        return tblCameraDao.insert(tblCamera);
    }

    public int insertBatch(List<TblCamera> list) {
        return tblCameraDao.insertBatch(list);
    }

    public int update(TblCamera tblCamera) {
        tblCamera.setDtModifyTime(new Date());
        return tblCameraDao.update(tblCamera);
    }

    public int updateBatch(List<TblCamera> list) {
        return tblCameraDao.updateBatch(list);
    }

    public int deleteById(String vCameraId) {
        return tblCameraDao.deleteById(vCameraId);
    }

    public int deleteByEntity(TblCamera tblCamera) {
        return tblCameraDao.deleteByEntity(tblCamera);
    }

    public int deleteByIds(List<String> list) {
        return tblCameraDao.deleteByIds(list);
    }

    public int countAll() {
        return tblCameraDao.countAll();
    }

    public int countByEntity(TblCamera tblCamera) {
        return tblCameraDao.countByEntity(tblCamera);
    }

    @Override
    public PageListResultEntity camerasBoundElevotor(SearchCamerasModule searchCamerasModule) {

        Integer pageIndex = searchCamerasModule.getPageIndex();
        Integer pageSize = searchCamerasModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;

        }

        PageHelper.startPage(pageIndex, pageSize);

        PageInfo<Map<String, Object>> hashMapPageInfo = new PageInfo<>(tblCameraDao.searchElevatorListByPage(searchCamerasModule), pageSize);

        return new PageListResultEntity<>(pageIndex, pageSize, (int) hashMapPageInfo.getTotal(), hashMapPageInfo.getList());

    }

    @Override
    public PageListResultEntity electricBicycleConfirm(String elevatorCode, String startTime, String endTime, int pageIndex, int pageSize) {

        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        PageInfo<Map> mapPageInfo = new PageInfo<>(tblCameraDao.electricBicycleConfirm(elevatorCode, startTime, endTime), pageSize);
        // 封装分页数据结构
        PageListResultEntity<Map> mapPageListResultEntity = new PageListResultEntity<>(pageIndex, pageSize, (int) mapPageInfo.getTotal(), mapPageInfo.getList());
        return mapPageListResultEntity;

    }

}