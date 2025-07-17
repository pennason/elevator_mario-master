package com.shmashine.api.service.system;

import java.util.List;

import com.shmashine.api.dao.TblSysSysteminfoDao;
import com.shmashine.common.entity.TblSysSysteminfo;

public interface TblSysSysteminfoServiceI {

    TblSysSysteminfoDao getTblSysSysteminfoDao();

    TblSysSysteminfo getById(Integer vSysid);

    List<TblSysSysteminfo> getByEntity(TblSysSysteminfo tblSysSysteminfo);

    List<TblSysSysteminfo> listByEntity(TblSysSysteminfo tblSysSysteminfo);

    List<TblSysSysteminfo> listByIds(List<Integer> ids);

    int insert(TblSysSysteminfo tblSysSysteminfo);

    int insertBatch(List<TblSysSysteminfo> list);

    int update(TblSysSysteminfo tblSysSysteminfo);

    int updateBatch(List<TblSysSysteminfo> list);

    int deleteById(Integer vSysid);

    int deleteByEntity(TblSysSysteminfo tblSysSysteminfo);

    int deleteByIds(List<Integer> list);

    int countAll();

    int countByEntity(TblSysSysteminfo tblSysSysteminfo);
}