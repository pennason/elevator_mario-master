package com.shmashine.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shmashine.common.entity.TblFaultDefinition0902;

public interface BizFaultDefinition0902Dao {

    /**
     * 根据平台 和 故障code 查找故障名称
     */
    String getFaultDefinitionByCodeAndPlatformType(@Param("platformType") String platformType, @Param("code") String code);

    List<TblFaultDefinition0902> getFaultDefinitionSearch(@Param("elevatorType") Integer elevatorType, @Param("eventType") Integer eventType);

    List<TblFaultDefinition0902> getDefinitionUncivilizedBehaviorSearch();


}
