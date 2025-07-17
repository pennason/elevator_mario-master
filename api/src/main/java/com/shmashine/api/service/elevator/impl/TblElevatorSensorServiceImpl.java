package com.shmashine.api.service.elevator.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.api.dao.BizElevatorDao;
import com.shmashine.api.dao.TblElevatorSensorDao;
import com.shmashine.api.dao.TblElevatorShieldDao;
import com.shmashine.api.dao.TblMonitorDefinitionDao;
import com.shmashine.api.dao.TblSensorDao;
import com.shmashine.api.service.elevator.TblElevatorSensorServiceI;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblElevatorSensor;
import com.shmashine.common.entity.TblElevatorShield;
import com.shmashine.common.entity.TblSensor;
import com.shmashine.common.utils.SnowFlakeUtils;

/**
 * 电梯传感器对应接口
 *
 * @author little.li
 */
@Service
public class TblElevatorSensorServiceImpl implements TblElevatorSensorServiceI {


    @Resource(type = TblElevatorSensorDao.class)
    private TblElevatorSensorDao tblElevatorSensorDao;

    @Resource(type = BizElevatorDao.class)
    private BizElevatorDao bizElevatorDao;

    @Resource(type = TblMonitorDefinitionDao.class)
    private TblMonitorDefinitionDao monitorDefinitionDao;

    @Resource(type = TblElevatorShieldDao.class)
    private TblElevatorShieldDao elevatorShieldDao;

    @Resource(type = TblSensorDao.class)
    private TblSensorDao sensorDao;


    @Override
    public TblElevatorSensorDao getTblElevatorSensorDao() {
        return tblElevatorSensorDao;
    }

    @Override
    public TblElevatorSensor getById(String vElevatorSensorId) {
        return tblElevatorSensorDao.getById(vElevatorSensorId);
    }

    @Override
    public List<TblElevatorSensor> getByEntity(TblElevatorSensor tblElevatorSensor) {
        return tblElevatorSensorDao.getByEntity(tblElevatorSensor);
    }

    @Override
    public List<TblElevatorSensor> listByEntity(TblElevatorSensor tblElevatorSensor) {
        return tblElevatorSensorDao.listByEntity(tblElevatorSensor);
    }

    @Override
    public List<TblElevatorSensor> listByIds(List<String> ids) {
        return tblElevatorSensorDao.listByIds(ids);
    }

    @Override
    public int insert(TblElevatorSensor tblElevatorSensor) {
        Date date = new Date();
        tblElevatorSensor.setDtCreateTime(date);
        tblElevatorSensor.setDtModifyTime(date);
        return tblElevatorSensorDao.insert(tblElevatorSensor);
    }

    @Override
    public int insertBatch(List<TblElevatorSensor> list) {
        return tblElevatorSensorDao.insertBatch(list);
    }

    @Override
    public int update(TblElevatorSensor tblElevatorSensor) {
        tblElevatorSensor.setDtModifyTime(new Date());
        return tblElevatorSensorDao.update(tblElevatorSensor);
    }

    @Override
    public int updateBatch(List<TblElevatorSensor> list) {
        return tblElevatorSensorDao.updateBatch(list);
    }

    @Override
    public int deleteById(String vElevatorSensorId) {
        return tblElevatorSensorDao.deleteById(vElevatorSensorId);
    }

    @Override
    public int deleteByEntity(TblElevatorSensor tblElevatorSensor) {
        return tblElevatorSensorDao.deleteByEntity(tblElevatorSensor);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblElevatorSensorDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblElevatorSensorDao.countAll();
    }

    @Override
    public int countByEntity(TblElevatorSensor tblElevatorSensor) {
        return tblElevatorSensorDao.countByEntity(tblElevatorSensor);
    }

