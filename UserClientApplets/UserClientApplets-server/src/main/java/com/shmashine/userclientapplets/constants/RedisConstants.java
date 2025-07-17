package com.shmashine.userclientapplets.constants;

/**
 * redis key常量
 *
 * @author jiangheng
 * @version 1.0
 * @date: 2021/9/16 10:49
 */
public class RedisConstants {

    /**
     * 设备离线一小时记录
     */
    public static final String TIMEOUT_DEVICE = "DEVICE:TIMEOUT";

    /**
     * 取证标识
     */
    public static final String HLS_IMAGE = "HLS:IMAGE";

    /**
     * 识别标识
     */
    public static final String HLS_IDENTIFICATION = "HLS:IDENTIFICATION";

    /**
     * 困人重新下载，锁标识afreshDownloadImage
     */
    public static final String AFRESDOWNLOADIMAGE_LOCK = "AFRESDOWNLOADIMAGE_LOCK";

    /**
     * 海康摄像头hls流缓存
     */
    public static final String CAMERA_HAIKANG_HLSURL = "CAMERA:HAIKANG:HLSURL:";

    /**
     * 用户查询缓存
     */
    public static final String USER_INFO = "USER_INFO:";

    /**
     * 用户部门缓存
     */
    public static final String USER_DEPT_INFO = "USER_DEPT_INFO:";

    /**
     * 用户故障标准缓存
     */
    public static final String FAULT_DEFINITION_SELECT_LIST = "FAULT_DEFINITION_SELECT_LIST:";

    /**
     * 故障消息分布式锁标记
     */
    public static final String ELEVATOR_FAULT_MESSAGE_MARK_LOCK = "ELEVATOR_FAULT_MESSAGE_MARK_LOCK";

    /**
     * 仪电故障事件数据接受
     */
    public static final String YIDIAN_EVENT_RECEIVE_LOCK = "YIDIAN:EVENT:RECEIVE:LOCK";

    /**
     * 电梯故障状态
     */
    public static final String ELEVATOR_FAULT_TYPE_MARK = "ELEVATOR:FAULT:TYPE:MARK:";

    /**
     * 传感器故障屏蔽缓存
     */
    public static final String SENSOR_FAULT_SHIELD_CACHE = "SENSOR:FAULT:SHIELD:CACHE:";

    /**
     * 麦信维保平台token
     */
    public static final String MAIXIN_MAINTENANCE_PLATFORM_TOKEN = "MAIXIN:MAINTENANCE:PLATFORM:TOKEN";


    /**
     * 麦信物业小程序电梯收藏
     */
    public static final String MAIXIN_WUYE_MINI_PROGRAM_USER_COLLECTION_ELEVATOR
            = "MAIXIN:WUYE:MINI_PROGRAM:USER_COLLECTION:ELEVATOR:";


}
