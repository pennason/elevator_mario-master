package com.shmashine.socket.message.bean;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

/**
 * 上海浮奈(电信扶梯) 监控消息
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/30 16:32
 */
@Data
public class MonitorMessageV100E {

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
     * version, int value, start from 0
     */
    private int version;

    /**
     * 时间戳
     */
    private Integer timestamp;

    /**
     * 运行模式: [ 0:正常 1:检修 ]
     */
    private Integer modeStatus;

    /**
     * 运行状态: [ 0:停止 1:运行 ]
     */
    private Integer runStatus;

    /**
     * 运行方向 0：停留，1：上行，2：下行
     */
    private Integer direction;

    /**
     * 0:normal  1:fault
     */
    private Integer faultStatus;

    /**
     * 故障编号
     */
    private Integer faultCode;

    /**
     * 累计运行时间
     */
    private Integer totalRunTimeHour;

    /**
     * 累计运行次数
     */
    private Integer totalRunCount;


    /**
     * 扶梯报文解析
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

        //  timestamp 时间戳
        timestamp = buffer.getInt();

        // version, int value, start from 0
        version = buffer.get();

        //运行模式: [ 0:正常 1:检修 ]
        modeStatus = (int) buffer.get();

        //运行状态: [ 0:停止 1:运行 ]
        runStatus = (int) buffer.get();

        //运行方向 0：停留，1：上行，2：下行
        direction = (int) buffer.get();

        //0:normal  1:fault
        faultStatus = (int) buffer.get();

        faultCode = (int) buffer.get();

        //  累计运行时间
        totalRunTimeHour = buffer.getInt();

        //  累计运行次数
        totalRunCount = buffer.getInt();

    }
}
