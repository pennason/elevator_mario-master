package com.shmashine.sender.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.sender.entity.PrometheusMetricsOfflineCountEntity;
import com.shmashine.sender.server.elevator.BizElevatorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 推送信息计数器 统计
 */
@Slf4j
@Component
public class SenderCounterServer {

    private static Map<String, List<String>> groups = Maps.newConcurrentMap();

    private static Map<String, SenderCounter> senderCounters = Maps.newConcurrentMap();

    private static Map<String, Map<String, SenderCounter>> senderDayCounters = Maps.newLinkedHashMap();

    private static Map<String, Long> idleTimes = Maps.newConcurrentMap();


    @Autowired
    private BizElevatorService bizElevatorService;

    /**
     * 计数
     *
     * @param groupId        组ID
     * @param registerNumber 电梯注册码
     * @param topic          电梯注册码
     */
    public void publish(String groupId, String registerNumber, String topic) {

        // 更新 group 推送记时
        idleTimes.put(groupId, System.currentTimeMillis());

        String key = groupId + ":" + registerNumber;
        SenderCounter senderCounter = senderCounters.get(key);
        if (null == senderCounter) {
            senderCounter = new SenderCounter();
            senderCounter.setGroupId(groupId);
            senderCounter.setRegisterNumber(registerNumber);
            // 获取电梯详细
            TblElevator elevator = bizElevatorService.getByElevatorCode(registerNumber);
            if (null != elevator) {
                senderCounter.setElevatorCode(elevator.getVElevatorCode());
                senderCounter.setElevatorName(elevator.getVElevatorName());
                senderCounter.setAdder(elevator.getVAddress());
                senderCounter.setProjectId(elevator.getVProjectId());
            }
            senderCounters.put(key, senderCounter);
            // 判定groups中是否存在
            List<String> group = groups.getOrDefault(groupId, Lists.newArrayList());
            if (!group.contains(registerNumber)) {
                group.add(registerNumber);
                groups.put(groupId, group);
            }
        }

        // 更新统计计数
        senderCounter.setLastTime(new Date());
        senderCounter.getTotalCount().incrementAndGet();
        senderCounter.setOnline(true);
        switch (topic) {
            case "monitor":
                senderCounter.getMonitorCount().incrementAndGet();
                break;
            case "fault":
                senderCounter.getFaultCount().incrementAndGet();
                break;
            default:
                break;
        }
        // 统计每日
        addCountOnDay(registerNumber, topic);
    }

    public static void addCountOnDay(String registerNumber, String topic) {
        senderDayCounters.putIfAbsent(registerNumber, Maps.newLinkedHashMap());
        Map<String, SenderCounter> countMap = senderDayCounters.get(registerNumber);
        // 获取日期字符串
        String day = DateUtils.format(new Date());
        countMap.putIfAbsent(day, new SenderCounter());
        SenderCounter senderCounter = countMap.get(day);
        senderCounter.setLastTime(new Date());
        senderCounter.getTotalCount().incrementAndGet();
        switch (topic) {
            case "monitor":
                senderCounter.getMonitorCount().incrementAndGet();
                break;
            case "fault":
                senderCounter.getFaultCount().incrementAndGet();
                break;
            case "tr":
                senderCounter.getStaticCount().incrementAndGet();
                break;
            default:
                break;
        }
    }

    /**
     * 依据分组查看统计
     */
    public static Map count(String groupId) {
        Map<String, Object> result = Maps.newHashMap();

        long monitorCount = 0;
        long offlineCount = 0;
        long faultCount = 0;
        long staticCount = 0;
        List<String> group = groups.get(groupId);
        List<SenderCounter> list = Lists.newArrayList();
        if (null != group && group.size() > 0) {
            for (String registerNumber : group) {
                String key = groupId + ":" + registerNumber;
                SenderCounter senderCounter = senderCounters.get(key);
                if (null != senderCounter) {
                    if (!senderCounter.isOnline()) {
                        offlineCount++;
                    }
                    monitorCount += senderCounter.getMonitorCount().get();
                    faultCount += senderCounter.getFaultCount().get();
                    staticCount += senderCounter.getStaticCount().get();
                    list.add(senderCounter);
                }
            }
        }
        result.put("size", list.size());
        result.put("offlineCount", offlineCount);
        result.put("onlineCount", list.size() - offlineCount);
        result.put("totalCount", monitorCount + faultCount + staticCount);
        result.put("monitorCount", monitorCount);
        result.put("faultCount", faultCount);
        result.put("staticCount", staticCount);
        result.put("registerNumbers", group);
        result.put("senderCounters", list);
        return result;
    }

