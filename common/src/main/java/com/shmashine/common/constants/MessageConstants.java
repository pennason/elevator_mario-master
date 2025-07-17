package com.shmashine.common.constants;

/**
 * 消息报文常量类
 *
 * @author little.li
 */
public class MessageConstants {


    /**
     * message中的TY
     */
    public static final String MESSAGE_TYPE = "TY";


    /**
     * message中的type
     */
    public static final String MESSAGE_TYPE_LITTLE = "type";


    /**
     * message中的ST
     */
    public static final String MESSAGE_STYPE = "ST";


    /**
     * message中的stype
     */
    public static final String MESSAGE_STYPE_LITTLE = "stype";


    /**
     * message中的sensor_type
     */
    public static final String SENSOR_TYPE = "sensorType";


    /**
     * message中的elevatorCode
     */
    public static final String ELEVATOR_CODE = "elevatorCode";


    /**
     * message中的 TY : M
     */
    public static final String TYPE_M = "M";


    /**
     * message中的 ST : M
     */
    public static final String STYPE_S = "S";


    /**
     * message中的 TY : Update
     */
    public static final String TYPE_UPDATE = "Update";

    /**
     * message中的 TY : Update
     */
    public static final String TYPE_REBOOT = "reboot";

    /**
     * message中的 ST : acc
     */
    public static final String STYPE_ACC = "acc";

    /**
     * message中的 ST : ip
     */
    public static final String STYPE_IP = "ip";


    /**
     * message中的 ST : frep
     */
    public static final String STYPE_FREP = "frep";


    /**
     * message中的 TY : confirm
     */
    public static final String TYPE_CONFIRM = "confirm";


    /**
     * message中的 ST : limit
     */
    public static final String STYPE_LIMIT = "limit";


    /**
     * message中的 TY : Monitor
     */
    public static final String TYPE_MONITOR = "Monitor";


    /**
     * message中的 TY : freq
     */
    public static final String TYPE_FREQ = "freq";


    /**
     * message中的 TY : Login
     */
    public static final String TYPE_LOGIN = "Login";


    /**
     * message中的 ST : login
     */
    public static final String STYPE_LOGIN = "login";


    /**
     * message中的 ST : deviceInfo
     */
    public static final String STYPE_DEVICE_INFO = "deviceInfo";


    /**
     * message中的 TY ：Fault
     */
    public static final String TYPE_FAULT = "Fault";

    /**
     * 传感器故障
     */
    public static final String TYPE_FAULT_SENSOR = "SensorFault";


    /**
     * message中的 TY ：FaultFront (前装设备故障)
     */
    public static final String TYPE_FAULT_FRONT = "FaultFront";

    /**
     * 扶梯设备故障
     */
    public static final String TYPE_FAULT_ESCALATOR = "FaultEscalator";

    /**
     * 扶梯设备模式切换
     */
    public static final String TYPE_EVENT_ESCALATOR = "EventEscalator";


    /**
     * message中的 fault_type : 1 故障类型
     */
    public static final String FAULT_TYPE = "fault_type";


    /**
     * message中的 fault_stype : 1 故障类型
     */
    public static final String FAULT_STYPE = "fault_stype";


    /**
     * message中的 ST:clear 故障恢复
     */
    public static final String STYPE_CLEAR = "clear";


    /**
     * message中的 ST:update 设备重启后故障处理
     */
    public static final String STYPE_UPDATE = "update";


    /**
     * message中的 ST:add 设备上报故障
     */
    public static final String STYPE_ADD = "add";


    /**
     * message中的 ST:disappear 设备消除故障
     */
    public static final String STYPE_DISAPPEAR = "disappear";


    /**
     * 故障清除状态 status ：1，手动清除故障 通知设备后，设备的返回 0 失败，1成功，2故障不存在
     */
    public static final String STATUS = "status";


    /**
     * message中的 TY : Monitor
     */
    public static final String TYPE_SYSTEM = "System";


    /**
     * message中的 TY : Monitor
     */
    public static final String STYPE_CPING = "cping";


    /**
     * message中的 TY : TR
     */
    public static final String TYPE_TR = "TR";


    /**
     * message中的 ST : update_log
     */
    public static final String STYPE_UPDATE_LOG = "update_log";


    /**
     * message中的 ST : update_log_front
     */
    public static final String STYPE_UPDATE_LOG_FRONT = "update_log_front";

    /**
     * 人流量统计
     */
    public static final String DETECTED_PEOPLE_NUMS = "DetectedPeopleNums";


    /**
     * message 中的 TY ： Dlog
     */
    public static final String TYPE_DLOG = "Dlog";
    /**
     * message 中的 TY ： SelfCheck
     */
    public static final String TYPE_SELF_CHECK = "SelfCheck";
    /**
     * message 中的 TY ： Dlog, SelfCheck
     */
    public static final String STYPE_START = "start";
    /**
     * message 中的 TY ： Dlog, SelfCheck
     */
    public static final String STYPE_STOP = "stop";

    /**
     * 停靠楼层
     */
    public static final String STOP_FLOOR = "stopFloor";


    /**
     * 向设备下发报文，开启监控，间隔时间为500毫秒
     */
    public static final String MONITOR_START_500 = "{\"TY\":\"Monitor\",\"ST\":\"start\",\"period\":500}";


    /**
     * 向设备下发报文，开启监控，间隔时间为5000毫秒
     */
    public static final String MONITOR_START_5000 = "{\"TY\":\"Monitor\",\"ST\":\"start\",\"period\":10000}";


    /**
     * 向设备下发报文，debug_info，开启
     */
    public static final String DEBUG_START_500 = "{\"TY\":\"TR\",\"ST\":\"debug_info\",\"command\":\"start\",\"period\":500}";


    /**
     * 向设备下发报文，debug_info，停止
     */
    public static final String DEBUG_STOP = "{\"TY\":\"TR\",\"ST\":\"debug_info\",\"command\":\"stop\"}";


    /**
     * 向设备下发报文，登录，login，res_id : 0
     */
    public static final String LOGIN_SUCCESS = "{\"TY\":\"Login\",\"ST\":\"login\",\"res_id\":0}";


    /**
     * 向设备下发报文，update_log，status : 1
     */
    public static final String TR_UPDATE_LOG_SUCCESS = "{\"TY\":\"TR\",\"ST\":\"update_log\",\"status\":1}";

    /**
     * 向设备下发报文，update_log，status : 1
     */
    public static final String TR_UPDATE_LOG_FRONT_SUCCESS = "{\"TY\":\"TR\",\"ST\":\"update_log_front\",\"status\":1}";

    public static final String DETECTED_PEOPLE_NUMS_SUCCESS = "{\"TY\":\"confirm\",\"ST\":\"DetectedPeopleNums\",\"status\":\"success\"}";

    /**
     *
     */
    public static final String FLOOR_STATUS_SUCCESS = "楼层设置成功";


    /**
     *
     */
    public static final String FLOOR_STATUS_ERROR = "楼层设置失败";


    /**
     * 设备升级文件存储路径
     */
    public static final String DEVICE_UPLOAD_URL = "/shmashine-deploy/java-oreo/api/device-file/";


    /**
     * 设备获取升级文件接口
     */
    public static final String DEVICE_UPLOAD_REQUEST_URL = "/api/device/file/wgetFile/";

    public static final String FALSE = "false";

    public static final String ERROR = "error";

    public static final String STYPE_DEBUG_INFO = "debug_info";

}
