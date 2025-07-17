package com.shmashine.api.service.system;

import java.util.List;
import java.util.Map;

import com.shmashine.api.dao.TblFaultShieldDao;
import com.shmashine.api.module.fault.input.FaultDefinitionModule;
import com.shmashine.common.entity.TblFaultShield;

public interface TblFaultShieldServiceI {

    TblFaultShieldDao getTblFaultShieldDao();

    TblFaultShield getById(String vFaultShieldId);

    List<TblFaultShield> getByEntity(TblFaultShield tblFaultShield);

    List<TblFaultShield> listByEntity(TblFaultShield tblFaultShield);

    List<TblFaultShield> listByIds(List<String> ids);

    int insert(TblFaultShield tblFaultShield);

    int insertBatch(List<TblFaultShield> list);

    int update(TblFaultShield tblFaultShield);

    int updateBatch(List<TblFaultShield> list);

    int deleteById(String vFaultShieldId);

    int deleteByEntity(TblFaultShield tblFaultShield);

    int deleteByIds(List<String> list);

    int countAll();

    int countByEntity(TblFaultShield tblFaultShield);

    List<Map<String, Object>> getShieldInfo(String elevatorCode, Integer elevatorType, int eventType);

    void updateFaultShield(String elevatorCode, List<Map<String, String>> faultShieldInfo);

    void batchUpdateFaultShield(FaultDefinitionModule faultDefinitionModule);
}