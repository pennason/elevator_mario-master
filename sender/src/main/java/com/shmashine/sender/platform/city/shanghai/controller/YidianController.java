package com.shmashine.sender.platform.city.shanghai.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.message.FaultMessage;
import com.shmashine.common.utils.DateUtils;
import com.shmashine.sender.platform.city.shanghai.PostResponse;
import com.shmashine.sender.platform.city.shanghai.YidianSender;
import com.shmashine.sender.server.elevator.BizElevatorService;


/**
 * 仪电梯厂过检手动推送接口
 *
 * @author yanjie.wu
 */
@RestController
@RequestMapping("/yidian")
public class YidianController {

    @Autowired
    private YidianSender yidianSender;
    @Autowired
    private BizElevatorService bizElevatorService;

    @GetMapping("/manufacturer/new/{code}")
    public Object getAuthNew(@PathVariable String code) {
        // 统计信息，实时信息，机柜编码和统一信用代码
        TblElevator elevator = bizElevatorService.getByElevatorCode(code);
        return yidianSender.postStatisticsData(elevator);
    }

    @GetMapping("/manufacturer/exist/{code}/{faultCode}")
    public Object getAuthExist(@PathVariable("code") String code, @PathVariable("faultCode") String faultCode) {
        Map<String, PostResponse> result = new HashMap<>();
        TblElevator elevator = bizElevatorService.getByElevatorCode(code);
        // 统计信息
        //        result.put("statistics",yidianSender.postStatisticsData(elevator));
        // 告警信息
        FaultMessage faultMessage = getFaultMessage(code, faultCode);
        result.put("fault", yidianSender.postFaultData(faultMessage));
        return result;
    }

    private FaultMessage getFaultMessage(String code, String faultCode) {
        if (StringUtils.isBlank(faultCode)) {
            faultCode = "38";
        }
        String message = "{\"elevatorCode\":\"MX3772\",\"ST\":\"add\",\"D\":\"TPk0XwEVAAEAAQAAAQAAAAEAABk=\","
                + "\"TY\":\"Fault\",\"sensorType\":\"CarRoof\",\"fault_type\":38,"
                + "\"failureId\":\"7982167356459523933\",\"time\":\"2020-10-27 12:00:52\",\"faultName\":\"梯内跳动/打闹\"}";
        // 根据faultType拉取redis中的故障报文
        FaultMessage faultMessage = JSON.parseObject(message, FaultMessage.class);
        faultMessage.setElevatorCode(code);
        faultMessage.setFault_type(faultCode);
        //        faultMessage.setFaultId(SnowFlakeUtils.nextStrId());
        faultMessage.setFaultId("7998117072570744832");
        faultMessage.setTime(DateUtils.getStringDate(DateUtils.DATE_TIME_PATTERN));
        return faultMessage;
    }
}
