package com.shmashine.userclientapplets.service;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shmashine.userclientapplets.entity.Event;
import com.shmashine.userclientapplets.entity.Fault;
import com.shmashine.userclientapplets.entity.PageListResultEntity;
import com.shmashine.userclientapplets.entity.SearchFaultModule;

/**
 * 故障服务
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/10 10:57
 */
public interface FaultService extends IService<Fault> {

    /**
     * 获取困人列表
     *
     * @param searchFaultModule 请求参数
     * @return 困人列表
     */
    PageListResultEntity queryTrappedPeopleByPage(SearchFaultModule searchFaultModule);

    /**
     * 获取故障数量
     *
     * @param searchFaultModule 请求参数
     * @return 故障数量
     */
    Integer queryFaultNumber(SearchFaultModule searchFaultModule);

    /**
     * 获取困人数
     *
     * @param searchFaultModule 请求参数
     * @return 困人数
     */
    Integer queryTrappedPeopleNumber(SearchFaultModule searchFaultModule);


    /**
     * 获取故障详情
     *
     * @param faultId 故障id
     * @return 故障详情
     */
    HashMap getFaultById(String faultId, Event event);

    /**
     * 获取故障列表
     *
     * @param searchFaultModule 请求参数
     * @return 故障列表
     */
    PageListResultEntity getFaultByPage(SearchFaultModule searchFaultModule);

    /**
     * 获取当日故障列表
     *
     * @param userId 用户id
     * @param admin  是否管理员
     * @return 故障列表
     */
    List<String> getFaultByDay(String userId, boolean admin);

    /**
     * 是否困人
     *
     * @param userId 用户id
     * @param admin  是否管理员
     * @return 是否困人
     */
    Boolean getTrappedPeopleStatus(String userId, boolean admin);

    /**
     * 救援时长统计麦信平台故障时长
     *
     * @param searchFaultModule 请求参数
     * @return 故障时长
     */
    Integer getTrappedPeopleTimeForMX(SearchFaultModule searchFaultModule);

    /**
     * 获取故障类型列表
     *
     * @param elevatorType 电梯类型
     * @param eventType    事件类型
     * @return 故障类型列表
     */
    JSONObject getFaultType(Integer elevatorType, Integer eventType);

    /**
     * 获取故障列表
     *
     * @param elevators       电梯ID列表
     * @param selectStartTime 开始时间
     * @param selectEndTime   结束时间
     * @param faultType       故障类型
     * @return 故障列表
     */
    List<Fault> getFaultList(List<String> elevators, String selectStartTime, String selectEndTime,
                             String faultType);
}
