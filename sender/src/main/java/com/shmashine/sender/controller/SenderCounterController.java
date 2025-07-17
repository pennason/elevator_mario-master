package com.shmashine.sender.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.common.model.Result;
import com.shmashine.sender.log.SenderCounter;
import com.shmashine.sender.log.SenderCounterServer;
import com.shmashine.sender.platform.city.shanghai.LinGangSender;
import com.shmashine.sender.server.metrics.PrometheusMetricsService;

/**
 * 统计相关
 */
@RestController
@RequestMapping("/counter")
public class SenderCounterController {

    @Autowired
    private SenderCounterServer senderCounterServer;

    @Autowired
    private LinGangSender linGangSender;

    @Autowired
    private PrometheusMetricsService metricsService;

    /**
     * 根据分组查看推送统计情况
     *
     * @param groupId 分组ID
     * @return obj
     */
    @GetMapping("/group/{groupId}")
    public Object total(@PathVariable String groupId) {
        Map map = SenderCounterServer.count(groupId);
        return Result.success(map, "success!");
    }

    /**
     * 根据分组查看推送统计情况， 只看离线和总数
     *
     * @param groupId 分组ID
     * @return obj
     */
    @GetMapping("/group-offline/{groupId}")
    public Object offlineTotal(@PathVariable String groupId) {
        var map = SenderCounterServer.offlineCount(groupId);
        metricsService.statisticsOfflineElevators();
        return Result.success(map, "success!");
    }


    @GetMapping("/group-offline-project/{groupId}")
    public Object offlineTotalProject(@PathVariable String groupId) {
        var map = SenderCounterServer.offlineCountByProject(groupId);
        metricsService.statisticsOfflineProjectElevators();
        return Result.success(map, "success!");
    }

    /**
     * 根据分组查看推送统计情况
     *
     * @param registerNumber 电梯注册码
     * @return obj
     */
    @GetMapping("/detail/{registerNumber}")
    public Object detail(@PathVariable String registerNumber) {
        Map<String, SenderCounter> resultMap = SenderCounterServer.detail(registerNumber);
        Map<String, SenderCounter> map = new LinkedHashMap<>();
        resultMap.entrySet().stream()
                .sorted(Map.Entry.<String, SenderCounter>comparingByKey().reversed())
                .forEachOrdered(e -> map.put(e.getKey(), e.getValue()));

        return Result.success(map, "success!");


    }

    /**
     * 监控小屏统计
     *
     * @param projectId 项目ID
     * @param groupId   分组ID
     * @return obj
     */
    @GetMapping("/getLitterScreen")
    public Object getLitterScreen(@RequestParam("projectId") String projectId,
                                  @RequestParam("groupId") String groupId) {

        Map map = senderCounterServer.getLitterScreen(projectId, groupId);
        return Result.success(map, "success!");
    }


    @PostMapping("/pushBasicInformation2Lingang")
    public String pushBasicInformation2Lingang() {

        linGangSender.pushBasicInformation2Lingang();

        return "success!";
    }
}

