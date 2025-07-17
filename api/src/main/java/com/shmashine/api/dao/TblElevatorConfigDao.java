package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorConfig;


/**
 * 电梯配置
 *
 * @author little.li
 */
public interface TblElevatorConfigDao {

    TblElevatorConfig getById(@NotNull String elevatorConfigId);

    List<TblElevatorConfig> listByEntity(TblElevatorConfig tblElevatorConfig);

    List<TblElevatorConfig> getByEntity(TblElevatorConfig tblElevatorConfig);

    List<TblElevatorConfig> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblElevatorConfig tblElevatorConfig);

    int insertBatch(@NotEmpty List<TblElevatorConfig> list);

    int update(@NotNull TblElevatorConfig tblElevatorConfig);

    int updateByField(@NotNull @Param("where") TblElevatorConfig where, @NotNull @Param("set") TblElevatorConfig set);

    int updateBatch(@NotEmpty List<TblElevatorConfig> list);

    int deleteById(@NotNull String elevatorConfigId);

    int deleteByEntity(@NotNull TblElevatorConfig tblElevatorConfig);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevatorConfig tblElevatorConfig);

    /**
     * 根据电梯id查询配置
     *
     * @param elevatorId
     * @return
     */
    TblElevatorConfig getByElevatorId(@Param("elevatorId") String elevatorId);

    /**
     * 根据电梯编号查询配置
     *
     * @param elevatorCode
     * @return
     */
    TblElevatorConfig getConfigByElevatorCode(String elevatorCode);
}