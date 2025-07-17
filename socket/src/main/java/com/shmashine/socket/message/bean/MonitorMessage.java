package com.shmashine.socket.message.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 监控消息
 *
 * @author little.li
 */

@Slf4j
@Data
public class MonitorMessage {


    /**
     * 消息类型
     */
    private String type = "Monitor";
    /**
     * 消息子类型
     */
    private String stype = "status";

    /**
     * 设备类型 Cube：轿顶（初版设备），CarRoof：轿顶，MotorRoom：机房，FRONT：前装设备
     */
    private String sensorType;

    /**
     * 设备固件monitor版本
     * 1：直梯单门，45（2D）：直梯双门
     */
    private int version;


    //////////////// 轿顶上报数据  ////////////////

    /**
     * 当前设备状态 0 正常 1 故障 2 平层关人 3 非平层关人
     */
    private Integer nowStatus;
    /**
     * 服务模式 0：正常，1：检修模式，2：停止服务
     * 检修模式从机房上报，与轿顶公用一个字段
     */
    private Integer modeStatus;
    /**
     * 设备当前电量 0-100
     * 轿顶和机房公用字段
     */
    private Integer battery;
    /**
     * 轿厢运行状态 0：停止，1：运行
     */
    private Integer runStatus;
    /**
     * 轿厢运行方向 0：停留，1：上行，2：下行
     */
    private Integer direction;
    /**
     * 电梯当前楼层
     */
    private String floor;
    /**
     * 平层状态 0：平层，1：非平层
     */
    private Integer floorStatus;
    /**
     * 关门到位 0：无关门到位，1：关门到位
     */
    private Integer droopClose;
    /**
     * 关门到位 0：无关门到位，1：关门到位
     */
    private Integer droopClose2;
    /**
     * 轿内是否有人 0：无人，1：有人
     */
    private Integer hasPeople;
    /**
     * 轿门状态-门锁锁止 0：锁止，1：非锁止
     */
    private Integer carStatus;
    /**
     * 供电状态 0：电源，1：电池，2：其他
     */
    private Integer powerStatus;
    /**
     * 电梯运行速度
     */
    private float speed;


    //////////////// 机房上报数据  ////////////////


    /**
     * 机房温度
     */
    private Float temperature;
    /**
     * 厅门状态-门锁锁止 0：非锁止，1：锁止
     */
    private Integer doorStatus;
    /**
     * 曳引机状态-制动器提起或释放  0：提起，1：释放
     */
    private Integer driveStatus;
    /**
     * 安全回路状态-正常或断开 0：正常 1：断开
     */
    private Integer safeLoop;

    /**
     * 层门锁回路断路   【0:正常 1:层门锁回路短路】
     */
    private Integer doorLoop;

    /**
     * 开锁区域外停止   【0:正常 1:开锁区域外停止】
     */
    private Integer stopOutLockArea;

    /**
     * 轿厢意外移动     【0:正常 1:意外移动】
     */
    private Integer carroofAccidentShift;


    //todo:西子扶梯上报数据

    /**
     * 模式状态：1:检修/0:正常
     */
    private Integer escalatorStatus;

    /**
     * 故障状态：0：正常/1：故障
     */
    private Integer faultStatus;

    /**
     * 速度状态：0:正常/1:高速
     */
    private Integer speedStatus;

    /**
     * 节能速度状态：节能/无
     */
    private Integer energySavingSpeed;

    /**
     * 扶手速度偏15%15S：扶手速度偏/无
     */
    private Integer armrestSpeedDviation;

    /**
     * 盖板开关开5分钟报警：报警状态/无
     */
    private Integer coverOpenAlarm;

    /**
     * 超速10%：超速状态/无
     */
    private Integer hypervelocity;

    /**
     * 逆转：逆转状态/无
     */
    private Integer reverse;

    /**
     * 急停：急停状态/无
     */
    private Integer emergencyStop;

    /**
     * 工频：工频状态 /无
     */
    private Integer powerFrequency;

    /**
     * 变频：变频状态/无
     */
    private Integer frequencyConversion;

    /**
     * 星型：星型状态/无
     */
    private Integer starStatus;

    /**
     * 三角型：三角型状态/无
     */
    private Integer triangleStatus;

    /**
     * 自启动待机：接通状态/无
     */
    private Integer selfStartStandby;

