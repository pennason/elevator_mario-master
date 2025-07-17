package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblElevatorSensor;

/**
 * 电梯传感器对应接口
 *
 * @author little.li
 */
public interface TblElevatorSensorDao {

    TblElevatorSensor getById(@NotNull String vElevatorSensorId);

    List<TblElevatorSensor> listByEntity(TblElevatorSensor tblElevatorSensor);

    List<TblElevatorSensor> getByEntity(TblElevatorSensor tblElevatorSensor);

    List<TblElevatorSensor> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblElevatorSensor tblElevatorSensor);

    int insertBatch(@NotEmpty List<TblElevatorSensor> list);

    int update(@NotNull TblElevatorSensor tblElevatorSensor);

    int updateByField(@NotNull @Param("where") TblElevatorSensor where, @NotNull @Param("set") TblElevatorSensor set);

    int updateBatch(@NotEmpty List<TblElevatorSensor> list);

    int deleteById(@NotNull String vElevatorSensorId);

    int deleteByEntity(@NotNull TblElevatorSensor tblElevatorSensor);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblElevatorSensor tblElevatorSensor);


    /**
     * 根据电梯编号获取传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    List<Map<String, Object>> getSensorListByElevatorCode(@Param("elevatorCode") String elevatorCode);


    /**
     * 清除电梯对应的传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    void batchRemoveByElevatorCode(@Param("elevatorCode") String elevatorCode);
}