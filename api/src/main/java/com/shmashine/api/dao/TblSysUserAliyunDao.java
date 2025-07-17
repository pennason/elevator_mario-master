package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysUserAliyun;


public interface TblSysUserAliyunDao {

    TblSysUserAliyun getById(@NotNull String vUserAliyunId);

    List<TblSysUserAliyun> listByEntity(TblSysUserAliyun tblSysUserAliyun);

    List<TblSysUserAliyun> getByEntity(TblSysUserAliyun tblSysUserAliyun);

    List<TblSysUserAliyun> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysUserAliyun tblSysUserAliyun);

    int insertBatch(@NotEmpty List<TblSysUserAliyun> list);

    int update(@NotNull TblSysUserAliyun tblSysUserAliyun);

    int updateByField(@NotNull @Param("where") TblSysUserAliyun where, @NotNull @Param("set") TblSysUserAliyun set);

    int updateBatch(@NotEmpty List<TblSysUserAliyun> list);

    int deleteById(@NotNull String vUserAliyunId);

    int deleteByEntity(@NotNull TblSysUserAliyun tblSysUserAliyun);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysUserAliyun tblSysUserAliyun);

    TblSysUserAliyun getByUserName(@Param("userName") String userName);
}