    /**
     * 主板安全回路监控点1….9：接通状态/无
     */
    private Integer motherboardSafetyLoopMonitoring1;
    private Integer motherboardSafetyLoopMonitoring2;
    private Integer motherboardSafetyLoopMonitoring3;
    private Integer motherboardSafetyLoopMonitoring4;
    private Integer motherboardSafetyLoopMonitoring5;
    private Integer motherboardSafetyLoopMonitoring6;
    private Integer motherboardSafetyLoopMonitoring7;
    private Integer motherboardSafetyLoopMonitoring8;
    private Integer motherboardSafetyLoopMonitoring9;

    /**
     * 相序：正常状态/无
     */
    private Integer phaseSequence;

    /**
     * 上光电无遮挡：无遮挡状态/无
     */
    private Integer topUnobstructedPhotoelectric;

    /**
     * 下光电无遮挡：无遮挡状态/无
     */
    private Integer underUnobstructedPhotoelectric;

    /**
     * 当前运行速度百分比：0-255 %
     */
    private Integer speedPercentage;

    /**
     * 当前左扶手速度百分比：0-255 %
     */
    private Integer leftArmrestSpeedPercentage;

    /**
     * 当前右扶手速度百分比：0-255 %
     */
    private Integer rightArmrestSpeedPercentage;

    /**
     * 上梯级速度：0-255 Hz
     */
    private Integer stepSpeed;

    /**
     * 下梯级速度：0-255 Hz
     */
    private Integer downStepSpeed;

    /**
     * 故障代码
     */
    private Integer errorCode;

    /**
     * 运行时间
     */
    private Integer runTime;


    //todo:数据处理

    /**
     * "D": "nITTXQEVAAAAAQAAAQAAAAAAAB4=" 数据处理
     * 轿顶上报 机房不上报
     */
    public void setFromBase64(JSONObject messageJson) {
        String encodedText = messageJson.getString("D");

        final Base64.Decoder decoder = Base64.getDecoder();
        byte[] res = decoder.decode(encodedText);
        ByteBuffer flagBuffer = ByteBuffer.allocate(res.length + 10);
        flagBuffer.put(res);
        flagBuffer.rewind();
        ByteBuffer buffer = flagBuffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();

        // 无用数据占位 --- 请不要删除 timestamp 时间戳
        int i = buffer.getInt();

        // 设备固件monitor版本
        // 1：直梯单门，45（2D）：直梯双门
        version = buffer.get();
        //当前设备状态 0 正常 1 故障 2 平层关人 3 非平层关人
        nowStatus = (int) buffer.get();
        //轿厢运行方向 0：停留，1：上行，2：下行
        direction = (int) buffer.get();
        //平层状态 0：平层，1：非平层
        floorStatus = (int) buffer.get();
        //电梯运行速度
        speed = buffer.get();
        speed = speed / 10;
        //轿门状态-门锁锁止 0：锁止，1：非锁止
        carStatus = (int) buffer.get();
        //轿内是否有人 0：无人，1：有人
        hasPeople = (int) buffer.get();
        //供电状态 0：电源，1：电池，2：其他
        powerStatus = (int) buffer.get();
        //当前楼层
        floor = String.valueOf(buffer.get());

        // 无用数据占位 --- 请不要删除 signal 1-100
        buffer.get();

        //设备当前电量 0-100
        battery = (int) buffer.get();
        //服务模式 0：正常，1：检修模式，2：停止服务
        modeStatus = (int) buffer.get();
        //轿厢运行状态 0：停止，1：运行
        runStatus = (int) buffer.get();
        //关门到位 0：无关门到位，1：关门到位
        droopClose = (int) buffer.get();

        // 无用数据占位 --- 请不要删除 motor_status 0stop 1run
        buffer.get();
        // 无用数据占位 --- 请不要删除 temperature 温度
        temperature = (float) buffer.get();
        if (temperature == 0) {
            temperature = null;
        }

        //关门到位 0：无关门到位，1：关门到位
        droopClose2 = (int) buffer.get();

        //安全回路断路信息 【0:正常 1:安全回路断路】
        safeLoop = (int) buffer.get();

        //层门锁回路断路   【0:正常 1:层门锁回路短路】
        doorLoop = (int) buffer.get();

        //开锁区域外停止   【0:正常 1:开锁区域外停止】
        stopOutLockArea = (int) buffer.get();

        //轿厢意外移动     【0:正常 1:意外移动】
        carroofAccidentShift = (int) buffer.get();
    }

