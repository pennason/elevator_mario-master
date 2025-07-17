// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shmashine.sender.message.handle.PeriodicHandle;
import com.shmashine.sender.platform.city.tools.CityTool;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/6/24 18:28
 * @since v1.0
 */

@ActiveProfiles("test")
@DisplayName("昆山电信推送")
@SpringBootTest
public class SenderTelecomKunshanTest {

    @Autowired
    CityTool cityTool;
    @Autowired
    PeriodicHandle periodicHandle;

    @Test
    public void testSendPeriodic() {
        //var message = "{\"door_times\":1,\"elevatorCode\":\"MX3847\",\"ST\":\"update_log\",\"level_off_times\":0,\"run_time\":0,\"TY\":\"TR\",\"sensorType\":\"CarRoof\",\"bend_count\":1,\"protocalVersion\":\"default\",\"time\":\"2024-06-24 18:09:26\",\"run_count\":1,\"run_distance\":\"88.1\"}";
        //periodicHandle.handle(message);

    }
}
