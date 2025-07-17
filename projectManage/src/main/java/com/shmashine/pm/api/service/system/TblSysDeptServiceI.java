package com.shmashine.pm.api.service.system;

import java.util.List;

import com.shmashine.pm.api.dao.TblSysDeptDao;
import com.shmashine.pm.api.entity.TblSysDept;

public interface TblSysDeptServiceI {

    TblSysDeptDao getTblSysDeptDao();

    TblSysDept getById(String vDeptId);

    List<TblSysDept> getByEntity(TblSysDept tblSysDept);

    List<TblSysDept> listByEntity(TblSysDept tblSysDept);

    List<TblSysDept> listByIds(List<String> ids);

    int insert(TblSysDept tblSysDept);

    int insertBatch(List<TblSysDept> list);

    int update(TblSysDept tblSysDept);

    int updateBatch(List<TblSysDept> list);

    int deleteById(String vDeptId);

    int deleteByEntity(TblSysDept tblSysDept);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysDept tblSysDept);
}
