package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysDeptRoleDao;
import com.shmashine.common.entity.TblSysDeptRole;

public interface TblSysDeptRoleServiceI {

    TblSysDeptRoleDao getTblSysDeptRoleDao();

    TblSysDeptRole getById(String vDeptId);

    List<TblSysDeptRole> getByEntity(TblSysDeptRole tblSysDeptRole);

    List<TblSysDeptRole> listByEntity(TblSysDeptRole tblSysDeptRole);

    List<TblSysDeptRole> listByIds(List<String> ids);

    int insert(TblSysDeptRole tblSysDeptRole);

    int insertBatch(List<TblSysDeptRole> list);

    int update(TblSysDeptRole tblSysDeptRole);

    int updateBatch(List<TblSysDeptRole> list);

    int deleteById(String vDeptId);

    int deleteByEntity(TblSysDeptRole tblSysDeptRole);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysDeptRole tblSysDeptRole);
}