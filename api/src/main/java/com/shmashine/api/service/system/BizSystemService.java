package com.shmashine.api.service.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shmashine.common.entity.TblSysProvincialCityEntity;

public interface BizSystemService {
    /**
     * 获取系统字典下拉框
     */
    List<Map> getSelectList(String mainId);

    /**
     * 获取系统字典下拉框
     */
    List<Map> platformSelectList(String mainId);

    /**
     * 工单的状态特殊的存在 获取工单状态下拉框
     */
    List<Map> getWorkStatusSelectList(String mainId);

    /**
     * 获取故障标准
     *
     * @param userId     用户id
     * @param projectIds 项目idS
     * @param villageIds 小区IDS
     * @return 故障标准
     */
    List<HashMap> getFaultDefinitionSelectList(String userId, List<String> projectIds, List<String> villageIds);

    // 城市相关

    List<Map> getAreaSelectList(String parentId);

    /**
     * 根据 城市名前缀 获取城市信息
     *
     * @param areaName 城市名前缀
     * @param level    城市等级 地区级别（1:省份province,2:市city,3:区县district,4:街道street）
     * @param cityCode 城市编码
     * @return 结果
     */
    TblSysProvincialCityEntity getAreaCityByNameAndLevel(String areaName, Integer level, String cityCode);

    /**
     * 根据区域编号 获取省信息
     *
     * @param areaCode 区域编号
     * @return 结果
     */
    TblSysProvincialCityEntity getAreaProvinceByAreaCode(String areaCode);
}
