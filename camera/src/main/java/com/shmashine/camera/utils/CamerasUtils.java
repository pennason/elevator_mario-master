package com.shmashine.camera.utils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.camera.constants.CameraConstants;
import com.shmashine.common.utils.RequestUtil;
import com.shmashine.common.utils.TimeMillisUtil;
import com.shmashine.common.utils.TimeUtils;

/**
 * 摄像头取流、历史视频...工具类
 *
 * @author little.li
 */
public class CamerasUtils {


    /**
     * 调用雄迈摄像头服务，下载历史视频
     */
    public static Integer saveXiongMaiHistoryVideo(String faultId, String serialNumber, Date startTime, Date endTime) {

        // 时间参数处理，开始时间提前一分钟，结束时间推后一分钟
        Calendar time1 = Calendar.getInstance();
        time1.setTime(startTime);
        time1.add(Calendar.SECOND, -60);
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time1.getTime());

        String end;
        if (endTime != null) {
            Calendar time2 = Calendar.getInstance();
            time2.setTime(endTime);
            time2.add(Calendar.SECOND, +60);
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2.getTime());
        } else {
            time1.add(Calendar.SECOND, +180);
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time1.getTime());
        }

        try {
            // 拼接请求参数
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.XMNet.SAVE_VIDEO_URL)
                    .queryParam("faultId", faultId)
                    .queryParam("deviceId", serialNumber)
                    .queryParam("start", start)
                    .queryParam("end", end);

            ResponseEntity<String> map = RequestUtil.sendGet(builder);
            JSONObject body = JSONObject.parseObject(map.getBody());
            return body.getInteger("code");
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }

    /**
     * 调用海康摄像头服务，下载历史视频
     * 已废弃方法，原因：Ehome服务（历史录像下载）于萤石云服务（实时流播放）不能共存
     * 替代方案：发生困人时调用HLS服务，录制实时流数据
     */
    @Deprecated
    public static Integer saveEzopenHistoryVideo(String faultId, String serialNumber, Date startTime, Date endTime) {
        // 时间参数处理，开始时间提前一分钟，结束时间推后一分钟
        Calendar time1 = Calendar.getInstance();
        time1.setTime(startTime);
        time1.add(Calendar.SECOND, -60);
        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time1.getTime());

        String end;
        if (endTime != null) {
            Calendar time2 = Calendar.getInstance();
            time2.setTime(endTime);
            time2.add(Calendar.SECOND, +60);
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2.getTime());
        } else {
            time1.add(Calendar.SECOND, +180);
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time1.getTime());
        }
        JSONObject body = null;
        try {
            // 拼接请求参数
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.SAVE_VIDEO_URL)
                    .queryParam("faultId", faultId)
                    .queryParam("deviceId", serialNumber)
                    .queryParam("start", start)
                    .queryParam("end", end);
            ResponseEntity<String> map = RequestUtil.sendGet(builder);
            body = JSONObject.parseObject(map.getBody());
            return body.getInteger("code");
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }


    /**
     * 测试hls流是否可以播放
     *
     * @param hlsUrl hls流地址
     * @return true:可以播放, false: 不可以播放
     */
    public static boolean testHlsUrlIsReady(String hlsUrl) {
        if (StringUtils.isBlank(hlsUrl)) {
            return false;
        }
        for (int i = 0; i < 3; i++) {
            try {
                FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(hlsUrl);
                ff.start();
                doExecuteFrame(ff.grabImage());
                ff.stop();
                return true;
            } catch (FrameGrabber.Exception e) {
                System.out.printf("%s --- 打开流失败...重试%s...\n", TimeUtils.nowTime(), i);
            }
        }
        System.out.printf("%s --- 打开流重试3次失败...\n", TimeUtils.nowTime());
        return false;
    }

    /**
     * 获取雄迈hls流
     *
     * @param cloudNumber 雄迈摄像头序列号
     * @return hls流
     */
    public static String getXiongMaiHls(String cloudNumber, String elevatorCode, String type) {
        if (StringUtils.isEmpty(cloudNumber)) {
            return "";
        }
        String hlsUrl = "";
        try {
            // 获取时间戳
            String timeMillis = TimeMillisUtil.getTimeMillis();
            String encryptStr;
            // 获取sign
            encryptStr = getEncryptStr(CameraConstants.XMNet.uuid,
                    CameraConstants.XMNet.appKey,
                    CameraConstants.XMNet.appSecret,
                    timeMillis, CameraConstants.XMNet.movedCard);
            // 拼接请求参数
            String hlsurltype = "";

            if (type.equals("http")) {
                hlsurltype = CameraConstants.XMNet.hlsUrlForhttp;
            } else {
                hlsurltype = CameraConstants.XMNet.hlsUrlForHttps;
            }
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hlsurltype)
                    .queryParam("uuid", CameraConstants.XMNet.uuid)
                    .queryParam("appkey", CameraConstants.XMNet.appKey)
                    .queryParam("tm", timeMillis)
                    .queryParam("sign", encryptStr)
                    .queryParam("sn", cloudNumber);
            ResponseEntity<String> map = RequestUtil.sendGet(builder);
            hlsUrl = JSONObject.parseObject(map.getBody()).getString("playUrl");
            System.out.printf("%s --- 获取雄迈 %s --- %s --- %s\n", TimeUtils.nowTime(), cloudNumber, elevatorCode, hlsUrl);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s --- 获取雄迈 Signature 异常 %s --- %s\n", TimeUtils.nowTime(), cloudNumber, elevatorCode);
        }
        return hlsUrl;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////private method/////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取签名字符串
     *
     * @param uuid       客户唯一标识
     * @param appKey     应用key
     * @param appSecret  应用密钥
     * @param timeMillis 时间戳
     * @param movedCard  移动取模基数
     */
    private static String getEncryptStr(String uuid, String appKey, String appSecret, String timeMillis, int movedCard) {
        String encryptStr = uuid + appKey + appSecret + timeMillis;
        byte[] encryptByte = encryptStr.getBytes(StandardCharsets.ISO_8859_1);
        byte[] changeByte = change(encryptStr, movedCard);
        byte[] mergeByte = mergeByte(encryptByte, changeByte);
        return DigestUtils.md5DigestAsHex(mergeByte);
    }

    /**
     * 简单移位
     */
    private static byte[] change(String encryptStr, int moveCard) {
        byte[] encryptByte = encryptStr.getBytes(StandardCharsets.ISO_8859_1);
        int encryptLength = encryptByte.length;
        byte temp;
        for (int i = 0; i < encryptLength; i++) {
            temp = ((i % moveCard) > ((encryptLength - i) % moveCard)) ? encryptByte[i] : encryptByte[encryptLength - (i + 1)];
            encryptByte[i] = encryptByte[encryptLength - (i + 1)];
            encryptByte[encryptLength - (i + 1)] = temp;
        }
        return encryptByte;
    }

    /**
     * 合并
     */
    private static byte[] mergeByte(byte[] encryptByte, byte[] changeByte) {
        int encryptLength = encryptByte.length;
        int encryptLength2 = encryptLength * 2;
        byte[] temp = new byte[encryptLength2];
        for (int i = 0; i < encryptByte.length; i++) {
            temp[i] = encryptByte[i];
            temp[encryptLength2 - 1 - i] = changeByte[i];
        }
        return temp;
    }

    /**
     * javacv 获取一帧图片，辅助判断hls流是否可以播放
     */
    private static void doExecuteFrame(Frame frame) {
        if (null == frame || null == frame.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        converter.getBufferedImage(frame);
    }

}
