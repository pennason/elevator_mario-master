package com.shmashine.sender.server.elevator.impl;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblProject;
import com.shmashine.mdf.cache.caffeine.annotation.CacheOptions;
import com.shmashine.mdf.cache.caffeine.core.CacheProvider;
import com.shmashine.sender.dao.TblElevatorDao;
import com.shmashine.sender.dao.TblProjectDao;
import com.shmashine.sender.entity.SendToUser;
import com.shmashine.sender.entity.TblHaierLiftInfo;
import com.shmashine.sender.redis.utils.RedisUtils;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.RequiredArgsConstructor;

/**
 * 电梯接口 业务层
 *
 * @author LiuLiFu
 * @since 2020/6/1215:49
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BizElevatorServiceImpl implements BizElevatorService {

    private final TblElevatorDao tblElevatorDao;
    private final TblProjectDao tblProjectDao;
    private final RedisUtils redisUtils;

    @Override
    public List<TblElevator> listOfAll() {
        return tblElevatorDao.listOfAll();
    }

    @Override
    @Cacheable(cacheNames = "elevator_cache", key = "#elevatorCode", unless = "#result == null")
    @CacheOptions(provider = CacheProvider.CAFFEINE, expireAfterWrite = 5, expireTimeUnit = ChronoUnit.HOURS,
            maximumSize = 10000L)
    public TblElevator getByElevatorCode(String elevatorCode) {
        /*var cache = redisUtils.get(RedisKeyUtils.getTblElevatorCacheKeyByCode(elevatorCode));
        if (StringUtils.hasText(cache)) {
            return JSON.parseObject(cache, TblElevator.class);
        }
        var res = tblElevatorDao.getByElevatorCode(elevatorCode);
        if (res == null) {
            return null;
        }
        redisUtils.set(RedisKeyUtils.getTblElevatorCacheKeyByCode(elevatorCode), JSON.toJSONString(res), 2 * 3600);
        return res;*/
        return tblElevatorDao.getByElevatorCode(elevatorCode);
    }

    @Override
    public List<TblElevator> getByPtCode(String ptCode) {
        return tblElevatorDao.getByPtCode(ptCode);
    }

    @Override
    public List<TblElevator> getByPtCodeAndProjectId(String ptCode, String projectId) {
        return tblElevatorDao.getByPtCodeAndProjectId(ptCode, projectId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMaintainDate(String registerNumber, Date completeTime) {
        tblElevatorDao.updateMaintainDateByRegisterNumber(registerNumber, completeTime);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateInspectDate(String registerNumber, Date lastDate, Date nextDate) {
        tblElevatorDao.updateInspectDateByRegisterNumber(registerNumber, lastDate, nextDate);
    }

    @Override
    public ArrayList<String> getRegNumberByProjectId(String projectId) {
        return tblElevatorDao.getRegNumberByProjectId(projectId);
    }

    @Override
    public List<SendToUser> taskReloadSendToUser() {
        return tblElevatorDao.taskReloadSendToUser();
    }

    @Override
    public TblHaierLiftInfo getLiftInfoCache(String equipmentCode) {
        return tblElevatorDao.getLiftInfoCache(equipmentCode);
    }

    @Override
    public List<String> getElevatorRegisterNumerIsNotNull() {
        return tblElevatorDao.getElevatorRegisterNumerIsNotNull();
    }

    @Override
    public List<TblProject> listProjectByIds(Collection<String> projectIds) {
        return tblProjectDao.listProjectByIds(projectIds);
    }
}