    /**
     * 单盒报文解析
     */
    public void setFromBase64ForSingleBox(JSONObject messageJson) {
        String encodedText = messageJson.getString("D");

        final Base64.Decoder decoder = Base64.getDecoder();
        byte[] res = decoder.decode(encodedText);
        ByteBuffer flagBuffer = ByteBuffer.allocate(res.length + 10);
        flagBuffer.put(res);
        flagBuffer.rewind();
        ByteBuffer buffer = flagBuffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();

        // 无用数据占位 --- 请不要删除 timestamp 时间戳
        int i = buffer.getInt();

        // 设备固件monitor版本
        // 1：直梯单门，45（2D）：直梯双门
        version = buffer.get();
        //当前设备状态 0 正常 1 故障 2 平层关人 3 非平层关人
        nowStatus = (int) buffer.get();
        //轿厢运行方向 0：停留，1：上行，2：下行
        direction = (int) buffer.get();
        //平层状态 0：平层，1：非平层
        floorStatus = (int) buffer.get();
        //电梯运行速度
        speed = buffer.get();
        speed = speed / 10;
        //轿门状态-门锁锁止 0：锁止，1：非锁止
        carStatus = (int) buffer.get();
        //轿内是否有人 0：无人，1：有人
        hasPeople = (int) buffer.get();
        //供电状态 0：电源，1：电池，2：其他
        powerStatus = (int) buffer.get();
        //当前楼层
        floor = String.valueOf(buffer.get());

        // 无用数据占位 --- 请不要删除 signal 1-100
        buffer.get();

        //设备当前电量 0-100
        battery = (int) buffer.get();
        //服务模式 0：正常，1：检修模式，2：停止服务
        modeStatus = (int) buffer.get();
        //轿厢运行状态 0：停止，1：运行
        runStatus = (int) buffer.get();
        //关门到位 0：无关门到位，1：关门到位
        droopClose = (int) buffer.get();

        //安全回路断路信息 【0:正常 1:安全回路断路】
        safeLoop = (int) buffer.get();

        // 无用数据占位 --- 请不要删除 temperature 温度
        temperature = (float) buffer.get();
        if (temperature == 0) {
            temperature = null;
        }

        //关门到位 0：无关门到位，1：关门到位
        droopClose2 = 1;

        //层门锁回路断路   【0:正常 1:层门锁回路短路】
        doorLoop = 0;

        //开锁区域外停止   【0:正常 1:开锁区域外停止】
        stopOutLockArea = 0;

        //轿厢意外移动     【0:正常 1:意外移动】
        carroofAccidentShift = 0;

        //曳引机状态-制动器提起或释放
        driveStatus = 1;

        //厅门状态-门锁锁止
        doorStatus = (carStatus == 1) ? 0 : 1;
    }

