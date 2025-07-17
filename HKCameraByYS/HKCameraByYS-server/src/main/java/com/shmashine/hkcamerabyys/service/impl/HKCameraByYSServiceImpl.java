package com.shmashine.hkcamerabyys.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.common.dto.TblCameraDTO;
import com.shmashine.common.entity.CameraStatusRecordEntity;
import com.shmashine.common.entity.ImageRecognitionMattingConfigEntity;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.hkcamerabyys.client.entity.DownLoadByHKYSRequestBody;
import com.shmashine.hkcamerabyys.client.entity.TblHKCameraDownload;
import com.shmashine.hkcamerabyys.dao.HKCameraByYSDao;
import com.shmashine.hkcamerabyys.dao.TblElevatorMapper;
import com.shmashine.hkcamerabyys.entity.HikCameraAlarmConfig;
import com.shmashine.hkcamerabyys.properties.EndpointProperties;
import com.shmashine.hkcamerabyys.service.CameraServiceI;
import com.shmashine.hkcamerabyys.service.FaultService;
import com.shmashine.hkcamerabyys.service.HKCameraByYSService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * HKCameraByYSServiceImpl
 *
 * @author jiangheng
 * @version v1.0 - 2021/11/5 15:28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HKCameraByYSServiceImpl implements HKCameraByYSService {
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;
    private final HKCameraByYSDao hkCameraByYSDao;
    private final TblElevatorMapper elevatorMapper;
    private final EndpointProperties properties;
    private final CameraServiceI cameraService;
    private final FaultService faultService;
    private final AliOssProperties aliOssProperties;

    private static Logger cloudRecordingLog = LoggerFactory.getLogger("cloudRecordingLogger");

    //创建一个线程池
    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(3, 6,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "HKCameraByYSService");


    /**
     * appTokenUrl
     */
    public static final String APP_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";

    /**
     * 云存储视频转码录制接口
     */
    public static final String OPENYS_VIDEOSAVE_URL = "https://open.ys7.com/api/open/cloud/v1/video/save";

    /**
     * 图片抓拍接口
     */
    public static final String OPENYS_PICSAVE_URL = "https://open.ys7.com/api/open/cloud/v1/capture/save";

    /**
     * 语音文件下发
     */
    public static final String OPENYS_VOICE_SEND = "https://open.ys7.com/api/lapp/voice/send";

    /**
     * 语音文件查询接口
     */
    public static final String OPENYS_VOICE_QUERY = "https://open.ys7.com/api/lapp/voice/query";

    /**
     * 删除云录制文件
     */
    public static final String OPENYS_DLEFILE_URL = "https://open.ys7.com/api/open/cloud/v1/file";

    /**
     * 获取文件下载地址接口
     */
    public static final String OPENYS_GET_DOWNLOAD_URL = "https://open.ys7.com/api/open/cloud/v1/file/download";

    /**
     * 删除文件接口
     */
    public static final String OPENYS_DELETE_FILE_URL = "https://open.ys7.com/api/open/cloud/v1/file";


    /**
     * 删除文件接口
     */
    public static final String OPENYS_DEVICE_INFO_URL = "https://open.ys7.com/api/lapp/device/info";

    /**
     * 创建云存储项目接口
     */
    public static final String OPENYS_CREATPROJECT_URL = "https://open.ys7.com/api/open/cloud/v1/project/";

    /**
     * appKey
     */
    public static final String APP_KEY = "8374cfb69acd473d8b4a65c8837c364a";
    /**
     * appSecret
     */
    public static final String APP_SECRET = "25f086df116c78899863c7fc0b8e24ae";


    /**
     * 下载视频
     *
     * @param downLoadByHKYSRequestBody 请求体
     */
    @Override
    public ResponseEntity downloadVideoFile(DownLoadByHKYSRequestBody downLoadByHKYSRequestBody) {

        //未安装梯不下载视频
        Boolean isNeedDownVideo = elevatorMapper.checkCanDownMedia(downLoadByHKYSRequestBody.getElevatorCode());

        if (!isNeedDownVideo) {
            return ResponseEntity.ok("未安装梯不下载视频！");
        }

        //根据故障类型构建视频下载开始时间和结束时间
        String faultType = downLoadByHKYSRequestBody.getFaultType();
        Date occurTime = downLoadByHKYSRequestBody.getOccurTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(occurTime);
        Date start;
        Date end;
        if ("7".equals(faultType) || "8".equals(faultType)) {
            calendar.add(Calendar.SECOND, -120);
            start = calendar.getTime();
            calendar.setTime(occurTime);
            calendar.add(Calendar.SECOND, +600);
            end = calendar.getTime();
        } else if ("37".equals(faultType)) {
            start = calendar.getTime();
            calendar.setTime(occurTime);
            calendar.add(Calendar.SECOND, +7);
            end = calendar.getTime();
        } else {
            calendar.add(Calendar.SECOND, -15);
            start = calendar.getTime();
            calendar.setTime(occurTime);
            calendar.add(Calendar.SECOND, +45);
            end = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String startTime = simpleDateFormat.format(start);
        String endTime = simpleDateFormat.format(end);

        TblHKCameraDownload hkCameraDownload = new TblHKCameraDownload();
        hkCameraDownload.setId(IdUtil.getSnowflake(1, 1).nextIdStr());
        hkCameraDownload.setElevatorCode(downLoadByHKYSRequestBody.getElevatorCode());
        hkCameraDownload.setSerialNumber(downLoadByHKYSRequestBody.getDeviceSerial());
        hkCameraDownload.setFaultId(downLoadByHKYSRequestBody.getFaultId());
        hkCameraDownload.setFaultType(downLoadByHKYSRequestBody.getFaultType());
        hkCameraDownload.setFileType(1);
        hkCameraDownload.setStartTime(startTime);
        hkCameraDownload.setEndTime(endTime);
        hkCameraDownload.setRequestFailedNum(0);
        hkCameraDownload.setUploadFailedNum(0);
        hkCameraDownload.setCreateTime(new Date());
        hkCameraDownload.setModifyTime(new Date());

        //等待下载
        addVideoTask(hkCameraDownload);

        return ResponseEntity.ok("正在下载视频...");
    }


    /**
     * 下载图片
     *
     * @param downLoadByHKYSRequestBody 图片下载请求体
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity downloadPictureFile(DownLoadByHKYSRequestBody downLoadByHKYSRequestBody) {

        //获取token
        String ezopenToken = getEzopenToken();

        TblHKCameraDownload hkCameraDownload = new TblHKCameraDownload();
        hkCameraDownload.setId(IdUtil.getSnowflake(1, 1).nextIdStr());
        hkCameraDownload.setElevatorCode(downLoadByHKYSRequestBody.getElevatorCode());
        hkCameraDownload.setSerialNumber(downLoadByHKYSRequestBody.getDeviceSerial());
        hkCameraDownload.setFaultId(downLoadByHKYSRequestBody.getFaultId());
        hkCameraDownload.setFaultType(downLoadByHKYSRequestBody.getFaultType());
        hkCameraDownload.setFileType(0);
        hkCameraDownload.setRequestFailedNum(0);
        hkCameraDownload.setUploadFailedNum(0);
        hkCameraDownload.setCreateTime(new Date());
        hkCameraDownload.setModifyTime(new Date());

        HashMap hashMap = getPic(ezopenToken, hkCameraDownload);

        return ResponseEntity.ok(hashMap);
    }

    /**
     * 定时下载故障取证文件
     */
    @Override
    public void downloadFaultFileTask() {

        //获取需要取证的文件
        List<TblHKCameraDownload> tblHKCameraDownloadList = hkCameraByYSDao.findNeedDownloadFileList();

        //获取token
        String ezopenToken = getEzopenToken();

        for (TblHKCameraDownload tblHKCameraDownload : tblHKCameraDownloadList) {

            executorService.submit(() -> {
                try {
                    upload(ezopenToken, tblHKCameraDownload);
                } catch (Exception e) {
                    log.error("下载视频失败，faultId：{}，errormsg：{}", tblHKCameraDownload.getFaultId(), e.getMessage());
                }

            });
        }
    }


    /**
     * 下载失败重试
     */
    @Override
    public void retryDownloadFailReportTask() {

        //获取需要重试记录
        List<TblHKCameraDownload> tblHKCameraDownloadList = hkCameraByYSDao.retryDownloadFailReportTask();

        //获取token
        String ezopenToken = getEzopenToken();

        //进行重试
        for (TblHKCameraDownload tblHKCameraDownload : tblHKCameraDownloadList) {

            executorService.submit(() -> {

                RLock lock = redissonClient.getLock("HIK_CAMERA_YS_REQUEST_FAULT_FILE_LOCK"
                        + tblHKCameraDownload.getFaultId());

                try {

                    //尝试加锁，最多等待100秒，上锁以后3分钟自动解锁
                    if (lock.tryLock(100, 180, TimeUnit.SECONDS)) {

                        try {
                            Integer fileType = tblHKCameraDownload.getFileType();

                            /*视频处理*/
                            if (fileType == 1) {

                                getVideo(ezopenToken, tblHKCameraDownload);

                            } else {     /*图片处理*/
                                getPic(ezopenToken, tblHKCameraDownload);
                            }

                        } finally {
                            lock.unlock();
                        }

                    }

                } catch (InterruptedException e) {
                    log.error("retryDownloadFailReportTask错误，error：{}", ExceptionUtils.getStackTrace(e));
                }

            });
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String videoFileReDownload(String faultId, String startTime, String endTime) {

        //时间格式化
        DateTime startDate = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss");
        startTime = DateUtil.format(startDate, "yyyyMMddHHmmss");
        DateTime endDate = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss");
        endTime = DateUtil.format(endDate, "yyyyMMddHHmmss");

        //删除萤石录制文件
        delYSFile(faultId + "_mp4");

        //获取基本信息
        HashMap<String, Object> downloadInfo = hkCameraByYSDao.queryDownloadInfoById(faultId);

        //如果为雄迈摄像头
        if (2 == (int) downloadInfo.get("cameraType")) {
            return "雄迈摄像头暂不支持自动下载，请手动下载上传";
        }

        //修改记录等待重新下载
        TblHKCameraDownload hkCameraDownload = hkCameraByYSDao.queryHkDownloadReportById(faultId, 1);

        if (hkCameraDownload == null) {

            hkCameraDownload = new TblHKCameraDownload();
            hkCameraDownload.setId(IdUtil.getSnowflakeNextIdStr());
            hkCameraDownload.setElevatorCode((String) downloadInfo.get("elevatorCode"));
            hkCameraDownload.setSerialNumber((String) downloadInfo.get("deviceSerial"));
            hkCameraDownload.setFaultId(faultId);
            hkCameraDownload.setFaultType((String) downloadInfo.get("faultType"));
            hkCameraDownload.setFileType(1);
            hkCameraDownload.setRequestFailedNum(0);
            hkCameraDownload.setUploadFailedNum(0);
            hkCameraDownload.setCreateTime(new Date());
            hkCameraDownload.setModifyTime(new Date());
            hkCameraDownload.setFileType(1);
            hkCameraDownload.setStartTime(startTime);
            hkCameraDownload.setEndTime(endTime);
            hkCameraDownload.setFileStatus(0);

            hkCameraByYSDao.insertHKCameraDownload(hkCameraDownload);

        } else {
            hkCameraDownload.setModifyTime(new Date());
            hkCameraDownload.setStartTime(startTime);
            hkCameraDownload.setEndTime(endTime);
            hkCameraDownload.setFileStatus(0);
            //更新记录表状态
            hkCameraByYSDao.updateDownloadReport(hkCameraDownload);
        }

        //删除已经取证文件记录
        try {
            hkCameraByYSDao.delSysFileById(faultId, 1);
            String fileName = ((String) downloadInfo.get("url")).substring(48);
            //oss文件删除
            OssInternalUtils.delFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "正在重新下载......";
    }

    @Override
    public void freeTimeRetryDownloadTask() {

        log.info("闲时重新下载失败文件");

        //录制失败重新录制
        hkCameraByYSDao.freeTimeRetryDownload();

        //录制文件问题删除文件重新录制
        List<TblHKCameraDownload> tblHKCameraDownloadList = hkCameraByYSDao.getFiledFileList();

        for (TblHKCameraDownload tblHKCameraDownload : tblHKCameraDownloadList) {

            //删除文件等待重新录制
            String delUrl = UriComponentsBuilder
                    .fromHttpUrl(OPENYS_DELETE_FILE_URL)
                    .queryParam("accessToken", getEzopenToken())
                    .queryParam("fileId", tblHKCameraDownload.getFaultId())
                    .queryParam("projectId", "shmashine_faultFile")
                    .build()
                    .encode()
                    .toString();

            try {
                restTemplate.delete(delUrl, String.class);
            } catch (RestClientException e) {
                e.printStackTrace();
            }

            //更新记录表状态
            tblHKCameraDownload.setUploadFailedNum(0);
            tblHKCameraDownload.setFileStatus(3);
            tblHKCameraDownload.setModifyTime(new Date());
            hkCameraByYSDao.updateDownloadReport(tblHKCameraDownload);
        }

    }

    @Override
    public String ysPlatformNotify(JSONObject requestBody) {

        String type = requestBody.getJSONObject("header").getString("type");

        String messageId = switch (type) {

            //云录制消息
            case "ys.open.cloud" -> cloudRecording(requestBody);
            //上下线消息
            case "ys.onoffline" -> onOffline(requestBody);
            //告警消息, ISAPI 上行消息, 呼叫消息, 托管设备变更消息, 国标告警消息, NVR通道设备上下线消息
            case "ys.alarm" -> alarm(requestBody);
            default -> {
                log.info("萤石平台推送-未知消息，body：{}", requestBody);
                yield requestBody.getJSONObject("header").getString("messageId");
            }
        };

        return JSON.toJSONString(Map.of("messageId", messageId));
    }

    @Override
    public ResponseEntity<HashMap<String, String>> getElevatorPicByElevators(List<String> elevatorCodes) {

        //获取token
        String ezopenToken = getEzopenToken();

        HashMap<String, String> resp = new HashMap<>();

        elevatorCodes.forEach(elevatorCode -> {

            try {
                //获取摄像头信息
                TblCameraDTO cameraInfo = hkCameraByYSDao.getCameraInfoByElevatorCode(elevatorCode);

                //构建请求参数
                Map requestParam = Map.of("accessToken", ezopenToken,
                        "channelNo", 1,
                        "deviceSerial", cameraInfo.getCloudNumber(),
                        "fileId", IdUtil.getSnowflakeNextIdStr(),
                        "projectId", "shmashine_faultFile");

                String respBody = HttpUtil.post(OPENYS_PICSAVE_URL, requestParam);
                String picurl = JSON.parseObject(respBody, JSONObject.class).getString("data");

                resp.put(elevatorCode, picurl);

            } catch (Exception e) {
                log.error("获取电梯图片异常, error:{}", ExceptionUtils.getStackTrace(e));
            }

        });

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<String> getLivePictureByElevatorCode(String elevatorCode) {

        //获取token
        String ezopenToken = getEzopenToken();

        //获取摄像头信息
        TblCameraDTO cameraInfo = hkCameraByYSDao.getCameraInfoByElevatorCode(elevatorCode);

        //构建请求参数
        Map requestParam = Map.of("accessToken", ezopenToken,
                "channelNo", 1,
                "deviceSerial", cameraInfo.getCloudNumber(),
                "fileId", IdUtil.getSnowflakeNextIdStr(),
                "projectId", "shmashine_faultFile");

        String respBody = HttpUtil.post(OPENYS_PICSAVE_URL, requestParam);
        String picurl = JSON.parseObject(respBody, JSONObject.class).getString("data");
        return ResponseEntity.ok(picurl);
    }

    @Override
    public ResponseEntity<String> pushCameraVoice(String vCloudNumber, String faultType) {

        //获取token
        String ezopenToken = getEzopenToken();

        //获取语音文件地址
        String voiceFileRespBody = HttpUtil.post(OPENYS_VOICE_QUERY,
                Map.of("accessToken", ezopenToken, "voiceName", "defaultTrappedPeopleVoice"));

        var voiceFileRespObj = JSON.parseObject(voiceFileRespBody);

        if ("200".equals(voiceFileRespObj.getString("code"))) {

            String fileUrl = voiceFileRespObj.getJSONArray("data").getJSONObject(0).getString("fileUrl");

            //构建请求参数
            Map requestParam = Map.of("accessToken", ezopenToken,
                    "channelNo", 1,
                    "deviceSerial", vCloudNumber,
                    "fileUrl", fileUrl);

            String respBody = HttpUtil.post(OPENYS_VOICE_SEND, requestParam);

            log.info("语音文件下发成功 - 请求参数：{} - 返回结果：{}", requestParam, respBody);

            return ResponseEntity.ok(respBody);
        }

        log.error("语音文件下发失败 - 获取语音文件地址失败 - voiceFileRespBody：{}", voiceFileRespBody);

        return ResponseEntity.ok("获取语音文件地址失败");
    }

    /**
     * 绘制并填充多边形
     *
     * @param srcImagePath 源图像路径
     * @param points       坐标数组
     * @param imageFormat  写入图形格式
     */
    /*public static void drawAndAlphaPolygon(String srcImagePath, List<JSONObject> points,
                                           String imageFormat, String toPath) {
        FileOutputStream fos = null;
        try {
            //获取图片
            BufferedImage image = ImageIO.read(new URL(srcImagePath));
            //根据xy点坐标绘制闭合多边形
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.BLACK);

            for (JSONObject point : points) {
                int[] xpoints = point.get("x", int[].class);
                int[] ypoints = point.get("y", int[].class);
                g2d.fillPolygon(xpoints, ypoints, xpoints.length);
            }

            fos = new FileOutputStream(toPath);
            ImageIO.write(image, imageFormat, fos);

            g2d.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/

    /**
     * 获取图片分辨率
     *
     * @param imgPath 图片地址
     * @return
     */
    /*public static Integer[] getImgSize(String imgPath) {
        Integer[] size = new Integer[2];
        try {
            //BufferedImage image = ImageIO.read(new File(imgPath));
            BufferedImage image = ImageIO.read(new URL(imgPath));
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }*/


    ///////////////////////////////////////private////////////////////////////////////////////////////

    /**
     * 文件上传阿里云
     *
     * @param fileUrl  文件路径
     * @param faultId  故障id
     * @param fileType 文件类型
     */
    private String upLoadFile(String fileUrl, String faultId, String fileLastName, String fileType) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        log.info("------开始上传阿里云，故障id：[{}]", faultId);
        String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + faultId + fileLastName;

        OssInternalUtils.setOSS(FileUtil.getBytesByRemotePath(fileUrl), uri, aliOssProperties.getUseInternal());
        log.info("------上传阿里云成功，故障id：[{}]", faultId);

        //落表
        hkCameraByYSDao.delSysFileById(faultId, Integer.parseInt(fileType));
        hkCameraByYSDao.addTblSysFile(IdUtil.getSnowflake(1, 1).nextIdStr(),
                faultId, OssInternalUtils.OSS_URL + uri, faultId + fileLastName, 2, new Date(), fileType);
        log.info("------故障取证文件落表成功成功，故障id：[{}]", faultId);
        return OssInternalUtils.OSS_URL + uri;
    }

    /**
     * 获取萤石云token
     *
     * @return String
     */
    private String getEzopenToken() {
        // 拼接请求参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(APP_TOKEN_URL)
                .queryParam("appKey", APP_KEY)
                .queryParam("appSecret", APP_SECRET);

        Map<String, Object> queryMap = new HashMap<>();

        ResponseEntity<String> res = restTemplate.postForEntity(
                builder.build().encode().toUri(),
                queryMap,
                String.class);

        JSONObject body = JSONObject.parseObject(res.getBody());
        return body.getJSONObject("data").getString("accessToken");
    }


    /**
     * 添加下载视频任务
     *
     * @param hkCameraDownload 视频下载任务
     * @return ResponseEntity
     */
    private void addVideoTask(TblHKCameraDownload hkCameraDownload) {

        hkCameraDownload.setFileStatus(0);

        hkCameraByYSDao.insertHKCameraDownload(hkCameraDownload);
    }

    /**
     * 下载视频
     *
     * @param ezopenToken      萤石云token
     * @param hkCameraDownload 视频下载任务
     * @return ResponseEntity
     */
    private HashMap getVideo(String ezopenToken, TblHKCameraDownload hkCameraDownload) {

        //先check摄像头是否在线
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> mulmap = new LinkedMultiValueMap<>();
        mulmap.add("accessToken", ezopenToken);
        mulmap.add("deviceSerial", hkCameraDownload.getSerialNumber());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mulmap, headers);
        JSONObject res = restTemplate.postForObject(OPENYS_DEVICE_INFO_URL,
                request, JSONObject.class);
        //在线状态：0-不在线，1-在线
        Integer status = res.getJSONObject("data").getInteger("status");

        if (status == 0) {

            log.info("......设备不在线! 等待上线, 设备序列号{}, 故障id:{}........",
                    hkCameraDownload.getSerialNumber(), hkCameraDownload.getFaultId());

            hkCameraDownload.setModifyTime(new Date());
            hkCameraDownload.setErrMessage("设备不在线");
            hkCameraByYSDao.insertHKCameraDownload(hkCameraDownload);

            return null;
        }

        HashMap hashMap = null;
        HashMap meta = null;
        Integer code = null;

        //构建请求参数
        mulmap.add("channelNo", 1);
        mulmap.add("fileId", hkCameraDownload.getFaultId() + "_mp4");
        mulmap.add("projectId", "shmashine_faultFile");
        mulmap.add("recType", "local");
        mulmap.add("voiceSwitch", "1");
        mulmap.add("startTime", hkCameraDownload.getStartTime());
        mulmap.add("endTime", hkCameraDownload.getEndTime());


        try {
            HttpEntity<MultiValueMap<String, Object>> request1 = new HttpEntity<>(mulmap, headers);

            hashMap = restTemplate.postForObject(OPENYS_VIDEOSAVE_URL, request1, HashMap.class);
            meta = (HashMap) hashMap.get("meta");
            code = (Integer) meta.get("code");
        } catch (RestClientException e) {

            log.info("......下载视频操作失败,等待重新下载,Exception:{},故障id:{}.........",
                    e.getMessage(), hkCameraDownload.getFaultId());

            //落表等待下载
            hkCameraDownload.setErrMessage(meta.get("message").toString());
            hkCameraDownload.setFileStatus(3);
            hkCameraDownload.setReturnCode(code);
            hkCameraDownload.setRequestFailedNum(hkCameraDownload.getRequestFailedNum() + 1);
        }

        if (200 == code) {

            log.info("......下载视频操作成功,正在下载视频,故障id:{}........", hkCameraDownload.getFaultId());
            //落表（正在下载）
            hkCameraDownload.setFileStatus(2);
            hkCameraDownload.setReturnCode(200);

        } else if (400 == code && "文件已经存在".equals(meta.get("message"))) {
            hkCameraDownload.setErrMessage(meta.get("message").toString());
            hkCameraDownload.setFileStatus(4);
            hkCameraDownload.setReturnCode(code);

        } else {

            log.info("......下载视频操作失败,等待重新下载,故障id:{}.........", hkCameraDownload.getFaultId());
            //落表等待下载
            hkCameraDownload.setFileStatus(3);
            hkCameraDownload.setReturnCode(code);
            hkCameraDownload.setRequestFailedNum(hkCameraDownload.getRequestFailedNum() + 1);

        }

        //添加萤石云下载历史视频记录
        hkCameraDownload.setModifyTime(new Date());
        int i = hkCameraByYSDao.insertHKCameraDownload(hkCameraDownload);

        if (i == 0) {
            log.info("......数据库操作失败，故障信息：{}", hkCameraDownload.toString());
        } else {
            log.info("......数据库操作成功，等待下载........");
        }
        return hashMap;
    }

    /**
     * 下载图片
     *
     * @param ezopenToken      萤石云token
     * @param hkCameraDownload 视频下载任务
     * @return ResponseEntity
     */
    private HashMap getPic(String ezopenToken, TblHKCameraDownload hkCameraDownload) {
        String picUrl = null;
        HashMap hashMap = null;
        HashMap meta = null;
        Integer code = null;
        //构建请求参数
        MultiValueMap<String, Object> mulmap = new LinkedMultiValueMap<>();
        mulmap.add("accessToken", ezopenToken);
        mulmap.add("channelNo", 1);
        mulmap.add("deviceSerial", hkCameraDownload.getSerialNumber());
        mulmap.add("fileId", hkCameraDownload.getFaultId() + "_jpg");
        mulmap.add("projectId", "shmashine_faultFile");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mulmap, headers);

        try {
            hashMap = restTemplate.postForObject(OPENYS_PICSAVE_URL, request, HashMap.class);
            meta = (HashMap) hashMap.get("meta");
            code = (Integer) meta.get("code");
            picUrl = (String) hashMap.get("data");
        } catch (RestClientException e) {

            log.info("......下载图片操作失败,等待重新下载,Exception:{},故障id:{}.........",
                    e.getMessage(), hkCameraDownload.getFaultId());

            //落表等待下载
            hkCameraDownload.setFileStatus(3);
            hkCameraDownload.setRequestFailedNum(hkCameraDownload.getRequestFailedNum() + 1);
        }

        if (picUrl != null && code != null && 200 == code) {

            log.info("......下载图片操作成功,准备保存图片,故障id:{}........", hkCameraDownload.getFaultId());

            //上传oss
            String mxUrl = upLoadFile(picUrl, hkCameraDownload.getFaultId(), ".jpg", "0");

            //图像二次识别
            imageIdentification(mxUrl, hkCameraDownload.getFaultType(), hkCameraDownload.getFaultId(),
                    hkCameraDownload.getElevatorCode());

            log.info("调用二次识别成功，图片地址：" + mxUrl + "故障id" + hkCameraDownload.getFaultId());

            //落表（下载成功）
            hkCameraDownload.setFileStatus(1);
            hkCameraDownload.setReturnCode(200);


            // 发送给Sender模块，通知其下载完成
            sendTaskFinishedToSenderService(hkCameraDownload.getFaultId());

        } else if (code != null && 400 == code && "文件已经存在".equals(meta.get("message"))) {
            hkCameraDownload.setFileStatus(4);
            hkCameraDownload.setReturnCode(code);

        } else {
            log.info("......下载图片操作失败,等待重新下载,故障id:{}.........", hkCameraDownload.getFaultId());
            //落表等待下载
            hkCameraDownload.setFileStatus(3);
            hkCameraDownload.setReturnCode(code);
            hkCameraDownload.setRequestFailedNum(hkCameraDownload.getRequestFailedNum() + 1);
        }

        //添加萤石云下载历史视频记录
        hkCameraDownload.setModifyTime(new Date());
        hkCameraByYSDao.insertHKCameraDownload(hkCameraDownload);

        return hashMap;
    }

    //CHECKSTYLE:OFF
    private void upload(String ezopenToken, TblHKCameraDownload tblHKCameraDownload) {

        RLock lock = redissonClient.getLock("HIK_CAMERA_YS_DOWNLOAD_FAULT_FILE_LOCK"
                + tblHKCameraDownload.getFaultId());

        //尝试加锁，最多等待100秒，上锁以后30分钟自动解锁
        try {

            if (lock.tryLock(100, 1800, TimeUnit.SECONDS)) {

                try {
                    //查询文件是否下载成功
                    //构建请求参数
                    MultiValueMap<String, Object> mulmap = new LinkedMultiValueMap<>();
                    mulmap.add("accessToken", ezopenToken);
                    mulmap.add("fileId", tblHKCameraDownload.getFaultId());
                    mulmap.add("projectId", "shmashine_faultFile");

                    String fileId = null;
                    String fileLastName = null;
                    if (tblHKCameraDownload.getFileType() == 1) {
                        fileId = tblHKCameraDownload.getFaultId() + "_mp4";
                        fileLastName = ".mp4";
                    }
                    if (tblHKCameraDownload.getFileType() == 0) {
                        fileId = tblHKCameraDownload.getFaultId() + "_jpg";
                        fileLastName = ".jpg";

                    }

                    String url = UriComponentsBuilder
                            .fromHttpUrl(OPENYS_GET_DOWNLOAD_URL)
                            .queryParam("accessToken", ezopenToken)
                            .queryParam("fileId", fileId)
                            .queryParam("projectId", "shmashine_faultFile")
                            .build()
                            .encode()
                            .toString();

                    HashMap hashMap = restTemplate.getForObject(url, HashMap.class);

                    HashMap meta = (HashMap) hashMap.get("meta");
                    int code = (Integer) meta.get("code");

                    if (200 == code) {
                        HashMap data = (HashMap) hashMap.get("data");
                        List<String> urls = (List<String>) data.get("urls");
                        if (urls != null) {
                            //上传oss
                            String mxUrl = upLoadFile(urls.get(0), tblHKCameraDownload.getFaultId(), fileLastName, "1");
                            //上传成功
                            tblHKCameraDownload.setFileStatus(1);
                            //调用图像识别
                            if (tblHKCameraDownload.getFileType() == 0) {
                                imageIdentification(mxUrl, tblHKCameraDownload.getFaultType(),
                                        tblHKCameraDownload.getFaultId(), tblHKCameraDownload.getElevatorCode());
                            }

                            // 发送给Sender模块，通知其下载完成
                            sendTaskFinishedToSenderService(tblHKCameraDownload.getFaultId());

                        } else {

                            log.info("获取录制文件URL为空，删除文件等待重新录制，faultId：{}", tblHKCameraDownload.getFaultId());

                            //删除文件等待重新录制
                            String delUrl = UriComponentsBuilder
                                    .fromHttpUrl(OPENYS_DELETE_FILE_URL)
                                    .queryParam("accessToken", ezopenToken)
                                    .queryParam("fileId", fileId)
                                    .queryParam("projectId", "shmashine_faultFile")
                                    .build()
                                    .encode()
                                    .toString();

                            try {
                                restTemplate.delete(delUrl, String.class);
                            } catch (RestClientException e) {
                                e.printStackTrace();
                            }

                            tblHKCameraDownload.setErrMessage("获取录制文件URL为空");
                            tblHKCameraDownload.setFileStatus(3);
                            tblHKCameraDownload.setUploadFailedNum(tblHKCameraDownload.getUploadFailedNum() + 1);
                        }

                    }
                    //资源不存在
                    if (404 == code && "文件上传失败".equals(meta.get("message"))) {

                        log.info("文件上传失败，删除文件等待重新录制，faultId：{}", tblHKCameraDownload.getFaultId());

                        //删除文件等待重新录制
                        String delUrl = UriComponentsBuilder
                                .fromHttpUrl(OPENYS_DELETE_FILE_URL)
                                .queryParam("accessToken", ezopenToken)
                                .queryParam("fileId", fileId)
                                .queryParam("projectId", "shmashine_faultFile")
                                .build()
                                .encode()
                                .toString();

                        try {
                            restTemplate.delete(delUrl, String.class);
                        } catch (RestClientException e) {
                            log.error("restTemplate调用删除文件错误，error：{}", ExceptionUtils.getStackTrace(e));
                        }

                        //重新下载
                        tblHKCameraDownload.setUploadFailedNum(tblHKCameraDownload.getUploadFailedNum() + 1);
                        tblHKCameraDownload.setFileStatus(3);
                        tblHKCameraDownload.setReturnCode(404);
                    }

                    tblHKCameraDownload.setModifyTime(new Date());
                    //更新记录表状态
                    hkCameraByYSDao.updateDownloadReport(tblHKCameraDownload);
                } finally {
                    lock.unlock();
                }

            } else {
                log.info("文件正在上传，faultId：{}", tblHKCameraDownload.getFaultId());
            }

        } catch (InterruptedException e) {
            log.error("upload错误，error：{}", ExceptionUtils.getStackTrace(e));
        }

    }      //CHECKSTYLE:ON

    /**
     * 删除萤石录制文件
     *
     * @param faultId 故障id
     */
    private void delYSFile(String faultId) {

        //获取token
        String ezopenToken = getEzopenToken();

        //构建请求参数
        MultiValueMap<String, Object> mulmap = new LinkedMultiValueMap<>();
        mulmap.add("accessToken", ezopenToken);
        mulmap.add("fileId", faultId);
        mulmap.add("projectId", "shmashine_faultFile");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mulmap, headers);

        restTemplate.exchange(OPENYS_DLEFILE_URL, HttpMethod.DELETE, request, HashMap.class);

    }

    /**
     * 调用二次识别服务
     *
     * @param workOrderId 故障id
     * @param fileUrl     待识别图片url
     * @param type        识别类型
     */
    /*private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = "http://47.105.214.0:10089/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            HttpGet httpGet = new HttpGet(url);
            try {
                httpclient.execute(httpGet);
            } catch (IOException e) {
                //
            }
        });
        try {
            Thread.sleep(300);
            httpclient.close();
        } catch (InterruptedException | IOException e) {
            //
        }
    }*/

    /**
     * 调用图像识别
     *
     * @param picUrl 图片地址
     */
    private void imageIdentification(String picUrl, String faultType, String faultId, String elevatorCode) {

        try {
            if ("6".equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "person");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                        aliOssProperties.getUseInternal());
                log.info("非平层停梯调用二次识别确认，故障id【{}】", faultId);
            }

            if ("37".equals(faultType)) {
                // restTemplateSendMessage(faultId, picUrl, "motorcycle");
                ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE,
                        aliOssProperties.getUseInternal());
                log.info("电动车乘梯调用二次识别确认，故障id【{}】", faultId);
            }

            //困人
            if ("7".equals(faultType) || "8".equals(faultType)) {

                Long remove = redisTemplate.opsForZSet().remove("HLS:IMAGE", faultId);

                if (remove > 0) {

                    try {
                        //图片下载成功释放锁
                        RLock lock = redissonClient.getLock("AFRESDOWNLOADIMAGE_LOCK" + faultId);
                        lock.forceUnlock();
                        log.info("---------平层困人图片下载成功，释放锁------------");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //抠图上传阿里云并返回文件地址
                    picUrl = imageMatting(faultId, picUrl, faultType, elevatorCode);

                    //调用图像识别
                    // restTemplateSendMessage(faultId, picUrl, "person");
                    ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                            aliOssProperties.getUseInternal());
                }
                //添加识别标识
                redisTemplate.opsForZSet().add("HLS:IDENTIFICATION", faultId, System.currentTimeMillis() / 1000);
            }

        } catch (Exception e) {
            log.info("调用识别服务失败：e： {}，故障id：{}，故障类型：{}", e.getMessage(), faultId, faultType);
        }
    }

    private String imageMatting(String faultId, String picUrl, String faultType, String elevatorCode) {

        try {
            if (StrUtil.isBlank(elevatorCode)) {
                return picUrl;
            }
            //根据故障id或电梯抠图配置
            ImageRecognitionMattingConfigEntity config =
                    elevatorMapper.getImageMattingConfigByFaultType(elevatorCode, faultType);

            if (config == null) {
                return picUrl;
            }

            var realCoordinates = JSON.parseArray(config.getRealCoordinates(), JSONObject.class);

            if (realCoordinates == null || realCoordinates.isEmpty()) {

                if (StrUtil.isBlank(config.getCoordinates())) {
                    return picUrl;
                }

                //抠图坐标点
                var points = JSON.parseArray(config.getCoordinates(), JSONObject.class);
                if (points == null || points.isEmpty()) {
                    return picUrl;
                }

                //获取图片分辨率
                Integer[] imgSize = ImageIdentifyUtils.getImgSize(picUrl, aliOssProperties.getUseInternal());

                //计算实际坐标点
                realCoordinates = getRealCoordinates(points, config.getWidth(), config.getHeight(), imgSize);

                //更新配置实际坐标点
                elevatorMapper.updateImageMattingConfig(JSON.toJSONString(realCoordinates), config.getId());

            }

            //本地存储路径
            String toPath = "/media/" + faultId + "_imageMatting.jpg";
            //抠图
            ImageIdentifyUtils.drawAndAlphaPolygon(picUrl, realCoordinates, "jpg", toPath,
                    aliOssProperties.getUseInternal());

            //上传阿里云
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/")
                    + "/" + faultId + "_imageMatting.jpg";

            OssInternalUtils.setOSS(FileUtil.getBytesByFile(toPath), uri, aliOssProperties.getUseInternal());
            log.info("------上传阿里云成功，故障id：[{}]", faultId);

            //删除本地文件
            new File(toPath).delete();

            // return OssInternalUtils.getOssUrl(SpringContextUtil.isProdEnv()) + uri;
            return OssInternalUtils.extendOssUrl(uri);
        } catch (Exception e) {
            log.error("抠图失败,faultId:[{}],error:[{}]", faultId, ExceptionUtils.getStackTrace(e));
        }

        return picUrl;
    }

    /**
     * 发送任务完成消息给sender服务
     *
     * @param faultId 故障ID
     */
    private void sendTaskFinishedToSenderService(String faultId) {
        try {
            //拼接请求参数
            String url = properties.getSenderServer()
                    + "/sender/fault/resend/{faultId}/{cameraType}".replace("{faultId}", faultId)
                    .replace("{cameraType}", String.valueOf(CameraTypeEnum.HIK_EZVIZ.getCode()));
            var res = HttpUtil.get(url, 15000);
            log.info("发送任务完成消息给sender服务，故障id：[{}]，返回结果：[{}]", faultId, res);
        } catch (Exception e) {
            log.error("发送任务完成消息给sender服务失败，故障id：[{}]，error：{}", faultId, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 计算实际坐标点
     *
     * @param points  坐标点
     * @param width   图片宽度
     * @param height  图片高度
     * @param imgSize 图片分辨率
     */
    private List<JSONObject> getRealCoordinates(List<JSONObject> points, Integer width, Integer height,
                                                Integer[] imgSize) {

        var resPoints = points.stream().map(p -> {
            var xpoints = p.getObject("x", Integer[].class);
            var ypoints = p.getObject("y", Integer[].class);

            var res = Map.of("x", Arrays.stream(xpoints).mapToInt(x -> (x * imgSize[0]) / width).toArray(),
                    "y", Arrays.stream(ypoints).mapToInt(y -> (y * imgSize[1]) / height).toArray());

            return JSON.parseObject(JSON.toJSONString(res), JSONObject.class);
        }).toList();
        return resPoints;
    }

    /**
     * 萤石告警消息
     *
     * @param requestBody 请求体
     */
    private String alarm(JSONObject requestBody) {

        log.info("萤石平台推送-告警消息，body：{}", requestBody);

        String messageId = requestBody.getJSONObject("header").getString("messageId");

        JSONObject body = requestBody.getJSONObject("body");

        //告警类型 参考：https://open.ys7.com/help/981
        String alarmType = body.getString("alarmType");
        //设备自己生成的UUID
        String alarmId = body.getString("alarmId");
        //设备序列号
        String devSerial = body.getString("devSerial");
        //告警时间
        Date alarmTime = body.getDate("alarmTime");
        //告警图片
        var pictureList = body.getJSONArray("pictureList");
        if (StringUtils.hasText(alarmType)) {

            //获取摄像头故障告警配置
            HikCameraAlarmConfig alarmConfig = faultService.getCameraAlarmConfig(devSerial, alarmType);

            if (alarmConfig != null && StringUtils.hasText(alarmConfig.getFaultType())) {

                String url = null;
                if (pictureList != null && !pictureList.isEmpty()) {
                    var pictureObj = JSON.parseObject(pictureList.get(0).toString());
                    url = pictureObj.getString("url");
                }
                String elevatorId = alarmConfig.getElevatorId();
                String elevatorCode = alarmConfig.getElevatorCode();
                String faultType = alarmConfig.getFaultType();
                Integer faultStatus = alarmConfig.getFaultStatus();

                //上报故障
                switch (faultType) {
                    case "37" -> faultService.batteryCarFault(elevatorId, elevatorCode,
                            faultType, faultStatus, alarmTime, url);
                    case "7", "8" -> faultService.trappedPeopleFault(elevatorId,
                            elevatorCode, faultType, faultStatus, alarmTime, url);
                    default -> faultService.defaultFault(elevatorId, elevatorCode,
                            faultType, faultStatus, alarmTime, url);
                }

            }

        }

        return messageId;
    }


    /**
     * 设备上下线消息处理
     *
     * @param requestBody 请求体
     */
    private String onOffline(JSONObject requestBody) {

        log.info("萤石平台推送-上下线消息，body：{}", requestBody);

        String messageId = requestBody.getJSONObject("header").getString("messageId");

        JSONObject body = requestBody.getJSONObject("body");

        //设备序列号
        String subSerial = body.getString("subSerial");
        //发生的时间
        String occurTime = body.getString("occurTime");
        //设备名称
        String deviceName = body.getString("deviceName");
        //消息类型：OFFLINE-设备离线消息，ONLINE-设备上线消息
        Integer status = "ONLINE".equals(body.getString("msgType")) ? 1 : 0;

        var cameraStatusRecord = CameraStatusRecordEntity.builder()
                .id(IdUtil.getSnowflakeNextId()).messageId(messageId).serialNumber(subSerial)
                .occurTime(occurTime).deviceName(deviceName).status(status).createTime(DateTime.now())
                .build();

        //添加摄像头上下线记录
        hkCameraByYSDao.insertCameraStatusRecord(cameraStatusRecord);

        //修改摄像头在线状态
        hkCameraByYSDao.updateCameraStatus(cameraStatusRecord);

        //根据摄像头序列号获取elevatorId
        String elevatorId = hkCameraByYSDao.getElevatorIdBySerialNumber(subSerial);

        if (StringUtils.hasText(elevatorId)) {

            //更新设备(device表)在线状态&上下线时间
            Integer deviceUpdateNum = elevatorMapper.updateDeviceOnlineStatus(elevatorId, status, occurTime);

            if (deviceUpdateNum > 0) {
                //更新电梯(elevator表)在线状态
                elevatorMapper.updateOnlineStatus(elevatorId, status);
            }

        }
        return messageId;
    }


    /**
     * 云录像消息处理
     *
     * @param requestBody 请求体
     */
    private String cloudRecording(JSONObject requestBody) {

        cloudRecordingLog.info("萤石平台推送-云录制消息，body：{}", requestBody);

        String messageId = requestBody.getJSONObject("header").getString("messageId");

        JSONObject body = requestBody.getJSONObject("body");
        //项目Id String projectId = body.getString("projectId");
        //任务id
        String taskId = body.getString("taskId");
        //设备序列号 String deviceSerial = body.getString("deviceSerial");
        //通道号 String channel = body.getString("channel");
        //任务产生文件数量 String fileNum = body.getString("fileNum");
        //任务产生文件大小 String totalSize = body.getString("totalSize");
        //返回码
        String errorCode = body.getString("errorCode");
        //返回信息
        String errorMsg = body.getString("errrorMsg");
        /**
         * 任务状态：
         * 0,已完成：任务正常录制完成并且过程中没发生报错
         * 1,排队中：回放任务下发后由于当前服务器并发任务太高，需要排队处理
         * 2,进行中：任务进行中
         * 3,已结束：任务开始后由开发者主动结束任务
         * 4,异常结束：任务过程中发生过异常，即使在重试后录制的内容是完整的，依然会展示异常，视频任务可以通过录像完整度字段判断录制结果是否可用。
         * 5,暂停中：任务在开始后，设备出流路数达到了上限，导致无法取流，任务默认等待24小时，24小时后还未能解除设备出流限制时任务会失败并上报异常
         * 6,已取消：任务未达开始时间且开发者取消了任务
         * 7,未开始：任务未达开始时间
         */
        String taskStatus = body.getString("taskStatus");

        //录制结束时间  String actualEndTime = body.getString("actualEndTime");

        if (StringUtils.hasText(taskStatus)) {
            switch (taskStatus) {
                //已完成
                case "0" -> cloudRecordingSuccessful(taskId);
                case "1" -> cloudRecordingLog.info("云录制-排队中-等待任务执行，taskID【{}】", taskId);
                case "2" -> cloudRecordingLog.info("云录制-进行中-等待任务执行，taskID【{}】", taskId);
                //异常结束
                case "4" -> cloudRecordingFailed(taskId, errorCode, errorMsg);
                default -> {
                    //
                }
            }
        }

        return messageId;
    }

    private void cloudRecordingFailed(String taskId, String errorCode, String errorMsg) {
        cloudRecordingLog.info("云录制-失败-待重新录制，taskID【{}】", taskId);
        cameraService.cloudRecordingFailed(taskId, errorCode, errorMsg);
    }

    /**
     * 开始OSS上传任务
     *
     * @param taskId 萤石任务id
     */
    private void cloudRecordingSuccessful(String taskId) {
        cloudRecordingLog.info("云录制-成功-待上传OSS，CloudTaskId【{}】", taskId);
        cameraService.startOssUploadCameraFileByTaskId(taskId);
    }

}
