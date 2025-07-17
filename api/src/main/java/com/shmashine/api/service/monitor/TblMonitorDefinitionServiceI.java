package com.shmashine.api.service.monitor;

import java.util.List;

import com.shmashine.api.dao.TblMonitorDefinitionDao;
import com.shmashine.common.entity.TblMonitorDefinition;

public interface TblMonitorDefinitionServiceI {

    TblMonitorDefinitionDao getTblMonitorDefinitionDao();

    TblMonitorDefinition getById(String vMonitorDefinitionId);

    List<TblMonitorDefinition> getByEntity(TblMonitorDefinition tblMonitorDefinition);

    List<TblMonitorDefinition> listByEntity(TblMonitorDefinition tblMonitorDefinition);

    List<TblMonitorDefinition> listByIds(List<String> ids);

    int insert(TblMonitorDefinition tblMonitorDefinition);

    int insertBatch(List<TblMonitorDefinition> list);

    int update(TblMonitorDefinition tblMonitorDefinition);

    int updateBatch(List<TblMonitorDefinition> list);

    int deleteById(String vMonitorDefinitionId);

    int deleteByEntity(TblMonitorDefinition tblMonitorDefinition);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblMonitorDefinition tblMonitorDefinition);
}