package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.building.SearchBuildingModule;

public interface BizBuildingDao {

    /**
     * 楼宇列表
     */
    List<Map> search(SearchBuildingModule searchBuildingModule);
}
