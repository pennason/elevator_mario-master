// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.shmashine.api.task.GroupLeasingTask;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/2/17 16:17
 * @since v1.0
 */

@Profile("local")
@SpringBootTest
public class GroupLeasingTest {
    @Autowired
    private GroupLeasingTask task;

    @Test
    public void testScheduledGroupLeasingStatisticsTask() {
        task.scheduledGroupLeasingStatisticsTask();
    }


    @Test
    public void testDoGroupLeasingStatisticsFloor() {
        var elevatorCode = "MX4832";
        var yesterday = "2023-02-16";
        task.doGroupLeasingStatisticsFloor(elevatorCode, yesterday);
    }

    @Test
    public void testDoGroupLeasingStatisticsElevator() {
        var elevatorCode = "MX4832";
        var yesterday = "2023-02-16";
        task.doGroupLeasingStatisticsElevator(elevatorCode, yesterday);
    }

    @Test
    public void testDoGroupLeasingStatisticsVillage() {
        var yesterday = "2023-02-16";
        task.doGroupLeasingStatisticsVillage(yesterday);
    }

    @Test
    public void testDoGroupLeasingStatisticsFloorLevel() {
        var yesterday = "2023-03-02";
        task.doGroupLeasingStatisticsFloorLevel(yesterday);
        task.doGroupLeasingStatisticsElevatorLevel(yesterday);
    }

    @Test
    public void testDoGroupLeasingStatisticsElevatorLevel() {
        var yesterday = "2023-03-02";
        task.doGroupLeasingStatisticsElevatorLevel(yesterday);
    }
}
