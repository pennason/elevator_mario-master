package com.shmashine.api.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.shmashine.api.dao.BizSystemDao;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.dao.TblSysProvincialCityMapper;
import com.shmashine.api.service.system.BizSystemService;
import com.shmashine.api.service.user.BizUserService;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.entity.TblSysProvincialCityEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @PackgeName: com.shmashine.api.service.system.impl
 * @ClassName: BizSystemServiceImpl
 * @Date: 2020/6/1013:11
 * @Author: LiuLiFu
 * @Description: 获取系统字典下拉框
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizSystemServiceImpl implements BizSystemService {

    private final BizUserService bizUserService;
    private final RedisTemplate redisTemplate;
    private final BizSystemDao bizSystemDao;
    private final TblElevatorDao elevatorDao;
    private final TblSysProvincialCityMapper provincialCityMapper;

    @Cacheable(value = "select_list_integer", key = "targetClass + methodName +#p0")
    @Override
    public List<Map> getSelectList(String mainId) {
        return bizSystemDao.getSelectList(mainId);
    }

    @Cacheable(value = "select_list_string", key = "targetClass + methodName +#p0")
    @Override
    public List<Map> platformSelectList(String mainId) {
        return bizSystemDao.getPlatformSelectList(mainId);
    }

    @Override
    public List<Map> getAreaSelectList(String parentId) {
        return bizSystemDao.getAreaSelectList(parentId);
    }

    @Override
    public List<Map> getWorkStatusSelectList(String mainId) {
        return bizSystemDao.getWorkOrderSelectList(mainId);
    }

    /**
     * TODO：没有做缓存更新机制
     *
     * @param userId     用户ID
     * @param projectIds 项目IDs
     * @param villageIds 小区IDs
     * @return
     */
    @Override
    public List<HashMap> getFaultDefinitionSelectList(String userId, List<String> projectIds, List<String> villageIds) {

        String key = RedisConstants.FAULT_DEFINITION_SELECT_LIST + userId;
        if (!CollectionUtils.isEmpty(villageIds)) {
            key = key + "_" + String.join("_", villageIds);
        } else if (!CollectionUtils.isEmpty(projectIds)) {
            key = key + "_" + String.join("_", projectIds);
        }

        List<HashMap> definitionList = redisTemplate.opsForList().range(key, 0, -1);

        if (definitionList.size() > 0) {
            return definitionList;
        }

        // 小区ID转电梯ID 项目ID转电梯ID
        List<String> elevatorIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(villageIds)) {
            elevatorIds = elevatorDao.getElevatorIdsByVillageIds(villageIds);
        } else if (!CollectionUtils.isEmpty(projectIds)) {
            elevatorIds = elevatorDao.getElevatorIdsByProjectIds(projectIds);
        }

        List<HashMap> faultDefinitionSelectList = bizSystemDao.getFaultDefinitionSelectList(bizUserService.isAdmin(userId), userId, elevatorIds);

        if (CollectionUtils.isEmpty(faultDefinitionSelectList)) {
            return faultDefinitionSelectList;
        }

        redisTemplate.delete(key);

        redisTemplate.opsForList().rightPushAll(key, faultDefinitionSelectList);

        return faultDefinitionSelectList;
    }

    // 城市相关


    @Override
    @Cacheable(value = "CITY_INFO", key = "#areaName+#level+#cityCode", unless = "#result == null")
    public TblSysProvincialCityEntity getAreaCityByNameAndLevel(String areaName, Integer level, String cityCode) {
        return provincialCityMapper.getByNameAndLevel(areaName, level, cityCode);
    }

    @Override
    @Cacheable(value = "CITY_INFO_PROVINCE", key = "#areaCode", unless = "#result == null")
    public TblSysProvincialCityEntity getAreaProvinceByAreaCode(String areaCode) {
        return provincialCityMapper.getProvinceByAreaCode(areaCode);
    }
}
