package com.shmashine.api.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.shmashine.api.dao.TblElevatorDao;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.api.service.system.TblDeviceServiceI;
import com.shmashine.api.service.system.TblElevatorServiceI;
import com.shmashine.common.constants.BusinessConstants;
import com.shmashine.common.entity.TblDevice;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.utils.SnowFlakeUtils;

@Service
public class TblElevatorServiceImpl implements TblElevatorServiceI {

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

    @Resource
    private TblDeviceServiceI deviceService;

    @Resource
    private ElevatorCacheService cacheService;

    @Override
    public TblElevatorDao getTblElevatorDao() {
        return tblElevatorDao;
    }

    @Override
    public TblElevator getById(String vElevatorId) {
        return tblElevatorDao.getById(vElevatorId);
    }

    @Override
    public TblElevator getByElevatorCode(String elevatorCode) {
        return tblElevatorDao.getByElevatorCode(elevatorCode);
    }

    @Override
    public TblElevator getByEquipmentCode(String equipmentCode) {
        return tblElevatorDao.getByEquipmentCode(equipmentCode);
    }

    @Override
    public TblElevator getByOneOfChoose(String elevatorId, String elevatorCode, String equipmentCode) {
        return tblElevatorDao.getByOneOfChoose(elevatorId, elevatorCode, equipmentCode);
    }

    @Override
    public List<TblElevator> getByEntity(TblElevator tblElevator) {
        return tblElevatorDao.getByEntity(tblElevator);
    }

    @Override
    public List<TblElevator> listByEntity(TblElevator tblElevator) {
        return tblElevatorDao.listByEntity(tblElevator);
    }

    @Override
    public List<TblElevator> listByIds(List<String> ids) {
        return tblElevatorDao.listByIds(ids);
    }

    @Override
    public List<TblElevator> listByCodes(List<String> codes) {
        return tblElevatorDao.listByCodes(codes);
    }

    @Override
    public int insert(TblElevator tblElevator) {
        Date date = new Date();
        tblElevator.setDtCreateTime(date);
        tblElevator.setDtModifyTime(date);
        return tblElevatorDao.insert(tblElevator);
    }

    @Override
    public int insertIsNotEmpty(TblElevator tblElevator) {
        Date date = new Date();
        tblElevator.setDtCreateTime(date);
        tblElevator.setDtModifyTime(date);
        return tblElevatorDao.insertIsNotEmpty(tblElevator);
    }

    @Override
    public int insertBatch(List<TblElevator> list) {
        return tblElevatorDao.insertBatch(list);
    }

    @Override
    public int update(TblElevator tblElevator) {
        tblElevator.setDtModifyTime(new Date());
        var res = tblElevatorDao.update(tblElevator);
        updateCache(tblElevator);
        return res;
    }

    @Override
    public int updateBatch(List<TblElevator> list) {
        var res = tblElevatorDao.updateBatch(list);
        updateCache(list);
        return res;
    }

    @Override
    public int deleteById(String vElevatorId) {
        return tblElevatorDao.deleteById(vElevatorId);
    }

    @Override
    public int deleteByEntity(TblElevator tblElevator) {
        return tblElevatorDao.deleteByEntity(tblElevator);
    }

    @Override
    public int deleteByIds(List<String> list) {
        return tblElevatorDao.deleteByIds(list);
    }

    @Override
    public int countAll() {
        return tblElevatorDao.countAll();
    }

    @Override
    public int countByEntity(TblElevator tblElevator) {
        return tblElevatorDao.countByEntity(tblElevator);
    }

    @Override
    public List<String> checkExistsByCodes(List<String> list) {
        return tblElevatorDao.checkExistsByCodes(list);
    }

    @Override
    @Transactional
    public int batchAddElevatorAndDeviceByProject(List<String> codes, String projectId, String deviceMark) {
        // 批量新增
        List<TblElevator> addElevatorList = Lists.newArrayList();
        List<TblDevice> addDeviceList = Lists.newArrayList();
        for (String code : codes) {
            // 电梯信息
            TblElevator tblElevator = new TblElevator();
            tblElevator.setVElevatorId(SnowFlakeUtils.nextStrId());
            tblElevator.setVProjectId(projectId);
            tblElevator.setVElevatorCode(code);
            addElevatorList.add(tblElevator);

            //添加设备信息表
            if ("1".equals(deviceMark)) {
                // 设备信息 - 前装
                TblDevice front = new TblDevice();
                front.setVDeviceId(SnowFlakeUtils.nextStrId());
                front.setVSensorType(BusinessConstants.FRONT);
                front.setVElevatorId(tblElevator.getVElevatorId());
                front.setVElevatorCode(tblElevator.getVElevatorCode());
                front.setDtCreateTime(new Date());
                addDeviceList.add(front);
            } else if ("2".equals(tblElevator.getDeviceMark())) {
                // 设备信息 - 迅达单盒
                TblDevice liftXunDa = new TblDevice();
                liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
                liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
                liftXunDa.setVElevatorId(tblElevator.getVElevatorId());
                liftXunDa.setVElevatorCode(tblElevator.getVElevatorCode());
                liftXunDa.setDtCreateTime(new Date());
                deviceService.insert(liftXunDa);
            } else if ("3".equals(tblElevator.getDeviceMark())) {
                // 设备信息 - 扶梯
                TblDevice escalator = new TblDevice();
                escalator.setVDeviceId(SnowFlakeUtils.nextStrId());
                escalator.setVSensorType(BusinessConstants.SENSOR_TYPE_ESCALATOR);
                escalator.setVElevatorId(tblElevator.getVElevatorId());
                escalator.setVElevatorCode(tblElevator.getVElevatorCode());
                escalator.setDtCreateTime(new Date());
                deviceService.insert(escalator);
            } else {
                // 设备信息 - 轿顶
                TblDevice carRoof = new TblDevice();
                carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
                carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
                carRoof.setVElevatorId(tblElevator.getVElevatorId());
                carRoof.setVElevatorCode(tblElevator.getVElevatorCode());
                carRoof.setDtCreateTime(new Date());
                addDeviceList.add(carRoof);
                // 设备信息 - 机房
                TblDevice motorRoom = new TblDevice();
                motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
                motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
                motorRoom.setVElevatorId(tblElevator.getVElevatorId());
                motorRoom.setVElevatorCode(tblElevator.getVElevatorCode());
                carRoof.setDtCreateTime(new Date());
                addDeviceList.add(motorRoom);
            }

        }

        int num = insertBatch(addElevatorList);
        deviceService.insertBatch(addDeviceList);
        return num;
    }


    private void updateCache(List<TblElevator> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(this::updateCache);
        }
    }

    private void updateCache(TblElevator tblElevator) {
        cacheService.updateCacheByEntityFromDatabase(tblElevator);
    }

}