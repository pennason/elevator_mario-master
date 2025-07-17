package com.shmashine.fault.fault.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.fault.fault.entity.TblFaultDefinition;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;

/**
 * 故障定义DAO
 *
 * @author little.li
 */
@Mapper
public interface TblFaultDefinitionDao {


    List<TblFaultDefinition> list(HashMap<Object, Object> objectObjectHashMap);

    void updateFaultDefinitionId(@Param("id") String id, @Param("nextId") String nextId);

    String getFaultNameByFaultType(@Param("faultType") Integer faultType);

    TblFaultDefinition0902 getByFaultType(@Param("faultType") String faultType);

    /**
     * 拿西子扶梯故障定义标准
     *
     * @param faultType    故障类型
     * @param platformType 平台（故障）标准
     */
    TblFaultDefinition0902 getByFaultTypeAndPlatformType(@Param("faultType") String faultType,
                                                         @Param("platformType") int platformType);

    TblFaultDefinition0902 getByFaultTypeAndSecondType(@Param("faultType") String faultType,
                                                       @Param("secondType") String secondType);

}