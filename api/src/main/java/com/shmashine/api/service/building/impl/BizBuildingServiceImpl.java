package com.shmashine.api.service.building.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizBuildingDao;
import com.shmashine.api.module.building.SearchBuildingModule;
import com.shmashine.api.service.building.BizBuildingService;


@Service
public class BizBuildingServiceImpl implements BizBuildingService {

    private BizBuildingDao bizBuildingDao;

    @Autowired
    public BizBuildingServiceImpl(BizBuildingDao bizBuildingDao) {
        this.bizBuildingDao = bizBuildingDao;
    }

    @Override
    public List<Map> search(SearchBuildingModule searchBuildingModule) {
        return bizBuildingDao.search(searchBuildingModule);
    }
}
