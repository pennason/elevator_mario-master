package com.shmashine.sender.server.fault.impl;

import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shmashine.mdf.cache.caffeine.annotation.CacheOptions;
import com.shmashine.mdf.cache.caffeine.core.CacheProvider;
import com.shmashine.sender.dao.TblFaultShieldDao;
import com.shmashine.sender.entity.TblFaultShield;
import com.shmashine.sender.server.fault.TblFaultShieldService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblFaultShieldServiceImpl implements TblFaultShieldService {

    @Resource
    private TblFaultShieldDao faultShieldDao;

    @Override
    @Cacheable(cacheNames = "fault_shield_cache", key = "#elevatorCode", unless = "#result == null")
    @CacheOptions(provider = CacheProvider.CAFFEINE, expireAfterWrite = 5, expireTimeUnit = ChronoUnit.HOURS,
            maximumSize = 10000L)
    public List<TblFaultShield> getFaultShieldByElevatorCode(String elevatorCode) {
        return faultShieldDao.getFaultShieldByElevatorCode(elevatorCode);
    }
}
