package com.shmashine.fault.task;


import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.SendMessageUtil;
import com.shmashine.fault.elevator.service.TblElevatorService;
import com.shmashine.fault.fault.service.TblFaultServiceI;
import com.shmashine.fault.user.entity.TblSysUser;
import com.shmashine.fault.user.service.TblSysUserServiceI;

import lombok.extern.slf4j.Slf4j;

/**
 * SendReportTask
 *
 * @author jiangheng
 * @version V1.0.0 -  2021/5/8 10:46
 */

@Slf4j
@Profile({"prod"})
@EnableScheduling
@EnableAsync
@Component
public class SendReportTask {

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(2,
            4, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "SendReportTask");

    @Autowired
    private TblSysUserServiceI tblSysUserServiceI;

    @Autowired
    TblElevatorService tblElevatorService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource
    private TblFaultServiceI tblFaultServiceI;

    /**
     * 每天8:00执行一次，短信发送一次短信通知
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void autoCleanDownloadingVideo() {

        executorService.submit(() -> {

            //拿到所有西子扶梯管理员用户
            List<TblSysUser> userList = tblSysUserServiceI.getAllPrincipal();

            for (TblSysUser user : userList) {

                String telStr = user.getVMobile();

                //是否配置手机号
                if (StringUtils.isNotBlank(telStr)) {
                    String[] tels = telStr.split(",");
                    for (String tel : tels) {
                        // 是否接收故障短信 0：接收，1：不接收
                        if (user.getISendMessageStatus() == 0) {

                            //拿到该用户所有前一天发生故障的电梯
                            List<String> elevatorCodes = tblElevatorService.getFaultElevator(user);
                            elevatorCodes.toString();
                            //发送故障统计短信
                            SendMessageUtil.sendFaultElevatorCountMessage(tel, elevatorCodes);
                        }

                    }

                }

            }

        });

    }

    /**
     * 设备离线告警(十分钟查询一次)
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void deviceTimeoutEvent() {

        executorService.submit(() -> {

            long nowTime = System.currentTimeMillis() / 1000;
            Set<String> set = stringRedisTemplate.opsForZSet().rangeByScore(RedisConstants.TIMEOUT_DEVICE, 0, nowTime);

            for (String value : set) {
                tblFaultServiceI.addDeviceTimeOutFault(value);
                stringRedisTemplate.opsForZSet().remove(RedisConstants.TIMEOUT_DEVICE, value);
            }

        });

    }

    /**
     * 传感器故障check（20：00 传感器故障次数未到3次恢复，3次延续故障）
     */
    @Scheduled(cron = "0 0 20 * * ? ")
    public void checkSensorFaultIsContinue() {

        executorService.submit(() -> tblFaultServiceI.checkSensorFaultIsContinue());

    }
}
