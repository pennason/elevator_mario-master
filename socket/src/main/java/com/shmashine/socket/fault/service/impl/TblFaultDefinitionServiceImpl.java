package com.shmashine.socket.fault.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.socket.fault.dao.TblFaultDefinitionDao;
import com.shmashine.socket.fault.entity.TblFaultDefinition;
import com.shmashine.socket.fault.entity.TblFaultDefinition0902;
import com.shmashine.socket.fault.service.TblFaultDefinitionServiceI;

/**
 * 故障定义实现
 */
@Service
public class TblFaultDefinitionServiceImpl implements TblFaultDefinitionServiceI {

    @Resource(type = TblFaultDefinitionDao.class)
    private TblFaultDefinitionDao tblFaultDefinitionDao;


    @Override
    public List<TblFaultDefinition> list(HashMap<Object, Object> objectObjectHashMap) {
        return tblFaultDefinitionDao.list(objectObjectHashMap);
    }

    @Override
    public void updateFaultDefinitionId(String id, long nextId) {
        tblFaultDefinitionDao.updateFaultDefinitionId(id, String.valueOf(nextId));
    }

    @Override
    public TblFaultDefinition0902 getByFaultType(String faultType) {
        return tblFaultDefinitionDao.getByFaultType(faultType);
    }

    /**
     * 拿扶梯故障定义标准故障
     *
     * @param faultType    故障类型
     * @param platformType 平台类型
     */
    @Override
    public TblFaultDefinition0902 getByFaultType(String faultType, int platformType) {
        return tblFaultDefinitionDao.getEscalatorDefByFaultType(faultType, platformType);
    }

    @Override
    public String getFaultNameByFaultType(String faultType) {
        return tblFaultDefinitionDao.getFaultNameByFaultType(faultType);
    }

    @Override
    public TblFaultDefinition0902 getByFaultTypeAndSecondType(String faultType, String secondType) {
        return tblFaultDefinitionDao.getByFaultTypeAndSecondType(faultType, secondType);
    }
}