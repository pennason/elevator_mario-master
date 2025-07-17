package com.shmashine.socket.fault.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.shmashine.socket.fault.entity.TblFaultDefinition;
import com.shmashine.socket.fault.entity.TblFaultDefinition0902;

/**
 * 故障定义DO
 *
 * @author little.li
 */
@Mapper
public interface TblFaultDefinitionDao {


    List<TblFaultDefinition> list(HashMap<Object, Object> objectObjectHashMap);

    void updateFaultDefinitionId(@Param("id") String id, @Param("nextId") String nextId);

    TblFaultDefinition0902 getByFaultType(@Param("faultType") String faultType);

    /**
     * 拿西子扶梯故障定义标准
     *
     * @param faultType    故障类型
     * @param platformType 平台类型
     */
    TblFaultDefinition0902 getEscalatorDefByFaultType(@Param("faultType") String faultType,
                                                      @Param("platformType") int platformType);

    String getFaultNameByFaultType(@Param("faultType") String faultType);

    TblFaultDefinition0902 getByFaultTypeAndSecondType(@Param("faultType") String faultType,
                                                       @Param("secondType") String secondType);
}