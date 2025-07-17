package com.shmashine.socket.websocket;

import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;

/**
 * websocket session连接管理
 *
 * @author little.li
 */
public class WebSocketManager {


    /**
     * session管理map
     * key:   Monitor_CarRoof_MX3390 / Debug_MotorRoom_MX3390 / Debug_Cube_MX3390 / Register_MX3390
     * value: [session1, session2]
     */
    private static final Map<String, List<Session>> ELEVATOR_SESSION_LIST_MAP = Maps.newConcurrentMap();


    /**
     * session打开
     *
     * @param elevatorCode 电梯编号
     * @param messageType  消息类型
     * @param session      session
     */
    public static void addSession(String elevatorCode, String messageType, String sensorType, Session session) {
        if (StringUtils.isBlank(elevatorCode) || StringUtils.isBlank(messageType) || StringUtils.isBlank(sensorType)) {
            return;
        }

        String key = messageType + "_" + sensorType + "_" + elevatorCode;
        addSession(key, session);
    }

    /**
     * session打开
     *
     * @param key     key
     * @param session session
     */
    private static void addSession(String key, Session session) {
        if (!ELEVATOR_SESSION_LIST_MAP.containsKey(key)) {
            ELEVATOR_SESSION_LIST_MAP.put(key, Lists.newArrayList(session));
        } else {
            ELEVATOR_SESSION_LIST_MAP.get(key).add(session);
        }
    }


    /**
     * session关闭
     *
     * @param elevatorCode 电梯编号
     * @param messageType  消息类型
     * @param session      session
     */
    public static void delSession(String elevatorCode, String messageType, String sensorType, Session session) {
        if (StringUtils.isBlank(elevatorCode) || StringUtils.isBlank(messageType) || StringUtils.isBlank(sensorType)) {
            return;
        }

        String key = messageType + "_" + sensorType + "_" + elevatorCode;
        delSession(key, session);
    }

    /**
     * session关闭
     *
     * @param key     key
     * @param session session
     */
    private static void delSession(String key, Session session) {
        List<Session> sessionList = ELEVATOR_SESSION_LIST_MAP.get(key);
        if (sessionList != null) {
            sessionList.remove(session);
        }
    }


    /**
     * 发送消息到客户端
     *
     * @param elevatorCode 电梯编号
     * @param messageType  消息类型
     * @param message      消息体
     */
    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    public static void sendMessageToBrowsers(String elevatorCode, String messageType, String sensorType,
                                             String message) {

        // 拼接key 获取session列表
        String key = messageType + "_" + sensorType + "_" + elevatorCode;
        List<Session> sessionList = ELEVATOR_SESSION_LIST_MAP.get(key);

        if (!CollectionUtils.isEmpty(sessionList)) {
            for (Session session : sessionList) {
                try {
                    if (session.isOpen()) {
                        synchronized (session) {
                            session.getBasicRemote().sendText(message);
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }
        }

        // 监控消息和故障消息同时推送到 “all” socket中
        if (messageType.equals(MessageConstants.TYPE_MONITOR) || messageType.equals(MessageConstants.TYPE_FAULT)) {
            // 拼接key 获取session列表
            String registerKey = messageType + "_" + SocketConstants.SOCKET_TYPE_ALL + "_" + elevatorCode;
            List<Session> registerSessionList = ELEVATOR_SESSION_LIST_MAP.get(registerKey);

            if (!CollectionUtils.isEmpty(registerSessionList)) {
                for (Session session : registerSessionList) {
                    try {
                        if (session.isOpen()) {
                            session.getBasicRemote().sendText(message);
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }

    }


    /**
     * 判断sessionList是否为空
     *
     * @param elevatorCode 电梯编号
     * @param messageType  消息类型
     * @return true: sessionList为空， false：sessionList不为空
     */
    public static boolean sessionListIsEmpty(String elevatorCode, String messageType, String sensorType) {
        // 拼接key 获取session列表
        String key = messageType + "_" + sensorType + "_" + elevatorCode;
        List<Session> sessionList = ELEVATOR_SESSION_LIST_MAP.get(key);
        return CollectionUtils.isEmpty(sessionList);
    }
}


