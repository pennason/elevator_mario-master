package com.shmashine.common.utils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shmashine.common.constants.CameraConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 摄像头取流、历史视频...工具类
 *
 * @author little.li
 */
@Slf4j
public class CameraUtils {


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
    /*public static boolean testHlsUrlIsReady(String hlsUrl) {
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
    }*/

    /**
     * 获取雄迈hls流
     *
     * @param cloudNumber 雄迈摄像头序列号
     * @return hls流
     */
    public static String getXiongMaiHls(String cloudNumber, String elevatorCode) {
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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.XMNet.hlsUrl)
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


    /**
     * 获取云存储的视频下载或播放地址
     *
     * <p>
     * [{ "PlayUrl": "https://misc.xmeye.net/api/m3u8cache?sn=xxx&tm=1612256319566&file=003_xxx_xxx_xxx-1.m3u8",
     * "StartTime": "2021-02-02 15:06:47", "VideoSize": 555103, "StopTime": "2021-02-02 15:07:03"}]
     *
     * @param cloudNumber 雄迈摄像头序列号
     * @return hls流
     */
    public static JSONArray getXMCssVideoCC(String cloudNumber) {
        if (StringUtils.isEmpty(cloudNumber)) {
            return null;
        }

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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.XMNet.cssVideoUrl)
                    .queryParam("uuid", CameraConstants.XMNet.uuid)
                    .queryParam("appkey", CameraConstants.XMNet.appKey)
                    .queryParam("tm", timeMillis)
                    .queryParam("sign", encryptStr);

            Map queryMap = Maps.newHashMap();
            queryMap.put("SerialNumber", cloudNumber);
            queryMap.put("StartTime", "2021-02-02 08:36:47");
            queryMap.put("StopTime", "2021-02-03 09:00:47");

            ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
            System.out.println(JSONObject.toJSONString(map));
            JSONArray cssFiles = JSONObject.parseObject(map.getBody()).getJSONArray("CssFiles");
            //System.out.println(cssFiles.toJSONString());
            return cssFiles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询云存储开通状态
     *
     * @param cloudNumber 雄迈摄像头序列号
     * @return hls流
     */
    public static String reqCaps(String cloudNumber) {
        if (StringUtils.isEmpty(cloudNumber)) {
            return "";
        }

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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.XMNet.reqCapsUrl)
                    .queryParam("uuid", CameraConstants.XMNet.uuid)
                    .queryParam("appkey", CameraConstants.XMNet.appKey)
                    .queryParam("tm", timeMillis)
                    .queryParam("sign", encryptStr);

            Map queryMap = Maps.newHashMap();
            queryMap.put("ver", 2);
            queryMap.put("sn", cloudNumber);
            List<String> caps = Lists.newArrayList("xmc.service");
            queryMap.put("caps", caps);

            ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
            System.out.println(JSONObject.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        getXMCssVideoCC("a9ea0277d99816e4");
    }


    /**
     * 获取雄迈hls流
     *
     * @param cloudNumber 雄迈摄像头序列号
     * @return hls流
     */
    public static String getXiongMaiHlsWithHttps(String cloudNumber, String elevatorCode) {
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
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.XMNet.hlsUrl_https)
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


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////private method/////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取签名字符串
     *
     * @param uuid       客户唯一标识
     * @param appKey     应用key
     * @param appSecret  应用密钥
     * @param timeMillis 时间戳
     * @param movedCard  移动取模基数
     */
    private static String getEncryptStr(String uuid, String appKey, String appSecret, String timeMillis,
                                        int movedCard) {
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
            temp = ((i % moveCard) > ((encryptLength - i) % moveCard))
                    ? encryptByte[i] : encryptByte[encryptLength - (i + 1)];
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
     * 获取海康萤石云直播流地址
     *
     * @param vCloudNumber 摄像头序列号
     * @param expireTime   过期时长，秒
     * @return 流地址
     */
    public static String getHaiKangUrl(String vCloudNumber, int expireTime) {
        // 获取token
        try {
            Map<String, Object> queryMap = Map.of("appKey", CameraConstants.Ezopen.APP_KEY,
                    "appSecret", CameraConstants.Ezopen.APP_SECRET);
            String post = HttpUtil.post(CameraConstants.Ezopen.APP_TOKEN_URL, queryMap, 1000);
            JSONObject body = JSONObject.parseObject(post);

            String accessToken = body.getJSONObject("data").getString("accessToken");

            // 获取url
            UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.VEDIO_URL)
                    .queryParam("accessToken", accessToken)
                    .queryParam("deviceSerial", vCloudNumber)
                    .queryParam("protocol", 2)
                    .queryParam("expireTime", expireTime);

            Map<String, Object> queryMap1 = new HashMap<>();
            ResponseEntity<String> map1 = RequestUtil.sendPost(builder1, queryMap1);
            JSONObject body1 = JSONObject.parseObject(map1.getBody());
            if (!"200".equals(body1.getString("code"))) {
                return body1.getString("msg");
            }
            return body1.getJSONObject("data").getString("url");
        } catch (Exception e) {
            log.error("获取海康萤石云直播流地址失败，error:{}", ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 获取海康萤石云直播流地址
     *
     * @param vCloudNumber 摄像头序列号
     * @param expireTime   过期时长，秒
     * @param protocol     流播放协议 1-ezopen、2-hls、3-rtmp、4-flv，默认为1
     * @return 流地址
     */
    public static String getHaiKangUrl(String vCloudNumber, Long expireTime, Integer protocol) {
        // 获取token
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.APP_TOKEN_URL)
                .queryParam("appKey", CameraConstants.Ezopen.APP_KEY)
                .queryParam("appSecret", CameraConstants.Ezopen.APP_SECRET);

        Map<String, Object> queryMap = new HashMap<>();
        ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
        JSONObject body = JSONObject.parseObject(map.getBody());
        String accessToken = body.getJSONObject("data").getString("accessToken");

        // 获取url
        UriComponentsBuilder builder1 = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.VEDIO_URL)
                .queryParam("accessToken", accessToken)
                .queryParam("deviceSerial", vCloudNumber)
                .queryParam("protocol", protocol)
                .queryParam("expireTime", expireTime);

        Map<String, Object> queryMap1 = new HashMap<>();
        ResponseEntity<String> map1 = RequestUtil.sendPost(builder1, queryMap1);
        JSONObject body1 = JSONObject.parseObject(map1.getBody());
        if (!"200".equals(body1.getString("code"))) {
            return body1.getString("msg");
        }
        return body1.getJSONObject("data").getString("url");
    }

    public static String getEzopenToken() {
        // 拼接请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CameraConstants.Ezopen.APP_TOKEN_URL)
                .queryParam("appKey", CameraConstants.Ezopen.APP_KEY)
                .queryParam("appSecret", CameraConstants.Ezopen.APP_SECRET);

        Map<String, Object> queryMap = new HashMap<>();
        ResponseEntity<String> map = RequestUtil.sendPost(builder, queryMap);
        JSONObject body = JSONObject.parseObject(map.getBody());
        String accessToken = body.getJSONObject("data").getString("accessToken");
        return accessToken;
    }

}
