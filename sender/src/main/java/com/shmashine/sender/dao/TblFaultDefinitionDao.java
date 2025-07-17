package com.shmashine.sender.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultDefinition;

/**
 * 默认说明
 *
 * @author little.li
 */
@Mapper
public interface TblFaultDefinitionDao {

    List<TblFaultDefinition> listByFaultTypeAndElevatorCode(
            @Param("faultType") Integer faultType, @Param("uncivilizedBehaviorFlag") Integer uncivilizedBehaviorFlag,
            @Param("elevatorType") Integer elevatorType);

    TblFaultDefinition getByFaultType(Integer faultType);

    List<TblFaultDefinition> list();
}