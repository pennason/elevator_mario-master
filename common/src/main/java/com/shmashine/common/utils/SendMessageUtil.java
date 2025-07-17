package com.shmashine.common.utils;

import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.aliyuncs.exceptions.ClientException;

/**
 * 发送短信工具类
 *
 * @author little.wu
 */
public class SendMessageUtil {

    public static void sendModeChangeMessage(String mobile, String elevatorCode, String local, int modeStatus) {
        try {

            String templateCode = modeStatus == 0 ? "SMS_168340431" : "SMS_168345394";

            Date now = new Date();
            String time = now.getHours() + ":" + now.getMinutes();
            String templateParam = "{\"address\":\"" + local + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\"}";

            SmsDemo.sendSms(mobile, templateCode, templateParam);

        } catch (ClientException e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送故障短信
     *
     * @param mobile       手机号
     * @param elevatorCode 电梯编号
     * @param local        电梯位置信息
     * @param faultName    故障名称
     */
    public static String sendFaultMessage(String mobile, String elevatorCode, String local, String faultName, String time) {
        if (!StringUtils.hasText(mobile)) {
            return null;
        }
        String templateCode = "SMS_168345405";
        String templateParam = "{\"address\":\"" + local + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\",\"fault\":\"" + faultName + "\"}";
        try {
            SmsDemo.sendSms(mobile, templateCode, templateParam);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return time;

    }

    /**
     * 发送困人短信
     *
     * @param mobile       手机号
     * @param type         平层|非平层
     * @param villageName  小区名
     * @param elevatorName 电梯名
     * @param reportTime   上报时间
     * @param floor        楼层
     */
    public static void sendEntrapMessage(String mobile, String type, String villageName, String elevatorName, String reportTime, String floor) {
        if (!StringUtils.hasText(mobile)) {
            return;
        }
        String templateCode = "SMS_464070728";
        String templateParam = "{\"type\":\"" + type + "\", \"villageName\":\"" + villageName + "\",\"elevatorName\":\"" + elevatorName + "\",\"floor\":\"" + floor + "\",\"reportTime\":\"" + reportTime + "\"}";
        try {
            SmsDemo.sendEntrapMessage(mobile, templateCode, templateParam);
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送故障短信携带楼层
     *
     * @param mobile       手机号
     * @param elevatorCode 电梯
     * @param local        地址
     * @param faultName    故障
     * @param floor        楼层
     * @param time         时间
     * @return
     */
    public static String sendFaultMessageWithFloor(String mobile, String elevatorCode, String local, String faultName, String floor, String time) {
        if (!StringUtils.hasText(mobile)) {
            return null;
        }
        String templateCode = "SMS_246825640";
        String templateParam = "{\"address\":\"" + local + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\",\"floor\":\"" + floor + "\",\"fault\":\"" + faultName + "\"}";
        try {
            SmsDemo.sendSms(mobile, templateCode, templateParam);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return time;

    }

    /**
     * 发送西子扶梯  故障短信/故障消除短信
     *
     * @param client       客户
     * @param mobile       手机
     * @param elevatorCode 电梯code
     * @param local        地址
     * @param faultName    故障名
     * @param time         时间
     * @return
     */
    public static String sendEscalatorFaultMessage(String mobile, String faultId, String elevatorCode, String local, String faultType, String faultName, String time, String templateCode, String client) {
        if (!StringUtils.hasText(mobile)) {
            return null;
        }

        String templateParam = "{\"client\":\"" + client + "\", \"local\":\"" + local + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\",\"faultid\":\"" + faultId + "\",\"type\":\"" + faultType + "\",\"fault\":\"" + faultName + "\"}";
        try {
            SmsDemo.sendSmsToXizi(mobile, templateCode, templateParam);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return time;

    }

    /**
     * 发送物联网盒子与扶梯断开连接3分钟故障
     */
    public static String sendCommunicationFaultMessage(String mobile, String faultId, String elevatorCode, String local, String faultType, String faultName, String time, String templateCode) {

        if (!StringUtils.hasText(mobile)) {
            return null;
        }

        String templateParam = "{\"local\":\"" + local + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\",\"faultid\":\"" + faultId + "\",\"faultName\":\"" + faultName + "\"}";
        try {
            SmsDemo.sendSmsToXizi(mobile, templateCode, templateParam);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return time;

    }

    /**
     * 模式切换消息通知
     *
     * @param mobile
     * @param elevatorCode
     * @param vAddress
     * @param eventType
     * @param time
     */
    public static void sendEventChangeMessage(String mobile, String elevatorCode, String vAddress, Integer eventType, String time) {

        if (StringUtils.hasText(mobile)) {

            String templateCode = "SMS_217436097";

            String typeName = "正常模式";

            if (eventType == 1) {
                typeName = "检修模式";
            }

            String templateParam = "{\"local\":\"" + vAddress + "\", \"code\":\"" + elevatorCode + "\",\"time\":\"" + time + "\",\"eventType\":\"" + typeName + "\"}";
            try {
                SmsDemo.sendSmsToXizi(mobile, templateCode, templateParam);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每天定时发送西子扶梯故障数据
     *
     * @param mobile
     * @param elevatorCodes
     */
    public static void sendFaultElevatorCountMessage(String mobile, List<String> elevatorCodes) {

        if (StringUtils.hasText(mobile)) {

            String templateCode = "SMS_217416078";

            String templateParam = "{\"description\":\"" + 0 + "\"}";

            //有电梯故障时
            if (elevatorCodes != null && elevatorCodes.size() > 0) {
                templateParam = "{\"description\":\"" + elevatorCodes.toString() + "\"}";
            }

            try {
                SmsDemo.sendSmsToXizi(mobile, templateCode, templateParam);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送故障接单短信
     *
     * @param mobile       手机号
     * @param elevatorName 电梯
     * @param handler      处理人
     * @param faultName    故障
     * @param time         时间
     */
    public static void sendGetOrderMessage(String mobile, String elevatorName, String handler, String faultName, String time) {


        //${elevatorName}电梯 ${time}发生${faultName}故障，所在楼层${floor}，维保人员${handler}已经接到报修并赶往现场救援维修。
        if (StringUtils.hasText(mobile)) {
            String templateCode = "SMS_227257374";
            String templateParam = "{\"elevatorName\":\"" + elevatorName + "\", \"time\":\"" + time + "\",\"faultName\":\"" + faultName + "\",\"handler\":\"" + handler + "\"}";
            try {
                SmsDemo.sendSms(mobile, templateCode, templateParam);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送故障接单短信携带楼层
     *
     * @param mobile       手机号
     * @param elevatorName 电梯
     * @param handler      处理人
     * @param faultName    故障
     * @param time         时间
     * @param floor        楼层
     */
    public static void sendGetOrderMessageWithFloor(String mobile, String elevatorName, String handler, String faultName, String floor, String time) {


        //${elevatorName}电梯 ${time}发生${faultName}故障，所在楼层${floor}，维保人员${handler}已经接到报修并赶往现场救援维修。
        if (StringUtils.hasText(mobile)) {
            String templateCode = "SMS_246665688";
            String templateParam = "{\"elevatorName\":\"" + elevatorName + "\", \"time\":\"" + time + "\",\"faultName\":\"" + faultName + "\",\"floor\":\"" + floor + "\",\"handler\":\"" + handler + "\"}";
            try {
                SmsDemo.sendSms(mobile, templateCode, templateParam);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param mobile
     * @param verifyCode
     */
    public static void sendVerifyCodeMessage(String mobile, String verifyCode) {
        if (StringUtils.hasText(mobile)) {
            try {
                SmsDemo.sendSmsVerifyCode(mobile, verifyCode);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
}
