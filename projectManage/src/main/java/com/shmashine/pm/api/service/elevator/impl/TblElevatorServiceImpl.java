package com.shmashine.pm.api.service.elevator.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.pm.api.dao.TblElevatorDao;
import com.shmashine.pm.api.service.elevator.TblElevatorService;

@Service
public class TblElevatorServiceImpl implements TblElevatorService {

    @Resource(type = TblElevatorDao.class)
    private TblElevatorDao tblElevatorDao;

//    @Resource
//    private TblDeviceServiceI deviceService;

    @Override
    public TblElevatorDao getTblElevatorDao() {
        return tblElevatorDao;
    }

    @Override
    public TblElevator getById(String vElevatorId) {
        return tblElevatorDao.getById(vElevatorId);
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
    public int insertBatchParty(List<TblElevator> list) {
        return tblElevatorDao.insertBatchParty(list);
    }

    @Override
    public int update(TblElevator tblElevator) {
        tblElevator.setDtModifyTime(new Date());
        return tblElevatorDao.update(tblElevator);
    }

    @Override
    public int updateBatch(List<TblElevator> list) {
        return tblElevatorDao.updateBatch(list);
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
    public int updateElevatorCodeBatch(List<TblElevator> list) {
        return tblElevatorDao.updateElevatorCodeBatch(list);
    }

    @Override
    public List<Map> countWithPmStatus(String vVillageId) {
        return tblElevatorDao.countWithPmStatus(vVillageId);
    }

    @Override
    public Map getRelatedInfoById(String elevatorId) {
        return tblElevatorDao.getRelatedInfoById(elevatorId);
    }

    @Override
    public TblElevator getByElevatorCode(String vElevatorCode) {
        return tblElevatorDao.getByElevatorCode(vElevatorCode);
    }


//    @Override
//    @Transactional
//    public int batchAddElevatorAndDeviceByProject(List<String> codes, String projectId, String deviceMark) {
//        // 批量新增
//        List<TblElevator> addElevatorList = Lists.newArrayList();
//        List<TblDevice> addDeviceList = Lists.newArrayList();
//        for (String code : codes) {
//            // 电梯信息
//            TblElevator tblElevator = new TblElevator();
//            tblElevator.setVElevatorId(SnowFlakeUtils.nextStrId());
//            tblElevator.setVProjectId(projectId);
//            tblElevator.setVElevatorCode(code);
//            addElevatorList.add(tblElevator);
//
//            //添加设备信息表
//            if ("1".equals(deviceMark)) {
//                // 设备信息 - 前装
//                TblDevice front = new TblDevice();
//                front.setVDeviceId(SnowFlakeUtils.nextStrId());
//                front.setVSensorType(BusinessConstants.FRONT);
//                front.setVElevatorId(tblElevator.getVElevatorId());
//                front.setVElevatorCode(tblElevator.getVElevatorCode());
//                front.setDtCreateTime(new Date());
//                addDeviceList.add(front);
//            } else if ("2".equals(tblElevator.getDeviceMark())) {
//                // 设备信息 - 迅达单盒
//                TblDevice liftXunDa = new TblDevice();
//                liftXunDa.setVDeviceId(SnowFlakeUtils.nextStrId());
//                liftXunDa.setVSensorType(BusinessConstants.SENSOR_TYPE_SINGLEBOX);
//                liftXunDa.setVElevatorId(tblElevator.getVElevatorId());
//                liftXunDa.setVElevatorCode(tblElevator.getVElevatorCode());
//                liftXunDa.setDtCreateTime(new Date());
//                deviceService.insert(liftXunDa);
//            } else if ("3".equals(tblElevator.getDeviceMark())) {
//                // 设备信息 - 扶梯
//                TblDevice escalator = new TblDevice();
//                escalator.setVDeviceId(SnowFlakeUtils.nextStrId());
//                escalator.setVSensorType(BusinessConstants.SENSOR_TYPE_ESCALATOR);
//                escalator.setVElevatorId(tblElevator.getVElevatorId());
//                escalator.setVElevatorCode(tblElevator.getVElevatorCode());
//                escalator.setDtCreateTime(new Date());
//                deviceService.insert(escalator);
//            } else {
//                // 设备信息 - 轿顶
//                TblDevice carRoof = new TblDevice();
//                carRoof.setVDeviceId(SnowFlakeUtils.nextStrId());
//                carRoof.setVSensorType(BusinessConstants.CAR_ROOF);
//                carRoof.setVElevatorId(tblElevator.getVElevatorId());
//                carRoof.setVElevatorCode(tblElevator.getVElevatorCode());
//                carRoof.setDtCreateTime(new Date());
//                addDeviceList.add(carRoof);
//                // 设备信息 - 机房
//                TblDevice motorRoom = new TblDevice();
//                motorRoom.setVDeviceId(SnowFlakeUtils.nextStrId());
//                motorRoom.setVSensorType(BusinessConstants.MOTOR_ROOM);
//                motorRoom.setVElevatorId(tblElevator.getVElevatorId());
//                motorRoom.setVElevatorCode(tblElevator.getVElevatorCode());
//                carRoof.setDtCreateTime(new Date());
//                addDeviceList.add(motorRoom);
//            }
//
//        }
//
//        int num = insertBatch(addElevatorList);
//        deviceService.insertBatch(addDeviceList);
//        return num;
//    }


}
