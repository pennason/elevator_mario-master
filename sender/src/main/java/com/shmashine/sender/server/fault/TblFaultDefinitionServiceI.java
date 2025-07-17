package com.shmashine.sender.server.fault;


import java.util.List;

import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;

/**
 * 默认说明
 *
 * @author little.li
 */
public interface TblFaultDefinitionServiceI {


    List<TblFaultDefinition0902> getFaultDefinitionListByElevatorType(Integer elevatorType, Integer sensorType);

    List<TblFaultDefinition> getAllFaultDefinition();


}