package com.shmashine.fault.task.service;

import org.apache.ibatis.annotations.Mapper;

/**
 * BizEnventNoticTaskServer
 */
@Mapper
public interface BizEnventNoticTaskServer {

    /**
     * 昨日故障/不文明行为 超过2次的电梯
     */
    void saveYesterdayElevatorFaultStatics();

}
