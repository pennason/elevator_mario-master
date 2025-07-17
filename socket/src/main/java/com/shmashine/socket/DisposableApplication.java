package com.shmashine.socket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.device.service.TblDeviceService;
import com.shmashine.socket.elevator.service.TblElevatorService;
import com.shmashine.socket.netty.ChannelManager;
import com.shmashine.socket.netty.server.carroof.CarRoofNettyServer;
import com.shmashine.socket.netty.server.cube.CubeNettyServer;
import com.shmashine.socket.netty.server.motorroom.MotorRoomNettyServer;
import com.shmashine.socket.redis.utils.RedisKeyUtils;
import com.shmashine.socket.redis.utils.RedisUtils;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 结束程序时处理
 * 关闭所有netty连接，将数据库中的电梯置为离线状态
 *
 * @author little.li
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DisposableApplication {
    private final RedisUtils redisUtils;

    private final TblElevatorService elevatorService;

    private final TblDeviceService deviceService;

    private final CubeNettyServer cubeNettyServer;

    private final CarRoofNettyServer carRoofNettyServer;

    private final MotorRoomNettyServer motorRoomNettyServer;


    @PreDestroy
    public void disposableApplication() {

        // 关闭netty服务
        cubeNettyServer.close();
        carRoofNettyServer.close();
        motorRoomNettyServer.close();

        Map<String, Channel> allChannel = ChannelManager.getAllChannel();

        log.info("disposableApplication --- allChannel : {}", allChannel);

        Collection<Channel> channelCollection = allChannel.values();
        for (Channel channel : channelCollection) {

            // 删除map中元素 并获取被删除的电梯编号和设备类型
            String channelKey = ChannelManager.delChannel(channel);
            // 关闭channel
            channel.close();
            if (StringUtils.isEmpty(channelKey)) {
                continue;
            }
            try {
                String elevatorCode = channelKey.split("_")[1];
                String sensorType = channelKey.split("_")[0];
                // 将设备状态记为离线
                disChannel(elevatorCode, sensorType);
            } catch (Exception e) {
                channel.close();
                e.printStackTrace();
            }
        }

    }


    @PostConstruct
    public void channelInit() {

        Map<String, Channel> allChannel = ChannelManager.getAllChannel();

        log.info("disposableApplication --- allChannel : {}", allChannel);

        Collection<Channel> channelCollection = allChannel.values();
        for (Channel channel : channelCollection) {
            // 删除map中元素 并获取被删除的电梯编号和设备类型
            String channelKey = ChannelManager.delChannel(channel);
            // 关闭channel
            channel.close();
            if (StringUtils.isEmpty(channelKey)) {
                continue;
            }
            try {
                String elevatorCode = channelKey.split("_")[1];
                String sensorType = channelKey.split("_")[0];
                // 将设备状态记为离线
                disChannel(elevatorCode, sensorType);
            } catch (Exception e) {
                channel.close();
                e.printStackTrace();
            }
        }

    }


    /**
     * 将设备状态记为离线
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     */
    private void disChannel(String elevatorCode, String sensorType) {
        String nowTime = TimeUtils.nowTime();

        // 更新电梯状态为离线
        elevatorService.updateOnlineStatus(elevatorCode, 0);
        // 更新设备状态为离线
        deviceService.updateOffLineStatus(elevatorCode, sensorType, nowTime);

        // 更新Redis中设备状态
        String deviceStatus = RedisKeyUtils.getDeviceStatus(elevatorCode, sensorType);
        Map<String, String> map = new HashMap<>();
        map.put("online", "0");
        map.put("offline_time", nowTime);
        redisUtils.hmSet(deviceStatus, map);

        // 更新Redis中电梯状态
        String elevatorStatus = RedisKeyUtils.getElevatorStatus(elevatorCode);
        redisUtils.hmSet(elevatorStatus, "online", "0");

        log.info("{} --- {} is destroy", TimeUtils.nowTime(), elevatorCode);
    }


}