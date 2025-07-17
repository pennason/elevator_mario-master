package com.shmashine.userclientapplets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.Event;
import com.shmashine.userclientapplets.entity.SearchFaultModule;

/**
 * EventService
 *
 * @author jiangheng
 * @version V1.0 - 2022/2/15 14:34
 */
public interface EventService extends IService<Event> {

    /**
     * 获取急修记录详情
     *
     * @param eventNumber 事件编号
     */
    Event getEventById(String eventNumber);

    /**
     * 获取急修列表
     *
     * @param searchFaultModule 查询条件
     */
    HashMap queryRepairByPage(SearchFaultModule searchFaultModule);

    /**
     * 根据故障id获取工单号
     *
     * @param faultId 故障id
     */
    Event getEventByFaultId(String faultId);

    /**
     * 获取故障工单状态
     *
     * @param faultId 故障id
     */
    List<Map> getEventStatus(String faultId);

    /**
     * 获取年检列表
     *
     * @param searchFaultModule 查询条件
     */
    HashMap getUnAnnualInspectionElevatorByPage(SearchFaultModule searchFaultModule);

}
