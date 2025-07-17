package com.shmashine.sender.server.dataAccount.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblElevatorInfos;
import com.shmashine.mdf.cache.caffeine.annotation.CacheOptions;
import com.shmashine.mdf.cache.caffeine.core.CacheProvider;
import com.shmashine.sender.dao.BizDataAccountDao;
import com.shmashine.sender.server.dataAccount.BizDataAccountService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class BizDataAccountServiceImpl implements BizDataAccountService {

    @Autowired
    private BizDataAccountDao bizDataAccountDao;

    @Override
    @Cacheable(cacheNames = "data_account")
    @CacheOptions(provider = CacheProvider.CAFFEINE, maximumSize = 8L)
    public List<Map> list() {
        return bizDataAccountDao.list();
    }

    @Override
    public List<TblElevator> getElevatorsByLingang() {
        return bizDataAccountDao.getElevatorsByLingang();
    }

    @Override
    public List<TblElevatorInfos> getElevatorsInfosByLingang() {
        return bizDataAccountDao.getElevatorsInfosByLingang();
    }
}
