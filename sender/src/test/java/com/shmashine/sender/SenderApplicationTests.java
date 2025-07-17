package com.shmashine.sender;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.sender.platform.city.shanghai.LinGangHttpUtil;
import com.shmashine.sender.platform.city.shanghai.LinGangSender;
import com.shmashine.sender.platform.city.shanghai.YidianRuiJinServer;
import com.shmashine.sender.redis.utils.RedisUtils;
import com.shmashine.sender.server.dataAccount.DataAccountService;

@SpringBootTest
class SenderApplicationTests {

    private static Logger log = LoggerFactory.getLogger("yidianScheduleTaskLogger");

    private static final String PT_CODE = "yidian";

    private static final String BASE_URL = "http://www.smartelevator.net";

    /**
     * 检验信息接URL
     */
    private static final String DEFAULT_LIFT_INSPECTS_URL = BASE_URL
            + "/platform/api/v1/liftInspects/queryPlaceName/latest?lastModifiedDate=0&placeName=ruijin";
    /**
     * 维保信息接URL
     */
    private static final String DEFAULT_LIFT_MAIN_PENANCE_USES_URL = BASE_URL
            + "/platform/api/v1/liftMaintenanceUses/queryPlaceName/latest?lastModifiedDate=0&placeName=ruijin";
    /**
     * 工单信息接URL
     */
    private static final String DEFAULT_MAINTENANCE_RECORDS_URL = BASE_URL
            + "/platform/api/v1/maintenanceRecords/queryPlaceName/latest?lastModifiedDate=0&placeName=ruijin";


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    YidianRuiJinServer yidianRuiJinServer;

    @Autowired
    LinGangHttpUtil linGangHttpUtil;

    @Autowired
    private LinGangSender linGangSender;

    @Autowired
    private DataAccountService dataAccountService;


    @Test
    void contextLoads() {
    }

    @Test
    void testJsonObject() {

        String key = "DATAACCOUNT:yidian:31103102302015070025";
        String text = redisUtils.get(key);
        System.out.println("text: " + text);
        String replaceStr = text.replaceAll("\\\\", "");
        System.out.println("replaceStr: " + replaceStr);
        String subStr = replaceStr.substring(1, replaceStr.length() - 1);
        System.out.println("subStr" + subStr);
        JSONObject obj = JSONObject.parseObject(subStr);
        System.out.println("obj: " + obj);
        System.out.println("lastPushTime:" + obj.getLongValue("samplingTime"));
    }

    //拉取维保工单
    @Test
    void testupdateThirdPartyRuiJinWorkOrderByPage() {
        long totalPage = yidianRuiJinServer.getTotalPage(DEFAULT_MAINTENANCE_RECORDS_URL);
        if (totalPage > 0) {
            for (int page = 0; page < totalPage; page++) {
                yidianRuiJinServer.updateThirdPartyRuiJinWorkOrderByPage(page);
            }
        }
        log.info("updateThirdPartyRuiJinMaintenanceTask>>>>>+" + totalPage + "电梯工单信息完成更新");
    }

    //推送基础信息到临港
    @Test
    void pushBasicInformation2Lingang() {

        linGangSender.pushBasicInformation2Lingang();
    }
}
