package com.shmashine.sender.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.shmashine.sender.entity.SendToUser;
import com.shmashine.sender.entity.SendToUserCache;
import com.shmashine.sender.server.dataAccount.DataAccountService;
import com.shmashine.sender.server.elevator.BizElevatorService;

/**
 * 默认说明
 *
 * @author jiangheng
 * @version 1.0 2021/8/16 11:32
 */

@Profile({"prod"})
@Component
public class TimeTaskHandle {

    @Autowired
    private BizElevatorService bizElevatorService;

    @Autowired
    private DataAccountService dataAccountService;

    /**
     * 半小时一次，刷新推送用户缓存
     */
    @Scheduled(fixedRate = 1800000, initialDelay = 300000)
    public void taskReloadSendToUser() {

        Map<String, List<SendToUser>> cacheMap = new HashMap<>();
        List<SendToUser> users = bizElevatorService.taskReloadSendToUser();

        if (CollectionUtils.isEmpty(users)) {
            return;
        }

        for (SendToUser user : users) {
            String elevatorCode = user.getElevatorCode();

            List<SendToUser> shieldList = cacheMap.get(elevatorCode);

            if (shieldList == null) {
                List<SendToUser> newShieldList = new ArrayList<>();
                newShieldList.add(user);
                cacheMap.put(elevatorCode, newShieldList);
            } else {
                shieldList.add(user);
            }
        }

        SendToUserCache.reloadCache(cacheMap);

    }

    /**
     * 推送统计数据 =====> 临港平台
     */
    @Scheduled(cron = "0 6 0 * * ?")
    public void taskPushStatistics2linGang() {
        dataAccountService.taskPushStatistics2linGang();
    }
}
