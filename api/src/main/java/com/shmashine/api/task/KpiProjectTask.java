// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shmashine.api.service.kpi.KpiProjectServiceI;
import com.shmashine.common.dto.KpiProjectIotNotifyDTO;
import com.shmashine.common.dto.KpiProjectNorthPushNotifyDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 按项目统计KPI
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/5/11 18:14
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@Component
@EnableAsync
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KpiProjectTask {
    private final KpiProjectServiceI kpiProjectService;

    //private static final Integer MIN_SEND_MESSAGE_ELEVATOR_COUNT = 4;
    private static final Integer BEGIN_SCHEDULED_HOUR = 7;

    @Scheduled(fixedDelay = 900000, initialDelay = 300000)
    public void scheduledKpiProjectIotTask() {
        log.info("KpiProjectTask-scheduledKpiProjectIotTask");
        if (!canRunScheduled()) {
            log.info("KpiProjectTask-scheduledKpiProjectIotTask-不满足执行条件，早上{}点后才执行", BEGIN_SCHEDULED_HOUR);
            return;
        }
        // 1. 获取电梯 离线，故障， 摄像头离线信息
        var kpiProjectIotEntityList = kpiProjectService.listKpiProjectIotEntity();
        if (CollectionUtils.isEmpty(kpiProjectIotEntityList)) {
            return;
        }
        // 2. 循环处理， 查询数据库中是否有该项目今日的记录， 如果有则比较，如果没有则新增
        kpiProjectIotEntityList.forEach(kpiProjectIotEntity -> {
            kpiProjectService.saveKpiProjectIotEntity(kpiProjectIotEntity);
            // 梯过少的不发消息
            /*if (kpiProjectIotEntity.getElevatorTotal() < MIN_SEND_MESSAGE_ELEVATOR_COUNT) {
                return;
            }*/
            // 创建通知内容
            var notifyDTO = kpiProjectService.buildProjectNotifyInfo(kpiProjectIotEntity);
            // 通知项目负责人
            notifyProjectPeople(notifyDTO);
        });
    }

    @Scheduled(fixedDelay = 900000, initialDelay = 240000)
    public void scheduledKpiProjectNorthPushTask() {
        log.info("KpiProjectTask-scheduledKpiProjectNorthPushTask");
        if (!canRunScheduled()) {
            log.info("KpiProjectTask-scheduledKpiProjectIotTask-不满足执行条件，早上{}点后才执行", BEGIN_SCHEDULED_HOUR);
            return;
        }
        // 1. 获取电梯 离线
        var kpiProjectNorthPushEntityList = kpiProjectService.listKpiProjectNorthPushEntity();
        if (CollectionUtils.isEmpty(kpiProjectNorthPushEntityList)) {
            return;
        }
        // 2. 循环处理， 查询数据库中是否有该项目今日的记录， 如果有则比较，如果没有则新增
        kpiProjectNorthPushEntityList.forEach(kpiProjectNorthPushEntity -> {
            kpiProjectService.saveKpiProjectNorthPushEntity(kpiProjectNorthPushEntity);
            // 梯过少的不发消息
            /*if (kpiProjectNorthPushEntity.getElevatorTotal() < MIN_SEND_MESSAGE_ELEVATOR_COUNT) {
                return;
            }*/
            // 创建通知内容
            var notifyDTO = kpiProjectService.buildProjectNorthPushNotifyInfo(kpiProjectNorthPushEntity);
            // 通知项目负责人
            notifyProjectPeople(notifyDTO);
        });
    }

    /**
     * 通知项目负责人
     *
     * @param notifyDTO 通知信息
     */
    private void notifyProjectPeople(KpiProjectIotNotifyDTO notifyDTO) {
        log.info("KpiProjectTask-notifyProjectPeople 通知项目负责人");
        // 获取项目负责人信息 1.根据项目ID获取电梯， 根据电梯ID获取负责的用户， 根据用户查询对应的微信负责人
        var wecomUserList = kpiProjectService.listWecomUserByProjectId(notifyDTO.getProjectId());
        if (CollectionUtils.isEmpty(wecomUserList)) {
            // 无项目负责人的，先发给管理员（几个开发人员）
            wecomUserList = kpiProjectService.listWecomUserAdmin();
        }
        // 将符合以上条件的记录整理成一条消息，发送给项目负责人
        kpiProjectService.notifyWecomUsers(wecomUserList, notifyDTO);
    }

    /**
     * 通知项目负责人
     *
     * @param notifyDTO 通知信息
     */
    private void notifyProjectPeople(KpiProjectNorthPushNotifyDTO notifyDTO) {
        log.info("KpiProjectTask-notifyProjectPeople 通知项目负责人");
        // 获取项目负责人信息 1.根据项目ID获取电梯， 根据电梯ID获取负责的用户， 根据用户查询对应的微信负责人
        var wecomUserList = kpiProjectService.listWecomUserByProjectId(notifyDTO.getProjectId());
        if (CollectionUtils.isEmpty(wecomUserList)) {
            // 无项目负责人的，先发给管理员（几个开发人员）
            wecomUserList = kpiProjectService.listWecomUserAdmin();
        }
        // 将符合以上条件的记录整理成一条消息，发送给项目负责人
        kpiProjectService.notifyWecomUsers(wecomUserList, notifyDTO);
    }

    /**
     * 判断是否可以执行定时任务
     *
     * @return true 可以执行
     */
    private Boolean canRunScheduled() {
        // 每天8点以后在计算统计
        var hour = java.time.LocalTime.now().getHour();
        if (hour < BEGIN_SCHEDULED_HOUR) {
            return false;
        }
        return true;
    }
}
