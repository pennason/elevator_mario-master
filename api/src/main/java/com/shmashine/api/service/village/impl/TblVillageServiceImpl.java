package com.shmashine.api.service.village.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.shmashine.api.dao.TblVillageDao;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.api.service.village.TblVillageServiceI;
import com.shmashine.common.entity.TblVillage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TblVillageServiceImpl implements TblVillageServiceI {

    @Resource(type = TblVillageDao.class)
    private TblVillageDao tblVillageDao;

    @Resource
    private ElevatorCacheService cacheService;

    @Override
    public TblVillageDao getTblVillageDao() {
        return tblVillageDao;
    }

    public TblVillage getById(String vVillageId) {
        return tblVillageDao.getById(vVillageId);
    }

    public List<TblVillage> getByEntity(TblVillage tblVillage) {
        return tblVillageDao.getByEntity(tblVillage);
    }

    public List<TblVillage> listByEntity(TblVillage tblVillage) {
        return tblVillageDao.listByEntity(tblVillage);
    }

    public List<TblVillage> listByIds(List<String> ids) {
        return tblVillageDao.listByIds(ids);
    }

    @Override
    public List<String> getVillageIdsByProjectId(String projectId) {
        return tblVillageDao.getVillageIdsByProjectId(projectId);
    }

    @Override
    public void extendVillageInfo(List<Map> list) {
        log.info("extendVillageInfo");
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        // List<Map> list = mapPageInfo.getList();
        List<String> villageIdList = list.stream()
                .filter(item -> item.get("villageId") != null || item.get("v_village_id") != null)
                .map(item -> item.get("villageId") == null
                        ? (item.get("v_village_id") == null ? "" : item.get("v_village_id").toString())
                        : item.get("villageId").toString())
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        log.info("village id list {}", villageIdList);
        if (CollectionUtils.isEmpty(villageIdList)) {
            return;
        }

        List<TblVillage> tblVillages = tblVillageDao.listByIds(villageIdList);
        if (CollectionUtils.isEmpty(tblVillages)) {
            return;
        }
        var villageMap = tblVillages.stream()
                .collect(Collectors.toMap(TblVillage::getVVillageId, Function.identity()));
        list.forEach(item -> {
            String tmpVillageId = item.get("villageId") == null
                    ? (item.get("v_village_id") == null ? "" : item.get("v_village_id").toString())
                    : item.get("villageId").toString();
            // log.info("village name extend id {}", tmpVillageId);
            if (StringUtils.hasText(tmpVillageId) && villageMap.containsKey(tmpVillageId)) {
                item.put("villageName", villageMap.get(tmpVillageId).getVVillageName());
                item.put("groupLeasingStatus", villageMap.get(tmpVillageId).getGroupLeasingStatus());
                item.put("groupLeasingResult", villageMap.get(tmpVillageId).getGroupLeasingResult());
                // log.info("village name extend name {}", villageNameMap.getOrDefault(tmpVillageId, ""));
            }
        });
    }

    @Override
    @Cacheable(value = "VILLAGE_INFO", key = "#projectId+#villageName", unless = "#result == null")
    public TblVillage getVillageByProjectIdAndVillageName(String projectId, String villageName) {
        return tblVillageDao.getVillageByProjectIdAndVillageName(projectId, villageName);
    }

    public int insert(TblVillage tblVillage) {
        Date date = new Date();
        tblVillage.setDtCreateTime(date);
        tblVillage.setDtModifyTime(date);
        return tblVillageDao.insert(tblVillage);
    }

    public int insertBatch(List<TblVillage> list) {
        return tblVillageDao.insertBatch(list);
    }

    public int update(TblVillage tblVillage) {
        tblVillage.setDtModifyTime(new Date());
        var res = tblVillageDao.update(tblVillage);
        updateCache(tblVillage);
        return res;
    }

    public int updateBatch(List<TblVillage> list) {
        var res = tblVillageDao.updateBatch(list);
        updateCache(list);
        return res;
    }

    public int deleteById(String vVillageId) {
        return tblVillageDao.deleteById(vVillageId);
    }

    public int deleteByEntity(TblVillage tblVillage) {
        return tblVillageDao.deleteByEntity(tblVillage);
    }

    public int deleteByIds(List<String> list) {
        return tblVillageDao.deleteByIds(list);
    }

    public int countAll() {
        return tblVillageDao.countAll();
    }

    public int countByEntity(TblVillage tblVillage) {
        return tblVillageDao.countByEntity(tblVillage);
    }

    private void updateCache(List<TblVillage> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::updateCache);
        }
    }

    private void updateCache(TblVillage tblVillage) {
        var tblVillages = tblVillageDao.listByEntity(tblVillage);
        if (!CollectionUtils.isEmpty(tblVillages)) {
            tblVillages.forEach(village -> cacheService.updateCacheByVillageIdFromDatabase(village.getVVillageId()));
        }
    }

}