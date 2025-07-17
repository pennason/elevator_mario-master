package com.shmashine.commonbigscreen.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shmashine.commonbigscreen.entity.Event;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;

/**
 * 仪电故障工单DO
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/7 11:39
 */
public interface EventDao extends BaseMapper<Event> {

    /**
     * 获取仪电困人数
     *
     * @param searchFaultModule 查询参数
     */
    Integer getPeopleTrappedCount(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电故障数
     *
     * @param searchFaultModule 查询参数
     */
    Integer getFaultCount(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为已确认和已完成的工单
     *
     * @param searchFaultModule 查询参数
     */
    List<HashMap<String, Object>> getFaultOrderByConfirmOrCompleted(SearchFaultModule searchFaultModule);

    /**
     * 仪电反推且状态为误报或新建工单两种状态的工单
     *
     * @param searchFaultModule 查询参数
     */
    List<HashMap<String, Object>> getFaultOrderByFalsePositiveOrNew(SearchFaultModule searchFaultModule);

    /**
     * 根据时间统计仪电反推且状态为已确认和已完成的工单
     *
     * @param searchFaultModule 查询参数
     */
    List<HashMap<String, Object>> getEventCountByTime(SearchFaultModule searchFaultModule);

    /**
     * 获取仪电反推故障工单
     *
     * @param searchFaultModule 查询参数
     */
    List<Event> getEventByPage(@Param("searchFaultModule") SearchFaultModule searchFaultModule);

    /**
     * 救援进度表
     *
     * @param eventId 事件id
     */
    List<HashMap<String, Object>> getEventRealTimeSchedule(String eventId);

    /**
     * 困人救援详情
     *
     * @param faultId 故障id
     */
    HashMap<String, Object> getPeopleTrappedDetails(String faultId);
}
