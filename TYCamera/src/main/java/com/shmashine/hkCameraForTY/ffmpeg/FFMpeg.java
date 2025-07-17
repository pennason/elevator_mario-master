package com.shmashine.hkCameraForTY.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

/**
 * ffmpeg工具类
 */
@Component
public class FFMpeg {

    private static Logger ffmpegInfo = LoggerFactory.getLogger("ffmpegInfo");

    private static String ffmpegPath = "/opt/ffmpeg/bin/ffmpeg";

    public void convertor(String videoInputPath, String videoOutputPath) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);

        command.add("-i");
        command.add(videoInputPath);
        command.add("-vcodec");
        command.add("copy");
        command.add(videoOutputPath);

        ffmpegInfo.info("video" + command.toString());
        Process p = new ProcessBuilder(command).start();
        //获取进程的标准输入流
        final InputStream inputStream = p.getInputStream();
        //获取进城的错误流
        final InputStream errorStream = p.getErrorStream();
        //启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
        ip(inputStream, errorStream);
        try {
            p.waitFor();
        } catch (Exception e) {
            ffmpegInfo.info(videoOutputPath, e);
        }
        p.destroy();
    }

    public void createVideo(String videoInputPath, String videoOutputPath) {
        try {
            convertor(videoInputPath, videoOutputPath);
        } catch (Exception e) {
            ffmpegInfo.info("Exception", e);
        }
    }

    public String createImgs(String veido_path) {
        String path = "";
        try {
            path = processImg(veido_path);
        } catch (IOException e) {
            ffmpegInfo.info("Exception", e);
        }
        return path;
    }

    public static String processImg(String veido_path) throws IOException {

        File file = new File(veido_path);
        if (!file.exists()) {
            ffmpegInfo.info("路径[" + veido_path + "]对应的视频文件不存在!");
        }
        List<String> commands = new ArrayList<String>();
        commands.add(ffmpegPath);
        commands.add("-i");
        commands.add(veido_path);
        //指定截取开始时间（第一秒）
        commands.add("-ss");
        commands.add("1");
        //指定截取间隔时间
        //commands.add("-r");
        //commands.add("0.2");
        //截取一帧
        commands.add("-vframes");
        commands.add("1");
        String path = veido_path.substring(0, veido_path.lastIndexOf("."));
        commands.add(path + ".jpg");
        ffmpegInfo.info("images---:>" + commands.toString());
        Process p = new ProcessBuilder(commands).start();
        InputStream inputStream = p.getInputStream();
        InputStream errorStream = p.getErrorStream();
        ip(inputStream, errorStream);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            ffmpegInfo.info(path, e);
        }
        p.destroy();
        return path;
    }

    /**
     * 获取视频时长
     *
     * @param viedoPath 视频路径
     */
    public static Long getVideoDuration(String viedoPath) {
        Long rl;

        try {
            File video = new File(viedoPath);
            Encoder encoder = new Encoder();
            MultimediaInfo mi = encoder.getInfo(video);
            rl = mi.getDuration();
        } catch (Exception e) {
            rl = null;
        }
        return rl;
    }

    public static void ip(InputStream inputStream, InputStream errorStream) {

        new Thread(() -> {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line1 = null;
                while ((line1 = br1.readLine()) != null) {
                    if (line1 != null) {
                        ffmpegInfo.info("inputStream----->:" + line1);
                    }
                }
            } catch (IOException e) {
                ffmpegInfo.info("IOException", e);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ffmpegInfo.info("IOException", e);
                }
            }
        }).start();

        new Thread(() -> {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(errorStream));
            try {
                String line2 = null;
                while ((line2 = br2.readLine()) != null){
                }
            } catch (IOException e) {
                ffmpegInfo.info("IOException", e);
            } finally {
                try {
                    errorStream.close();
                } catch (IOException e) {
                    ffmpegInfo.info("IOException", e);
                }
            }
        }).start();

    }

}
