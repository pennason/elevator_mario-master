package com.shmashine.sender.server.fault.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.sender.dao.TblFaultDao;
import com.shmashine.sender.server.fault.ElevatorFailureService;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service("elevatorFailureService")
public class ElevatorFailureServiceImpl implements ElevatorFailureService {

    @Autowired
    private TblFaultDao tblFaultDao;

    @Override
    public List<String> getFaultTypeInTimeByElevatorCode(String elevatorCode) {
        return tblFaultDao.getFaultTypeByElevatorCode(elevatorCode);
    }

}
