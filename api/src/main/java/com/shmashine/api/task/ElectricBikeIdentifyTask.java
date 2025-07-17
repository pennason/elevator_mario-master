// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.api.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.shmashine.api.redis.utils.RedisUtils;
import com.shmashine.api.service.camera.CameraServerClientBeanService;
import com.shmashine.api.service.camera.TblCameraImageIdentifyServiceI;
import com.shmashine.api.service.elevator.ElevatorCacheService;
import com.shmashine.common.dto.CamareMediaDownloadRequestDTO;
import com.shmashine.common.entity.TblCameraImageIdentifyEntity;
import com.shmashine.common.enums.CameraMediaTypeEnum;
import com.shmashine.common.enums.CameraTaskTypeEnum;
import com.shmashine.common.properties.AliOssProperties;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.ImageIdentifyUtils;
import com.shmashine.common.utils.OssInternalUtils;
import com.shmashine.common.utils.RedisKeyUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 电动车识别流程 kafka消息
 * -> ElectricBikeIdentifyHandler.checkAndSaveByElevator 过滤记录
 * -> ElectricBikeIdentifyTask.scheduledElectricBikeIdentifyTask 异步处理记录
 * -> 下载当时照片, 并记录到数据库
 * -> ElectricBikeIdentifyTask 扫描数据库，完成获取照片的，执行图像识别
 * -> 图像识别结果回填到数据库
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/10/13 11:46
 * @since v1.0
 */

