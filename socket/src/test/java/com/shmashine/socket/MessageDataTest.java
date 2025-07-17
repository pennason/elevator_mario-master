package com.shmashine.socket;

import com.alibaba.fastjson2.JSON;
import com.shmashine.socket.message.bean.MessageData;

/**
 * MessageDataTest
 */
public class MessageDataTest {

    public static void main(String[] args) {
        MessageData messageData = new MessageData();
        messageData.setElevatorCode("123");

        System.out.println(JSON.toJSONString(messageData));

    }
}
