package com.shmashine.common.constants;

/**
 * 业务默认常量类
 */
public class BusinessConstants {

    /**
     * 初始用户密码
     */
    public static final String INITIAL_PASSWORD = "123456";

    /**
     * 用户 有效状态
     */
    public static final Integer USER_NORMAL = 0;

    /**
     * 用户 无效状态
     */
    public static final Integer USER_INVALID = 1;

    /**
     * 超级管理员编号
     */
    public static final String ADMIN_USER_ID = "admin";

    /**
     * 默认系统名称
     */
    public static final String DEFAULT_SYSTEM_NAME = "麦信智慧平台";

    /**
     * 默认系统图片文件编号
     */
    public static final String DEFAULT_SYSTEM_LOG_FILE_URL = "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/Oreo_Project/system_log/default.png";

    /**
     * 事件来源 仪电（瑞金）
     */
    public static final String EVENT_CHANNEL = "2";
    /**
     * 事件已完成
     */
    public static final Integer EVENT_CURRENT_FINISH = 6;

    /**
     * 数据是否有效  有效
     */
    public static final Integer DELETE_FLAG_YES = 0;
    /**
     * 数据是否有效  无效
     */
    public static final Integer DELETE_FLAG_NO = 0;

    /**
     * 故障标识 故障
     */
    public static final Integer UNCIVILIZED_BEHAVIOR_FLAG_0 = 0;
    /**
     * 故障标识 不文明行为
     */
    public static final Integer UNCIVILIZED_BEHAVIOR_FLAG_1 = 0;
    /**
     * 故障状态   故障中
     */
    public static final Integer FAULT_STATUS_YES = 0;
    /**
     * 故障状态 已恢复
     */
    public static final Integer FAULT_STATUS_NO = 1;
    /**
     * 当前服务模式  正常
     */
    public static final Integer MODULE_STATUS = 0;
    /**
     * 当前操作 无操作
     */
    public static final Integer MANUAL_CLEAR_NONE = -2;
    /**
     * 是否推送故障
     */
    public static final Integer FAULT_PUSH_NO = 0;

    /**
     * 故障次数 默认
     */
    public static final Integer FAULT_NUMBER = 1;
    /**
     * 故障等级
     */
    public static final Integer FAULT_LEVEL_4 = 4;
    /**
     * 故障等级 名称
     */
    public static final String FAULT_LEVEL_4_NAME = "普通Lv4";

    /**
     * 麦信盒子传感器类型
     */
    public static final String CAR_ROOF = "CarRoof";

    /**
     * 麦信盒子传感器类型
     */
    public static final String MOTOR_ROOM = "MotorRoom";

    /**
     * 麦信盒子传感器类型
     */
    public static final String FRONT = "FRONT";

    /**
     * 单盒设备标识
     */
    public static final String SENSOR_TYPE_SINGLEBOX = "SINGLEBOX";

    /**
     * 301设备
     */
    public static final String CAR_DOOR = "CarDoor";

    /**
     * 扶梯设备标识
     */
    public static final String SENSOR_TYPE_ESCALATOR = "Escalator";

    /**
     * 物联网卡appId
     */
    public static final String IOT_CARD_APPID = "908dfrgw4erdfg";

    /**
     * 物联网卡key
     */
    public static final String IOT_CARD_KEY = "shmxsj";
}
