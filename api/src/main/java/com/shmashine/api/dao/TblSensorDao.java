package com.shmashine.api.dao;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.shmashine.common.entity.TblSensor;

/**
 * sensor dao
 *
 * @author chenx
 */

public interface TblSensorDao {

    TblSensor getById(@NotNull String vSensorId);

    List<TblSensor> listByEntity(TblSensor tblSensor);

    List<TblSensor> getByEntity(TblSensor tblSensor);

    List<TblSensor> listByIds(@NotEmpty List<String> list);

    int insert(@NotNull TblSensor tblSensor);

    int insertBatch(@NotEmpty List<TblSensor> list);

    int update(@NotNull TblSensor tblSensor);

    int deleteById(@NotNull String vSensorId);

    int deleteByEntity(@NotNull TblSensor tblSensor);

    int deleteByIds(@NotEmpty List<String> list);

    int countAll();

    int countByEntity(TblSensor tblSensor);

}