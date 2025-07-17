package com.shmashine.fault.task.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.fault.task.entity.TblEnventNotice;

/**
 * BizEnventNoticTaskDao
 */
@Mapper
public interface BizEnventNoticTaskDao {

    /**
     * 昨日故障/不文明行为 超过2次的电梯
     */
    List<Map> getYesterdayElevatorFaultStatics();

    /**
     * 当天发生且处于故障中的梯
     */
    List<Map> getLatestFaultStatistics();

    /**
     * 最近7天 topN 故障梯
     */
    List<Map> getTopHundredElevatorCode(Integer topN);


    void insertBatch(List<TblEnventNotice> list);

}
