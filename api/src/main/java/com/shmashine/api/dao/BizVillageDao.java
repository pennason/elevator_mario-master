package com.shmashine.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.shmashine.api.module.village.input.SearchVillaListModule;
import com.shmashine.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.common.entity.TblVillage;

public interface BizVillageDao {

    /**
     * 小区列表
     */
    List<Map> searchVillageList(SearchVillaListModule searchVillaSelectListModule);

    /**
     * 获取小区下拉框
     */
    List<Map> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule);

    /**
     * 小区列表（有电梯）
     */
    List<Map> searchVillageListWithElevator(SearchVillaListModule searchVillaSelectListModule);

    /**
     * 获取小区列表
     *
     * @param userId
     * @param admin
     * @param villageName
     * @return
     */
    List<TblVillage> getVillageList(@Param("userId") String userId, @Param("admin") Boolean admin, @Param("villageName") String villageName);
}
