package com.shmashine.api.service.fault;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.fault.input.FaultDefinitionModule;
import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;

/**
 * @author little.li
 */
public interface TblFaultDefinitionServiceI {


    List<TblFaultDefinition0902> getFaultDefinitionListByElevatorType(Integer elevatorType, Integer sensorType);

    List<TblFaultDefinition> getAllFaultDefinition();


    List<Map<String, Object>> faultShieldInfo(String elevatorCode);

    void updateFaultShield(String elevatorCode, List<Map<String, String>> faultShieldInfo);

    void batchUpdateFaultShield(FaultDefinitionModule faultDefinitionModule);
}