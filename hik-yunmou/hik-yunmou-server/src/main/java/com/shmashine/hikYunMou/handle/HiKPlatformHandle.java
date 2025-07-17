package com.shmashine.hikYunMou.handle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.enums.CameraTypeEnum;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.common.utils.RedisKeyUtils;
import com.shmashine.hikYunMou.constants.HikvPlatformConstants;
import com.shmashine.hikYunMou.entity.FileDownloadTbl;
import com.shmashine.hikYunMou.entity.HikCameraVideoFileDownloadTask;
import com.shmashine.hikYunMou.properties.EndpointProperties;
import com.shmashine.hikYunMou.service.FileDownloadService;
import com.shmashine.hikYunMou.service.HikCameraVideoFileDownloadTaskService;
import com.shmashine.hikYunMou.utils.HikPlatformUtil;
import com.shmashine.hikYunMou.utils.RedisUtils;
import com.shmashine.hikYunMou.videoHandle.VideoStreamDownloadHandle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author jiangheng
 * @create 2022/10/10 17:59
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class HiKPlatformHandle {
    private final VideoStreamDownloadHandle videoDownloadHandle;
    private final HikPlatformUtil hikPlatformUtil;
    private final HikCameraVideoFileDownloadTaskService videoFileDownloadTaskService;
    private final FileDownloadService fileDownloadService;
    private final RedisUtils redisUtils;
    private final RedissonClient redissonClient;
    private final EndpointProperties properties;
    private final AliOssProperties aliOssProperties;

    /**
     * 下载图片
     */
    public void downloadPictureFile(String elevatorCode, String faultId, String faultType, String deviceSerial) {
        log.info("HiKPlatformHandle-downloadPictureFile from downloadPictureFile, elevatorCode: {}, faultType:{}, deviceSerial: {} faultId:{}",
                elevatorCode, faultType, deviceSerial, faultId);
        try {
            downloadPicture(faultId, faultType, deviceSerial);

        } catch (Exception e) {

            log.error("------文件下载失败，故障id：[{}]，error:{}", faultId, e.getMessage());

            HikCameraVideoFileDownloadTask downloadTask = HikCameraVideoFileDownloadTask.builder()
                    .id(IdUtil.getSnowflakeNextId()).fileStatus(3).deviceSerial(deviceSerial)
                    .fileType(0).elevatorCode(elevatorCode)
                    .faultType(faultType).faultId(faultId)
                    .errorMsg(e.getMessage()).createTime(DateUtil.date())
                    .modifyTime(DateUtil.date()).build();

            videoFileDownloadTaskService.save(downloadTask);
        }

    }

    /**
     * 获取图片地址
     */
    public String getPictureURL(String deviceSerial) {

        return hikPlatformUtil.getPicture(deviceSerial);
    }


    /**
     * 添加下载故障视频任务
     *
     * @param elevatorCode
     * @param faultId
     * @param faultType
     */
    public void downloadFaultVideoFile(String deviceSerial, String elevatorCode, String faultId, String faultType, String faultTime) {

        log.info("收到故障下载请求, 故障id:{}, faultType:{}, occurTime:{}", faultId, faultType, faultTime);

        String startTime;
        String endTime;

        DateTime occurTime = DateUtil.parse(faultTime, DatePattern.NORM_DATETIME_PATTERN);

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
            calendar.add(Calendar.SECOND, +5);
            end = calendar.getTime();
        } else {
            calendar.add(Calendar.SECOND, -15);
            start = calendar.getTime();
            calendar.setTime(occurTime);
            calendar.add(Calendar.SECOND, +45);
            end = calendar.getTime();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = simpleDateFormat.format(start);
        endTime = simpleDateFormat.format(end);


        //添加到待取证表
        HikCameraVideoFileDownloadTask fileDownloadTask = HikCameraVideoFileDownloadTask.builder()
                .id(IdUtil.getSnowflakeNextId()).fileStatus(0).fileType(1).elevatorCode(elevatorCode)
                .deviceSerial(deviceSerial).faultType(faultType).faultId(faultId).startTime(startTime)
                .endTime(endTime).createTime(DateUtil.date()).modifyTime(DateUtil.date()).build();

        videoFileDownloadTaskService.save(fileDownloadTask);

        log.info("------添加到待下载队列", faultId);

    }

    /**
     * 图片重新下载
     */
    public void reDownloadPicFile(HikCameraVideoFileDownloadTask downloadTask) {
        log.info("HiKPlatformHandle-downloadPictureFile from reDownloadPicFile, {},", JSON.toJSONString(downloadTask));
        try {

            downloadPicture(downloadTask.getFaultId(), downloadTask.getFaultType(), downloadTask.getDeviceSerial());

            downloadTask.setFileStatus(1);
            downloadTask.setModifyTime(DateUtil.date());
            videoFileDownloadTaskService.updateById(downloadTask);

            // 发送给Sender模块，通知其下载完成
            sendTaskFinishedToSenderService(downloadTask.getFaultId());

        } catch (Exception e) {

            log.error("------文件下载失败，故障id：[{}]，error:{}", downloadTask.getFaultId(), e.getMessage());

            downloadTask.setFileStatus(3);
            downloadTask.setDownloadFailedNum(downloadTask.getDownloadFailedNum() + 1);
            downloadTask.setErrorMsg(e.getMessage().substring(0, 500));
            downloadTask.setModifyTime(DateUtil.date());
            videoFileDownloadTaskService.updateById(downloadTask);
        }

    }

    /**
     * 视频下载
     *
     * @param downloadTask 待下载任务
     */
    public void reDownloadVideo(HikCameraVideoFileDownloadTask downloadTask) {

        log.info("开始录制视频，elevatorCode：{}，faultId：{}", downloadTask.getElevatorCode(), downloadTask.getFaultId());
        downloadTask.setFileStatus(2);
        videoFileDownloadTaskService.updateById(downloadTask);

        //获取视频回放流地址
        var playbackRes = hikPlatformUtil.getPlaybackURL(downloadTask.getDeviceSerial(), "3", 2, downloadTask.getStartTime(),
                downloadTask.getEndTime(), "600");
        if (HttpStatus.OK.value() != playbackRes.getCode()) {
            log.info("获取视频回放流地址失败，elevatorCode：{}，faultId：{}， result: {}", downloadTask.getElevatorCode(),
                    downloadTask.getFaultId(), JSON.toJSONString(playbackRes));
            return;
        }

        /**
         * 下载视频
         */
        new Thread(() -> {
            var recordStatusRedisKey = RedisKeyUtils.getHikCloudRecordVideoStatusKey(downloadTask.getDeviceSerial());
            // 判断是否有已经在取视频的记录
            var hasRecording = redisUtils.setIfAbsent(recordStatusRedisKey, DateUtil.now(), 10L, TimeUnit.MINUTES);
            if (!hasRecording) {
                return;
            }

            var filePath = HikvPlatformConstants.PATH_FILE_DOWNLOAD_LOCAL + DateUtil.today();
            try {
                String localUrl = videoDownloadHandle.run(playbackRes.getData().getUrl(), filePath, downloadTask.getFaultId());
                // 删除正在取视频的记录
                redisUtils.deleteObject(recordStatusRedisKey);

                downloadTask.setFileStatus(1);
                videoFileDownloadTaskService.updateById(downloadTask);

                //文件上传阿里云
                upLoadFile(localUrl, downloadTask.getFaultId(), ".mp4", "1");

                // 发送给Sender模块，通知其下载完成
                sendTaskFinishedToSenderService(downloadTask.getFaultId());


            } catch (Exception e) {

                log.error("下载视频失败,error:{},故障id:{}", e.getMessage(), downloadTask.getFaultId());
                downloadTask.setFileStatus(3);
                downloadTask.setDownloadFailedNum(downloadTask.getDownloadFailedNum() + 1);
                downloadTask.setErrorCode(500);
                downloadTask.setErrorMsg("下载视频失败，error:" + e.getMessage().substring(0, 500));
                downloadTask.setModifyTime(DateUtil.date());

                videoFileDownloadTaskService.updateById(downloadTask);
            }

        }).start();

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
     * @return 回放地址
     */
    public String playbackURLs(String deviceSerial, String protocol, Integer quality,
                               String startTime, String stopTime, String expireTime) {

        var playbackRes = hikPlatformUtil.getPlaybackURL(deviceSerial, protocol, quality, startTime, stopTime, expireTime);
        if (HttpStatus.OK.value() != playbackRes.getCode()) {
            log.info("获取回放地址失败，deviceSerial：{}，result：{}", deviceSerial, JSON.toJSONString(playbackRes));
            return null;
        }
        return playbackRes.getData().getUrl();
    }


    /**
     * 获取监控点预览取流URL
     *
     * @param deviceSerial 摄像头序列号
     * @param protocol     流播放协议，2-hls、3-rtmp、4-flv
     * @param quality      视频清晰度，1-高清，2-标清
     * @param expireTime   过期时间
     */
    public String previewURLs(String deviceSerial, String protocol, Integer quality, String expireTime) {

        return hikPlatformUtil.getPreviewURLs(deviceSerial, protocol, quality, expireTime);
    }

    /**
     * 获取取流token
     *
     * @return
     */
    public String getStreamToken() {

        return hikPlatformUtil.getStreamToken();
    }

    ///////////////////////////////////////////////////////////private////////////////////////////////////////////////////////////


    /**
     * 下载图片
     *
     * @param faultId
     * @param faultType
     * @param deviceSerial
     * @throws Exception
     */
    private void downloadPicture(String faultId, String faultType, String deviceSerial) throws Exception {

        //获取图片流地址
        String picUrl = hikPlatformUtil.getPicture(deviceSerial);

        //存储文件到本地
        String localUrl = downloadFile2Local(picUrl, faultId, ".jpg");

        //文件上传阿里云
        String ossUrl = upLoadFile(localUrl, faultId, ".jpg", "0");

        //图像二次识别
        imageIdentification(ossUrl, faultType, faultId);

    }

    /**
     * 下载文件到本地
     *
     * @param fileUrl 资源路径
     * @param faultId 故障id
     */
    private String downloadFile2Local(String fileUrl, String faultId, String fileLastName) throws Exception {

        log.info("------开始下载文件，故障id：[{}]， fileName:{}", faultId, fileLastName);

        String filePath = HikvPlatformConstants.PATH_FILE_DOWNLOAD_LOCAL + DateUtil.today() + "/";

        String localUrl = FileUtil.downloadFile2LocalByUrl(fileUrl, filePath, faultId + fileLastName);

        log.info("------文件下载成功，故障id：[{}], fileName: {}", faultId, fileLastName);

        return localUrl;
    }

    /**
     * 文件上传阿里云
     *
     * @param fileUrl
     * @param faultId
     * @param fileType
     */
    private String upLoadFile(String fileUrl, String faultId, String fileLastName, String fileType) {

        log.info("------开始上传阿里云，故障id：[{}]", faultId);
        String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + faultId + fileLastName;

        OssInternalUtils.setOSS(FileUtil.getBytesByFile(fileUrl), uri, aliOssProperties.getUseInternal());
        log.info("------上传阿里云成功，故障id：[{}]", faultId);

        //String ossUrl = "https://oss-mashine.oss-cn-qingdao.aliyuncs.com/" + uri;
        String ossUrl = OssInternalUtils.OSS_URL + uri;

        FileDownloadTbl tblSysFile = FileDownloadTbl.builder()
                .vFileId(IdUtil.getSnowflake(1, 1).nextIdStr())
                .vFileType(fileType).vFileName(faultId + fileLastName).vBusinessId(faultId)
                .iBusinessType("2").dtCreateTime(new Date()).vUrl(ossUrl).build();

        //落表
        fileDownloadService.saveOrUpdate(tblSysFile);

        log.info("------故障取证文件落表成功成功，故障id：[{}]", faultId);

        return ossUrl;
    }

    /**
     * 调用图像识别
     *
     * @param picUrl
     */
    private void imageIdentification(String picUrl, String faultType, String faultId) {

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

            //平层困人
            if ("7".equals(faultType) || "8".equals(faultType)) {

                long remove = redisUtils.deleteSet("HLS:IMAGE", Set.of(faultId));

                if (remove > 0) {

                    try {
                        //图片下载成功释放锁
                        RLock lock = redissonClient.getLock("AFRESDOWNLOADIMAGE_LOCK" + faultId);
                        lock.forceUnlock();
                        log.info("---------平层困人图片下载成功，释放锁------------");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //调用图像识别
                    // restTemplateSendMessage(faultId, picUrl, "person");
                    ImageIdentifyUtils.identifyImage(faultId, picUrl, ImageIdentifyUtils.IDENTIFY_TYPE_PERSON,
                            aliOssProperties.getUseInternal());
                }
                //添加识别标识
                redisUtils.setCacheZSet("HLS:IDENTIFICATION", Set.of(faultId), System.currentTimeMillis() / 1000);
            }

        } catch (Exception e) {
            log.info("调用识别服务失败：e： {}，故障id：{}，故障类型：{}", e.getMessage(), faultId, faultType);
        }
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

        HttpUtil.get(url, 1000);

    }*/

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
                    .replace("{cameraType}", String.valueOf(CameraTypeEnum.HIK_CLOUD.getCode()));
            var res = HttpUtil.get(url, 15000);
            log.info("发送任务完成消息给sender服务，故障id：[{}]，返回结果：[{}]", faultId, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
