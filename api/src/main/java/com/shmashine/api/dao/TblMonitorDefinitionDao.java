package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblMonitorDefinition;

public interface TblMonitorDefinitionDao {

    TblMonitorDefinition getById(@NotNull String vMonitorDefinitionId);

    List<TblMonitorDefinition> listByEntity(TblMonitorDefinition tblMonitorDefinition);

    List<TblMonitorDefinition> getByEntity(TblMonitorDefinition tblMonitorDefinition);

    List<TblMonitorDefinition> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblMonitorDefinition tblMonitorDefinition);

    int insertBatch(@NotEmpty List<TblMonitorDefinition> list);

    int update(@NotNull TblMonitorDefinition tblMonitorDefinition);

    int updateByField(@NotNull @Param("where") TblMonitorDefinition where, @NotNull @Param("set") TblMonitorDefinition set);

    int updateBatch(@NotEmpty List<TblMonitorDefinition> list);

    int deleteById(@NotNull String vMonitorDefinitionId);

    int deleteByEntity(@NotNull TblMonitorDefinition tblMonitorDefinition);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblMonitorDefinition tblMonitorDefinition);

    List<TblMonitorDefinition> list();

    /**
     * 获取监控相关屏蔽列表
     *
     * @param deviceShieldInfo
     * @return
     */
    List<Map<String, Object>> monitorShieldList(Map<String, String> deviceShieldInfo);

    /**
     * 获取设备类型和协议版本
     *
     * @param elevatorCode
     * @param sensorType
     * @return
     */
    Map<String, String> getProtocalVersionAndEType(@Param("elevatorCode") String elevatorCode);
}