package com.shmashine.api.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.api.ApiApplication;
import com.shmashine.api.service.fault.impl.BizFaultTempServiceImpl;

/**
 * 发送短信测试
 */
@SpringBootTest(classes = ApiApplication.class)
public class SendEntrapSMSTest {

    /*
     *${type}困人: ${villageName}的${elevatorName}在${floor}层，于${reportTime}发生困人，请及时救援!
     */

    @Autowired
    private BizFaultTempServiceImpl bizFaultTempService;

    /**
     * 困人审核平层
     */
    @org.junit.jupiter.api.Test
    void test1() {
        bizFaultTempService.faultConfirm("11111", 1);
    }

}
