package com.shmashine.api.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.api.service.elevator.BizElevatorService;
import com.shmashine.api.service.fault.BizFaultService;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务处理
 *
 * @author little.li
 */
@Slf4j
@Profile({"prod"})
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TimeTaskHandle {

    private final BizElevatorService elevatorService;
    private final BizFaultService faultService;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(8, 16, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "TimeTaskHandle");

    /**
     * 电动车二次识别没有回调再次调用识别
     */
    @Scheduled(fixedRate = 600000, initialDelay = 10000)
    public void taskReloadFaultConfirm() {

        executorService.submit((() -> {

            log.info("电动车二次识别失败再次调用识别任务start");
            faultService.taskReloadFaultConfirm();
        }));
    }

    /**
     * 校验电动车乘梯，恢复故障，5分钟一次
     */
    @Scheduled(fixedRate = 300000, initialDelay = 10000)
    @Profile({"prod"})
    public void taskReloadFaultDisappear() {

        executorService.submit(() -> {

            log.info("电动车乘梯未恢复自动恢复任务start");

            faultService.taskReloadFaultDisappear();
        });
    }

    /**
     * 定时查询电话卡流量使用（每天凌晨0，2，4，6点更新一次）
     */
    @Scheduled(cron = "0 0 0,2,4,6 * * ? ")
    public void updateIotCardInfo() {

        executorService.submit(() -> {

            log.info("流量卡使用查询任务start");

            List<String> iccidList = elevatorService.searchAllIotCardIccid();

            int batchCount = 10;
            ArrayList<String> tempList = new ArrayList<>();
            int iccidSize = iccidList.size();
            for (int i = 0; i < iccidSize; i++) {
                tempList.add(iccidList.get(i));
                if ((i + 1) % batchCount == 0 || (i + 1) == iccidSize) {
                    elevatorService.updateIotCardInfo(tempList);
                    tempList.clear();
                }
            }


        });

    }

    /**
     * 统计运行次数 每小时一次
     */
    @Scheduled(cron = "0 1 0/1 * * ? ")
    public void updateRunCount() {

        executorService.submit(() -> {

            log.info("统计每日运行次数任务start");
            elevatorService.searchAllRunCount();

        });

    }

    /**
     * 每日凌晨清除故障中故障
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void clearInFaultingFault() {

        executorService.submit(() -> {

            log.info("凌晨故障恢复任务start");
            faultService.clearInFaultingFault();

        });

    }

}
