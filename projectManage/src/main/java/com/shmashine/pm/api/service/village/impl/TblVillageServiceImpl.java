package com.shmashine.pm.api.service.village.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shmashine.common.entity.TblVillage;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.pm.api.dao.TblVillageDao;
import com.shmashine.pm.api.redis.utils.RedisUtils;
import com.shmashine.pm.api.service.village.TblVillageServiceI;

import lombok.RequiredArgsConstructor;

/**
 * 小区服务实现类
 *
 * @author chenx
 */

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TblVillageServiceImpl implements TblVillageServiceI {
    private final TblVillageDao tblVillageDao;
    private final RedisUtils redisUtils;

    @Override
    public TblVillageDao getTblVillageDao() {
        return tblVillageDao;
    }

    @Override
    public TblVillage getById(String vVillageId) {
        return tblVillageDao.getById(vVillageId);
    }

    @Override
    public List<TblVillage> getByEntity(TblVillage tblVillage) {
        return tblVillageDao.getByEntity(tblVillage);
    }

    @Override
    public List<TblVillage> listByEntity(TblVillage tblVillage) {
        return tblVillageDao.listByEntity(tblVillage);
    }

    @Override
    public List<TblVillage> listByIds(List<String> ids) {
        return tblVillageDao.listByIds(ids);
    }

    @Override
    public int insert(TblVillage tblVillage) {
        Date date = new Date();
        tblVillage.setDtCreateTime(date);
        tblVillage.setDtModifyTime(date);
        return tblVillageDao.insert(tblVillage);
    }

    @Override
    public int insertBatch(List<TblVillage> list) {
        return tblVillageDao.insertBatch(list);
    }

    @Override
    public int update(TblVillage tblVillage) {
        tblVillage.setDtModifyTime(new Date());
        var res = tblVillageDao.update(tblVillage);
        // 清除缓存
        if (StringUtils.hasText(tblVillage.getVVillageId())) {
            redisUtils.del(RedisKeyUtils.getCityPushPlatformProjectExistsKey(tblVillage.getVVillageId()));
        }
        return res;
    }

    @Override
    public int updateBatch(List<TblVillage> list) {
        var res = tblVillageDao.updateBatch(list);
        // 清理缓存
        list.forEach(item -> {
            if (StringUtils.hasText(item.getVVillageId())) {
                redisUtils.del(RedisKeyUtils.getCityPushPlatformProjectExistsKey(item.getVVillageId()));
            }
        });
        return res;
    }

    @Override
    public int deleteById(String vVillageId) {
        return tblVillageDao.deleteById(vVillageId);
    }

    @Override
    public int deleteByEntity(TblVillage tblVillage) {
        return tblVillageDao.deleteByEntity(tblVillage);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblVillageDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblVillageDao.countAll();
    }

    @Override
    public int countByEntity(TblVillage tblVillage) {
        return tblVillageDao.countByEntity(tblVillage);
    }

    @Override
    public int existsByName(String vVillageName, String vProjectId) {
        return tblVillageDao.existsByName(vVillageName, vProjectId);
    }
}
