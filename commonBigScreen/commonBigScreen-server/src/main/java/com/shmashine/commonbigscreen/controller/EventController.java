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
import com.shmashine.commonbigscreen.service.EventService;


/**
 * 仪电故障工单接口
 *
 * @author jiangheng
 * @version V1.0 - 2022/3/11 11:08
 */
@RestController
@RequestMapping("/event")
public class EventController extends BaseRequestEntity {

    @Autowired
    private EventService eventService;

    /**
     * 根据时间统计故障工单柱状图
     */
    @PostMapping("/getEventCountByTime")
    public ResponseEntity getEventCountByTime(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(eventService.getEventCountByTime(searchFaultModule));
    }

    /**
     * 获取仪电反推故障工单
     *
     * @param searchFaultModule 请求体
     */
    @PostMapping("/getEventByPage")
    public ResponseEntity getEventByPage(@RequestBody SearchFaultModule searchFaultModule) {
        searchFaultModule.setUserId(getUserId());
        return ResponseEntity.ok(eventService.getEventByPage(searchFaultModule));
    }

    /**
     * 救援进度表
     */
    @GetMapping("/getEventRealTimeSchedule/{eventId}")
    public ResponseEntity getEventRealTimeSchedule(@PathVariable("eventId") String eventId) {
        return ResponseEntity.ok(eventService.getEventRealTimeSchedule(eventId));
    }

    /**
     * 困人救援详情
     *
     * @param faultId 故障id
     */
    @GetMapping("/getPeopleTrappedDetails/{faultId}")
    public ResponseEntity getPeopleTrappedDetails(@PathVariable("faultId") String faultId) {
        return ResponseEntity.ok(eventService.getPeopleTrappedDetails(faultId));
    }

}
