package com.shmashine.fault;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.fault.message.handle.DefaultFaultHandle;
import com.shmashine.fault.message.handle.PreFaultHandle;

/**
 * 发送短信测试
 */
@ActiveProfiles("dev")
@SpringBootTest
public class SendEntrapSMSTest {

    /*
     *${type}困人: ${villageName}的${elevatorName}在${floor}层，于${reportTime}发生困人，请及时救援!
     */

    @Autowired
    private DefaultFaultHandle defaultFaultHandle;

    @Autowired
    private PreFaultHandle preFaultHandle;

    /**
     * 已确认故障
     */
    @Test
    void test1() {

        String message = "{\"elevatorCode\":\"MX3737\",\"ST\":\"add\","
                + "\"D\":\"ExgEXwEOAAAAAQAAEgBgAAAAABo=\",\"TY\":\"Fault\""
                + ",\"sensorType\":\"CarRoof\",\"fault_type\":7,\"faultId\":\"1280390375395176448\""
                + ",\"time\":\"2020-07-07 14:37:08\",\"faultName\":\"平层困人\"}";

        JSONObject messageJson = JSONObject.parseObject(message);
        defaultFaultHandle.addFault(messageJson);
    }

    /**
     * 困人审核非平层
     */
    @Test
    void test2() {
        String message = "{\"elevatorCode\":\"MX3737\",\"ST\":\"add\","
                + "\"D\":\"ExgEXwEOAAAAAQAAEgBgAAAAABo=\",\"TY\":\"Fault\","
                + "\"sensorType\":\"CarRoof\",\"fault_type\":8,\"faultId\":\"1280390375395176448\""
                + ",\"time\":\"2023-11-30 14:37:08\",\"faultName\":\"非平层困人\"}";

        JSONObject messageJson = JSONObject.parseObject(message);
        preFaultHandle.addFault(messageJson);
    }


}
