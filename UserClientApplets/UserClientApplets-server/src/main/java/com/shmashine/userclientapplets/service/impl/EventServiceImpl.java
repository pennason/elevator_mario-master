package com.shmashine.userclientapplets.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shmashine.userclientapplets.client.RemoteApiClient;
import com.shmashine.userclientapplets.dao.EventDao;
import com.shmashine.userclientapplets.entity.Elevator;
import com.shmashine.userclientapplets.entity.Event;
import com.shmashine.userclientapplets.entity.SearchFaultModule;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.service.EventService;
import com.shmashine.userclientapplets.service.FaultService;

import lombok.RequiredArgsConstructor;


/**
 * EventServiceImpl
 *
 * @author jiangheng
 * @version V1.0。0 - 2022/2/15 14:35
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EventServiceImpl extends ServiceImpl<EventDao, Event> implements EventService {

    private final FaultService faultService;
    private final ElevatorService elevatorService;
    private final RemoteApiClient remoteApiClient;

    @Override
    public Event getEventById(String eventNumber) {

        Event event = getOne(new QueryWrapper<Event>()
                .select("event_desc", "handler", "handler_tel", "reporter", "register_number")
                .and(wrapper -> wrapper.eq("event_number", eventNumber)
                        .or().like("reporter", eventNumber)).last("limit 1"));

        String faultId = event.getReporter().substring(5);

        Elevator elevator = elevatorService.getOne(new QueryWrapper<Elevator>().select("v_elevator_name")
                .eq("v_equipment_code", event.getRegisterNumber()));

        HashMap fault = (HashMap) faultService.getFaultById(faultId, event).get("info");

        event.setFaultId(faultId);
        if (fault != null) {
            event.setVideoUrl((String) fault.get("videoUrl"));
            ArrayList<String> imageList = ((ArrayList) fault.get("imageList"));
            event.setImageUrl(imageList.size() > 0 ? imageList.get(0) : null);
            HashMap<String, Object> detail = (HashMap) fault.get("detail");
            event.setModeStatusName((String) detail.get("i_mode_status_name"));
            event.setAddress((String) detail.get("v_address"));
            event.setStatusName((String) detail.get("i_status_name"));
            event.setReportTime((String) detail.get("dt_report_time"));
            event.setEndTime((String) detail.get("dt_end_time"));
        }

        if (elevator != null) {
            event.setElevatorName(elevator.getVElevatorName());
        }

        return event;
    }

    @Override
    public HashMap queryRepairByPage(SearchFaultModule searchFaultModule) {
        var jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
        jsonObject.put("dtReportTime", searchFaultModule.getStartTime());
        jsonObject.put("dtEndTime", searchFaultModule.getEndTime());
        return remoteApiClient.queryFaultList(jsonObject, searchFaultModule.getUserId());
    }

    @Override
    public Event getEventByFaultId(String faultId) {
        return getOne(new QueryWrapper<Event>()
                .select("event_number")
                .like("reporter", faultId).last("limit 1"));
    }

    @Override
    public List<Map> getEventStatus(String faultId) {
        return baseMapper.getEventStatusByfaultId(faultId);
    }

    @Override
    public HashMap getUnAnnualInspectionElevatorByPage(SearchFaultModule searchFaultModule) {
        var jsonObject = JSON.parseObject(JSON.toJSONString(searchFaultModule), JSONObject.class);
        return remoteApiClient.getUnAnnualInspectionElevatorByPage(jsonObject, searchFaultModule.getUserId());
    }
}
