package com.shmashine.userclientapplets;


import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.shmashine.userclientapplets.task.SendFaultCountMessage;


/**
 * UserClientAppletsTest
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/17 11:39
 */
@SpringBootTest
@ActiveProfiles("dev")
public class UserClientAppletsTest {

    @Resource
    private SendFaultCountMessage sendFaultCountMessage;

    @Test
    public void testTaskSendFaultCountMessage() {
        sendFaultCountMessage.taskSendFaultCountMessage();
    }
}
