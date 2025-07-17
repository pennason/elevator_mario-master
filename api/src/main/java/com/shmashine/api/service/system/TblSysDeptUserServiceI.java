package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysDeptUserDao;
import com.shmashine.common.entity.TblSysDeptUser;

public interface TblSysDeptUserServiceI {

    TblSysDeptUserDao getTblSysDeptUserDao();

    TblSysDeptUser getById(String vDeptId);

    List<TblSysDeptUser> getByEntity(TblSysDeptUser tblSysDeptUser);

    List<TblSysDeptUser> listByEntity(TblSysDeptUser tblSysDeptUser);

    List<TblSysDeptUser> listByIds(List<String> ids);

    int insert(TblSysDeptUser tblSysDeptUser);

    int insertBatch(List<TblSysDeptUser> list);

    int update(TblSysDeptUser tblSysDeptUser);

    int updateBatch(List<TblSysDeptUser> list);

    int deleteById(String vDeptId, String vUserId);

    int deleteByEntity(TblSysDeptUser tblSysDeptUser);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysDeptUser tblSysDeptUser);
}