    /**
     * 获取 离线数量
     *
     * @param groupId 分组
     * @return 结果
     */
    public static PrometheusMetricsOfflineCountEntity offlineCount(String groupId) {
        Map<String, Object> result = Maps.newHashMap();
        var offlineCount = 0;
        List<String> group = groups.get(groupId);
        List<SenderCounter> list = Lists.newArrayList();
        if (null != group && group.size() > 0) {
            for (String registerNumber : group) {
                String key = groupId + ":" + registerNumber;
                var senderCounter = senderCounters.get(key);
                if (null != senderCounter) {
                    if (!senderCounter.isOnline()) {
                        offlineCount++;
                    }
                    list.add(senderCounter);
                }
            }
        }
        //result.put("size", list.size());
        //result.put("offlineCount", offlineCount);
        return PrometheusMetricsOfflineCountEntity.builder()
                .total(list.size())
                .offline(offlineCount)
                .build();
    }

    /**
     * 获取 离线数量
     *
     * @param groupId 分组 yidian
     * @return 结果
     */
    public static Map<String, PrometheusMetricsOfflineCountEntity> offlineCountByProject(String groupId) {
        Map<String, PrometheusMetricsOfflineCountEntity> result = Maps.newHashMap();
        List<String> group = groups.get(groupId);

        if (null != group && group.size() > 0) {
            for (String registerNumber : group) {
                String key = groupId + ":" + registerNumber;
                var senderCounter = senderCounters.get(key);
                if (null != senderCounter) {
                    var projectString = senderCounter.getProjectId();
                    if (!StringUtils.hasText(projectString)) {
                        continue;
                    }
                    if (result.get(projectString) == null) {
                        result.put(projectString, PrometheusMetricsOfflineCountEntity.builder()
                                .total(0)
                                .offline(0)
                                .build());
                    }
                    result.get(projectString).setTotal(result.get(projectString).getTotal() + 1);
                    if (!senderCounter.isOnline()) {
                        result.get(projectString).setOffline(result.get(projectString).getOffline() + 1);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 监控小屏统计
     */
    // CHECKSTYLE:OFF
    public Map getLitterScreen(String projectId, String groupId) {
        Map<String, Object> result = Maps.newHashMap();

        long monitorCount = 0;
        long offlineCount = 0;
        long faultCount = 0;
        long staticCount = 0;
        long s = 0;

        //查询项目对应的电梯code
        ArrayList<String> listCode = bizElevatorService.getRegNumberByProjectId(projectId);

        List<String> group = groups.get(groupId);
        List<SenderCounter> list = Lists.newArrayList();
        if (null != group && group.size() > 0) {

            if (!StringUtils.hasText(projectId)) {

                for (String registerNumber : group) {

                    String key = groupId + ":" + registerNumber;
                    SenderCounter senderCounter = senderCounters.get(key);
                    if (null != senderCounter) {
                        if (!senderCounter.isOnline()) {
                            offlineCount++;
                        }
                        monitorCount += senderCounter.getMonitorCount().get();
                        faultCount += senderCounter.getFaultCount().get();
                        staticCount += senderCounter.getStaticCount().get();
                        list.add(senderCounter);
                    }

                }
            } else {
                for (String registerNumber : group) {

                    //项目存在才统计
                    if (listCode.contains(registerNumber)) {
                        String key = groupId + ":" + registerNumber;
                        SenderCounter senderCounter = senderCounters.get(key);
                        if (null != senderCounter) {
                            if (!senderCounter.isOnline()) {
                                offlineCount++;
                            }
                            monitorCount += senderCounter.getMonitorCount().get();
                            faultCount += senderCounter.getFaultCount().get();
                            staticCount += senderCounter.getStaticCount().get();
                            list.add(senderCounter);
                        }
                    }

                }
            }

        }
        result.put("size", list.size());
        result.put("offlineCount", offlineCount);
        result.put("onlineCount", list.size() - offlineCount);
        result.put("totalCount", monitorCount + faultCount + staticCount);
        result.put("senderCounters", list);
        return result;
    }
    // CHECKSTYLE:ON

    /**
     * 更新失效得信息
     */
    public static void updateTtl(long ttl) {
        for (String key : senderCounters.keySet()) {
            SenderCounter counter = senderCounters.get(key);
            if (System.currentTimeMillis() - counter.getLastTime().getTime() > ttl) {
                counter.setOnline(false);
            }
        }
    }

    /**
     * 单梯的统计
     */
    public static Map<String, SenderCounter> detail(String registerNumber) {
        return senderDayCounters.get(registerNumber);
    }

    /**
     * 获取超时后一直未推送信息的分组
     * n 分钟
     */
    public static List<String> ttlGroupIds(long n) {
        // 超时时间分割线
        long ttlTime = System.currentTimeMillis() - n * 60 * 1000L;

        List<String> list = Lists.newArrayList();
        for (String groupId : idleTimes.keySet()) {
            long lastTime = idleTimes.get(groupId);
            if (ttlTime > lastTime) {
                list.add(groupId + ":" + lastTime);
            }
        }
        return list;
    }
}

