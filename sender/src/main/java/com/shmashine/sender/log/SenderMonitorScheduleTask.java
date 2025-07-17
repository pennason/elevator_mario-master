package com.shmashine.sender.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shmashine.sender.platform.city.shanghai.YidianRuiJinServer;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Component
@EnableScheduling   // 1.开启定时任务
@Async
public class SenderMonitorScheduleTask {

    @Autowired
    YidianRuiJinServer yidianRuiJinServer;

    //    @Async
    //    @Scheduled(cron = "0 0/3 * * * ?")
    //    public void updateRuiJinInfo() {
    //        List<String> ttls = SenderCounterServer.ttlGroupIds(3);
    //        if (ttls != null && ttls.size() > 0) {
    //            for (String ttl : ttls) {
    //                SendMessageUtil.sendFaultMessage("15029019114", ttl, "37服务器", "不再推送报文",
    //                      DateUtils.formatWithTime(new Date()));
    //            }
    //        }
    //    }

    @Async
    @Scheduled(cron = "0 0/3 * * * ?")
    public void resetRuiJinInfo() {
        SenderCounterServer.updateTtl(5 * 60 * 1000L);
    }
}