// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.test;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.alibaba.fastjson2.JSON;
import com.shmashine.api.task.NightWatchTask;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/16 17:01
 * @since v1.0
 */

@Profile("test")
@SpringBootTest
public class NightWatchTest {
    @Autowired
    NightWatchTask nightWatchTask;

    @Test
    public void testDoNightWatchTask() {
        var data = new HashMap<String, String>();
        data.put("time", "2023-02-16 16:32:55");
        data.put("elevatorCode", "MX3890");
        data.put("hasPeople", "0");
        nightWatchTask.doNightWatchTask(JSON.toJSONString(data));

    }

}