    /**
     * 根据电梯编号获取传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public List<Map<String, Object>> getSensorListByElevatorCode(String elevatorCode) {
        return tblElevatorSensorDao.getSensorListByElevatorCode(elevatorCode);
    }

    /**
     * 清除电梯对应的传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public void batchRemoveByElevatorCode(String elevatorCode) {
        tblElevatorSensorDao.batchRemoveByElevatorCode(elevatorCode);
    }

    /**
     * 清除电梯对应的传感器列表
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public void batchSave(String elevatorCode, List<String> arr) {
        TblElevator elevator = bizElevatorDao.getElevatorIdByCode(elevatorCode);

        TblElevatorSensor elevatorSensor = new TblElevatorSensor();
        elevatorSensor.setVElevatorCode(elevatorCode);
        elevatorSensor.setVElevatorId(elevator.getVElevatorId());
        elevatorSensor.setDtCreateTime(new Date());
        elevatorSensor.setIDelFlag(0);
        for (String sensorId : arr) {
            elevatorSensor.setVSensorId(sensorId);
            tblElevatorSensorDao.insert(elevatorSensor);
        }
    }


    /**
     * 根据电梯编号获取页面展示屏蔽表
     *
     * @param elevatorCode 电梯编号
     */
    @Override
    public Map<String, Map<String, Object>> getPageDisplayByElevatorCode(String elevatorCode) {
        Map<String, Map<String, Object>> result = new HashMap<>();

        TblElevatorShield elevatorShield = elevatorShieldDao.getShieldByElevatorCode(elevatorCode);
        String monitorType = "";
        if (elevatorShield != null) {
            monitorType = elevatorShield.getVMonitorType();
        }

        //获取设备类型和协议版本
        Map<String, String> deviceShieldInfo = monitorDefinitionDao.getProtocalVersionAndEType(elevatorCode);
        deviceShieldInfo.put("monitorType", monitorType);

        // 获取监控相关屏蔽列表
        List<Map<String, Object>> shieldList = monitorDefinitionDao.monitorShieldList(deviceShieldInfo);

        // 构建前端结构
        shieldList.forEach(definition -> {
            Map<String, Object> temp = new HashMap<>();
            boolean isShow = (long) definition.get("shieldFlag") == 0;

            temp.put("name", definition.get("monitorName"));
            temp.put("isShow", isShow);
            result.put((String) definition.get("monitorField"), temp);
        });
        return result;
    }


    @Override
    public void insertElevatorSensor(String elevatorCode, List<String> arr) {
        // 清除对应记录
        tblElevatorSensorDao.batchRemoveByElevatorCode(elevatorCode);
        // 添加记录
        TblElevator elevator = bizElevatorDao.getElevatorIdByCode(elevatorCode);
        TblElevatorSensor elevatorSensor = new TblElevatorSensor();
        elevatorSensor.setVElevatorCode(elevatorCode);
        elevatorSensor.setVElevatorId(elevator.getVElevatorId());
        elevatorSensor.setDtCreateTime(new Date());
        elevatorSensor.setIDelFlag(0);

        // 根据传感器列表 更新电梯屏蔽表记录
        StringBuilder faultType = new StringBuilder();
        StringBuilder monitorType = new StringBuilder();
        for (String sensorId : arr) {
            TblSensor sensor = sensorDao.getById(sensorId);
            String vFaultType = sensor.getVFaultType();
            String vMonitorType = sensor.getVMonitorType();
            elevatorSensor.setVElevatorSensorId(SnowFlakeUtils.nextStrId());
            elevatorSensor.setVSensorId(sensorId);
            faultType.append(vFaultType);
            faultType.append(",");
            monitorType.append(vMonitorType);
            monitorType.append(",");
            tblElevatorSensorDao.insert(elevatorSensor);
        }
        if (faultType.length() > 1) {
            faultType.deleteCharAt(faultType.length() - 1);
            monitorType.deleteCharAt(monitorType.length() - 1);
        }
        // 更新电梯屏蔽表记录
        elevatorShieldDao.deleteByElevatorCode(elevatorCode);
        TblElevatorShield elevatorShield = new TblElevatorShield();
        elevatorShield.setVElevatorShieldId(SnowFlakeUtils.nextStrId());
        elevatorShield.setVElevatorId(elevator.getVElevatorId());
        elevatorShield.setVElevatorCode(elevatorCode);
        elevatorShield.setVFaultType(faultType.toString());
        elevatorShield.setVMonitorType(monitorType.toString());
        elevatorShieldDao.insert(elevatorShield);
    }


}