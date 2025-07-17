package com.shmashine.api.service.camera;

import java.util.List;

import com.shmashine.api.dao.TblCameraDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.camera.SearchCamerasModule;
import com.shmashine.common.entity.TblCamera;

public interface TblCameraServiceI {

    TblCameraDao getTblCameraDao();

    TblCamera getById(String vCameraId);

    List<TblCamera> getByEntity(TblCamera tblCamera);

    List<TblCamera> listByEntity(TblCamera tblCamera);

    List<TblCamera> listByIds(List<String> ids);

    int insert(TblCamera tblCamera);

    int insertBatch(List<TblCamera> list);

    int update(TblCamera tblCamera);

    int updateBatch(List<TblCamera> list);

    int deleteById(String vCameraId);

    int deleteByEntity(TblCamera tblCamera);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblCamera tblCamera);

    /**
     * 分页获取摄像头绑定信息
     *
     * @param searchCamerasModule
     * @return
     */
    PageListResultEntity camerasBoundElevotor(SearchCamerasModule searchCamerasModule);

    PageListResultEntity electricBicycleConfirm(String elevatorCode, String startTime, String endTime, int pageIndex, int pageSize);
}