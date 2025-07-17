package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysSysteminfo;

public interface TblSysSysteminfoDao {

    TblSysSysteminfo getById(@NotNull Integer vSysid);

    List<TblSysSysteminfo> listByEntity(TblSysSysteminfo tblSysSysteminfo);

    List<TblSysSysteminfo> getByEntity(TblSysSysteminfo tblSysSysteminfo);

    List<TblSysSysteminfo> listByIds(@NotEmpty List<Integer> list);

    int insert(@NotNull TblSysSysteminfo tblSysSysteminfo);

    int insertBatch(@NotEmpty List<TblSysSysteminfo> list);

    int update(@NotNull TblSysSysteminfo tblSysSysteminfo);

    int updateByField(@NotNull @Param("where") TblSysSysteminfo where, @NotNull @Param("set") TblSysSysteminfo set);

    int updateBatch(@NotEmpty List<TblSysSysteminfo> list);

    int deleteById(@NotNull Integer vSysid);

    int deleteByEntity(@NotNull TblSysSysteminfo tblSysSysteminfo);

    int deleteByIds(@NotEmpty List<Integer> list);

    int countAll();

    int countByEntity(TblSysSysteminfo tblSysSysteminfo);

    List<TblSysSysteminfo> getMaintenanceType();

    List<TblSysSysteminfo> getBySysId(@NotNull Integer sysId);

}