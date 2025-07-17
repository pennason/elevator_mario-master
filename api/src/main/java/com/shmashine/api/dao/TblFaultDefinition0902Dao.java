package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultDefinition0902;

public interface TblFaultDefinition0902Dao {

    TblFaultDefinition0902 getById(@NotNull String faultDefinitionId);

    List<TblFaultDefinition0902> listByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

    List<TblFaultDefinition0902> getByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

    List<TblFaultDefinition0902> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblFaultDefinition0902 tblFaultDefinition0902);

    int insertBatch(@NotEmpty List<TblFaultDefinition0902> list);

    int update(@NotNull TblFaultDefinition0902 tblFaultDefinition0902);

    int updateByField(@NotNull @Param("where") TblFaultDefinition0902 where, @NotNull @Param("set") TblFaultDefinition0902 set);

    int updateBatch(@NotEmpty List<TblFaultDefinition0902> list);

    int deleteById(@NotNull String faultDefinitionId);

    int deleteByEntity(@NotNull TblFaultDefinition0902 tblFaultDefinition0902);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

}