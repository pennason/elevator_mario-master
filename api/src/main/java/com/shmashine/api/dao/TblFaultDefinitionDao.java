package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;

/**
 * @author little.li
 */
@Mapper
public interface TblFaultDefinitionDao {


    List<TblFaultDefinition> listByEntity(TblFaultDefinition faultDefinition);

    List<TblFaultDefinition0902> listByFaultTypeAndElevatorCode(@Param("faultType") String faultType,
                                                                @Param("uncivilizedBehaviorFlag") Integer uncivilizedBehaviorFlag,
                                                                @Param("elevatorType") Integer elevatorType,
                                                                @Param("eventType") String eventType);

    List<TblFaultDefinition> list();
}