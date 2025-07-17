package com.shmashine.common.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.constants.MessageConstants;
import com.shmashine.common.constants.SocketConstants;

/**
 * 报文消息处理工具
 *
 * @author little.li
 */
public class MessageUtils {

    /**
     * 发送到设备的报文 处理为设备对应的报文格式
     *
     * @param message    原报文
     * @param sensorType 设备类型 Cube CarRoof MotorRoom
     */
    public static String convertMessageToSensor(String message, String sensorType) {
        JSONObject messageJson = JSONObject.parseObject(message);
        if (SocketConstants.SENSOR_TYPE_CUBE.equals(sensorType)) {
            messageJson = tyConvertToType(messageJson);
            return SocketConstants.MESSAGE_PREFIX + JSONObject.toJSONString(messageJson)
                    + SocketConstants.MESSAGE_SUFFIX;
        } else if (SocketConstants.SENSOR_TYPE_MOTOR_ROOM.equals(sensorType)) {
            messageJson = typeConvertToTY(messageJson, true);
            return JSONObject.toJSONString(messageJson);
        } else {
            messageJson = typeConvertToTY(messageJson, false);
            return JSONObject.toJSONString(messageJson);
        }
    }


    /**
     * 将设备上报的消息转换为统一格式
     *
     * @param message    消息内容
     * @param sensorType 设备类型
     * @return 处理后的消息
     */
    public static JSONObject formatMessage(String message, String sensorType) {
        message = message.replace("\r", "").replace("\n", "");
        message = StringUtils.trim(message);
        if (SocketConstants.SENSOR_TYPE_CUBE.equals(sensorType)) {
            message = message.replaceAll(SocketConstants.MESSAGE_PREFIX, "")
                    .replaceAll(SocketConstants.MESSAGE_SUFFIX, "");
        } else {
            message += "}";
        }
        JSONObject messageJson = JSONObject.parseObject(message);
        return typeConvertToTY(messageJson, false);
    }


    /**
     * 报文中增加时间
     *
     * @param message 字符串
     * @param time    当前时间字符串
     * @return 添加后的内容
     */
    public static String appendTime(String message, String time) {

        String ap = ",\"time\":\"" + time + "\"}";
        if (message.contains(",}")) {
            return message.replace(",}", ap);
        } else {
            return message.replace("}", ap);
        }

    }


    /**
     * 报文中增加设备类型
     *
     * @param message    报文
     * @param sensorType 设备类型
     * @return 添加后的内容
     */
    public static String appendSensorType(String message, String sensorType) {

        String ap = ",\"sensorType\":\"" + sensorType + "\"}";
        if (message.contains(",}")) {
            return message.replace(",}", ap);
        } else {
            return message.replace("}", ap);
        }

    }


    /**
     * 报文中增加电梯编号
     *
     * @param message      报文
     * @param elevatorCode 电梯编号
     * @return 添加后的内容
     */
    public static String appendElevatorCode(String message, String elevatorCode) {
        String ap = ",\"elevatorCode\":\"" + elevatorCode + "\"}";
        if (message.contains(",}")) {
            return message.replace(",}", ap);
        } else {
            return message.replace("}", ap);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////private method/////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 将报文中的type，stype 转换为 TY，ST
     * type: Monitor 转换为 TY: M (机房设备不进行这一条转换)
     *
     * @param messageJson 原报文
     * @return 转换后的报文
     */
    private static JSONObject typeConvertToTY(JSONObject messageJson, boolean isMotorRoom) {

        JSONObject resultJson = new JSONObject();
        for (Map.Entry<String, Object> entry : messageJson.entrySet()) {
            if (MessageConstants.MESSAGE_TYPE_LITTLE.equals(entry.getKey())) {
                resultJson.put(MessageConstants.MESSAGE_TYPE, entry.getValue());
            } else if (MessageConstants.MESSAGE_STYPE_LITTLE.equals(entry.getKey())) {
                resultJson.put(MessageConstants.MESSAGE_STYPE, entry.getValue());
            } else {
                resultJson.put(entry.getKey(), entry.getValue());
            }
        }
        if (MessageConstants.TYPE_MONITOR.equals(resultJson.getString(MessageConstants.MESSAGE_TYPE))) {
            resultJson.put(MessageConstants.MESSAGE_TYPE, MessageConstants.TYPE_M);
        }
        return resultJson;
    }


    /**
     * 将报文中的TY，ST 转换为 type，stype
     * TY: M 转换为 type: Monitor
     *
     * @param messageJson 原报文
     * @return 转换后的报文
     */
    private static JSONObject tyConvertToType(JSONObject messageJson) {

        JSONObject resultJson = new JSONObject();
        for (Map.Entry<String, Object> entry : messageJson.entrySet()) {
            if (MessageConstants.MESSAGE_TYPE.equals(entry.getKey())) {
                resultJson.put(MessageConstants.MESSAGE_TYPE_LITTLE, entry.getValue());
            } else if (MessageConstants.MESSAGE_STYPE.equals(entry.getKey())) {
                resultJson.put(MessageConstants.MESSAGE_STYPE_LITTLE, entry.getValue());
            } else {
                resultJson.put(entry.getKey(), entry.getValue());
            }
        }
        if (resultJson.getString(MessageConstants.MESSAGE_TYPE_LITTLE).equals(MessageConstants.TYPE_M)) {
            resultJson.put(MessageConstants.MESSAGE_TYPE_LITTLE, MessageConstants.TYPE_MONITOR);
        }
        return resultJson;
    }


}
