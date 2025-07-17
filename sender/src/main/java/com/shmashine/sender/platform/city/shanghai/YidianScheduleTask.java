package com.shmashine.sender.platform.city.shanghai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.common.entity.TblElevator;
import com.shmashine.common.entity.TblFaultSendLog;
import com.shmashine.sender.server.elevator.BizElevatorService;
import com.shmashine.sender.server.fault.TblFaultSendLogService;
import com.shmashine.sender.server.ruijin.TblThridPartyRuijinCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */
@Slf4j
@Component
@Profile({"prod"})
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class YidianScheduleTask {

    private static final String PT_CODE = "yidian";

    @Autowired
    BizElevatorService bizElevatorService;

    @Autowired
    YidianSender yidianSender;

    @Autowired
    YidianRuiJinServer yidianRuiJinServer;

    @Autowired
    private TblThridPartyRuijinCheckService tblThridPartyRuijinCheckService;

    @Autowired
    TblFaultSendLogService tblFaultSendLogService;

    @Autowired
    YidianHttpUtil yidianHttpUtil;

    /**
     * 同步电梯运行统计信息到仪电平台
     */
    @Async
    @Scheduled(cron = "0 0 6 * * ?")//每天4,8,12,16点执行
    public void postStatisticsData() {
        // 查询 仪电的设备
        List<TblElevator> elevatorList = bizElevatorService.getByPtCode(PT_CODE);
        for (TblElevator elevator : elevatorList) {
            // 发送统计数据报文
            yidianSender.postStatisticsData(elevator);
        }
    }

    /**
     * 同步瑞金项目电梯基本信息
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")//每天凌晨12点执行
    public void updateThirdPartyRuiJinElevatorTask() {
        // 电梯基本信息缓存
        yidianRuiJinServer.updateThirdPartyRuiJinElevator();
        log.info("updateThirdPartyRuiJinElevatorTask>>>>>电梯基本信息更新完成");
    }


    /**
     * 同步瑞金项目电梯检验信息
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行
    public void updateThridPartyRuiJinCheckTask() {
        yidianRuiJinServer.updateThridPartyRuiJinCheck();
        // 电梯检验信息
        /*long totalPage = yidianRuiJinServer.getTotalPage(DEFAULT_LIFT_INSPECTS_URL);
        for (int i = 0; i < totalPage; i++) {
            yidianRuiJinServer.updateThridPartyRuiJinCheckByPage(i);
        }*/
        log.info("updateThridPartyRuiJinCheckTask>>>>>+电梯检验信息已完成");
    }

    @Async
    @Scheduled(cron = "0 30 1 * * ?")//每天凌晨1点30分执行
    public void updateElevatorInspectDateTask() {
        // 更新电梯检验年检信息
        List<String> elevatorList = bizElevatorService.getElevatorRegisterNumerIsNotNull();
        for (String registerNumber : elevatorList) {
            var check = tblThridPartyRuijinCheckService.getElevatorLastCheckInfoByRegisterNumer(registerNumber);
            if (null != check) {
                bizElevatorService.updateInspectDate(check.getRegisterNumber(), check.getInspectDate(),
                        check.getNextInspectDate());
            }
        }
        log.info("updateElevatorInspectDateTask>>>>>+" + elevatorList.size() + "电梯年检信息完成更新");
    }

    /**
     * 同步瑞金项目电梯维保信息
     */
    @Async
    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点执行
    public void updateThirdPartyRuiJinMaintenanceTask() {
        // 电梯维保信息
        /*long totalPage = yidianRuiJinServer.getTotalPage(DEFAULT_LIFT_MAIN_PENANCE_USES_URL);
        for (int i = 0; i < totalPage; i++) {
            yidianRuiJinServer.updateThirdPartyRuiJinMaintenanceByPage(i);
        }*/
        yidianRuiJinServer.updateThirdPartyRuiJinMaintenance();
        log.info("updateThirdPartyRuiJinMaintenanceTask>>>>>电梯维保信息完成更新");
    }

    /**
     * 同步瑞金项目电梯工单信息
     */
    @Async
    @Scheduled(cron = "0 0 3 * * ?")//每天凌晨3点执行
    public void updateThirdPartyRuiJinWorkOrderTask() {
        /*long totalPage = yidianRuiJinServer.getTotalPage(StrUtil.format(DEFAULT_MAINTENANCE_RECORDS_URL,
                DateUtil.lastMonth().getTime()));
        if (totalPage > 0) {
            for (int page = 0; page < totalPage; page++) {
                yidianRuiJinServer.updateThirdPartyRuiJinWorkOrderByPage(page);
            }
        }*/
        yidianRuiJinServer.updateThirdPartyRuiJinWorkOrder();
        log.info("updateThirdPartyRuiJinMaintenanceTask>>>>>+电梯工单信息完成更新");
    }

    /**
     * 故障推送失败重新推送（5分钟执行一次）
     */
    @Async
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void retryPushFaultData() {

        log.info("retryPushFaultData>>>>>" + "故障重新推送开始");
        //获取最近一小时推送失败的记录
        List<TblFaultSendLog> needRetryPushFaults = tblFaultSendLogService.getNeedRetryPushFaults();

        needRetryPushFaults.forEach(fault -> {

            String responseMsg = "未知响应！";
            try {
                PostResponse response = yidianHttpUtil.send(fault.getRegisterNo(), "fault",
                        YidianSender.DEFAULT_ALARM_URL, fault.getSendMessage());

                responseMsg = response.getMessage();

                log.info("故障重新推送，registerNo：{}，sendMsg:{}, resp:{}",
                        fault.getRegisterNo(), fault.getSendMessage(), response.getMessage());

            } catch (Exception e) {
                log.error("故障重新推送失败，registerNo：{}，sendMsg:{}，error:{}", fault.getRegisterNo(),
                        fault.getSendMessage(), e.getMessage());
            }

            fault.setResponseMessage(responseMsg);

            //更新推送日志
            tblFaultSendLogService.updateById(fault);
        });

        log.info("retryPushFaultData>>>>>" + "故障重新推送完成");
    }

}