package com.shmashine.fault.fault.service;

import com.shmashine.fault.fault.entity.TblElevatorEvent;

/**
 * 模式切换
 *
 * @author jiangheng
 * @version V1.0.0 - 2021/5/6 14:06
 */
public interface EventService {

    /**
     * 模式切换记录
     *
     * @param tblElevatorEvent 切换记录
     */
    void insert(TblElevatorEvent tblElevatorEvent);
}
