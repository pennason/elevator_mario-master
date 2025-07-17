package com.shmashine.api.task;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.api.service.fault.BizFaultTempService;

import lombok.extern.slf4j.Slf4j;

/**
 * 故障记录临时表-定时任务处理
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2025/3/3 14:24
 * @Since: 1.0.0
 */
@Slf4j
@Profile({"prod"})
@Component
@EnableAsync
public class FaultTempTask {

    @Resource
    private BizFaultTempService faultTempService;

    /**
     * 每日凌晨恢复困人临时表中故障
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void clearInFaultingFault() {

        log.info("恢复困人临时表中故障-开始");
        faultTempService.recoverPeopleTrappedFault();
    }

}
