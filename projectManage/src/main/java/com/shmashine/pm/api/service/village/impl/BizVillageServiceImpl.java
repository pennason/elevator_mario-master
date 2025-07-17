package com.shmashine.pm.api.service.village.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.entity.TblGroupLeasingStatisticsEntity;
import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.CryptoUtil;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.pm.api.contants.SystemConstants;
import com.shmashine.pm.api.dao.BizVillageDao;
import com.shmashine.pm.api.dao.TblGroupLeasingStatisticsMapper;
import com.shmashine.pm.api.dao.TblProjectDao;
import com.shmashine.pm.api.dao.TblVillageDao;
import com.shmashine.pm.api.entity.TblProject;
import com.shmashine.pm.api.entity.base.PageListResultEntity;
import com.shmashine.pm.api.entity.dto.TblVillageDto;
import com.shmashine.pm.api.module.village.input.SearchVillaListModule;
import com.shmashine.pm.api.module.village.input.SearchVillaSelectListModule;
import com.shmashine.pm.api.redis.utils.RedisUtils;
import com.shmashine.pm.api.service.village.BizVillageService;

import lombok.RequiredArgsConstructor;

/**
 * 小区业务相关.
 *
 * @author chenx
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizVillageServiceImpl implements BizVillageService {
    private final BizVillageDao bizVillageDao;
    private final TblVillageDao tblVillageDao;
    private final TblProjectDao tblProjectDao;
    private final TblGroupLeasingStatisticsMapper groupLeasingStatisticsMapper;
    private final RedisUtils redisUtils;


    /**
     * 小区列表
     *
     * @param searchVillaSelectListModule 查询条件
     * @return 结果
     */
    @Override
    public PageListResultEntity<Map<String, Object>> searchVillageList(
            SearchVillaListModule searchVillaSelectListModule) {
        Integer pageIndex = searchVillaSelectListModule.getPageIndex();
        Integer pageSize = searchVillaSelectListModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var pageInfo = new PageInfo<>(bizVillageDao.searchVillageList(searchVillaSelectListModule), pageSize);

        pageInfo.getList().stream().peek(map -> {
            String installerName = (String) map.get("v_installer_name");
            String investigatorName = (String) map.get("v_investigator_name");
            map.put("v_installer_name", CryptoUtil.decryptAesBase64("vName", String.valueOf(installerName)));
            map.put("v_investigator_name", CryptoUtil.decryptAesBase64("vName", String.valueOf(investigatorName)));
        });

        // 分页后扩展可疑楼和可疑楼层
        //extendGroupLeasingSuspicious(pageInfo.getList());

        // 封装分页数据结构
        return new PageListResultEntity(pageIndex, pageSize, (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 获取所有小区结果
     *
     * @param searchVillaSelectListModule 查询条件
     * @return 结果
     */
    @Override
    public List<Map<String, Object>> searchAllVillage(SearchVillaListModule searchVillaSelectListModule) {
        var list = bizVillageDao.searchVillageList(searchVillaSelectListModule);
        // 扩展可疑楼和可疑楼层
        //extendGroupLeasingSuspicious(list);
        return list;
    }


    /**
     * 小区下拉框
     *
     * @return 返回下拉框
     */
    @Override
    public List<Map<String, Object>> searchVillageSelectList(SearchVillaSelectListModule searchVillaSelectListModule) {
        return bizVillageDao.searchVillageSelectList(searchVillaSelectListModule);
    }

    @Override
    public TblVillageDto getBizInfoById(String villageId) {
        return bizVillageDao.getBizInfoById(villageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int clearVillageAllInfo(String villageId) {
        TblVillage tblVillage = tblVillageDao.getById(villageId);

        TblProject tblProject = tblProjectDao.getById(tblVillage.getVProjectId());
        int res = bizVillageDao.clearVillageAllInfo(villageId);

        if (res > 0) {
            tblProject.setiVillageCount(tblProject.getiVillageCount() < 0 ? 0 : tblProject.getiVillageCount() - 1);
            tblProjectDao.update(tblProject);
        }
        // 清除小区缓存
        redisUtils.del(RedisKeyUtils.getCityPushPlatformProjectExistsKey(villageId));
        return res;
    }

    @Override
    public void extendGroupLeasingElevatorAndFloorCoefficient(TblVillageDto villageDto) {
        // 电梯
        var elevatorCoefficientList = groupLeasingStatisticsMapper
                .findByEntity(TblGroupLeasingStatisticsEntity.builder()
                        .villageId(villageDto.getVVillageId())
                        .statisticsType("elevator")
                        .build())
                .stream()
                .collect(Collectors.toMap(TblGroupLeasingStatisticsEntity::getElevatorCode, this::buildCoefficientMap));
        villageDto.setGroupLeasingElevatorCoefficient(elevatorCoefficientList);
        // 楼层
        var floorCoefficientList = groupLeasingStatisticsMapper
                .findByEntity(TblGroupLeasingStatisticsEntity.builder()
                        .villageId(villageDto.getVVillageId())
                        .statisticsType("floor")
                        .build())
                .stream()
                .collect(Collectors.groupingBy(TblGroupLeasingStatisticsEntity::getElevatorCode,
                        Collectors.toMap(TblGroupLeasingStatisticsEntity::getFloor, this::buildCoefficientMap)));
        villageDto.setGroupLeasingFloorCoefficient(floorCoefficientList);
    }

    private Map<String, Object> buildCoefficientMap(TblGroupLeasingStatisticsEntity entity) {
        return (Map<String, Object>) new HashMap<String, Object>() {
            {
                //put("elevatorCode", entity.getElevatorCode());
                //put("floor", entity.getFloor());
                put("dayCoefficient", entity.getDayCoefficient());
                put("averageCoefficient", entity.getAverageCoefficient());
                put("percent", entity.getPercent());
                put("level", entity.getLevel());
            }
        };
    }

    /**
     * 扩展 群租功能的 可疑楼层和电梯
     *
     * @param list 列表
     */
    private void extendGroupLeasingSuspicious(List<Map<String, Object>> list) {
        if (!CollectionUtils.isEmpty(list)) {
            // 可疑电梯
            var groupLeasingElevatorSuspicious = groupLeasingStatisticsMapper
                    .findByEntity(TblGroupLeasingStatisticsEntity.builder()
                            .statisticsType("elevator")
                            .level(3)
                            .build())
                    .stream()
                    .collect(Collectors.groupingBy(TblGroupLeasingStatisticsEntity::getVillageId));
            // 可疑楼层
            var groupLeasingFloorSuspicious = groupLeasingStatisticsMapper
                    .findByEntity(TblGroupLeasingStatisticsEntity.builder()
                            .statisticsType("floor")
                            .level(3)
                            .build())
                    .stream()
                    .collect(Collectors.groupingBy(TblGroupLeasingStatisticsEntity::getVillageId));
            // 扩展可疑电梯，楼层信息
            list.forEach(item -> {
                var villageId = item.get("v_village_id") == null
                        ? item.get("vVillageId").toString()
                        : item.get("v_village_id").toString();
                if (!groupLeasingElevatorSuspicious.isEmpty()
                        && groupLeasingElevatorSuspicious.containsKey(villageId)) {
                    item.put("elevatorSuspicious", JSON.toJSON(groupLeasingElevatorSuspicious.get(villageId)
                            .stream()
                            .collect(Collectors.toMap(TblGroupLeasingStatisticsEntity::getElevatorCode,
                                    TblGroupLeasingStatisticsEntity::getPercent))));
                    /*.map(entity -> new HashMap<String, Object>(){
                        {
                            put("elevatorCode", entity.getElevatorCode());
                            //put("dayCoefficient", entity.getDayCoefficient());
                            //put("averageCoefficient", entity.getAverageCoefficient());
                            //put("percent", entity.getPercent());
                            //put("level", entity.getLevel());
                        }
                    })
                    .collect(Collectors.toList())));*/
                }
                if (!groupLeasingFloorSuspicious.isEmpty() && groupLeasingFloorSuspicious.containsKey(villageId)) {
                    item.put("floorSuspicious", JSON.toJSON(groupLeasingFloorSuspicious.get(villageId)
                            .stream()
                            .collect(Collectors.groupingBy(TblGroupLeasingStatisticsEntity::getElevatorCode,
                                    Collectors.toMap(TblGroupLeasingStatisticsEntity::getFloor,
                                            TblGroupLeasingStatisticsEntity::getPercent)))));
                    /*.map(entity -> new HashMap<String, Object>(){
                        {
                            put("elevatorCode", entity.getElevatorCode());
                            //put("dayCoefficient", entity.getDayCoefficient());
                            //put("averageCoefficient", entity.getAverageCoefficient());
                            put("floor", entity.getFloor());
                            //put("percent", entity.getPercent());
                            //put("level", entity.getLevel());
                        }
                    })
                    .collect(Collectors.toList())));*/
                }
            });
        }
    }
}
