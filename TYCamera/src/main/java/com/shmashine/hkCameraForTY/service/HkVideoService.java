package com.shmashine.hkCameraForTY.service;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/6/15 15:48
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import com.alibaba.fastjson2.JSONObject;
import com.shmashine.hkCameraForTY.dao.CameraForTYDao;
import com.shmashine.hkCameraForTY.entity.TblResponseHkReport;
import com.shmashine.hkCameraForTY.ffmpeg.FFMpeg;
import com.shmashine.hkCameraForTY.utils.FileUtil;
import com.shmashine.hkCameraForTY.utils.OSS;
import com.shmashine.hkCameraForTY.utils.SnowFlakeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 海康历史视频下载上传
 */
@Slf4j
@Service
public class HkVideoService {

    //创建一个10个大小的线程池
    private final ExecutorService executorService = new ThreadPoolExecutor(2, 8,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(300), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private static Logger hkVideologger = LoggerFactory.getLogger("hkVideologger");

    //获取历史视频下载连接
    private static final String GET_VIDEO_RECORD_URL =
            "http://101.89.203.135:10035/viot-openapi/openapi/GBDevApi/V2/record/playRecord";

    @Autowired
    private FFMpeg ffmpeg;

    @Resource
    CameraForTYDao cameraForTYDao;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 首次视频下载
     *
     * @param workOrderId  故障id
     * @param elevatorCode 电梯code
     * @param startTime    开始时间
     * @param endTime      结束时间
     */
    public void hkVideoDownload(String workOrderId, String elevatorCode, Date startTime, Date endTime, int faultType) {

        try {
            String result = downloadAndUpload(workOrderId, elevatorCode, startTime, endTime, String.valueOf(faultType));
            if (result != null) {
                hkVideologger.info("------视频取证失败，电梯：[{}]，故障id：[{}]，error：[{}]", elevatorCode, workOrderId, result);
                cameraForTYDao.add(3, new Date(), Integer.parseInt(JSONObject.parseObject(result).getString("code")),
                        elevatorCode, workOrderId, startTime, endTime, 1,
                        JSONObject.parseObject(result).getString("msg"), String.valueOf(faultType));
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * 定时二次下载
     */
    public void hkVideoDownloadReset(TblResponseHkReport hkReport) {

        executorService.submit(() -> {
            try {
                String res = downloadAndUpload(hkReport.getFaultId(), hkReport.getElevatorCode(),
                        hkReport.getStartTime(), hkReport.getEndTime(), hkReport.getFaultType());

                if (res == null) {
                    /*更新状态*/
                    cameraForTYDao.updateHkReport(1, hkReport.getFaultId(), hkReport.getRequestFailedNum());
                } else {
                    /*更新状态*/
                    cameraForTYDao.updateHkReport(3, hkReport.getFaultId(), hkReport.getRequestFailedNum() + 1);
                }

            } catch (Exception e) {
                //
            }
        });

    }

    private String downloadAndUpload(String workOrderId, String elevatorCode, Date startTime,
                                     Date endTime, String faultType) {

        String result = null;

        try {
            //查询海康摄像头配置
            HashMap<String, String> info = cameraForTYDao.getHkCameraInfo(elevatorCode);

            if (info.isEmpty()) {
                hkVideologger.info("-------电梯：[{}]，海康摄像头未配置", elevatorCode);
                return null;
            }

            String tenantNo = info.get("tenantNo");
            String tenantKey = info.get("tenantKey");
            String deviceNo = info.get("deviceNo");

            result = getURl(tenantNo, tenantKey, deviceNo, DateUtil.format(startTime, DatePattern.UTC_SIMPLE_PATTERN),
                    DateUtil.format(endTime, DatePattern.UTC_SIMPLE_PATTERN));

            hkVideologger.info("-----------拉取视频流结束----拉流结果：" + result);

            String messageJson = JSONObject.parseObject(result).getString("result");

            String flvUrl = JSONObject.parseObject(messageJson).getString("flvHttp");

            //本地文件夹目录
            String fileUrl = "/shmashine-deploy/java-oreo/HKCameraForTY/download/" + elevatorCode;

            mkdir(fileUrl);

            String videoInputPath = fileUrl + "/" + workOrderId + ".flv";
            String videoOutputPath = fileUrl + "/" + workOrderId + ".mp4";

            downloadFile(flvUrl, videoInputPath);
            hkVideologger.info("-------电梯：[{}]，下载视频成功，故障id：[{}]", elevatorCode, workOrderId);

            ffmpeg.createVideo(videoInputPath, videoOutputPath);
            hkVideologger.info("-------视频转换成功");

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

            //截取图片
            try {
                String imgs = ffmpeg.createImgs(videoOutputPath);
                hkVideologger.info("-------，视频截取成功");
                hkVideologger.info("------图片开始上传阿里云，电梯：[{}]，故障id：[{}]", elevatorCode, workOrderId);
                String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + workOrderId + ".jpg";
                String imgPath = fileUrl + "/" + workOrderId + ".jpg";
                OSS.setOSS(FileUtil.getBytesByFile(imgPath), uri);
                hkVideologger.info("------图片上传阿里云成功，电梯：[{}]，故障id：[{}]", elevatorCode, workOrderId);
                //落表
                cameraForTYDao.addTblSysFile(SnowFlakeUtils.nextStrId(), workOrderId,
                        "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/" + uri, "workOrderId" + ".jpg",
                        2, new Date(), "0");

                //非平层停梯需要调用二次识别确认是否困人
                if ("6".equals(faultType)) {
                    restTemplateSendMessage(workOrderId, "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/" + uri,
                            "person");

                    hkVideologger.info("非平层停梯调用二次识别确认，故障id【{}】", workOrderId);
                }

                //删除本地图片&视频
                new File(videoInputPath).delete();
                new File(imgPath).delete();
            } catch (Exception e) {
                hkVideologger.info("-------电梯：[{}]，图片截取失败，故障id：[{}]，error：[{}]",
                        elevatorCode, workOrderId, e.getMessage());
            }


            hkVideologger.info("------视频开始上传阿里云，电梯：[{}]，故障id：[{}]", elevatorCode, workOrderId);
            String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + workOrderId + ".mp4";
            OSS.setOSS(FileUtil.getBytesByFile(videoOutputPath), uri);
            hkVideologger.info("------视频上传阿里云成功，电梯：[{}]，故障id：[{}]", elevatorCode, workOrderId);
            //删除本地视频
            hkVideologger.info("------故障视频落表，电梯：[{}]，故障id：[{}]", elevatorCode, workOrderId);
            cameraForTYDao.addTblSysFile(SnowFlakeUtils.nextStrId(), workOrderId,
                    "https://oss-maixin.oss-cn-shanghai.aliyuncs.com/" + uri,
                    "workOrderId" + ".mp4", 2, new Date(), "1");

            hkVideologger.info("------故障视频落表成功");
            //删除本地转换后视频
            new File(videoOutputPath).delete();
            return null;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * 获取历史视频下载连接
     */
    private String getURl(String tenantNo, String tenantKey, String deviceNo, String startTime, String endTime) {

        Map reqMap = new HashMap();
        reqMap.put("deviceNo", deviceNo);
        reqMap.put("recordType", "time");
        reqMap.put("startTime", startTime);
        reqMap.put("endTime", endTime);

        JSONObject reqBody = new JSONObject();
        reqBody.put("tenantNo", tenantNo);
        String time = DateUtil.format(DateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder signSb = new StringBuilder();
        String singStr = signSb.append(tenantNo + time + tenantKey).toString();
        String sign = DigestUtils.md5DigestAsHex(singStr.getBytes());
        reqBody.put("sign", sign);
        reqBody.put("time", time);
        reqBody.put("req", reqMap);

        hkVideologger.info("开始拉取下载视频流");

        String result = restTemplate.postForObject(GET_VIDEO_RECORD_URL, reqBody, String.class);

        return result;
    }

    /**
     * 创建文件夹
     */
    private void mkdir(String localFilePath) {

        File file = new File(localFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 通过获取的url下载历史视频到本地
     */
    private void downloadFile(String remoteFilePath, String localFilePath) {

        URL urlfile;
        HttpURLConnection httpUrl;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        File f = new File(localFilePath);
        try {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection) urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("下载失败---- {}", remoteFilePath);
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void start(String workOrderId, String elevatorCode, int faultType) {

        executorService.submit(() -> {

            //get故障上报时间和故障消除时间，并判断最长是否大于2小时
            Map<String, Object> time = cameraForTYDao.getStartAndEndTime(workOrderId);
            //故障上报时间
            Date reportTime = (Date) time.get("startTime");
            //Date endTime = (Date) time.get("endTime");

            /*开始时间结束时间延展*/
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reportTime);
            calendar.add(Calendar.SECOND, -15);
            Date startTime = calendar.getTime();
            calendar.setTime(reportTime);
            calendar.add(Calendar.SECOND, +45);
            Date endTime = calendar.getTime();
            //困人故障前后1分钟/其他故障取证1分钟
            if ("7".equals(faultType) || "8".equals(faultType)) {
                calendar.setTime(reportTime);
                calendar.add(Calendar.MINUTE, -1);
                startTime = calendar.getTime();
                calendar.setTime(reportTime);
                calendar.add(Calendar.MINUTE, +14);
                endTime = calendar.getTime();
            }

            //比较时长，最长不能超过两小时
            /*calendar.setTime(startTime);
            calendar.add(Calendar.HOUR, 2);
            Date sd = calendar.getTime();
            int i = endTime.compareTo(sd);
            if (i > 0) {
                endTime = sd;
            }*/

            //视频下载上传
            hkVideoDownload(workOrderId, elevatorCode, startTime, endTime, faultType);
        });

    }

    /**
     * 调用二次识别
     *
     * @param workOrderId 故障id
     * @param fileUrl     待识别图片url
     * @param type        识别类型
     */
    private void restTemplateSendMessage(String workOrderId, String fileUrl, String type) {
        //拼接请求参数
        String url = "http://172.31.183.101:10087/?type=" + type + "&url=" + fileUrl + "&faultId=" + workOrderId;

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            HttpGet httpGet = new HttpGet(url);
            try {
                httpclient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                log.error(throwable.getMessage());
                return null;
            }
        });

        try {
            Thread.sleep(100);
            httpclient.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}