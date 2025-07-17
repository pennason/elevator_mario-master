package com.shmashine.fault.fault.dao;

import org.apache.ibatis.annotations.Mapper;

import com.shmashine.fault.fault.entity.TblElevatorEvent;


/**
 * 电梯模式记录
 *
 * @author jiangheng
 * @version 1.0 -2021/5/6 14:12
 */
@Mapper
public interface TblElevatorEventDao {

    /**
     * 电梯模式切换记录
     *
     * @param tblElevatorEvent 电梯模式切换记录
     */
    void insert(TblElevatorEvent tblElevatorEvent);
}
