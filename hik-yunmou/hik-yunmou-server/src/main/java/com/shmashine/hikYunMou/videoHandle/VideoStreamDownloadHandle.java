package com.shmashine.hikYunMou.videoHandle;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 视频录制
 *
 * @author jiangheng
 * @version v1.1 2022/10/14 17:03
 * <p>
 * 视频流下载处理
 */
@Slf4j
@Component
public class VideoStreamDownloadHandle {

    private final Set<String> working = new HashSet<>();

    /**
     * 录制视频
     *
     * @param streamUrl 流地址
     * @param filePath  文件保存路径
     * @param workId    工单ID
     * @return 文件路径
     */
    public String run(String streamUrl, String filePath, String workId) {
        var format = "mp4";
        return run(streamUrl, filePath, format, workId);
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
    public String run(String streamUrl, String filePath, String format, String workId) {

        filePath = buildVideoFilePath(filePath, workId, format);

        var success = run(streamUrl, filePath, 1280, 720, 1, 25, 800000, format, workId);
        log.info("视频文件录制 {}，faultId:{}", success ? "成功" : "失败", workId);
        return success ? filePath : null;

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
    private Boolean run(String streamUrl, String filePath, Integer imageWidth, Integer imageHeight, Integer audioChannel,
                        Integer frameRate, Integer videoBitrate, String format, String workId) {

        working.add(workId);

        try (
                // 获取视频源
                var grabber = new FFmpegFrameGrabber(streamUrl);
                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
                var recorder = new FFmpegFrameRecorder(filePath, imageWidth, imageHeight, audioChannel);
        ) {
            grabber.start();
            Frame frame = grabber.grabFrame();
            if (frame != null) {
                File outFile = new File(filePath);
                if (!outFile.isFile()) {
                    var fileRes = outFile.createNewFile();
                    log.info("创建视频文件结果：{}", fileRes);
                }
                // 直播流格式
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                // 录制的视频格式
                recorder.setFormat(format);
                // 帧数
                recorder.setFrameRate(frameRate);
                recorder.setVideoBitrate(videoBitrate);
                recorder.start();
                while (frame != null && working.contains(workId)) {
                    // 录制
                    recorder.record(frame);
                    // 获取下一帧
                    frame = grabber.grabFrame();
                }

                // 停止录制
                recorder.stop();
                grabber.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("视频录制失败，workId:{}", workId);
            return false;
        }
        return true;
    }

    /**
     * 停止录制
     *
     * @param workId 工单id
     */
    public void stop(String workId) {
        working.remove(workId);
    }
}
