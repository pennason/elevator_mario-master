package com.shmashine.fault.fault.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.fault.fault.dao.TblFaultDefinitionDao;
import com.shmashine.fault.fault.entity.TblFaultDefinition;
import com.shmashine.fault.fault.entity.TblFaultDefinition0902;
import com.shmashine.fault.fault.service.TblFaultDefinitionServiceI;

/**
 * 故障定义服务实现
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
    public TblFaultDefinition0902 getByFaultType(String faultType) {
        return tblFaultDefinitionDao.getByFaultType(faultType);

    }

    @Override
    public TblFaultDefinition0902 getByFaultType(String faultType, int platformType) {
        return tblFaultDefinitionDao.getByFaultTypeAndPlatformType(faultType, platformType);

    }

    @Override
    public TblFaultDefinition0902 getByFaultTypeAndSecondType(String faultType, String faultSecondType) {
        return tblFaultDefinitionDao.getByFaultTypeAndSecondType(faultType, faultSecondType);
    }


}