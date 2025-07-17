package com.shmashine.fault;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.fault.message.handle.DefaultFaultHandle;
import com.shmashine.fault.message.handle.FaultHandle;
import com.shmashine.hkcamerabyys.client.entity.DownLoadByHKYSRequestBody;

@ActiveProfiles("dev")
@SpringBootTest
class FaultApplicationTests {

    @Autowired
    FaultHandle faultHandle;

    @Autowired
    DefaultFaultHandle defaultFaultHandle;


    @Test
    void contextLoads() {
        JSONObject messageJson = new JSONObject();
        messageJson.put("faultId", "text001");
        messageJson.put("elevatorCode", "MX3737");
        messageJson.put("fault_type", "38");
        messageJson.put("fault_stype", "202");
        messageJson.put("ST", "add");
        messageJson.put("sensorType", 1);
        faultHandle.addFault(messageJson);
    }

    @Test
    void testhkCameraByYS() {
        DownLoadByHKYSRequestBody downLoadByHKYSRequestBody = new DownLoadByHKYSRequestBody();

        downLoadByHKYSRequestBody.setDeviceSerial("G16987203");
        downLoadByHKYSRequestBody.setElevatorCode("MX3757");
        downLoadByHKYSRequestBody.setFaultId("123465777777");
        downLoadByHKYSRequestBody.setFaultType("37");
        downLoadByHKYSRequestBody.setOccurTime(new Date());
        downLoadByHKYSRequestBody.setSType("add");

        //下载视频
        //hikEzvizClient.downloadVideoFile(downLoadByHKYSRequestBody);
        //下载图片
        //hikEzvizClient.downloadPictureFile(downLoadByHKYSRequestBody);
    }


    /**
     * 测试短信
     */
    @Test
    void faultNotify() {
        //defaultFaultHandle.faultNotify("text001", "平层困人", "MX4158", "2024-04-16 11:42:00", "1", "7");
    }

}
