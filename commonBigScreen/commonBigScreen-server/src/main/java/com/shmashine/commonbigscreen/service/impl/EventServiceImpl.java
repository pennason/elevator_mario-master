package com.shmashine.commonbigscreen.service.impl;


import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shmashine.common.constants.SystemConstants;
import com.shmashine.commonbigscreen.dao.EventDao;
import com.shmashine.commonbigscreen.entity.Event;
import com.shmashine.commonbigscreen.entity.PageListResultEntity;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.EventService;
import com.shmashine.commonbigscreen.service.UserService;
import com.shmashine.commonbigscreen.utils.EChartDateUtils;

/**
 * 仪电故障工单服务实现
 *
 * @author jiangheng
 * @version V1.0 -  2022/3/7 11:37
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventDao, Event> implements EventService {

    @Autowired
    private UserService userService;

    @Override
    public Integer getPeopleTrappedCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getPeopleTrappedCount(searchFaultModule);
    }

    @Override
    public Integer getFaultCount(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultCount(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByConfirmOrCompleted(searchFaultModule);
    }

    @Override
    public List<HashMap<String, Object>> getFaultOrderByFalsePositiveOrNew(SearchFaultModule searchFaultModule) {
        return baseMapper.getFaultOrderByFalsePositiveOrNew(searchFaultModule);
    }

    @Override
    public HashMap<String, Object> getEventCountByTime(SearchFaultModule searchFaultModule) {

        searchFaultModule.setAdminFlag(userService.isAdmin(searchFaultModule.getUserId()));

        List<HashMap<String, Object>> eventCounts = baseMapper.getEventCountByTime(searchFaultModule);

        String timeFlag = searchFaultModule.getTimeFlag();

        if (timeFlag.equals("00")) {
            return EChartDateUtils.getDataOnWeek(eventCounts, null, null, null);
        }

        if (timeFlag.equals("11")) {
            return EChartDateUtils.getDataOnMonth(eventCounts, null, null, null);
        }

        return null;
    }

    @Override
    public PageListResultEntity getEventByPage(SearchFaultModule searchFaultModule) {
        Integer pageIndex = searchFaultModule.getPageIndex();
        Integer pageSize = searchFaultModule.getPageSize();
        if (pageIndex == null || pageSize == null) {
            pageIndex = SystemConstants.DEFAULT_PAGE_INDEX;
            pageSize = SystemConstants.DEFAULT_PAGE_SIZE;
        }
        // 设置参数
        PageHelper.startPage(pageIndex, pageSize);
        var events = new PageInfo<>(baseMapper.getEventByPage(searchFaultModule), pageSize);
        return new PageListResultEntity(pageIndex, pageSize, (int) events.getTotal(), events.getList());
    }

    @Override
    public List<HashMap<String, Object>> getEventRealTimeSchedule(String eventId) {
        return baseMapper.getEventRealTimeSchedule(eventId);
    }

    @Override
    public HashMap<String, Object> getPeopleTrappedDetails(String faultId) {
        return baseMapper.getPeopleTrappedDetails(faultId);
    }
}
