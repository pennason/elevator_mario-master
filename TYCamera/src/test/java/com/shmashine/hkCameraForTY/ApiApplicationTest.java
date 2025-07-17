package com.shmashine.hkCameraForTY;


import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.hkCameraForTY.service.HkVideoService;

/**
 * 测试类
 * @author: jiangheng
 * @version: 1.0
 * @date: 2021/6/29 14:28
 */

@SpringBootTest
public class ApiApplicationTest {

    @Resource
    HkVideoService hkVideoService;

    @Test
    public void test() {
        start("123456789", "MX3829", 11);
    }

    public void start(String workOrderId, String elevatorCode, int faultType) {

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, -15);
        Date startTime = calendar.getTime();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, +45);

        Date endTime = calendar.getTime();
        //困人故障取证15分钟/其他故障取证1分钟
        if ("7".equals(faultType) || "8".equals(faultType)) {
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, -60);
            startTime = calendar.getTime();
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, +(14 * 60));
            endTime = calendar.getTime();
        }

        //视频下载上传
        hkVideoService.hkVideoDownload(workOrderId, elevatorCode, startTime, endTime, faultType);

    }
}
