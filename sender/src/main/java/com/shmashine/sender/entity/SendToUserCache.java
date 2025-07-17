package com.shmashine.sender.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认说明
 *
 * @author jiangheng
 * @version 1.0 2021/8/16 10:58
 */
public class SendToUserCache implements Serializable {

    private static final long serialVersionUID = 4789784897989798L;

    public static Map<String, List<SendToUser>> SEND_TOUSER_MAP = new ConcurrentHashMap();


    /**
     * 刷新推送用户
     */
    public static void reloadCache(Map<String, List<SendToUser>> cacheMap) {
        try {
            SEND_TOUSER_MAP.clear();
            SEND_TOUSER_MAP.putAll(cacheMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取推送的对接用户
     */
    public static List<SendToUser> getSendCache(String elevatorCode) {
        return SEND_TOUSER_MAP.get(elevatorCode);
    }
}