@Slf4j
@Profile({"prod"})
@Component
// @EnableScheduling
@EnableAsync
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ElectricBikeIdentifyTask {
    private final RedisUtils redisUtils;
    private final CameraServerClientBeanService cameraServerClientBeanService;
    private final ElevatorCacheService elevatorCacheService;
    private final TblCameraImageIdentifyServiceI imageIdentifyService;
    private final AliOssProperties aliOssProperties;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(4, 8, 2, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "ElectricBikeIdentifyTask");

    /**
     * 从1楼关门上行记录到数据库
     * 从队列中获取所有需要处理的记录 记录1楼上升中的电梯，用于判断是否是电动车乘梯
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 15000)
    public void scheduledElectricBikeIdentifyTask() {
        var list = redisUtils.lGetAndRemove(RedisKeyUtils.getElevatorElectricBikeIdentifyQueue(), 0);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(item -> {
            executorService.submit(() -> {
                doElectricBikeIdentifyTask(item.toString());
            });
        });
    }

    /**
     * 已经下载到图片的进行图像识别，用于判断
     */
    @Scheduled(fixedDelay = 60000, initialDelay = 25000)
    public void scheduledSendRecordToIdentifyTask() {
        var yesterday = DateUtil.format(DateUtil.yesterday(), DatePattern.NORM_DATETIME_PATTERN);
        var res = imageIdentifyService.listLatestUnIdentifyRecords(yesterday);
        if (CollectionUtils.isEmpty(res)) {
            return;
        }

        // 将状态更新为识别中
        var ids = new ArrayList<Long>();
        res.forEach(item -> ids.add(item.getId()));
        imageIdentifyService.updateStatusToIdentifying(ids);

        // 设置最小值为1最大128
        int corePoolSize = Math.max(1, (res.size() / 10) > 128 ? 128 : res.size() / 10);
        var executor = new ShmashineThreadPoolExecutor(
                corePoolSize, corePoolSize,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Math.max(res.size(), 1)),
                ShmashineThreadFactory.of(),
                PersistentRejectedExecutionHandler.of("scheduledSendRecordToIdentifyTask"), "ElectricBikeIdentifyTask");

        try {

            CompletableFuture<?>[] completableFutures = res.stream()
                    .map(task -> CompletableFuture.runAsync(() -> doImageIdentifyTask(task), executor))
                    .toArray(CompletableFuture[]::new);

            try {
                CompletableFuture.allOf(completableFutures).get(100, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("批量执图像识别任务失败，error：{}", ExceptionUtils.getStackTrace(e));
                throw new RuntimeException(e);
            }

        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { // 等待终止
                    executor.shutdownNow(); // 取消当前执行的任务
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // 再次设置中断状态
                executor.shutdownNow(); // 取消当前执行的任务
            }
        }
    }

    //    @Scheduled(fixedDelay = 15000, initialDelay = 25000)
    public void scheduledRetryInitTask() {
        var identifyList = imageIdentifyService.listInitRecordHoursAgo();

        // 发起图片下载
        identifyList.stream().filter(it -> it.getIdentifyType() == 1).forEach(this::doCameraDownloadImage);
    }


    public void doElectricBikeIdentifyTask(String taskString) {
        var jsonObject = JSON.parseObject(taskString, Map.class);
        // 字段判断
        var timeObj = jsonObject.get("time");
        var elevatorCodeObj = jsonObject.get("elevatorCode");
        if (timeObj == null || elevatorCodeObj == null) {
            return;
        }
        var elevatorCode = elevatorCodeObj.toString();
        var time = timeObj.toString();
        // 将时间向后推3秒
        time = DateUtil.format(DateUtil.parse(time, DatePattern.NORM_DATETIME_PATTERN).offset(DateField.SECOND, 3),
                DatePattern.NORM_DATETIME_PATTERN);

        var timeShort = time.replace("-", "").replace(" ", "").replace(":", "");
        var taskCustomId = timeShort + "-" + elevatorCode;
        var floor = jsonObject.get("floor") == null ? null : jsonObject.get("floor").toString();

        var elevator = elevatorCacheService.getByElevatorCode(elevatorCode);

        var identifyEntity = new TblCameraImageIdentifyEntity(taskCustomId)
                .setElevatorId(elevator.getElevatorId())
                .setElevatorCode(elevatorCode)
                .setCollectTime(time)
                .setFloor(floor)
                // 记录执行状态 0:初始状态，1：待识别，2：识别中， 3：已识别
                .setStatus(0);

        // 记录到数据库
        imageIdentifyService.saveImageIdentifyRecord(identifyEntity);
        // 发起截图
        doCameraDownloadImage(identifyEntity);
    }

    public void doCameraDownloadImage(TblCameraImageIdentifyEntity identifyEntity) {
        var entity = CamareMediaDownloadRequestDTO.builder()
                .elevatorCode(identifyEntity.getElevatorCode())
                .collectTime(identifyEntity.getCollectTime())
                .startTime(identifyEntity.getCollectTime().replace("-", "").replace(" ", "").replace(":", ""))
                .endTime(null)
                .floor(identifyEntity.getFloor())
                .taskType(CameraTaskTypeEnum.ELECTRIC_BIKE_IDENTIFY)
                .taskCustomId(identifyEntity.getTaskCustomId())
                .taskCustomType(0)
                .mediaType(CameraMediaTypeEnum.JPG)
                .build();
        var res = cameraServerClientBeanService.downloadCameraFileByElevatorCode(entity);
        log.info("ElectricBikeIdentify-downloadCameraFile result status:{}, data:{}", res.getStatusCode(),
                res.getBody());
    }

    private void doImageIdentifyTask(TblCameraImageIdentifyEntity entity) {

        //人流量统计识别
        if (entity.getIdentifyType() == 4) {

            //抠图上传阿里云并返回文件地址
            String picUrl = imageMatting(entity.getTaskCustomId(), entity.getOssUrl(), "7,8", entity.getElevatorCode());

            //拼接请求参数
            // imageIdentifyService.restTemplateSendMessage(entity.getTaskCustomId(), picUrl, "person");
            ImageIdentifyUtils.identifyImage(entity.getTaskCustomId(), picUrl,
                    ImageIdentifyUtils.IDENTIFY_TYPE_PERSON, aliOssProperties.getUseInternal());

            log.info("人流量统计识别请求，id：{}，url：{}，type：{}", entity.getTaskCustomId(), picUrl, "person");
            return;
        }

        // 目前只将 自研电动车识别的请求发给识别服务，其他暂时不支持
        // imageIdentifyService.restTemplateSendMessage(entity.getTaskCustomId(), entity.getOssUrl(), "motorcycle");
        ImageIdentifyUtils.identifyImage(entity.getTaskCustomId(), entity.getOssUrl(),
                ImageIdentifyUtils.IDENTIFY_TYPE_MOTOR_CYCLE, aliOssProperties.getUseInternal());


    }

    /**
     * 计算抠图
     */
    private String imageMatting(String faultId, String picUrl, String faultType, String elevatorCode) {

        // picUrl = picUrl.replace(OssInternalUtils.OSS_URL, OssInternalUtils.OSS_URL_INTERNAL);
        try {
            if (StrUtil.isBlank(elevatorCode)) {
                return picUrl;
            }
            //根据故障id或电梯抠图配置
            var config = imageIdentifyService.getImageMattingConfigByFaultId(elevatorCode, faultType);

            if (config == null || StrUtil.isBlank(config.getCoordinates())) {
                return picUrl;
            }
            var realCoordinates = JSON.parseArray(config.getRealCoordinates(), JSONObject.class);

            if (realCoordinates == null || realCoordinates.size() == 0) {

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
                imageIdentifyService.updateImageMattingConfig(JSON.toJSONString(realCoordinates), config.getId());

            }

            //本地存储路径
            String toPath = "/media/" + faultId + "_imageMatting.jpg";
            //抠图
            ImageIdentifyUtils.drawAndAlphaPolygon(picUrl, realCoordinates, "jpg", toPath,
                    aliOssProperties.getUseInternal());

            //上传阿里云
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            log.info("------开始上传阿里云，故障id：[{}]", faultId);
            String uri = "Oreo_Project/fault/" + DateUtil.today().replace("-", "/") + "/" + faultId
                    + "_imageMatting.jpg";
            OssInternalUtils.setOSS(FileUtil.getBytesByFile(toPath), uri, aliOssProperties.getUseInternal());
            log.info("------上传阿里云成功，故障id：[{}]", faultId);

            //删除本地文件
            new File(toPath).delete();

            return OssInternalUtils.extendOssUrl(uri);
        } catch (Exception e) {
            log.error("抠图失败,faultId:[{}],error:[{}]", faultId, ExceptionUtils.getStackTrace(e));
        }

        return picUrl;
    }

    /**
     * 绘制并填充多边形
     *
     * @param srcImagePath 源图像路径
     * @param points       坐标数组
     * @param imageFormat  写入图形格式
     * @throws IOException
     */
    /*private void drawAndAlphaPolygon(String srcImagePath, List<cn.hutool.json.JSONObject> points, String imageFormat,
        String toPath) {
        FileOutputStream fos = null;
        try {
            //获取图片
            BufferedImage image = ImageIO.read(new URL(srcImagePath));
            //根据xy点坐标绘制闭合多边形
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.BLACK);

            for (cn.hutool.json.JSONObject point : points) {
                int[] xPoints = point.get("x", int[].class);
                int[] yPoints = point.get("y", int[].class);
                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
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
     * 获取图片大小
     *
     * @param imgPath 图片地址
     * @return
     */
    /*private Integer[] getImgSize(String imgPath) {
        Integer[] size = new Integer[2];
        try {
//            BufferedImage image = ImageIO.read(new File(imgPath));
            BufferedImage image = ImageIO.read(new URL(imgPath));
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }*/

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

        return points.stream().map(p -> {

            var pointsX = p.getObject("x", Integer[].class);
            var pointsY = p.getObject("y", Integer[].class);


            var res = Map.of("x", Arrays.stream(pointsX).mapToInt(x -> (x * imgSize[0]) / width).toArray(),
                    "y", Arrays.stream(pointsY).mapToInt(y -> (y * imgSize[1]) / height).toArray());

            return JSON.parseObject(JSON.toJSONString(res), JSONObject.class);
        }).toList();
    }

}
