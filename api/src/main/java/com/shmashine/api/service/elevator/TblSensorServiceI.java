package com.shmashine.api.service.elevator;

import java.util.List;

import com.shmashine.api.dao.TblSensorDao;
import com.shmashine.common.entity.TblSensor;

/**
 * sensor service
 *
 * @author chenx
 */

public interface TblSensorServiceI {

    TblSensorDao getTblSensorDao();

    TblSensor getById(String vSensorId);

    List<TblSensor> getByEntity(TblSensor tblSensor);

    List<TblSensor> listByEntity(TblSensor tblSensor);

    List<TblSensor> listByIds(List<String> ids);

    int insert(TblSensor tblSensor);

    int insertBatch(List<TblSensor> list);

    int update(TblSensor tblSensor);

    int deleteById(String vSensorId);

    int deleteByEntity(TblSensor tblSensor);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblSensor tblSensor);
}