package com.shmashine.hikYunMou.utils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.shmashine.common.entity.TblCameraDownloadTaskEntity;
import com.shmashine.common.entity.TblCameraHikCloudProjectEntity;
import com.shmashine.hikYunMou.client.dto.HikCloudConsumerIdResponseDTO;
import com.shmashine.hikYunMou.client.dto.HikCloudMessageResponseDTO;
import com.shmashine.hikYunMou.dto.HikCloudCascadeChannelDTO;
import com.shmashine.hikYunMou.dto.HikCloudCascadeConfigDTO;
import com.shmashine.hikYunMou.dto.HikCloudFileDownloadResponseDTO;
import com.shmashine.hikYunMou.dto.HikCloudPageResponseDTO;
import com.shmashine.hikYunMou.dto.HikCloudProjectIdResponseDTO;
import com.shmashine.hikYunMou.dto.HikCloudTaskIdResponseDTO;
import com.shmashine.hikYunMou.dto.HikCloudVideoUrlResponseDTO;
import com.shmashine.hikYunMou.dto.requests.HikCloudProjectCreateRequestDTO;
import com.shmashine.hikYunMou.dto.requests.HikCloudRecordImageRequestDTO;
import com.shmashine.hikYunMou.dto.requests.HikCloudRecordVideoRequestDTO;
import com.shmashine.hikYunMou.properties.HikCloudApplicationProperties;
import com.shmashine.hikYunMou.properties.HikCloudMqProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author jiangheng
 * @create 2022/10/9 18:10
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HikPlatformUtil {
    private final HikCloudApplicationProperties properties;
    private final HikCloudMqProperties mqProperties;
    private final RedisUtils redisUtils;

    //private static final String client_id = "4ef54256cd5342e88f007bd691e9372d";
    //private static final String client_secret = "2ea6c29b02ce40cf9404654b0df7984b";
    //private static final String grant_type = "client_credentials";

    private static final String TOKEN_HIK_YUNMOU_ACCESSTOKEN = "TOKEN:HIK:YUNMOU:ACCESSTOKEN";

    private static final String HIK_CLOUD_REDIS_CONSUMER_ID_KEY = "HIK:CLOUD:CONSUMER_ID";

    /**
     * 海康云眸平台API调用地址
     */
    private static final String HIK_CLOUD_BASE_URL = "https://api2.hik-cloud.com";
    /**
     * 创建消费者地址
     */
    private static final String URI_HIK_CLOUD_CONSUMER_POST = HIK_CLOUD_BASE_URL + "/api/v1/mq/consumer/{groupId}";
    /**
     * 消费消息
     */
    private static final String URI_HIK_CLOUD_CONSUMER_CONSUME_POST = HIK_CLOUD_BASE_URL + "/api/v1/mq/consumer/messages";


    /**
     * 获取access_token
     */
    private static final String URI_TOKEN_POST = HIK_CLOUD_BASE_URL + "/oauth/token";
    /**
     * 获取预览/回放地址
     */
    private static final String URI_VIDEO_PLAYBACK_GET = HIK_CLOUD_BASE_URL + "/v1/carrier/wing/endpoint/video/getVideoUrl";
    /**
     * 抓图接口
     */
    private static final String URI_CAPTURE_PICTURE_POST = HIK_CLOUD_BASE_URL + "/api/v1/open/basic/channels/actions/capture";
    /**
     * 获取标准流预览地址
     */
    private static final String URI_VIDEO_PREVIEW_GET = HIK_CLOUD_BASE_URL + "/v1/carrier/wing/endpoint/video/getVideoUrl";

    private static final String URI_STREAM_TOKEN_GET = HIK_CLOUD_BASE_URL + "/v1/ezviz/account/info";

    /**
     * 视频云录制，获取视频回放，历史视频地址
     */
    private static final String URI_RECORD_VIDEO = HIK_CLOUD_BASE_URL + "/api/v1/ezviz/record/video";
    /**
     * 按时间点抽帧
     */
    private static final String URI_RECORD_IMAGE = HIK_CLOUD_BASE_URL + "/api/v1/ezviz/record/video/frame/timing";
    /**
     * 文件下载地址
     */
    private static final String URI_RECORD_FILE_DOWNLOAD = HIK_CLOUD_BASE_URL + "/api/v1/ezviz/record/file/download";

    /**
     * 项目相关
     */
    private static final String URI_PROJECT_CREATE = HIK_CLOUD_BASE_URL + "/api/v1/ezviz/record/project";

    /**
     * 国标级联 查询上级平台列表
     */
    private static final String URI_CASCADE_PLATFORM_LIST = HIK_CLOUD_BASE_URL + "/api/v1/cascade/config/actions/getConfigPage";
    /**
     * 国标级联 根据上级平台查询通道列表
     */
    private static final String URI_CASCADE_CHANNEL_LIST = HIK_CLOUD_BASE_URL + "/api/v1/cascade/config/actions/getChannelPage";


    /**
     * 抓图
     *
     * @return
     */
    public String getPicture(String deviceSerial) {

        //String token = getToken(client_id, client_secret, grant_type);
        var token = getHikToken();

        JSONObject reqBody = JSONUtil.createObj().set("deviceSerial", deviceSerial)
                .set("channelNo", 1)
                .set("quality", 0);

        String result = HttpRequest.post(URI_CAPTURE_PICTURE_POST).bearerAuth(token).body(reqBody.toString()).timeout(6000).execute().body();

        return JSONUtil.parseObj(result).getJSONObject("data").getStr("picUrl");
    }

    /**
     * 获取回放地址
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param startTime    开始时间
     * @param stopTime     结束时间
     * @param expireTime   过期时间
     */
    public HikCloudVideoUrlResponseDTO getPlaybackURL(String deviceSerial, String protocol, Integer quality, String startTime,
                                                      String stopTime, String expireTime) {

        protocol = StrUtil.isEmpty(protocol) ? "3" : protocol;
        quality = quality == null ? 2 : quality;
        expireTime = StrUtil.isEmpty(expireTime) ? "600" : expireTime;

        //String token = getToken(client_id, client_secret, grant_type);
        var token = getHikToken();

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("deviceSerial", deviceSerial);
        paramMap.put("channelNo", 1);
        paramMap.put("protocol", protocol);
        paramMap.put("quality", quality);
        paramMap.put("startTime", startTime);
        paramMap.put("stopTime", stopTime);
        paramMap.put("expireTime", expireTime);
        //协议地址的类型，1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
        paramMap.put("type", 2);

        var httpRes = HttpRequest.get(URI_VIDEO_PLAYBACK_GET).bearerAuth(token).form(paramMap).timeout(6000).execute().body();
        log.info("Hik-Cloud getPlaybackURL uri: {}, body: {}, result {}", URI_VIDEO_PLAYBACK_GET,
                JSONUtil.toJsonStr(paramMap), httpRes);
        if (!StringUtils.hasText(httpRes)) {
            log.info("获取回放视频地址失败，结果为空");
            return null;
        }
        return JSONUtil.toBean(httpRes, HikCloudVideoUrlResponseDTO.class);
    }


    /**
     * 获取监控预览流
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param expireTime   过期时间 ，单位秒
     */
    public String getPreviewURLs(String deviceSerial, String protocol, Integer quality, String expireTime) {

        protocol = StrUtil.isEmpty(protocol) ? "3" : protocol;
        quality = quality == null ? 2 : quality;
        expireTime = StrUtil.isEmpty(expireTime) ? "600" : expireTime;

        //String token = getToken(client_id, client_secret, grant_type);
        var token = getHikToken();

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("deviceSerial", deviceSerial);
        paramMap.put("channelNo", 1);
        paramMap.put("protocol", protocol);
        paramMap.put("quality", quality);
        paramMap.put("expireTime", expireTime);
        //协议地址的类型，1-预览，2-本地录像回放，3-云存储录像回放，非必选，默认为1
        paramMap.put("type", 2);

        var httpRes = HttpRequest.get(URI_VIDEO_PREVIEW_GET).bearerAuth(token).form(paramMap).timeout(6000).execute().body();
        log.info("Hik-Cloud getPreviewURLs uri: {}, body: {}, result {}", URI_VIDEO_PREVIEW_GET,
                JSONUtil.toJsonStr(paramMap), httpRes);
        if (!StringUtils.hasText(httpRes)) {
            log.info("获取视频预览地址失败，结果为空");
            return null;
        }
        var res = JSONUtil.toBean(httpRes, HikCloudVideoUrlResponseDTO.class);
        if (HttpStatus.OK.value() != res.getCode()) {
            log.info("获取视频预览地址失败，status: {}, msg: {}", res.getCode(), res.getMessage());
            return null;
        }
        return res.getData().getUrl();

    }

    /**
     * 获取取流token
     *
     * @return
     */
    public String getStreamToken() {

        //String token = getToken(client_id, client_secret, grant_type);
        var token = getHikToken();

        String result = HttpRequest.get(URI_STREAM_TOKEN_GET).bearerAuth(token).timeout(6000).execute().body();

        return JSONUtil.parseObj(result).getJSONObject("data").getStr("token");
    }

    // 消息处理

    public String getConsumerId() {
        String consumerId = redisUtils.getCacheObject(HIK_CLOUD_REDIS_CONSUMER_ID_KEY);
        if (consumerId != null) {
            return consumerId;
        }
        var token = getHikToken();
        var res = HttpRequest.post(URI_HIK_CLOUD_CONSUMER_POST.replace("{groupId}",
                        mqProperties.getConsumer().getGroupId()))
                .bearerAuth(token)
                .form(Map.of())
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud get consumer id result {}", res);
        var consumerIdResponseDTO = JSONUtil.toBean(res, HikCloudConsumerIdResponseDTO.class);
        if (!consumerIdResponseDTO.getSuccess()) {
            return null;
        }
        consumerId = consumerIdResponseDTO.getData().getConsumerId();
        redisUtils.setCacheObject(HIK_CLOUD_REDIS_CONSUMER_ID_KEY, consumerId, 250L, TimeUnit.SECONDS);
        return consumerId;
    }

    public HikCloudMessageResponseDTO listConsumeMessage(String consumerId) {
        var token = getHikToken();
        var res = HttpRequest.post(URI_HIK_CLOUD_CONSUMER_CONSUME_POST)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .bearerAuth(token)
                .form(Map.of("consumerId", consumerId, "autoCommit", "true"))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud get message consumer: {}, res {}", consumerId, res);
        return JSONUtil.toBean(res, HikCloudMessageResponseDTO.class);
    }


    // Token 相关

    private String getHikToken() {
        return getToken(properties.getClientId(), properties.getClientSecret(), properties.getGrantType());
    }

    /**
     * 获取token
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @param grantType    授权类型
     * @return token
     */
    private String getToken(String clientId, String clientSecret, String grantType) {

        String token = redisUtils.getCacheObject(TOKEN_HIK_YUNMOU_ACCESSTOKEN);

        if (StrUtil.isEmpty(token)) {

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("client_id", clientId);
            paramMap.put("client_secret", clientSecret);
            paramMap.put("grant_type", grantType);

            String result = HttpUtil.post(URI_TOKEN_POST, paramMap, 6000);

            JSONObject respObj = JSONUtil.parseObj(result);
            //token
            token = respObj.getStr("access_token");
            //过期时间 单位 s
            Long expires_in = respObj.getLong("expires_in");

            redisUtils.setCacheObject(TOKEN_HIK_YUNMOU_ACCESSTOKEN, token, expires_in, TimeUnit.SECONDS);
        }

        return token;
    }

    // 云眸项目管理

    /**
     * 获取项目列表 需要开通云录制
     *
     * @param requestDTO 请求参数
     * @return 结果
     */
    public HikCloudProjectIdResponseDTO createProject(HikCloudProjectCreateRequestDTO requestDTO) {
        var res = HttpRequest.post(URI_PROJECT_CREATE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bearerAuth(getHikToken())
                .body(JSONUtil.toJsonStr(requestDTO))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud create project uri: {}, body: {}, result {}", URI_PROJECT_CREATE, JSONUtil.toJsonStr(requestDTO), res);
        return JSONUtil.toBean(res, HikCloudProjectIdResponseDTO.class);
    }

    /**
     * 获取历史视频或图片，  需要开通云录制
     *
     * @param task 任务
     * @return 结果
     */
    public HikCloudTaskIdResponseDTO doRecordHistoryVideo(TblCameraDownloadTaskEntity task,
                                                          TblCameraHikCloudProjectEntity project) {
        var request = HikCloudRecordVideoRequestDTO.builder()
                .projectId(project.getHikCloudProjectId())
                .fileId(task.getTaskCustomId())
                .channelNo(1)
                .deviceSerial(task.getCloudNumber())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .recType("local")
                .validateCode(null)
                .streamType(1)
                .sliceDuration(30)
                .build();
        var res = HttpRequest.post(URI_RECORD_VIDEO)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bearerAuth(getHikToken())
                .body(JSONUtil.toJsonStr(request))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud doRecordHistoryVideo uri: {}, body: {}, result {}", URI_RECORD_VIDEO,
                JSONUtil.toJsonStr(request), res);
        return JSONUtil.toBean(res, HikCloudTaskIdResponseDTO.class);

    }

    public HikCloudTaskIdResponseDTO doRecordHistoryImage(TblCameraDownloadTaskEntity task,
                                                          TblCameraHikCloudProjectEntity project) {
        var timePoint = StringUtils.hasText(task.getCollectTime())
                ? task.getCollectTime().replace("-", "").replace(" ", "").replace(":", "")
                : (StringUtils.hasText(task.getStartTime()) ? task.getStartTime() : null);
        var request = HikCloudRecordImageRequestDTO.builder()
                .projectId(project.getHikCloudProjectId())
                .deviceSerial(task.getCloudNumber())
                .channelNo(1)
                .validateCode(null)
                .recType("local")
                .devProto(null)
                .frameModel("0")
                .streamType(1)
                .timingPoints(timePoint)
                .build();
        var res = HttpRequest.post(URI_RECORD_IMAGE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bearerAuth(getHikToken())
                .body(JSONUtil.toJsonStr(request))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud doRecordHistoryImage uri: {}, body: {}, result {}", URI_RECORD_IMAGE,
                JSONUtil.toJsonStr(request), res);
        return JSONUtil.toBean(res, HikCloudTaskIdResponseDTO.class);

    }

    /**
     * 获取历史视频地址， 需要开通云录制
     *
     * @param task    任务
     * @param project 项目
     * @return 结果
     */
    public HikCloudFileDownloadResponseDTO getRecordFileDownloadUrl(TblCameraDownloadTaskEntity task,
                                                                    TblCameraHikCloudProjectEntity project) {
        var params = new HashMap<String, Object>(4) {{
            put("projectId", project.getHikCloudProjectId());
            put("fileId", task.getTaskCustomId());
        }};
        var res = HttpRequest.get(URI_RECORD_FILE_DOWNLOAD)
                .bearerAuth(getHikToken())
                .form(params)
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud getRecordFileDownloadUrl uri: {}, params: {}, result {}", URI_RECORD_FILE_DOWNLOAD, params, res);
        return JSONUtil.toBean(res, HikCloudFileDownloadResponseDTO.class);
    }

    /**
     * 国标级联 查询上级平台列表
     */
    public List<HikCloudCascadeConfigDTO> listCascadePlatform() {
        var request = new HashMap<String, Object>(8) {{
            put("businessCode", "open");
            put("pageNo", 1);
            put("pageSize", 500);
        }};
        var res = HttpRequest.post(URI_CASCADE_PLATFORM_LIST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bearerAuth(getHikToken())
                .body(JSONUtil.toJsonStr(request))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud get cascade platrom list uri: {}, body: {}, result {}", URI_CASCADE_PLATFORM_LIST, JSONUtil.toJsonStr(request), res);
        var resObj = JSONUtil.toBean(res, HikCloudPageResponseDTO.class);
        if (null == resObj || null == resObj.getData()) {
            return null;
        }
        return resObj.getData().getRows()
                .stream()
                .map(map -> JSONUtil.toBean(JSONUtil.toJsonStr(map), HikCloudCascadeConfigDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 国标级联 查询级联通道列表
     *
     * @param configId 级联配置id
     * @return 结果
     */
    public List<HikCloudCascadeChannelDTO> listCascadeChannel(String configId) {
        // 平台最大支持500条数据
        var pageSize = 500;
        var pageNo = 1;
        var res = listCascadeChannelWithPage(configId, pageNo, pageSize);
        if (null == res || null == res.getData()) {
            return null;
        }
        var resObj = res.getData().getRows()
                .stream()
                .map(map -> JSONUtil.toBean(JSONUtil.toJsonStr(map), HikCloudCascadeChannelDTO.class))
                .collect(Collectors.toList());
        // 有多页的则拼装数据
        if (res.getData().getTotalPage() > 1) {
            for (var i = 2; i <= res.getData().getTotalPage(); i++) {
                var tmp = listCascadeChannelWithPage(configId, i, pageSize);
                if (null != tmp && null != tmp.getData()) {
                    res.getData().getRows()
                            .stream()
                            .map(map -> JSONUtil.toBean(JSONUtil.toJsonStr(map), HikCloudCascadeChannelDTO.class))
                            .forEach(resObj::add);
                }
            }
        }
        return resObj;
    }


    /**
     * 国标级联 查询级联通道列表, 带页码信息
     *
     * @param configId 级联配置id
     * @param pageNo   页码
     * @param pageSize 页大小
     * @return 结果
     */
    private HikCloudPageResponseDTO listCascadeChannelWithPage(String configId, Integer pageNo, Integer pageSize) {
        var request = new HashMap<String, Object>(8) {{
            put("configId", configId);
            put("pageNo", pageNo);
            put("pageSize", pageSize);
        }};
        var res = HttpRequest.post(URI_CASCADE_CHANNEL_LIST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bearerAuth(getHikToken())
                .body(JSONUtil.toJsonStr(request))
                .timeout(6000)
                .execute()
                .body();
        log.info("Hik-Cloud get cascade channel list uri: {}, body: {}, result {}", URI_CASCADE_CHANNEL_LIST, JSONUtil.toJsonStr(request), res);
        return JSONUtil.toBean(res, HikCloudPageResponseDTO.class);
    }

}
