package com.shmashine.socket.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpUtil;

import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;
import com.shmashine.socket.netty.ChannelManager;

import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket服务
 *
 * @author little.li
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@ServerEndpoint(value = "/ws/{messageType}/{sensorType}/{elevatorCode}/{token}")
public class WebSocketServer {


    private String elevatorCode;
    private String messageType;
    private String sensorType;
    private Session session;

    /**
     * 连接打开时执行
     *
     * @param elevatorCode 电梯编号
     * @param session      客户端session
     */
    @OnOpen
    public void onOpen(@PathParam("messageType") String messageType,
                       @PathParam("sensorType") String sensorType,
                       @PathParam("elevatorCode") String elevatorCode,
                       @PathParam("token") String token,
                       Session session) throws IOException {

        // 保存电梯编号和session
        this.elevatorCode = elevatorCode;
        this.messageType = messageType;
        this.sensorType = sensorType;
        this.session = session;


        try {
            StpUtil.isLogin(token);
        } catch (Exception e) {
            log.info("websocket打开失败-token失效-token:{}", token);
            session.getBasicRemote().sendText("websocket打开失败-token失效-请重新刷新页面请求");
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid token"));
            return;
        }

        // 判断sessionList是否为空
        boolean isEmpty = WebSocketManager.sessionListIsEmpty(this.elevatorCode, this.messageType, this.sensorType);
        // socket连接类型为all时改变发送消息频率
        if (isEmpty && !this.sensorType.equals(SocketConstants.SOCKET_TYPE_ALL)) {
            // 提升设备发送消息频率
            String message = getMessageByMessageType(this.messageType, false);
            ChannelManager.sendMessageToChannel(this.elevatorCode, this.sensorType, message);
        }
        // 添加session
        WebSocketManager.addSession(this.elevatorCode, this.messageType, this.sensorType, this.session);
    }


    /**
     * 收到消息时执行
     *
     * @param message 客户端消息
     */
    @OnMessage
    public void onMessage(String message) {
        // 通过webSocket向设备下发报文，仅限TEST使用
        if (this.messageType.equals(SocketConstants.MESSAGE_TYPE_TEST)) {
            ChannelManager.sendMessageToChannel(this.elevatorCode, this.sensorType, message);
        }
    }


    /**
     * 连接关闭时执行
     */
    @OnClose
    public void onClose() {

        WebSocketManager.delSession(this.elevatorCode, this.messageType, this.sensorType, this.session);

        // 判断sessionList是否为空
        boolean isEmpty = WebSocketManager.sessionListIsEmpty(this.elevatorCode, this.messageType, this.sensorType);
        if (isEmpty) {
            // 降低设备发送消息频率
            String message = getMessageByMessageType(this.messageType, true);
            ChannelManager.sendMessageToChannel(this.elevatorCode, this.sensorType, message);
        }
    }


    /**
     * 连接错误时执行
     */
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }


    //////////////////////////////private method//////////////////////////////////


    /**
     * 根据消息类型获取消息
     *
     * @param messageType 消息类型
     * @param isReduction 是否降频/停止
     * @return 降频/停止消息
     */
    private String getMessageByMessageType(String messageType, boolean isReduction) {
        if (isReduction) {
            // 降低消息频率消息
            if (messageType.equals(SocketConstants.MESSAGE_TYPE_MONITOR)) {
                return MessageConstants.MONITOR_START_5000;
            }
            if (messageType.equals(SocketConstants.MESSAGE_TYPE_DEBUG)) {
                return MessageConstants.DEBUG_STOP;
            }
        } else {
            // 提升消息频率消息
            if (messageType.equals(SocketConstants.MESSAGE_TYPE_MONITOR)) {
                return MessageConstants.MONITOR_START_500;
            }
            if (messageType.equals(SocketConstants.MESSAGE_TYPE_DEBUG)) {
                return MessageConstants.DEBUG_START_500;
            }
        }
        return "";
    }


}
