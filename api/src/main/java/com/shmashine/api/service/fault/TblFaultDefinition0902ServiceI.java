package com.shmashine.api.service.fault;

import java.util.List;

import com.shmashine.api.dao.TblFaultDefinition0902Dao;
import com.shmashine.common.entity.TblFaultDefinition0902;

public interface TblFaultDefinition0902ServiceI {

    TblFaultDefinition0902Dao getTblFaultDefinition0902Dao();

    TblFaultDefinition0902 getById(String faultDefinitionId);

    List<TblFaultDefinition0902> getByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

    List<TblFaultDefinition0902> listByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

    List<TblFaultDefinition0902> listByIds(List<String> ids);

    int insert(TblFaultDefinition0902 tblFaultDefinition0902);

    int insertBatch(List<TblFaultDefinition0902> list);

    int update(TblFaultDefinition0902 tblFaultDefinition0902);

    int updateBatch(List<TblFaultDefinition0902> list);

    int deleteById(String faultDefinitionId);

    int deleteByEntity(TblFaultDefinition0902 tblFaultDefinition0902);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblFaultDefinition0902 tblFaultDefinition0902);
}