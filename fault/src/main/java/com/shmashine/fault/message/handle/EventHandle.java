package com.shmashine.fault.message.handle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.fault.elevator.entity.TblElevator;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.entity.TblElevatorEvent;
import com.shmashine.fault.fault.entity.TblFault;
import com.shmashine.fault.fault.service.EventService;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.user.entity.TblSysUser;
import com.shmashine.fault.user.service.TblSysUserServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * 模式切换处理
 *
 * @author jiangheng
 * @version V1.0.0 - 2021/5/6 10:53
 */
@Slf4j
@Component
public class EventHandle {

    private final TblFaultServiceI faultService;

    private final TblElevatorService elevatorService;

    private final TblSysUserServiceI userService;

    private final EventService eventService;

    @Autowired
    public EventHandle(TblElevatorService elevatorService, TblSysUserServiceI userService,
                       TblFaultServiceI faultService, EventService eventService) {
        this.elevatorService = elevatorService;
        this.userService = userService;
        this.faultService = faultService;
        this.eventService = eventService;
    }

    public void eventChange(JSONObject messageJson) {

        //电梯状态  0正常，1检修
        int eventType = Integer.parseInt(messageJson.getString("event_type"));

        //获取电梯当前模式
        TblElevator elevator = elevatorService.getByElevatorCode(messageJson.getString("elevatorCode"));

        //不为当前模式，则发送短信通知
        if (eventType != elevator.getIModeStatus()) {

            //通过电梯编号，查找故障中的故障记录
            List<TblFault> inFaults = faultService.getInFault(elevator.getVElevatorCode());

            TblElevatorEvent tblElevatorEvent = new TblElevatorEvent();
            Integer status = 0;

            if (inFaults != null && inFaults.size() > 0) {
                tblElevatorEvent.setFaultType(Integer.parseInt(inFaults.get(0).getIFaultType()));
                tblElevatorEvent.setFaultName(inFaults.get(0).getVFaultName());
                status = 1;
            }
            tblElevatorEvent.setElevatorCode(elevator.getVElevatorCode());
            tblElevatorEvent.setEventType(eventType);
            tblElevatorEvent.setStatus(status);
            tblElevatorEvent.setCreatTime(new Date());
            //记录当前模式以及梯的状态
            eventService.insert(tblElevatorEvent);

            //修改当前电梯模式状态
            elevatorService.updateModeStatus(elevator.getVElevatorCode(), eventType);
            //获取当前时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            //发送短信
            eventNotify(eventType, elevator.getVElevatorCode(), time);
        }

    }

    /**
     * 模式切换时 发短信
     *
     * @param eventType    模式切换类型
     * @param elevatorCode 电梯编号
     */
    private void eventNotify(Integer eventType, String elevatorCode, String time) {

        TblElevator elevator = elevatorService.getByElevatorCode(elevatorCode);
        if (elevator == null) {
            return;
        }

        // 获取该电梯的负责人
        List<TblSysUser> userList = userService.queryElevatorPrincipal(elevator.getVElevatorId());

        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        for (TblSysUser user : userList) {
            String telStr = user.getVMobile();
            if (StringUtils.isNotBlank(telStr)) {
                String[] tels = telStr.split(",");
                for (String tel : tels) {
                    // 是否接收故障短信 0：接收，1：不接收
                    if (user.getISendMessageStatus() == 0) {
                        // 多长时间内发送同一手机号不允许超过多少次（边界情况：传感器坏了，开关一直处于active）
                        SendMessageUtil.sendEventChangeMessage(tel, elevatorCode, elevator.getVAddress(),
                                eventType, time);
                    }

                }

            }
        }
    }
}
