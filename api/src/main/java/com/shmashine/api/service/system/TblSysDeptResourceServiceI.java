package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysDeptResourceDao;
import com.shmashine.common.entity.TblSysDeptResource;

public interface TblSysDeptResourceServiceI {

    TblSysDeptResourceDao getTblSysDeptResourceDao();

    TblSysDeptResource getById(String vDeptId);

    List<TblSysDeptResource> getByEntity(TblSysDeptResource tblSysDeptResource);

    List<TblSysDeptResource> listByEntity(TblSysDeptResource tblSysDeptResource);

    List<TblSysDeptResource> listByIds(List<String> ids);

    int insert(TblSysDeptResource tblSysDeptResource);

    int insertBatch(List<TblSysDeptResource> list);

    int update(TblSysDeptResource tblSysDeptResource);

    int updateBatch(List<TblSysDeptResource> list);

    int deleteById(String vDeptId);

    int deleteByEntity(TblSysDeptResource tblSysDeptResource);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysDeptResource tblSysDeptResource);
}