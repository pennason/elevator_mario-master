package com.shmashine.socket.fault.service;


import java.util.HashMap;
import java.util.List;

import com.shmashine.socket.fault.entity.TblFaultDefinition;
import com.shmashine.socket.fault.entity.TblFaultDefinition0902;

/**
 * 故障定义实现
 *
 * @author little.li
 */
public interface TblFaultDefinitionServiceI {


    List<TblFaultDefinition> list(HashMap<Object, Object> objectObjectHashMap);

    void updateFaultDefinitionId(String id, long nextId);

    TblFaultDefinition0902 getByFaultType(String faultType);

    TblFaultDefinition0902 getByFaultType(String faultType, int platformType);

    String getFaultNameByFaultType(String faultType);

    TblFaultDefinition0902 getByFaultTypeAndSecondType(String faultType, String secondType);
}