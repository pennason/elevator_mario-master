package com.shmashine.fault;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shmashine.fault.entity.maiXinMaintenancePlatform.FaultMessageReqVO;
import com.shmashine.fault.entity.maiXinMaintenancePlatform.GovernFaultTypeEnum;
import com.shmashine.fault.entity.maiXinMaintenancePlatform.RealMessageData;
import com.shmashine.fault.message.MessageHandle;
import com.shmashine.fault.utils.MaiXinMaintenancePlatformUtil;

/**
 * 维保平台推送测试
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/3/7 11:42
 */
@ActiveProfiles("dev")
@SpringBootTest
public class MaiXinMaintenancePlatformPushTest {

    private static final String MESSAGE =
            "{\"elevatorCode\":\"MXCS123123\",\"ST\":\"add\",\"D\":\"vUbpZQEeAAAAAQABBQAAAAAAAAoMK1k=\","
                    + "\"TY\":\"SensorFault\",\"sensorType\":\"SINGLEBOX\",\"fault_type\":\"8\","
                    + "\"faultId\":\"8454031169813413888\",\"time\":\"2024-03-07 15:46:54\",\"faultName\":\"非平层关人\"}";

    @Autowired
    private MessageHandle messageHandle;

    @Test
    void testPushFaultMessage() {

        String elevatorCode = "MXCS123123";
        String registerNumber = "356585825445874123";
        String faultId = "2024030711483500";
        String address = "上海市浦东新区海科路";
        String floor = "1";
        Date reportTime = new Date();


        RealMessageData data = RealMessageData.builder()
                .serviceMode(1).floorPosition(floor)
                .collectionTime(reportTime).build();

        FaultMessageReqVO faultMessageReqVO = FaultMessageReqVO.builder()
                .platformCode("MX201").elevatorCode(elevatorCode).registerNumber(registerNumber)
                .equCode(registerNumber).alarmId(faultId).faultStatus(0)
                .alarmChannel("S04").faultTypeEnum(GovernFaultTypeEnum.ENTRAP2)
                .faultDescription(GovernFaultTypeEnum.ENTRAP2.getFaultName()).address(address)
                .floor(floor).occurTime(reportTime).currentTime(new Date())
                .data(data).build();

        MaiXinMaintenancePlatformUtil.pushEmergencyRescue(faultMessageReqVO);

    }

    @Test
    public void consumerFaultMessage() {
        messageHandle.preFaultMessageHandle(MESSAGE);
    }
}
