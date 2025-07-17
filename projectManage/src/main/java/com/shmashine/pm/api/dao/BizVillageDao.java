package com.shmashine.pm.api.dao;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.dto.TblVillageDto;
import com.shmashine.pm.api.module.village.input.SearchVillaListModule;
import com.shmashine.pm.api.module.village.input.SearchVillaSelectListModule;

public interface BizVillageDao {
    /**
     * 小区列表
     */
    List<Map<String, Object>> searchVillageList(SearchVillaListModule searchVillaSelectListModule);

    /**
     * 获取小区下拉框
     */
    List<Map<String, Object>> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule);

    TblVillageDto getBizInfoById(String vVillageId);

    int clearVillageAllInfo(String vVillageId);
}
