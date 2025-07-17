package com.shmashine.socket.nezha.service;


import java.util.Map;

/**
 * ElevatorNewService
 *
 * @author chglee
 * @version 2018-02-03 22:53:02
 */
public interface ElevatorNewService {

    /**
     * 更新设备信息
     */
    int updateDevice(Map<String, Object> map);

    /**
     * 更新设备软件版本信息
     */
    int updateVersion(Map<String, Object> map);


    /**
     * 更新设备为在线
     */
    int updateOnline(String code, String sensorType);

    /**
     * 更新设备为离线
     */
    int updateOffline(String code, String objectType);


}