    /**
     * 扶梯设备数据解析
     */
    public void setFromBase64ForEscalator(JSONObject messageJson) {
        String encodedText = messageJson.getString("D");

        final Base64.Decoder decoder = Base64.getDecoder();
        byte[] res = decoder.decode(encodedText);
        ByteBuffer flagBuffer = ByteBuffer.allocate(res.length + 10);
        flagBuffer.put(res);
        flagBuffer.rewind();
        ByteBuffer buffer = flagBuffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();

        // 无用数据占位 --- 请不要删除 timestamp 时间戳
        buffer.getInt();

        // 设备固件monitor版本
        version = buffer.get();

        /**
         * 电梯状态  0：非正常/1：正常
         */
        nowStatus = (int) buffer.get();

        /**
         * 故障状态  0：正常/1：故障
         */
        faultStatus = (int) buffer.get();

        /**
         * 模式状态  0:正常/1:检修
         */
        escalatorStatus = (int) buffer.get();

        /**
         * 运行方向  0:停梯/1:上行/2:下行
         */
        direction = (int) buffer.get();

        /**
         * 速度状态  0:正常/1:高速
         */
        speedStatus = (int) buffer.get();

        /**
         * 节能速度状态  1:节能/0:无
         */
        energySavingSpeed = (int) buffer.get();

        /**
         * 扶手速度偏15%15S  1:扶手速度偏/0:无
         */
        armrestSpeedDviation = (int) buffer.get();

        /**
         * 盖板开关开5分钟报警  1:报警状态/0:无
         */
        coverOpenAlarm = (int) buffer.get();

        /**
         * 超速10%  1:超速状态/0:无
         */
        hypervelocity = (int) buffer.get();

        /**
         * 逆转  1:逆转状态/0:无
         */
        reverse = (int) buffer.get();

        /**
         * 急停  1:急停状态/0:无
         */
        emergencyStop = (int) buffer.get();

        /**
         * 工频  1:工频状态/0:无
         */
        powerFrequency = (int) buffer.get();

        /**
         * 变频  1:变频状态/0无
         */
        frequencyConversion = (int) buffer.get();

        /**
         * 星型 1:星型状态/0:无
         */
        starStatus = (int) buffer.get();

        /**
         * 三角型  1:三角型状态/0:无
         */
        triangleStatus = (int) buffer.get();

        /**
         * 自启动待机  1:接通状态/0:无
         */
        selfStartStandby = (int) buffer.get();

        /**
         * 主板安全回路监控点1….9  1:接通状态/0:无
         */
        motherboardSafetyLoopMonitoring1 = (int) buffer.get();
        motherboardSafetyLoopMonitoring2 = (int) buffer.get();
        motherboardSafetyLoopMonitoring3 = (int) buffer.get();
        motherboardSafetyLoopMonitoring4 = (int) buffer.get();
        motherboardSafetyLoopMonitoring5 = (int) buffer.get();
        motherboardSafetyLoopMonitoring6 = (int) buffer.get();
        motherboardSafetyLoopMonitoring7 = (int) buffer.get();
        motherboardSafetyLoopMonitoring8 = (int) buffer.get();
        motherboardSafetyLoopMonitoring9 = (int) buffer.get();

        /**
         * 相序  1:正常状态/0:无
         */
        phaseSequence = (int) buffer.get();

        /**
         * 上光电无遮挡  1:无遮挡状态/0:无
         */
        topUnobstructedPhotoelectric = (int) buffer.get();

        /**
         * 下光电无遮挡  1:无遮挡状态/0:无
         */
        underUnobstructedPhotoelectric = (int) buffer.get();

        /**
         * 当前运行速度百分比  0-255 %
         */

        speedPercentage = (int) buffer.getShort();

        /**
         * 当前左扶手速度百分比 0-255 %
         */
        leftArmrestSpeedPercentage = (int) buffer.getShort();

        /**
         * 当前右扶手速度百分比 0-255 %
         */
        rightArmrestSpeedPercentage = (int) buffer.getShort();

        /**
         * 上梯级速度  0-255 Hz
         */
        stepSpeed = (int) buffer.getShort();

        /**
         * 下梯级速度  0-255 Hz
         */
        downStepSpeed = (int) buffer.getShort();

        /**
         * 故障代码
         */
        errorCode = (int) buffer.getShort();

        /**
         * 运行时间
         */
        runTime = buffer.getInt();
    }

    /**
     * TEST
     */
    /*public static void main(String[] args) throws UnsupportedEncodingException {

        MonitorMessage monitorMessage = new MonitorMessage();
        JSONObject messageJson =
                JSONObject.parseObject("{\"TY\":\"M\",\"ST\":\"S\",\"D\":\"lpPuYAEAAAEAAQAAAQBcAAAAACY=\"}");

        monitorMessage.setFromBase64(messageJson);
        log.info(JSONObject.toJSONString(monitorMessage));

        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();
        int s = 8;
        byte floor = toBytes(s)[0];
        byte[] x = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, floor, 0, 0, 0, 0, 0};

        //编码
        final String encodedText = encoder.encodeToString(x);

        JSONObject messageJson = JSONObject.parseObject("{\"TY\":\"M\",\"ST\":\"S\",\"D\":\"" + encodedText + "\"}");
        monitorMessage.setFromBase64(messageJson);

        System.out.println("----------------" + encodedText);

        byte[] bytes = decoder.decode(encodedText);
        ByteBuffer flagBuffer = ByteBuffer.allocate(bytes.length + 10);
        flagBuffer.put(bytes);
        flagBuffer.rewind();
        ByteBuffer buffer = flagBuffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.rewind();
    }

    public static byte[] toBytes(int number) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) number;
        bytes[1] = (byte) (number >> 8);
        bytes[2] = (byte) (number >> 16);
        bytes[3] = (byte) (number >> 24);
        return bytes;
    }*/

}
