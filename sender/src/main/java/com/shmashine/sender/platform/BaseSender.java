package com.shmashine.sender.platform;

import com.shmashine.sender.platform.city.shanghai.PostResponse;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

public interface BaseSender {

    /**
     * 发送消息
     *
     * @param registerNumber 电梯注册码
     * @param topic          推送主题
     * @param url            url
     * @param body           推送报文内容
     * @return 结果
     * @throws Exception 异常
     */
    PostResponse send(String registerNumber, String topic, String url, String body) throws Exception;
}
