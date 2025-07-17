package com.shmashine.api.service.fault;

import java.util.List;

import com.shmashine.common.entity.TblFaultDefinition0902;

public interface BizFaultDefinition0902ServiceI {

    String getFaultDefinitionByCodeAndPlatformType(String platformType, String code);

    List<TblFaultDefinition0902> getFaultDefinitionSearch(Integer elevatorType, Integer eventType);

    List<TblFaultDefinition0902> getDefinitionUncivilizedBehaviorAll();
}
