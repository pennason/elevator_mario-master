// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.cache.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.shmashine.api.service.cache.CacheServiceI;

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


}
