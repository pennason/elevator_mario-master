package com.shmashine.socket.message.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

/**
 * 监控消息
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/30 16:32
 */
@Data
public class MonitorMessageV200 {

    /**
     * 消息类型
     */
    private String type = "Monitor";

    /**
     * 消息子类型
     */
    private String stype = "status";

    /**
     * 设备类型
     */
    private String sensorType;

    /**
     * 设备固件monitor版本
     * 1：直梯单门，45（2D）：直梯双门
     */
    private int version;

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
     * 轿内是否有人 0：无人，1：有人
     */
    private Integer hasPeople;


    /**
     * 电梯运行速度
     */
    private String speed;

    /**
     * 机房温度
     */
    private String temperature;

    /**
     * 门状态[0 关门 1 开门 2 关门中 3 开门中]
     */
    private Integer doorStatus;

    /**
     * 电动车入梯【0：正常 1：入梯】
     */
    private Integer motobike;

    /**
     * 信号
     */
    private Integer signal;

    /**
     * 气压 pa
     */
    private String barometer;

    /**
     * 高度 m
     */
    private String altitude;


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

        //运行速度X
        buffer.getShort();

        //电梯运行速度
        speed = String.format("%.2f", buffer.getShort() * 0.01);

        //门状态[0 关门 1 开门 2 关门中 3 开门中]
        doorStatus = (int) buffer.get();

        //当前楼层
        floor = String.valueOf(buffer.get());

        // 信号 signal 1-100
        signal = (int) buffer.get();


        //电动车 0 normal, 1 detected
        //motobike = (int) buffer.get();


        //服务模式 0：正常，1：检修模式，2：停止服务
        //modeStatus = (int) buffer.get();

        //运行状态 0：停止，1：运行
        runStatus = (int) buffer.get();

        //关门到位 0：无关门到位，1：关门到位
        droopClose = (int) buffer.get();

        //轿内是否有人 0：无人，1：有人
        hasPeople = (int) buffer.get();

        // 无用数据占位 --- 请不要删除 temperature 温度
        temperature = String.format("%.2f", buffer.getShort() * 0.01);

        //气压pa
        barometer = String.format("%.2f", buffer.getInt() * 0.01);

        //高度m
        altitude = String.format("%.2f", buffer.getInt() * 0.01);

    }
}
