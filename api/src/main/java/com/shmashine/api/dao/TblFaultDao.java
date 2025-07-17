package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFault;

public interface TblFaultDao {

    TblFault getById(@NotNull String vFaultId);

    List<TblFault> listByEntity(TblFault tblFault);

    List<TblFault> getByEntity(TblFault tblFault);

    List<TblFault> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblFault tblFault);

    int insertBatch(@NotEmpty List<TblFault> list);

    int update(@NotNull TblFault tblFault);

    int updateByField(@NotNull @Param("where") TblFault where, @NotNull @Param("set") TblFault set);

    int updateBatch(@NotEmpty List<TblFault> list);

    int deleteById(@NotNull String vFaultId);

    int deleteByEntity(@NotNull TblFault tblFault);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblFault tblFault);

}