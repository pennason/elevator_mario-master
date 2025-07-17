package com.shmashine.fault.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.fault.task.service.BizEnventNoticTaskServer;

/**
 * 时间通知任务
 */
@Profile({"prod"})
@EnableScheduling
public class EnventNoticeTask {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2,
            4, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "EnventNoticeTask");

    @Autowired
    private BizEnventNoticTaskServer bizEnventNoticTaskServer;

    // 昨天故障/不文明行为发生总数最多电梯
    @Scheduled(cron = "0 0 4 * * ?")
    private void configureTasks() {

        executorService.submit(() -> bizEnventNoticTaskServer.saveYesterdayElevatorFaultStatics());

    }

    // 昨天故障/不文明行为发生总数最多电梯

    // 最近30反正发生故障的梯，且处于故障状态

}