package com.shmashine.socket.message.handle.defaultHandle;


import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.socket.elevator.entity.TblElevator;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.message.MessageHandle;
import com.shmashine.socket.message.handle.UpdateHandle;
import com.shmashine.socket.netty.ChannelManager;

/**
 * 默认update消息处理
 *
 * @author little.li
 */
@Component
public class DefaultUpdateHandle implements UpdateHandle {

    private static final String PROTOCAL_VERSION = "default";

    private final TblElevatorService elevatorService;

    @Autowired
    public DefaultUpdateHandle(TblElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @PostConstruct
    public void registerHandle() {
        // 注册到监控、故障、困人消息的处理流程
        MessageHandle.register(this);
    }

    @Override
    public String getProtocalVersion() {
        return PROTOCAL_VERSION;
    }

    /**
     * 更新设备ip 返回处理状态
     */
    @Override
    public void ipHandle(JSONObject messageJson, String elevatorCode, String sensorType) {
        // 设备返回更新失败时 再次更新ip
        if (MessageConstants.FALSE.equals(messageJson.get(MessageConstants.STATUS))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("TY", MessageConstants.TYPE_UPDATE);
            map.put("ST", MessageConstants.STYPE_IP);
            map.put("ip", "ip");
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, JSON.toJSONString(map));
        }
    }


    /**
     * 更新设备frep 返回处理状态
     */
    @Override
    public void frepHandle(JSONObject messageJson, String elevatorCode, String sensorType) {
        // 设备返回更新失败时 再次更新frep
        if (MessageConstants.FALSE.equals(messageJson.get(MessageConstants.STATUS))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("TY", MessageConstants.TYPE_UPDATE);
            map.put("ST", MessageConstants.STYPE_FREP);
            map.put("frep", "frep");
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, JSON.toJSONString(map));
        }
    }


    /**
     * limit处理
     */
    @Override
    public void limitHandle(JSONObject messageJson, String elevatorCode, String sensorType) {
        // 设备返回更新失败时 再次更新limit
        if (MessageConstants.ERROR.equals(messageJson.get(MessageConstants.STATUS))) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("TY", MessageConstants.TYPE_UPDATE);
            map.put("ST", MessageConstants.STYPE_LIMIT);
            // 获取电梯楼层
            TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
            map.put("min", elevator.getIMinFloor() == null ? 1 : elevator.getIMinFloor());
            map.put("max", elevator.getIMaxFloor() == null ? 12 : elevator.getIMaxFloor());
            ChannelManager.sendMessageToChannel(elevatorCode, sensorType, JSON.toJSONString(map));
        }
    }


}
