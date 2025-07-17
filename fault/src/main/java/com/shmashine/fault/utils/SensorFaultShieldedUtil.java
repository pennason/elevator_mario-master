package com.shmashine.fault.utils;

import static com.shmashine.common.constants.RedisConstants.SENSOR_FAULT_SHIELD_CACHE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.shmashine.fault.fault.dao.TblFaultDao;
import com.shmashine.fault.redis.util.RedisUtils;

/**
 * 传感器关联故障屏蔽
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/12/14 11:23
 */
@Component
public class SensorFaultShieldedUtil {

    HashMap<String, List<String>> sensorLinkFault = new HashMap<>();

    @Resource(type = TblFaultDao.class)
    private TblFaultDao tblFaultDao;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 初始化加载传感器关联故障屏蔽
     */
    @PostConstruct
    public void init() {

        //获取所有故障中故障
        List<HashMap<String, String>> sensorFaults = tblFaultDao.getAllSensorFaultOnFaulting(2, null);

        //获取传感器关联故障表
        tblFaultDao.getSensorConfig().forEach(it -> {
            if (it != null) {
                List<String> faults = Arrays.stream(it.get("v_fault_type").split(",")).toList();
                Arrays.stream(it.get("v_sensorFault_type").split(",")).forEach(f -> sensorLinkFault.put(f, faults));
            }
        });

        ConcurrentHashMap<String, List<String>> shieldCache = new ConcurrentHashMap<>();

        //加载故障屏蔽缓存
        for (HashMap<String, String> sensorFault : sensorFaults) {

            String elevatorCode = sensorFault.get("v_elevator_code");
            String faultType = sensorFault.get("i_fault_type");
            List<String> linkFaults = sensorLinkFault.get(faultType).stream().distinct().collect(Collectors.toList());

            List<String> shieldCacheFault = shieldCache.get(elevatorCode);
            if (shieldCacheFault == null) {
                shieldCache.put(elevatorCode, linkFaults);
            } else {
                shieldCacheFault.addAll(linkFaults);
            }
        }

        shieldCache.keySet().forEach(elevatorCode -> {

            List<String> faults = shieldCache.get(elevatorCode);

            redisUtils.deleteObject(SENSOR_FAULT_SHIELD_CACHE + elevatorCode);
            redisUtils.setCacheList(SENSOR_FAULT_SHIELD_CACHE + elevatorCode,
                    faults.stream().distinct().collect(Collectors.toList()));

        });

        System.out.println("初始化加载传感器关联故障屏蔽");
    }

    /**
     * 传感器故障消除 清除屏蔽缓存
     */
    public void disappearShieldCache(String elevator) {

        redisUtils.deleteObject(SENSOR_FAULT_SHIELD_CACHE + elevator);

        List<HashMap<String, String>> sensorFaults = tblFaultDao.getAllSensorFaultOnFaulting(2, elevator);

        //加载故障屏蔽缓存
        if (sensorFaults != null && sensorFaults.size() > 0) {

            List<String> linkFaults = new ArrayList<>();
            for (HashMap<String, String> sensorFault : sensorFaults) {
                linkFaults.addAll(sensorLinkFault.get(sensorFault.get("i_fault_type")));
            }

            redisUtils.setCacheList(SENSOR_FAULT_SHIELD_CACHE + elevator,
                    linkFaults.stream().distinct().collect(Collectors.toList()));
        }

    }

    /**
     * 传感器故障新增 添加屏蔽缓存
     */
    public void addShieldCache(String elevator, String fault) {

        redisUtils.deleteObject(SENSOR_FAULT_SHIELD_CACHE + elevator);

        List<HashMap<String, String>> sensorFaults = tblFaultDao.getAllSensorFaultOnFaulting(2, elevator);

        //加载故障屏蔽缓存
        if (sensorFaults != null && sensorFaults.size() > 0) {

            List<String> linkFaults = sensorLinkFault.get(fault).stream().distinct().collect(Collectors.toList());
            for (HashMap<String, String> sensorFault : sensorFaults) {
                linkFaults.addAll(sensorLinkFault.get(sensorFault.get("i_fault_type")));
            }

            redisUtils.setCacheList(SENSOR_FAULT_SHIELD_CACHE + elevator,
                    linkFaults.stream().distinct().collect(Collectors.toList()));
        }

    }


    /**
     * 是否屏蔽
     *
     * @param elevatorCode 电梯编号
     * @param faultType    故障类型
     * @return 屏蔽状态
     */
    public boolean checkShielded(String elevatorCode, String faultType) {

        List<String> sh = redisUtils.getCacheList(SENSOR_FAULT_SHIELD_CACHE + elevatorCode);

        if (sh == null) {
            return false;
        }

        return sh.contains(faultType);
    }
}
