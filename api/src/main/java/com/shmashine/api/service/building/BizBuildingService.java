package com.shmashine.api.service.building;

import java.util.List;
import java.util.Map;

import com.shmashine.api.module.building.SearchBuildingModule;

public interface BizBuildingService {

    List<Map> search(SearchBuildingModule searchBuildingModule);
}
