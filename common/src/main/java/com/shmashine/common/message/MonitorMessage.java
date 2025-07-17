package com.shmashine.common.message;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Base64;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

/**
 * 监控消息
 *
 * @author little.li
 */
@Data
public class MonitorMessage implements Serializable {


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


    /**
     * "D": "nITTXQEVAAAAAQAAAQAAAAAAAB4=" 数据处理
     * 轿顶上报 机房不上报
     */
    /*public void setFromBase64(JSONObject messageJson) {
        String encodedText = messageJson.getString("D");

        if (encodedText != null) {
            final Base64.Decoder decoder = Base64.getDecoder();
            byte[] res = decoder.decode(encodedText);
            ByteBuffer flagBuffer = ByteBuffer.allocate(res.length + 10);
            flagBuffer.put(res);
            flagBuffer.rewind();
            ByteBuffer buffer = flagBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.rewind();

            // 无用数据占位 --- 请不要删除 timestamp 时间戳
            buffer.getInt();

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
            buffer.get();

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
        } else {
            return;
        }


    }*/
    public void setFromBase64(JSONObject messageJson) {
        String encodedText = messageJson.getString("D");

        if (encodedText != null) {
            final Base64.Decoder decoder = Base64.getDecoder();
            byte[] res = decoder.decode(encodedText);
            ByteBuffer flagBuffer = ByteBuffer.allocate(res.length + 10);
            flagBuffer.put(res);
            flagBuffer.rewind();
            ByteBuffer buffer = flagBuffer.order(java.nio.ByteOrder.LITTLE_ENDIAN);
            buffer.rewind();

            // 无用数据占位 --- 请不要删除 timestamp 时间戳
            buffer.getInt();

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
            buffer.get();

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
        } else {
            return;
        }


    }

}
