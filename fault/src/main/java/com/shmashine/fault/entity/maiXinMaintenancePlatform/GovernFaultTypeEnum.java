package com.shmashine.fault.entity.maiXinMaintenancePlatform;

/**
 * 政府故障类型枚举
 *
 * @author jiangheng
 * @version V1.0.0 - 2024/3/6 18:17
 */
public enum GovernFaultTypeEnum {

    UP_LIMIT("1", "冲顶", "upLimit"),
    DOWN_LIMIT("2", "蹲底", "downLimit"),
    OVER_SPEED("3", "超速", "overSpeed"),
    OPEN_RUN("4", "运行中开门", "openRun"),
    UCMP("5", "开门走车", "ucmp"),
    LEVEL_STOP("6", "门区外停梯", "levelStop"),
    ENTRAP("7", "平层关人", "entrap"),
    ENTRAP2("8", "非平层关人", "entrap2"),
    POWER_SUPPLY("9", "停电", "pwrSupply"),
    SAFETY("10", "安全回路断路", "safety"),
    NOT_OPEN("11", "开门故障", "notOpen"),
    NOT_CLOSE("12", "关门故障", "notClose"),
    DOOR_LOCK("13", "门锁回路断路", "doorLock"),
    BUTTON_ADHESION("14", "按钮粘连", "buttonAdhesion"),
    ABNORMAL_SPEED("15", "运行速度异常", "overSpeed"),
    ABNORMAL_BRAKING_SYSTEM("16", "制动系统故障", "otherAbnormal"),
    TIME_OVER("17", "电动机运转时间限制器动作", "timeOver"),
    TEMPERATURE_SENSOR_ABNORMAL("18", "温度传感器异常", "sensorAbnormal"),
    SELECTOR("19", "楼层信息丢失", "selector"),
    DOOR_BLOCKING("20", "关门时受阻挡", "doorBlocking"),
    INTER_PHONE("21", "用户报警", "interPhone"),
    BATTERY_CAPACITY("22", "电池电量", "batteryCapacity"),
    ES_SAFETY("23", "安全回路断路", "safety"),
    ES_SPEED_PROTECT_ABNORMAL("24", "超速保护", "protectAbnormal"),
    ES_REVERSE("25", "非操纵逆转保护", "reverse"),
    ES_FLOOR_PROTECT_ABNORMAL("26", "梯级缺失保护", "protectAbnormal"),
    ES_NO_CLASS_ABNORMAL("27", "未分类故障", "otherAbnormal"),
    ES_MODE_AUTO("28", "恢复自动运行模式", "modeAuto"),
    ES_MODE_OVERHAUL("29", "进入检修运行模式", "modeOverhaul"),
    ES_POWER_SUPPLY("30", "主电源断开", "pwrSupply"),
    ES_EMERGENCY_STOP_BUTTON("31", "紧急停止开关动作", "emergencyStopButton"),
    ES_PLATE_OPEN("32", "检修盖板和（或）楼层板打开", "otherAbnormal"),
    ES_STEP("33", "踏板缺失保护", "step"),
    ES_UNRELEASED_BRAKING_SYSTEM("34", "制动系统未释放", "otherAbnormal"),
    ES_TOOTH_PLATE_STUCK("35", "梳齿板卡阻", "otherAbnormal"),
    ES_MODE_MAINTENANCE("36", "扶梯检修模式", "modeOverhaul"),
    ELECTRIC_VEHICLE_LADDER("37", "电动车乘梯", "electricVehicleLadder"),
    VIBRATION_ABNORMAL("38", "电梯异常震动", "vibrationAbnormal"),
    NOT_WEARING_MASK("39", "未佩戴口罩", "notWearingMask"),
    DOOR_MONITOR_BLOCKING("40", "门电机阻塞", "doorMotorBlocking"),
    SWITCH_POWER_OFF("41", "主动开关断电", "switchPowerOff"),
    AIR_PRESSURE_SENSOR_ABNORMAL("42", "气压传感器异常", "sensorAbnormal");

    private final String shmashineCode;
    private final String faultName;
    private final String faultCode;

    public static GovernFaultTypeEnum getEnumByShmashineCode(String shmashineCode) {
        GovernFaultTypeEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GovernFaultTypeEnum e = var1[var3];
            if (e.getShmashineCode().equals(shmashineCode)) {
                return e;
            }
        }

        return null;
    }

    public static GovernFaultTypeEnum getEnumByFaultCode(String faultCode) {
        GovernFaultTypeEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            GovernFaultTypeEnum e = var1[var3];
            if (e.getFaultCode().equals(faultCode)) {
                return e;
            }
        }

        return null;
    }

    private GovernFaultTypeEnum(String shmashineCode, String faultName, String faultCode) {
        this.shmashineCode = shmashineCode;
        this.faultName = faultName;
        this.faultCode = faultCode;
    }

    public String getShmashineCode() {
        return this.shmashineCode;
    }

    public String getFaultName() {
        return this.faultName;
    }

    public String getFaultCode() {
        return this.faultCode;
    }

}
