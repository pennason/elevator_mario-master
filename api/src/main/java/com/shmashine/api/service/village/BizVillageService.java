package com.shmashine.api.service.village;

import java.util.List;
import java.util.Map;

import com.shmashine.api.controller.village.vo.VillagesAndPermissionStatusReqVO;
import com.shmashine.api.entity.base.PageListResultEntity;
import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.common.entity.TblVillage;

public interface BizVillageService {


    List<Map> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule);

    PageListResultEntity<Map> searchVillageList(SearchVillaListModule searchVillaSelectListModule);

    PageListResultEntity<Map> searchVillageListWithElevator(SearchVillaListModule searchVillaSelectListModule);

    List<Map> searchAllVillage(SearchVillaListModule searchVillaSelectListModule);

    List<Map> searchVillagesByProject(VillagesAndPermissionStatusReqVO villagesReqVO);

    List<TblVillage> getVillageList(String userId, boolean isAdmin, String villageName);

}
