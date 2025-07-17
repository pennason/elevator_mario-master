package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysUserResourceDao;
import com.shmashine.api.entity.SysUserResourceForExcel;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblSysUserResource;


public interface TblSysUserResourceServiceI {

    TblSysUserResourceDao getTblSysUserResourceDao();

    TblSysUserResource getById(String vUserId);

    List<String> getElevatorCodesByUserId(String userId);

    List<String> getUserIdListByResourceIdList(List<String> resourceIdList);

    List<TblSysUserResource> getByEntity(TblSysUserResource tblSysUserResource);

    List<TblSysUserResource> listByEntity(TblSysUserResource tblSysUserResource);

    List<TblSysUserResource> listByIds(List<String> ids);

    List<String> getElevatorIdListByUserId(String userId);

    int insert(TblSysUserResource tblSysUserResource);

    int insertBatch(List<TblSysUserResource> list);

    int update(TblSysUserResource tblSysUserResource);

    int updateBatch(List<TblSysUserResource> list);

    int deleteById(String vUserId, String vResourceId);

    int deleteByEntity(TblSysUserResource tblSysUserResource);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysUserResource tblSysUserResource);

    int batchUpdateByUserId(String userId, List<TblElevator> elevators, String createUserId);

    /**
     * 对Excel添加成功的电梯进行授权
     *
     * @param sysUserResourceList
     */
    void addElevatorByExcel(List<SysUserResourceForExcel> sysUserResourceList);

    /**
     * 添加电梯后需要进行绑定
     *
     * @param userId
     * @param tblElevator
     */
    void batchByUserId(String userId, TblElevator tblElevator);

    /**
     * 对批量添加的电梯授权
     *
     * @param userId
     * @param elevators
     */
    void batchaddElevatorsByUserId(String userId, List<TblElevator> elevators);

    /**
     * 批量删除用户授权电梯
     *
     * @param userId      用户
     * @param removedData 需要删除的梯
     */
    void deleteByUserAndResourcesId(String userId, List<String> removedData);
}