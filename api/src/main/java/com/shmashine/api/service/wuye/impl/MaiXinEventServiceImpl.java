package com.shmashine.api.service.wuye.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.MaiXinWuyeEventDao;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.MaiXinEventService;

@Service
public class MaiXinEventServiceImpl implements MaiXinEventService {

    @Autowired
    private MaiXinWuyeEventDao baseMapper;

    @Autowired
    private BizUserService userService;

    @Override
    public List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByConfirmOrCompleted(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getPeopleTrappedCountByVillage(SearchFaultModule searchFaultModule) {
        return baseMapper.getPeopleTrappedCountByVillage(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getBlockDoorByVillage(SearchFaultModule searchFaultModule) {
        return baseMapper.getBlockDoorByVillage(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getPeopleTrappedCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getPeopleTrappedCount(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getFaultOrderByConfirmOrCompletedTotal(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByConfirmOrCompletedTotal(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getBlockDoorTotal(SearchFaultModule searchFaultModule) {
        return baseMapper.getBlockDoorTotal(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getUncivilizedBehaviorTotal(SearchFaultModule searchFaultModule) {
        return baseMapper.getUncivilizedBehaviorTotal(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getElectronBikeTotal(SearchFaultModule searchFaultModule) {
        return baseMapper.getElectronBikeTotal(searchFaultModule);
    }

    @Override
    public List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return baseMapper.intelligentSupervision(faultStatisticalQuantitySearchModule);
    }

    @Override
    public List<Map> intelligentSupervisionCQ(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return baseMapper.intelligentSupervisionCQ(faultStatisticalQuantitySearchModule);
    }
}
