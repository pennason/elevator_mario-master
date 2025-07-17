package com.shmashine.sender.server.fault.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblFaultDefinition;
import com.shmashine.common.entity.TblFaultDefinition0902;
import com.shmashine.sender.dao.TblFaultDefinitionDao;
import com.shmashine.sender.server.fault.TblFaultDefinitionServiceI;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Service
public class TblFaultDefinitionServiceImpl implements TblFaultDefinitionServiceI {

    //
    //    @Autowired
    //    private TblFaultDefinition0902Dao tblFaultDefinition0902Dao;

    @Autowired
    private TblFaultDefinitionDao tblFaultDefinitionDao;


    @Override
    public List<TblFaultDefinition0902> getFaultDefinitionListByElevatorType(Integer elevatorType, Integer sensorType) {
        //        TblFaultDefinition0902 faultDefinition = new TblFaultDefinition0902();
        //        faultDefinition.setElevatorType(elevatorType);
        //        faultDefinition.setSensorType(sensorType);
        //        return tblFaultDefinition0902Dao.getByEntity(faultDefinition);
        return null;
    }

    @Override
    public List<TblFaultDefinition> getAllFaultDefinition() {
        return tblFaultDefinitionDao.list();
    }


}