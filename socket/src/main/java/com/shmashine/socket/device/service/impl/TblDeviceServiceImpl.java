package com.shmashine.socket.device.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.common.utils.SnowFlakeUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.device.dao.TblDeviceDao;
import com.shmashine.socket.device.dao.TblDeviceEventRecordDao;
import com.shmashine.socket.device.entity.DeviceParamConfDO;
import com.shmashine.socket.device.entity.TblDevice;
import com.shmashine.socket.device.entity.TblDeviceEventRecord;
import com.shmashine.socket.device.service.TblDeviceService;

/**
 * 设备表(TblDevice)表服务实现类
 *
 * @author little.li
 * @since 2020-06-14 15:14:33
 */
@Service
public class TblDeviceServiceImpl implements TblDeviceService {


    @Resource
    private TblDeviceDao tblDeviceDao;

    @Resource
    private TblDeviceEventRecordDao deviceEventRecordDao;


    /**
     * 根据所属电梯编号、设备类型 检查设备是否存在
     */
    @Override
    public boolean checkDevice(String elevatorCode, String sensorType) {
        return tblDeviceDao.queryByElevatorCodeAndSensorType(elevatorCode, sensorType) != null;
    }


    /**
     * 更新设备信息
     */
    @Override
    public void updateDevice(JSONObject messageJson, String elevatorCode, String sensorType) {
        // 更新版本号等信息
        TblDevice device = new TblDevice();
        device.setVElevatorCode(elevatorCode);
        device.setVSensorType(sensorType);
        device.setVCcid(messageJson.getString("ccid"));
        device.setVImei(messageJson.getString("imei"));
        device.setVHwVersion(messageJson.getString("hw_version"));
        device.setVSwVersion(messageJson.getString("sw_version"));
        device.setVAliiotSecret(messageJson.getString("debug"));

        //设备类型
        device.seteType(messageJson.getString(SocketConstants.E_TYPE));

        //协议版本
        device.setProtocalVersion(messageJson.getString(SocketConstants.PROTOCAL_VERSION));


        device.setVUpdateMethod(messageJson.getString("updatemethod"));
        // 主从设备更新软件版本号
        device.setVMasterVersion(messageJson.getString("build_m"));
        device.setVSlaveVersion(messageJson.getString("build_s"));
        tblDeviceDao.updateByElevatorCodeAndSensorType(device);

    }

    /**
     * 更新设备在线状态
     */
    @Override
    public void updateOnLineStatus(String elevatorCode, String sensorType, String serverIp, String nowTime) {
        Date date = TimeUtils.stringToDate(nowTime);

        // 更新设备在线状态
        TblDevice device = new TblDevice();
        device.setVElevatorCode(elevatorCode);
        device.setVSensorType(sensorType);
        device.setVServerIp(serverIp);
        device.setDtOnlineTime(date);
        device.setIOnlineStatus(1);
        tblDeviceDao.updateByElevatorCodeAndSensorType(device);

    }


    /**
     * 更新设备离线状态
     */
    @Override
    public void updateOffLineStatus(String elevatorCode, String sensorType, String nowTime) {
        Date date = TimeUtils.stringToDate(nowTime);

        // 更新设备离线状态
        TblDevice device = new TblDevice();
        device.setVElevatorCode(elevatorCode);
        device.setVSensorType(sensorType);
        device.setDtOfflineTime(date);
        device.setIOnlineStatus(0);
        tblDeviceDao.updateByElevatorCodeAndSensorType(device);
    }


    @Override
    public void updateDeviceId(String elevatorCode, String sensorType, String id) {
        tblDeviceDao.updateDeviceId(elevatorCode, sensorType, id);
    }


    /**
     * 新建设备上下线记录
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @param type         1：上线，2：离线，3：重连
     */
    @Override
    public void insertDeviceEventRecord(String elevatorCode, String sensorType, int type, String reason) {
        Date date = new Date();
        TblDeviceEventRecord eventRecord = new TblDeviceEventRecord();

        eventRecord.setVDeviceEventRecordId(SnowFlakeUtils.nextStrId());
        eventRecord.setVElevatorCode(elevatorCode);
        eventRecord.setVSensorType(sensorType);
        eventRecord.setVReason(reason);
        eventRecord.setDtHappenTime(date);
        eventRecord.setIType(type);
        eventRecord.setDtCreateTime(date);
        deviceEventRecordDao.insert(eventRecord);
    }

    @Override
    public List<TblDevice> getDeviceListByElevatorCode(String elevatorCode) {
        return tblDeviceDao.listByElevatorCode(elevatorCode);
    }

    @Override
    public void cancelDeviceTimeOutEvent(String elevatorCode, String sensorType) {
        List<String> ids = tblDeviceDao.getDeviceTimeOutEvent(elevatorCode, sensorType);
        ids.stream().forEach(
                it -> tblDeviceDao.cancelDeviceTimeOutEvent(it)
        );
    }

    @Override
    public List<HashMap<String, String>> getSensorFaultShields() {
        return tblDeviceDao.getSensorFaultShields();
    }

    @Override
    public List<HashMap<String, String>> getFaultShields() {
        return tblDeviceDao.getFaultShields();
    }

    @Override
    public HashMap<String, String> getSensorFaultShieldsByElevator(String elevatorCode) {
        return tblDeviceDao.getSensorFaultShieldsByElevator(elevatorCode);
    }

    @Override
    public TblDevice getDevice(String elevatorCode, String sensorType) {
        return tblDeviceDao.queryByElevatorCodeAndSensorType(elevatorCode, sensorType);
    }

    @Override
    public DeviceParamConfDO getDeviceParamConf(String elevatorCode, String sensorType) {
        return tblDeviceDao.getDeviceParamConf(elevatorCode, sensorType);
    }

}