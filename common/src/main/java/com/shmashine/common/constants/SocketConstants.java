package com.shmashine.common.constants;


/**
 * socket模块常量类
 *
 * @author little.li
 */
public class SocketConstants {


    /**
     * 2.0/2.1版本设备标识
     */
    public static final String SENSOR_TYPE_CUBE = "Cube";

    /**
     * 2.2版本轿顶设备标识
     */
    public static final String SENSOR_TYPE_CAR_ROOF = "CarRoof";

    /**
     * 2.2版本机房设备标识
     */
    public static final String SENSOR_TYPE_MOTOR_ROOM = "MotorRoom";

    /**
     * 3.0版本前装设备标识
     */
    public static final String SENSOR_TYPE_FRONT = "FRONT";
    /**
     * 单盒设备标识
     */
    public static final String SENSOR_TYPE_SINGLEBOX = "SINGLEBOX";
    /**
     * 扶梯设备标识
     */
    public static final String SENSOR_TYPE_ESCALATOR = "Escalator";

    /**
     * 301设备标识
     */
    public static final String SENSOR_TYPE_CAR_DOOR = "CarDoor";

    public static final String CUBE_NEED_LOGIN = "{\"type\":\"Login\",\"stype\":\"need_login\"}";

    public static final String CAR_ROOF_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String MOTOR_ROOM_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String ESCALATOR_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String SINGLEBOX_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String CAR_DOOR_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String FRONT_NEED_LOGIN = "{\"TY\":\"Login\",\"ST\":\"need_login\"}";

    public static final String CPING_MESSAGE = "{\"TY\":\"System\",\"ST\":\"cping\"}";

    public static final String PING_MESSAGE = "{\"TY\":\"System\",\"ST\":\"ping\"}";

    public static final String MESSAGE_PREFIX = "#10#";

    public static final String MESSAGE_SUFFIX = "#13#";

    public static final String MESSAGE_TYPE_MONITOR = "Monitor";

    public static final String MESSAGE_TYPE_DEBUG = "Debug";

    public static final String MESSAGE_TYPE_TEST = "TEST";

    public static final String MESSAGE_EID = "eid";

    public static final String PROTOCAL_VERSION = "protocal_version";

    public static final String E_TYPE = "etype";

    public static final String MESSAGE_REASON = "reason";

    public static final String SOCKET_TYPE_ALL = "all";

    public static final String SERVER_CHANNEL_ERROR = "server channel error";

    public static final String SERVER_PING_TIMEOUT = "server ping timeout";

    public static final String SENSOR_IP = "ip";

    public static final String SCREEN_TRAPPED_ADD_MESSAGE = "{\"TY\":\"Relay\", \"ST\":\"F4S\", \"TA\":\"screen\", \"fault\":\"T\", \"result\":1}";

    public static final String SCREEN_TRAPPED_DIS_MESSAGE = "{\"TY\":\"Relay\", \"ST\":\"F4S\", \"TA\":\"screen\", \"fault\":\"T\", \"result\":0}";

    /**
     * 二次识别地址， 自研图像识别地址
     */
    // public static final String IMAGE_IDENTIFY_URL = "http://47.105.214.0:10089/";

}
