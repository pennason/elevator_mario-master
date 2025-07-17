package com.shmashine.api.service.wuye.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.WuyeEventDao;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.fault.input.SearchFaultModule;
import com.shmashine.api.module.ruijin.FaultStatisticalQuantitySearchModule;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.api.service.wuye.EventService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private WuyeEventDao baseMapper;

    @Autowired
    private BizUserService userService;

    @Override
    public HashMap<String, Object> getPeopleTrappedCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getPeopleTrappedCount(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getPeopleTrappedCountByVillage(SearchFaultModule searchFaultModule) {
        return baseMapper.getPeopleTrappedCountByVillage(searchFaultModule);
    }

    @Override
    public Integer getFaultCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultCount(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByConfirmOrCompleted(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getFaultOrderByConfirmOrCompletedTotal(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByConfirmOrCompletedTotal(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getFaultOrderByFalsePositiveOrNew(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByFalsePositiveOrNew(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getEventCountByTime(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public PageListResultEntity getEventByPage(SearchFaultModule searchFaultModule) {
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getEventRealTimeSchedule(String eventId) {
        return baseMapper.getEventRealTimeSchedule(eventId);
    }

    @Override
    public HashMap<String, Object> getPeopleTrappedDetails(String faultId) {
        return baseMapper.getPeopleTrappedDetails(faultId);
    }

    @Override
    public List<Map> intelligentSupervision(FaultStatisticalQuantitySearchModule faultStatisticalQuantitySearchModule) {
        return baseMapper.intelligentSupervision(faultStatisticalQuantitySearchModule);
    }
}
