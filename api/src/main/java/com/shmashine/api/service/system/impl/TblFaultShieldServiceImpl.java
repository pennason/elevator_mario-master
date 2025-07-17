package com.shmashine.api.service.system.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.TblFaultShieldDao;
import com.shmashine.api.module.fault.input.FaultDefinitionModule;
import com.shmashine.api.service.system.TblFaultShieldServiceI;
import com.shmashine.common.entity.TblFaultShield;
import com.shmashine.common.utils.SnowFlakeUtils;

@Service
public class TblFaultShieldServiceImpl implements TblFaultShieldServiceI {

    @Resource(type = TblFaultShieldDao.class)
    private TblFaultShieldDao tblFaultShieldDao;

    @Resource(type = BizElevatorDao.class)
    private BizElevatorDao elevatorDao;

    @Override
    public TblFaultShieldDao getTblFaultShieldDao() {
        return tblFaultShieldDao;
    }

    @Override
    public TblFaultShield getById(String vFaultShieldId) {
        return tblFaultShieldDao.getById(vFaultShieldId);
    }

    @Override
    public List<TblFaultShield> getByEntity(TblFaultShield tblFaultShield) {
        return tblFaultShieldDao.getByEntity(tblFaultShield);
    }

    @Override
    public List<TblFaultShield> listByEntity(TblFaultShield tblFaultShield) {
        return tblFaultShieldDao.listByEntity(tblFaultShield);
    }

    @Override
    public List<TblFaultShield> listByIds(List<String> ids) {
        return tblFaultShieldDao.listByIds(ids);
    }

    @Override
    public int insert(TblFaultShield tblFaultShield) {
        Date date = new Date();
        tblFaultShield.setDtCreateTime(date);
        tblFaultShield.setDtModifyTime(date);
        return tblFaultShieldDao.insert(tblFaultShield);
    }

    @Override
    public int insertBatch(List<TblFaultShield> list) {
        return tblFaultShieldDao.insertBatch(list);
    }

    @Override
    public int update(TblFaultShield tblFaultShield) {
        tblFaultShield.setDtModifyTime(new Date());
        return tblFaultShieldDao.update(tblFaultShield);
    }

    @Override
    public int updateBatch(List<TblFaultShield> list) {
        return tblFaultShieldDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vFaultShieldId) {
        return tblFaultShieldDao.deleteById(vFaultShieldId);
    }

    @Override
    public int deleteByEntity(TblFaultShield tblFaultShield) {
        return tblFaultShieldDao.deleteByEntity(tblFaultShield);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblFaultShieldDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblFaultShieldDao.countAll();
    }

    @Override
    public int countByEntity(TblFaultShield tblFaultShield) {
        return tblFaultShieldDao.countByEntity(tblFaultShield);
    }

    @Override
    public List<Map<String, Object>> getShieldInfo(String elevatorCode, Integer elevatorType, int eventType) {
        return tblFaultShieldDao.getShieldInfo(elevatorCode, elevatorType, eventType);
    }

    @Override
    @Transactional
    public void updateFaultShield(String elevatorCode, List<Map<String, String>> faultShieldInfo) {

        tblFaultShieldDao.deleteByElevatorCode(elevatorCode);

        for (Map<String, String> map : faultShieldInfo) {
            if (!"1".equals(map.get("is_shield"))) {
                TblFaultShield tblFaultShield = new TblFaultShield();
                tblFaultShield.setVFaultShieldId(SnowFlakeUtils.nextStrId());
                tblFaultShield.setVElevatorId(map.get("elevator_id"));
                tblFaultShield.setVElevatorCode(elevatorCode);
                tblFaultShield.setIFaultType(Integer.valueOf(map.get("fault_type")));
                tblFaultShield.setVFaultName(map.get("fault_name"));
                tblFaultShield.setIIsUserVisible(Integer.valueOf(map.get("user_visible")));
                tblFaultShield.setIIsReport(Integer.valueOf(map.get("is_report")));
                tblFaultShieldDao.insert(tblFaultShield);
            }
        }
    }

    @Override
    @Transactional
    public void batchUpdateFaultShield(FaultDefinitionModule faultDefinitionModule) {

        List<String> elevatorCodes = faultDefinitionModule.getElevatorCodes();
        List<Map<String, String>> faultShieldInfo = faultDefinitionModule.getFaultShieldInfo();

        for (String code : elevatorCodes) {
            tblFaultShieldDao.deleteByElevatorCode(code);

            List<TblFaultShield> list = new ArrayList<>();

            for (Map<String, String> map : faultShieldInfo) {
                if (!"1".equals(map.get("is_shield"))) {
                    TblFaultShield tblFaultShield = new TblFaultShield();
                    tblFaultShield.setVFaultShieldId(SnowFlakeUtils.nextStrId());
                    tblFaultShield.setVElevatorId(map.get("elevator_id"));
                    tblFaultShield.setVElevatorCode(code);
                    tblFaultShield.setIFaultType(Integer.valueOf(map.get("fault_type")));
                    tblFaultShield.setVFaultName(map.get("fault_name"));
                    tblFaultShield.setIIsUserVisible(Integer.valueOf(map.get("user_visible")));
                    tblFaultShield.setIIsReport(Integer.valueOf(map.get("is_report")));
                    list.add(tblFaultShield);
                }
            }

            if (list.size() > 0) {
                tblFaultShieldDao.insertBatch(list);
            }

        }
    }
}