package com.shmashine.api.service.elevatorconfig;

import java.util.List;

import com.shmashine.api.dao.TblElevatorConfigDao;
import com.shmashine.common.entity.TblElevatorConfig;

/**
 * 电梯配置
 *
 * @author little.li
 */
public interface TblElevatorConfigServiceI {

    TblElevatorConfigDao getTblElevatorConfigDao();

    TblElevatorConfig getById(String elevatorConfigId);

    List<TblElevatorConfig> getByEntity(TblElevatorConfig tblElevatorConfig);

    List<TblElevatorConfig> listByEntity(TblElevatorConfig tblElevatorConfig);

    List<TblElevatorConfig> listByIds(List<String> ids);

    int insert(TblElevatorConfig tblElevatorConfig);

    int insertBatch(List<TblElevatorConfig> list);

    int update(TblElevatorConfig tblElevatorConfig);

    int updateBatch(List<TblElevatorConfig> list);

    int deleteById(String elevatorConfigId);

    int deleteByEntity(TblElevatorConfig tblElevatorConfig);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblElevatorConfig tblElevatorConfig);

    TblElevatorConfig getByElevatorId(String elevatorId);

    void updateConfig(String elevatorId, String elevatorCode, TblElevatorConfig elevatorConfig);
}