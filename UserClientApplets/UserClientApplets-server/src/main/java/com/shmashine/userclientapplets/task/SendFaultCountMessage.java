package com.shmashine.userclientapplets.task;

import static com.shmashine.userclientapplets.constants.SmsConstants.PUSH_BATTERY_CAR_COUNT_TEMPLATE;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.shmashine.userclientapplets.entity.Elevator;
import com.shmashine.userclientapplets.entity.Fault;
import com.shmashine.userclientapplets.entity.WeChatUser;
import com.shmashine.userclientapplets.service.ElevatorService;
import com.shmashine.userclientapplets.service.FaultService;
import com.shmashine.userclientapplets.service.VillageService;
import com.shmashine.userclientapplets.service.WeChatLoginService;
import com.shmashine.userclientapplets.utils.SmsUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送故障统计短信
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/4/15 14:37
 * @Since: 1.0.0
 */
@Slf4j
@Profile({"prod"})
@EnableScheduling
@Component
public class SendFaultCountMessage {

    @Resource
    private WeChatLoginService weChatService;

    @Resource
    private ElevatorService elevatorService;

    @Resource
    private VillageService villageService;

    @Resource
    private FaultService faultService;

    @Resource
    private SmsUtils smsUtils;

    /**
     * 8点、12点、18点发送电瓶车统计短信
     * 模板CODE
     * SMS_465600545
     * 模板内容
     * 您关注的${village}小区，物联网电梯共计${elevatorNum}部，${time}，有${elevatorFaultNum}部电梯出现电动车入梯状况，共${faultNum}次。
     */
    @Scheduled(cron = "0 0 8,12,18 * * ?")
    public void taskSendFaultCountMessage() {

        //获取当前时间点
        DateTime now = DateTime.now();
        int hour = DateUtil.hour(now, true);

        // 获取已配置推送用户
        List<WeChatUser> users = weChatService.getPushBatteryCarUsers(hour);

        if (CollectionUtils.isEmpty(users)) {
            return;
        }

        users.forEach(user -> {

            //获取该用户对应梯列表
            List<Elevator> elevators = elevatorService.getUserElevatorList(user.getUserId());

            if (CollectionUtils.isEmpty(elevators)) {
                return;
            }

            //电梯数
            long elevatorNum = elevators.stream().count();

            //小区||小区数量
            List<String> villageIds = elevators.stream().map(Elevator::getVillageId)
                    .distinct().collect(Collectors.toList());

            String village;
            if (villageIds.size() > 1) {
                village = villageIds.size() + "个";
            } else {
                village = villageService.getVillageNameByVillageId(villageIds.get(0));
            }

            DateTime yesterday = DateUtil.offsetDay(now, -1);
            String selectStartTime = DateUtil.beginOfDay(yesterday).toString();
            String selectEndTime = DateUtil.endOfDay(yesterday).toString();

            //电瓶车告警列表
            List<Fault> faultList = faultService.getFaultList(
                    elevators.stream().map(Elevator::getVElevatorId).collect(Collectors.toList()),
                    selectStartTime, selectEndTime, "37");

            int faultNum;
            long elevatorFaultNum;
            if (CollectionUtils.isEmpty(faultList)) {
                faultNum = 0;
                elevatorFaultNum = 0;
            } else {
                //电瓶车告警数
                faultNum = faultList.size();
                //电瓶车告警梯数
                elevatorFaultNum = faultList.stream().map(Fault::getVElevatorId).distinct().count();
            }

            //时间
            String time = DateUtil.format(yesterday, "yyyy年MM月dd日");

            // 参数
            String param = String.format(PUSH_BATTERY_CAR_COUNT_TEMPLATE, village, elevatorNum, time,
                    elevatorFaultNum, faultNum);

            //发送短信
            smsUtils.sendMessage(user.getPhoneNumber(), "麦信科技", "SMS_465600545", param);
        });


    }
}
