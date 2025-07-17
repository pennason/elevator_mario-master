package com.shmashine.socketClients;

/**
 * 异常回调
 *
 * @author jiangheng
 * @version V1.0.0  2020/12/24 —— 20:09
 */
public class SocketClientFallback implements SocketClient {
    @Override
    public String getStatus(String elevatorCode, String sensorType) {
        return "获取所有channel名单失败";
    }

    @Override
    public String sendMessageToCube(String message) {
        return "消息推送到设备失败";
    }
}
