package com.shmashine.api.service.fault.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.api.dao.TblFaultDao;
import com.shmashine.api.dao.TblSysUserResourceDao;
import com.shmashine.api.service.fault.FaultStatisticalService;

/**
 * 故障统计接口
 *
 * @author little.li
 */
@Service
public class FaultStatisticalServiceImpl implements FaultStatisticalService {


    @Autowired
    private TblSysUserResourceDao userResourceDao;

    @Autowired
    private TblFaultDao faultDao;

    @Override
    public Map<String, Object> getFaultStatisticalByDate(Map<String, Object> parms, String userId) {

        // 故障 多选
        Object faultTypeList = parms.get("faultTypeList");
        // 电梯 多选
        Object elevatorIdList = parms.get("elevatorIdList");

        // 电梯类型 单选
        Object elevatorType = parms.get("elevatorType");
        // 设备类型 单选
        Object sensorType = parms.get("sensorType");
        // 所属项目 单选
        Object projectId = parms.get("projectId");

        // 开始 - 结束时间 2020-05-19 - 2020-06-18
        Object startTime = parms.get("startTime");
        Object endTime = parms.get("endTime");

        return null;
    }


    @Override
    public Map<String, Object> getFaultStatisticalByElevator(Map<String, Object> parms, String userId) {

        // 故障 多选
        Object faultTypeList = parms.get("faultTypeList");
        // 电梯 多选
        Object elevatorIdList = parms.get("elevatorIdList");

        // 电梯类型 单选
        Object elevatorType = parms.get("elevatorType");
        // 设备类型 单选
        Object sensorType = parms.get("sensorType");
        // 所属项目 单选
        Object projectId = parms.get("projectId");

        // 开始 - 结束时间 2020-05-19 - 2020-06-18
        Object startTime = parms.get("startTime");
        Object endTime = parms.get("endTime");

        return null;
    }


}
