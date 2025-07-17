// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.pm.api.service.cache.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.pm.api.service.cache.CacheServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/9/26 14:37
 * @since v1.0
 */

@Slf4j
@Service
public class CacheServiceImpl implements CacheServiceI {

    @Override
    public void clearCacheAll() {
        clearProjectCache();
        clearDeptCache();
        clearSelectListIntegerCache();
        clearSelectListStringCache();
        clearCheckingTaskCache();
        clearDistributionTaskCache();
        clearInstallingTaskCache();
        clearInvestigateTaskCache();
        clearTestingTaskCache();
    }


    @CacheEvict(value = "project", allEntries = true)
    public void clearProjectCache() {
        log.info("清除项目缓存");
    }

    @CacheEvict(value = "dept", allEntries = true)
    public void clearDeptCache() {
        log.info("清除部门缓存");
    }

    @CacheEvict(value = "select_list_integer", allEntries = true)
    public void clearSelectListIntegerCache() {
        log.info("清除下拉列表缓存");
    }

    @CacheEvict(value = "select_list_string", allEntries = true)
    public void clearSelectListStringCache() {
        log.info("清除下拉列表缓存");
    }


    @CacheEvict(value = "checkingTask", allEntries = true)
    public void clearCheckingTaskCache() {
        log.info("清除检查任务缓存");
    }

    @CacheEvict(value = "distributionTask", allEntries = true)
    public void clearDistributionTaskCache() {
        log.info("清除分配任务缓存");
    }

    @CacheEvict(value = "installingTask", allEntries = true)
    public void clearInstallingTaskCache() {
        log.info("清除安装任务缓存");
    }

    @CacheEvict(value = "investigateTask", allEntries = true)
    public void clearInvestigateTaskCache() {
        log.info("清除勘察任务缓存");
    }

    @CacheEvict(value = "testingTask", allEntries = true)
    public void clearTestingTaskCache() {
        log.info("清除测试任务缓存");
    }

}
