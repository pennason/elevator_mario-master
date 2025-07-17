package com.shmashine.commonbigscreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.commonbigscreen.entity.BaseRequestEntity;
import com.shmashine.commonbigscreen.entity.SearchFaultModule;
import com.shmashine.commonbigscreen.service.FaultService;


/**
 * 故障接口
 *
 * @author jiangheng
 * @version 1.0 - 2022/3/7 11:05
 */
@RestController
@RequestMapping("/fault")
public class FaultController extends BaseRequestEntity {

    @Autowired
    private FaultService faultService;

    /**
     * 获取故障统计
     *
     * @param searchFaultModule 查询参数
     */
    @PostMapping("/getFaultCount")
    public ResponseEntity getFaultCount(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(faultService.getFaultCount(searchFaultModule));
    }

    /**
     * 根据小区统计急修、隐患、电瓶车入梯
     *
     * @param searchFaultModule 查询参数
     */
    @PostMapping("/getFaultCountByVillage")
    public ResponseEntity getFaultCountByVillage(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(faultService.getFaultCountByVillage(searchFaultModule));
    }

    /**
     * 根据时间获取故障柱状图
     *
     * @param searchFaultModule 查询参数
     */
    @PostMapping("/getFaultChartByTime")
    public ResponseEntity getFaultChartByTime(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(faultService.getFaultChartByTime(searchFaultModule));
    }

    /**
     * 事件回溯
     */
    @PostMapping("/getHistoryRecordCount")
    public ResponseEntity getHistoryRecordCount(@RequestBody SearchFaultModule searchFaultModule) {
        return ResponseEntity.ok(faultService.getHistoryRecordCount(searchFaultModule));
    }

    /**
     * 获取故障取证文件
     */
    @GetMapping("/getFaultFileById/{faultId}")
    public ResponseEntity getFaultFileById(@PathVariable("faultId") String faultId) {
        return ResponseEntity.ok(faultService.getFaultFileById(faultId));
    }
}
