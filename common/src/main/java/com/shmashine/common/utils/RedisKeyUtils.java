package com.shmashine.common.utils;

/**
 * redis key 工具
 *
 * @author little.li
 */
public class RedisKeyUtils {


    /**
     * 构建路由 缓存key
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return GATEWAY:ROUTE:{elevatorCode}:{sensorType}
     */
    public static String getGateWayRouteKey(String elevatorCode, String sensorType) {
        return "GATEWAY:ROUTE:" + elevatorCode + ":" + sensorType;
    }

    /**
     * 电梯状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @param sensorType   设备类型
     * @return ELEVATOR:STATUS:{elevatorCode}:{sensorType}
     */
    public static String getElevatorStatus(String elevatorCode, String sensorType) {
        return "ELEVATOR:STATUS:" + elevatorCode + ":" + sensorType;
    }

    /**
     * 电梯状态 缓存key
     *
     * @param elevatorCode 电梯编号
     * @return 缓存key
     */
    public static String getElevatorStatus(String elevatorCode) {
        return "ELEVATOR:STATUS:" + elevatorCode;
    }


    public static String getCameraHistoryStatus(String elevatorCode) {
        return "ELEVATOR:CAMERA:" + elevatorCode;
    }

    public static String getXiongMaiHlsUrl(String elevatorCode) {
        return "CAMERA:HLSURL:" + elevatorCode;
    }

    /**
     * 电梯编号存储
     *
     * @return elevator:info
     */
    public static String getElevatorMembers() {
        return "elevator:info";
    }

    /**
     * 电梯信息缓存 redis key
     *
     * @param elevatorCode 电梯编号
     * @return ELEVATOR:CACHE:INFO:{elevatorCode}
     */
    public static String getElevatorCacheKeyByCode(String elevatorCode) {
        return "ELEVATOR:CACHE:INFO:" + elevatorCode;
    }

    /**
     * 电梯信息 全量字段缓存
     *
     * @param elevatorCode 电梯编号
     * @return ELEVATOR:CACHE:DETAIL:{elevatorCode}  TblElevator 对象
     */
    public static String getTblElevatorCacheKeyByCode(String elevatorCode) {
        return "ELEVATOR:CACHE:TBL:DETAIL:" + elevatorCode;
    }


    /**
     * 夜间守护 待处理队列
     *
     * @return ELEVATOR:NIGHT:WATCH:QUEUE
     */
    public static String getElevatorNightWatchQueue() {
        return "ELEVATOR:NIGHT_WATCH:QUEUE";
    }

    /**
     * 夜间守护 上次记录电梯有人状态与时间
     *
     * @param elevatorCode 电梯编号
     * @return ELEVATOR:NIGHT_WATCH:LATEST_PEOPLE_STATUS:{elevatorCode}
     */
    public static String getElevatorNightWatchLatestPeopleStatus(String elevatorCode) {
        return "ELEVATOR:NIGHT_WATCH:LATEST_PEOPLE_STATUS:" + elevatorCode;
    }

    /**
     * 群租识别 待处理队列
     *
     * @return ELEVATOR:GROUP_LEASING:QUEUE
     */
    public static String getElevatorGroupLeasingQueue() {
        return "ELEVATOR:GROUP_LEASING:QUEUE";
    }

    /**
     * 群租识别 楼层最新停靠 可以
     *
     * @param elevatorCode 电梯编号
     * @return ELEVATOR:GROUP_LEASING:LATEST_FLOOR:{elevatorCode}
     */
    public static String getElevatorGroupLeasingLatestFloor(String elevatorCode) {
        return "ELEVATOR:GROUP_LEASING:LATEST_FLOOR:" + elevatorCode;
    }

    /**
     * 群租识别 每个楼层统计的 总量（停靠数 * 时间系数）
     *
     * @param elevatorCode 电梯编号
     * @param date         日期 yyyy-MM-dd -> yyyyMMdd
     * @return ELEVATOR:GROUP_LEASING:STATISTICS:{elevatorCode}:{date}
     */
    public static String getElevatorDateMapKey(String elevatorCode, String date) {
        return "ELEVATOR:GROUP_LEASING:STATISTICS:" + elevatorCode + ":" + date.replace("-", "");
    }

    /**
     * 海康云眸摄像头录制本地视频只允许一个通道录制
     *
     * @param cloudNumber 云眸摄像头编号
     * @return HIKCLOUD:CAMERA:RECORDING:{cloudNumber}
     */
    public static String getHikCloudRecordVideoStatusKey(String cloudNumber) {
        return "HIKCLOUD:CAMERA:RECORDING:" + cloudNumber;
    }

    /**
     * 天翼视联摄像头录制本地视频保证只允许一个通道录制
     *
     * @param cloudNumber 云眸摄像头编号
     * @return TYSL:CAMERA:RECORDING:{cloudNumber}
     */
    public static String getTyslRecordVideoStatusKey(String cloudNumber) {
        return "TYSL:CAMERA:RECORDING:" + cloudNumber;
    }

    /**
     * 麦信平台自研电动车识别 待处理队列
     *
     * @return ELEVATOR:ELECTRIC_BIKE:IDENTIFY:QUEUE
     */
    public static String getElevatorElectricBikeIdentifyQueue() {
        return "ELEVATOR:ELECTRIC_BIKE:IDENTIFY:QUEUE";
    }


    public static String getDataAccountKey(String dataAccountCode, String registerNumber) {
        return "DATAACCOUNT:" + dataAccountCode + ":" + registerNumber;
    }

    public static String getCityPushPlatformElevatorExistsKey(String elevatorCode) {
        return "GOVERN:CITY_PUSH:ELEVATOR:" + elevatorCode;
    }

    public static String getCityPushPlatformProjectExistsKey(String projectId) {
        return "GOVERN:CITY_PUSH:PROJECT:" + projectId;
    }

    public static String getGovernPlatformKey(String appKey) {
        return "GOVERN:CITY_PUSH:TOKEN:" + appKey;
    }

    public static String getRunKafkaMessageCacheKey(String elevatorCode) {
        return "KAFKA:MESSAGE:CACHE:RUN:" + elevatorCode;
    }

    public static String getOnOfflineKafkaMessageCacheKey(String elevatorCode) {
        return "KAFKA:MESSAGE:CACHE:ON_OFFLINE:" + elevatorCode;
    }

    public static String getYiDianTokenCacheKey() {
        return "YidianController:YIDIAN:TOKEN";
    }
}
