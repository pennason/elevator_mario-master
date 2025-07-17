package com.shmashine.api.service.fault.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizFaultDefinition0902Dao;
import com.shmashine.api.service.fault.BizFaultDefinition0902ServiceI;
import com.shmashine.common.entity.TblFaultDefinition0902;

@Service
public class BizFaultDefinition0902ServiceImpl implements BizFaultDefinition0902ServiceI {

    private BizFaultDefinition0902Dao bizFaultDefinition0902Dao;

    @Autowired
    public BizFaultDefinition0902ServiceImpl(BizFaultDefinition0902Dao bizFaultDefinition0902Dao) {
        this.bizFaultDefinition0902Dao = bizFaultDefinition0902Dao;
    }


    @Override
    public String getFaultDefinitionByCodeAndPlatformType(String platformType, String code) {
        return bizFaultDefinition0902Dao.getFaultDefinitionByCodeAndPlatformType(platformType, code);
    }

    @Override
    public List<TblFaultDefinition0902> getFaultDefinitionSearch(Integer elevatorType, Integer eventType) {
        return bizFaultDefinition0902Dao.getFaultDefinitionSearch(elevatorType, eventType);
    }

    @Override
    public List<TblFaultDefinition0902> getDefinitionUncivilizedBehaviorAll() {
        return bizFaultDefinition0902Dao.getDefinitionUncivilizedBehaviorSearch();
    }
}
