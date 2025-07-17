package com.shmashine.api.service.elevatorproject;

import java.util.List;

import com.shmashine.api.dao.TblProjectDao;
import com.shmashine.common.entity.TblProject;

public interface TblProjectServiceI {

    TblProjectDao getTblProjectDao();

    TblProject getById(String vProjectId);

    List<TblProject> getByEntity(TblProject tblProject);

    List<TblProject> listByEntity(TblProject tblProject);

    List<TblProject> listByIds(List<String> ids);

    int insert(TblProject tblProject);

    int insertBatch(List<TblProject> list);

    int update(TblProject tblProject);

    int updateBatch(List<TblProject> list);

    int deleteById(String vProjectId);

    int deleteByEntity(TblProject tblProject);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblProject tblProject);
}