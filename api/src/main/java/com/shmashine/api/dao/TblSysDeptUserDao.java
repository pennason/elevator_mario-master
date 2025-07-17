package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblSysDeptUser;


public interface TblSysDeptUserDao {

    TblSysDeptUser getById(@NotNull String vDeptId);

    List<TblSysDeptUser> listByEntity(TblSysDeptUser tblSysDeptUser);

    List<TblSysDeptUser> getByEntity(TblSysDeptUser tblSysDeptUser);

    List<TblSysDeptUser> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysDeptUser tblSysDeptUser);

    int insertBatch(@NotEmpty List<TblSysDeptUser> list);

    int update(@NotNull TblSysDeptUser tblSysDeptUser);

    int updateByField(@NotNull @Param("where") TblSysDeptUser where, @NotNull @Param("set") TblSysDeptUser set);

    int updateBatch(@NotEmpty List<TblSysDeptUser> list);

    int deleteById(@NotNull String vDeptId, String vUserId);

    int deleteByEntity(@NotNull TblSysDeptUser tblSysDeptUser);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysDeptUser tblSysDeptUser);

}