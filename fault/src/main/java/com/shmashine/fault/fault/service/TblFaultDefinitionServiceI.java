package com.shmashine.fault.fault.service;

import java.util.HashMap;
import java.util.List;

import com.shmashine.fault.fault.entity.TblFaultDefinition;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;

/**
 * 故障定义服务
 *
 * @author little.li
 */
public interface TblFaultDefinitionServiceI {


    List<TblFaultDefinition> list(HashMap<Object, Object> objectObjectHashMap);

    TblFaultDefinition0902 getByFaultTypeAndSecondType(String faultType, String faultSecondType);

    TblFaultDefinition0902 getByFaultType(String faultType);

    /**
     * 拿到西子扶梯故障标准
     *
     * @param faultType    故障类型
     * @param platformType 平台（故障）标准
     */
    TblFaultDefinition0902 getByFaultType(String faultType, int platformType);


}