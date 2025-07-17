package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblMaintenanceDefinition;

public interface TblMaintenanceDefinitionDao {

    TblMaintenanceDefinition getById(@NotNull String vMaintenanceDefinitionId);

    List<TblMaintenanceDefinition> listByEntity(TblMaintenanceDefinition tblMaintenanceDefinition);

    List<TblMaintenanceDefinition> getByEntity(TblMaintenanceDefinition tblMaintenanceDefinition);

    List<TblMaintenanceDefinition> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblMaintenanceDefinition tblMaintenanceDefinition);

    int insertBatch(@NotEmpty List<TblMaintenanceDefinition> list);

    int update(@NotNull TblMaintenanceDefinition tblMaintenanceDefinition);

    int updateByField(@NotNull @Param("where") TblMaintenanceDefinition where, @NotNull @Param("set") TblMaintenanceDefinition set);

    int updateBatch(@NotEmpty List<TblMaintenanceDefinition> list);

    int deleteById(@NotNull String vMaintenanceDefinitionId);

    int deleteByEntity(@NotNull TblMaintenanceDefinition tblMaintenanceDefinition);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblMaintenanceDefinition tblMaintenanceDefinition);

    List<TblMaintenanceDefinition> listByMaintenanceType(@Param("maintenanceType") Integer maintenanceType);

}