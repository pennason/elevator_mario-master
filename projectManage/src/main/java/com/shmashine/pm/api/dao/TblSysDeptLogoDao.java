package com.shmashine.pm.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.pm.api.entity.TblSysDeptLogo;

public interface TblSysDeptLogoDao {

    TblSysDeptLogo getById(@NotNull String vLogoId);

    List<TblSysDeptLogo> listByEntity(TblSysDeptLogo tblSysDeptLogo);

    List<TblSysDeptLogo> getByEntity(TblSysDeptLogo tblSysDeptLogo);

    List<TblSysDeptLogo> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSysDeptLogo tblSysDeptLogo);

    int insertBatch(@NotEmpty List<TblSysDeptLogo> list);

    int update(@NotNull TblSysDeptLogo tblSysDeptLogo);

    int updateByField(@NotNull @Param("where") TblSysDeptLogo where, @NotNull @Param("set") TblSysDeptLogo set);

    int updateBatch(@NotEmpty List<TblSysDeptLogo> list);

    int deleteById(@NotNull String vLogoId, String vDeptId);

    int deleteByEntity(@NotNull TblSysDeptLogo tblSysDeptLogo);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSysDeptLogo tblSysDeptLogo);
}
