package com.shmashine.userclientapplets.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.userclientapplets.client.RemoteApiClient;
import com.shmashine.userclientapplets.dao.FaultDao;
import com.shmashine.userclientapplets.entity.Event;
import com.shmashine.userclientapplets.entity.Fault;
import com.shmashine.userclientapplets.entity.PageListResultEntity;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.FaultService;

import lombok.RequiredArgsConstructor;


/**
 * 故障服务实现
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/10 10:58
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FaultServiceImpl extends ServiceImpl<FaultDao, Fault> implements FaultService {
    private final RemoteApiClient remoteApiClient;

    @Override
    public PageListResultEntity queryTrappedPeopleByPage(SearchFaultModule searchFaultModule) {

        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var faultList = new PageInfo<>(baseMapper.queryTrappedPeopleByPage(searchFaultModule), pageSize);

        // 封装分页数据结构
        return new PageListResultEntity<>(pageIndex, pageSize, faultList.getTotal(),
                faultList.getList());

    }

    @Override
    public Integer queryFaultNumber(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultTotal(searchFaultModule);
    }

    @Override
    public Integer queryTrappedPeopleNumber(SearchFaultModule searchFaultModule) {
        return baseMapper.getTrappedPeopleTotal(searchFaultModule);
    }

    @Override
    public HashMap getFaultById(String faultId, Event event) {

        HashMap faultDetail = remoteApiClient.getFaultDetail(faultId);
        if (event != null) {
            faultDetail.put("eventNumbe", event.getEventNumber());
        }
        return faultDetail;
    }

    @Override
    public PageListResultEntity getFaultByPage(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var faultList = new PageInfo<>(baseMapper.queryFaultList(searchFaultModule), pageSize);

        return new PageListResultEntity(pageIndex, pageSize, faultList.getTotal(), faultList.getList());
    }

    @Override
    public List<String> getFaultByDay(String userId, boolean admin) {
        return baseMapper.getFaultByDay(userId, admin);
    }

    @Override
    public Boolean getTrappedPeopleStatus(String userId, boolean admin) {

        SearchFaultModule searchFaultModule = new SearchFaultModule();
        searchFaultModule.setUserId(userId);
        searchFaultModule.setAdminFlag(admin);
        searchFaultModule.setiStatus(0);

        Integer trappedPeopleTotal = baseMapper.getTrappedPeopleTotal(searchFaultModule);
        return trappedPeopleTotal > 0;
    }

    @Override
    public Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule) {
        return baseMapper.getTrappedPeopleTimeForMX(searchFaultModule);
    }

    @Override
    public JSONObject getFaultType(Integer elevatorType, Integer eventType) {
        return remoteApiClient.getFaultType(elevatorType, eventType);
    }

    @Override
    public List<Fault> getFaultList(List<String> elevators, String selectStartTime, String selectEndTime,
                                    String faultType) {
        return baseMapper.getFaultList(elevators, selectStartTime, selectEndTime, faultType);
    }
}
