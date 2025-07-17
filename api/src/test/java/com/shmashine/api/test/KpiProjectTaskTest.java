// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.shmashine.api.ApiApplication;
import com.shmashine.api.task.KpiProjectTask;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/12/6 10:23
 * @since v1.0
 */

@Profile("prod")
@SpringBootTest(classes = ApiApplication.class)
public class KpiProjectTaskTest {
    @Autowired
    KpiProjectTask kpiProjectTask;

    @Test
    public void testScheduledKpiProjectTask() {
        kpiProjectTask.scheduledKpiProjectIotTask();
        kpiProjectTask.scheduledKpiProjectNorthPushTask();
    }
}
