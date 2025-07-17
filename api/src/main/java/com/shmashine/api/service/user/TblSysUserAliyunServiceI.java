package com.shmashine.api.service.user;

import java.util.List;

import com.shmashine.api.dao.TblSysUserAliyunDao;
import com.shmashine.common.entity.TblSysUserAliyun;

public interface TblSysUserAliyunServiceI {

    TblSysUserAliyunDao getTblSysUserAliyunDao();

    TblSysUserAliyun getById(String vUserAliyunId);

    List<TblSysUserAliyun> getByEntity(TblSysUserAliyun tblSysUserAliyun);

    List<TblSysUserAliyun> listByEntity(TblSysUserAliyun tblSysUserAliyun);

    List<TblSysUserAliyun> listByIds(List<String> ids);

    int insert(TblSysUserAliyun tblSysUserAliyun);

    int insertBatch(List<TblSysUserAliyun> list);

    int update(TblSysUserAliyun tblSysUserAliyun);

    int updateBatch(List<TblSysUserAliyun> list);

    int deleteById(String vUserAliyunId);

    int deleteByEntity(TblSysUserAliyun tblSysUserAliyun);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSysUserAliyun tblSysUserAliyun);

    TblSysUserAliyun getByUserName(String userName);
}