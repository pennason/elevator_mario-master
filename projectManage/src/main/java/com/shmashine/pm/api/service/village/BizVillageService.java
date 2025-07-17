package com.shmashine.pm.api.service.village;

import java.util.List;
import java.util.Map;

import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblVillageDto;
import com.shmashine.pm.api.module.village.input.SearchVillaListModule;
import com.shmashine.pm.api.module.village.input.SearchVillaSelectListModule;

/**
 * 小区相关
 *
 * @author chenx
 */
public interface BizVillageService {

    List<Map<String, Object>> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule);

    PageListResultEntity<Map<String, Object>> searchVillageList(SearchVillaListModule searchVillaSelectListModule);

    List<Map<String, Object>> searchAllVillage(SearchVillaListModule searchVillaSelectListModule);

    TblVillageDto getBizInfoById(String villageId);

    int clearVillageAllInfo(String villageId);

    /**
     * 给小区信息 扩展群租系数相关统计
     *
     * @param villageDto 小区信息
     */
    void extendGroupLeasingElevatorAndFloorCoefficient(TblVillageDto villageDto);
}
