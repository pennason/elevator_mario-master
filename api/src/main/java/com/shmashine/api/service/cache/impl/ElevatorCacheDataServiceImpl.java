// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.service.cache.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.properties.EndpointProperties;
import com.shmashine.api.service.cache.ElevatorCacheDataServiceI;
import com.shmashine.common.model.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/11/28 15:08
 * @since v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElevatorCacheDataServiceImpl implements ElevatorCacheDataServiceI {
    private final EndpointProperties properties;
    private final static String URI_CACHE_DATA_RUNNING = "/sender/cache/running/{elevatorCode}";
    private final static String URI_CACHE_DATA_FAULT_LIST = "/sender/cache/fault-list/{elevatorCode}";

    @Override
    public Result getRunningDataFromCache(String elevatorCode) {
        var uri = URI_CACHE_DATA_RUNNING.replace("{elevatorCode}", elevatorCode);
        var res = getDataFromSender(uri);
        return StringUtils.hasText(res) ? JSON.parseObject(res, Result.class) : Result.success("无运行数据");
    }

    @Override
    public Result getFaultListDataFromCache(String elevatorCode) {
        var uri = URI_CACHE_DATA_FAULT_LIST.replace("{elevatorCode}", elevatorCode);
        var res = getDataFromSender(uri);
        return StringUtils.hasText(res) ? JSON.parseObject(res, Result.class) : Result.success(Collections.emptyList(), "无故障级联");
    }


    /**
     * 从sender服务中获取结果
     *
     * @param uri 电梯编号
     */
    private String getDataFromSender(String uri) {
        log.info("getDataFromSender uri：{}", uri);
        try {
            //拼接请求参数
            String url = properties.getSenderServer() + uri;
            var res = HttpUtil.get(url, 15000);
            log.info("getDataFromSender，uri：[{}]，返回结果：[{}]", uri, res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
