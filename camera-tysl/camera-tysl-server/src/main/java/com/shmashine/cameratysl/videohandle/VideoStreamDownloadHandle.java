package com.shmashine.cameratysl.videohandle;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import cn.hutool.core.date.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 视频流下载处理
 *
 * @author jiangheng
 * @version v1.1 2022/10/14 17:03
 */
@Slf4j
@Component
@EnableScheduling
public class VideoStreamDownloadHandle {
    private final Set<String> working = new HashSet<>();
    private final Map<String, WorkMapDO> workMap = new ConcurrentHashMap<>();

    public String capture(String streamUrl, String filePath, String workId, String ssrc) {
        var format = "jpg";
        return capture(streamUrl, filePath, format, workId, ssrc);
    }

    public String capture(String streamUrl, String filePath, String format, String workId, String ssrc) {
        log.info("视频截图开始 ,workId ：{}, streamUrl:{} {}", workId, streamUrl, ssrc);
        filePath = buildVideoFilePath(filePath, workId, format);
        FFmpegLogCallback.set();
        log.info("视频截图 ,workId ：{}, init ", workId);

        String finalFilePath = filePath;
        AtomicReference<String> result = new AtomicReference<>();
        var executor = Executors.newSingleThreadExecutor();
        // 缓存工单ID,用于检测超时
        workMap.put(workId, buildWorkMapDO(workId, ssrc, System.currentTimeMillis(), DateUtil.now(), format, executor));
        var future = executor.submit(() -> {
            FFmpegFrameGrabber grabber = null;
            try {
                grabber = new FFmpegFrameGrabber(streamUrl);
                log.info("视频截图 ,workId ：{}, {} 准备 ", workId, ssrc);
                grabber.start();
                workMap.remove(workId);
                Frame frame = grabber.grabImage();
                var image = new Java2DFrameConverter().convert(frame);
                File outputImageFile = new File(finalFilePath);
                ImageIO.write(image, format, outputImageFile);
                result.set(outputImageFile.getAbsolutePath());
                log.info("视频截图 ,workId ：{}, 完成 ", workId);
            } catch (Exception e) {
                log.error("视频截图失败, workId:{}, error:{}", workId, ExceptionUtils.getStackTrace(e));
                log.info("视频截图失败，workId:{}, error:{}", workId, e.getMessage());
            } finally {
                if (null != grabber) {
                    try {
                        grabber.close();
                    } catch (FrameGrabber.Exception e) {
                        log.error("视频截图，关闭grabber失败， error:{} {}", workId, ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        });
        try {
            future.get();
        } catch (Exception e) {
            log.error("视频截图，任务执行失败， error:{} {}", workId, ExceptionUtils.getStackTrace(e));
        }
        workMap.remove(workId);
        log.info("视频截图完成 ,workId ：{}, result:{} ", workId, result.get());
        return result.get();
    }

    /**
     * 录制视频
     *
     * @param streamUrl 流地址
     * @param filePath  文件保存路径
     * @param workId    工单ID
     * @return 文件路径
     */
    public String run(String streamUrl, String filePath, String workId, String ssrc) {
        var format = "mp4";
        return run(streamUrl, filePath, format, workId, ssrc);
    }

    /**
     * 录制视频
     *
     * @param streamUrl 流地址
     * @param filePath  文件保存路径
     * @param format    录制格式
     * @param workId    工单ID
     * @return 文件路径
     */
    public String run(String streamUrl, String filePath, String format, String workId, String ssrc) {

        filePath = buildVideoFilePath(filePath, workId, format);

        var success = run(streamUrl, filePath, 1280, 720, 1, 25, 800000, format, workId, ssrc);
        log.info("视频录制文件录制 {}，faultId:{}", success ? "成功" : "失败", workId);
        return success ? filePath : null;
    }

    /**
     * 录制视频
     *
     * @param streamUrl    流地址
     * @param filePath     文件保存路径
     * @param imageWidth   视频分辨率 宽
     * @param imageHeight  视频分辨率 高
     * @param audioChannel 音频 （0:不录制/1:录制）
     * @param frameRate    帧率
     * @param videoBitrate 码率
     * @param format       录制格式
     * @param workId       任务id
     * @return 是否成功
     */
    // CHECKSTYLE:OFF
    private Boolean run(String streamUrl, String filePath, Integer imageWidth, Integer imageHeight,
                        Integer audioChannel, Integer frameRate, Integer videoBitrate, String format, String workId,
                        String ssrc) {
        working.add(workId);
        log.info("视频录制开始 ,workId ：{}, streamUrl:{} {}", workId, streamUrl, ssrc);
        File outFile = new File(filePath);
        if (!outFile.isFile()) {
            try {
                var fileRes = outFile.createNewFile();
                log.info("创建视频文件夹结果：{}", fileRes);
            } catch (Exception e) {
                log.error("创建视频文件夹失败，workId:{}, error:{}", workId, ExceptionUtils.getStackTrace(e));
            }
        }
        log.info("视频录制 ,workId ：{}, 准备录制0， 文件路径：{}", workId, filePath);
        final int[] res = {0};

        var executor = Executors.newSingleThreadExecutor();
        // 缓存工单ID,用于检测超时
        workMap.put(workId, buildWorkMapDO(workId, ssrc, System.currentTimeMillis(), DateUtil.now(), format, executor));
        var future = executor.submit(() -> {
            FFmpegLogCallback.set();
            FFmpegFrameGrabber grabber = null;
            FFmpegFrameRecorder recorder = null;
            try {
                // 获取视频源
                grabber = new FFmpegFrameGrabber(streamUrl);
                // 设置超时时间, 避免请求地址无响应导致程序卡死, 不清楚是否有作用
                grabber.setTimeout(5 * 60 * 1000);
                log.info("视频录制 ,workId ：{}, {} 准备录制1", workId, ssrc);
                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
                recorder = new FFmpegFrameRecorder(filePath, imageWidth, imageHeight, audioChannel);
                log.info("视频录制 ,workId ：{}, {} 准备录制2", workId, ssrc);
                grabber.start();
                workMap.remove(workId);
                log.info("视频录制 ,workId ：{}, 原地址已成功打开", workId);
                // 直播流格式
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
                recorder.setAudioChannels(grabber.getAudioChannels());
                // 录制的视频格式
                recorder.setFormat(format);
                // 帧数
                recorder.setFrameRate(frameRate);
                recorder.setVideoBitrate(videoBitrate);
                log.info("视频录制 ,workId ：{}, 开始录制", workId);
                recorder.start();
                log.info("视频录制 ,workId ：{}, 开始录制1", workId);
                // 录制
                Frame frame = null;
                while ((frame = grabber.grabFrame()) != null && working.contains(workId)) {
                    recorder.record(frame);
                }
                // 停止录制
                //recorder.stop();
                //grabber.stop();
                log.info("视频录制 ,workId ：{}, 结束录制", workId);
                res[0] = 1;
            } catch (Throwable e) {
                log.error("视频录制失败, workId:{}, error:{}", workId, ExceptionUtils.getStackTrace(e));
                log.info("视频录制失败, workId:{}, error:{}", workId, e.getMessage());
                if (outFile.isFile()) {
                    log.info("视频录制失败, workId:{}, 删除本地文件 {}", workId, outFile.delete());
                }
            } finally {
                if (null != grabber) {
                    try {
                        grabber.close();
                    } catch (FrameGrabber.Exception e) {
                        log.error("视频录制失败, workId:{}, 关闭grabber失败， error:{}", workId, ExceptionUtils.getStackTrace(e));
                    }
                }
                if (null != recorder) {
                    try {
                        recorder.close();
                    } catch (FrameRecorder.Exception e) {
                        log.error("视频录制失败, workId:{}, 关闭recorder失败， error:{}", workId, ExceptionUtils.getStackTrace(e));
                    }
                }
                working.remove(workId);
            }
        });
        try {
            future.get();
        } catch (Exception ignored) {
        }
        workMap.remove(workId);
        log.info("视频录制 ,workId ：{}, 结束， 状态：{}", workId, res[0]);
        return res[0] == 1;
    }
    // CHECKSTYLE:ON

    /**
     * 定时清理异常任务
     */
    @Scheduled(fixedDelay = 30000, initialDelay = 10000)
    private void clearExceptionTask() {
        log.info("定时清理异常任务 {}", workMap.toString());
        var now = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(workMap)) {
            return;
        }
        var keys = workMap.keySet();
        var timeOutKeys = new ArrayList<>();
        for (var workId : keys) {
            var work = workMap.get(workId);
            if (null != work && now - work.getTime() > 40000) {
                var executor = work.getExecutor();
                if (null != executor) {
                    log.info("任务连接超时，workId:{} {} {}", workId, executor.isShutdown(), executor.isTerminated());
                    timeOutKeys.add(workId);
                    if (!executor.isShutdown()) {
                        executor.shutdownNow();
                        log.info("关闭任务，workId:{}", workId);
                    }
                }
            }
        }
        log.info("定时清理异常任务结束，workIds: {}, timeOutWorkIds: {}", keys.toString(), timeOutKeys.toString());
    }

    private String buildVideoFilePath(String filePath, String workId, String format) {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            var mkdirRes = targetFile.mkdirs();
            log.info("创建文件夹结果：{}", mkdirRes);
        }

        filePath = filePath.endsWith("/") ? filePath : filePath + "/";
        filePath = filePath + workId + "." + format;
        return filePath;
    }

    /**
     * 停止录制
     *
     * @param workId 工单id
     */
    public void stop(String workId) {
        // 取消正在执行的进程任务
        var work = workMap.get(workId);
        if (null != work) {
            var executor = work.getExecutor();
            if (null != executor && !executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
        working.remove(workId);
    }

    private WorkMapDO buildWorkMapDO(String workId, String ssrc, long time, String timeString, String format,
                                     ExecutorService executor) {
        return WorkMapDO.builder()
                .workId(workId)
                .ssrc(ssrc)
                .time(time)
                .timeString(timeString)
                .format(format)
                .executor(executor)
                .build();
    }

    /**
     * 参数缓存
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class WorkMapDO implements Serializable {
        private String workId;
        private String ssrc;
        private Long time;
        private String timeString;
        private String format;
        private ExecutorService executor;
    }
}
