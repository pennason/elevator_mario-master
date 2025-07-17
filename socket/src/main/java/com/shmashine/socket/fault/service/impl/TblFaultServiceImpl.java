package com.shmashine.socket.fault.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.socket.fault.dao.TblFaultDao;
import com.shmashine.socket.fault.service.TblFaultService;


/**
 * 故障记录表(TblFault)表服务实现类
 *
 * @author little.li
 * @since 2020-06-14 15:35:02
 */
@Service("tblFaultService")
public class TblFaultServiceImpl implements TblFaultService {


    @Resource
    private TblFaultDao tblFaultDao;


    /**
     * 更新手动恢复故障状态
     *
     * @param elevatorCode 电梯编号
     * @param faultNum     故障数
     * @param manualClear  手动恢复
     */
    @Override
    public void updateManualClear(String elevatorCode, int faultNum, Integer manualClear) {
        tblFaultDao.updateManualClear(elevatorCode, faultNum, manualClear);
    }


    /**
     * 更加电梯编号获取所有故障中的故障编号
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public List<String> getFaultTypeByCode(String elevatorCode) {
        return tblFaultDao.getByCodeFailure(elevatorCode);
    }

    @Override
    public List<String> getFaultSecondTypeByCode(String elevatorCode) {
        return tblFaultDao.getFaultSecondTypeByCode(elevatorCode);
    }

    @Override
    public void disappearAllTempFault(String elevatorCode) {
        tblFaultDao.disappearAllTempFault(elevatorCode);
    }


}