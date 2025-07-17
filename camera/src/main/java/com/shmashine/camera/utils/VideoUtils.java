package com.shmashine.camera.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.shmashine.common.utils.OSSUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 视频处理工具类
 *
 * @author little.li
 */
@Slf4j
public class VideoUtils {


    /**
     * h264格式转为mp4格式
     *
     * @param oldPath h264文件路径
     */
    public static String h264ToMp4(String oldPath) {

        String faultH264 = OSSUtil.saveFaultMP4(oldPath);
        assert faultH264 != null;

        if (StringUtils.hasText(faultH264)) {
            String faultMp4 = generateMp4PathByH264Path(faultH264);
            AliTranscode.ossH264ToMp4(faultH264, faultMp4);
            return faultMp4;
        }
        return null;
    }


    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.delete()) {
            log.info("--- {} --- 文件删除成功 ---", filePath);
        }
    }


    /**
     * MP4截取图片
     *
     * @param videoRealPath mp4路径
     * @return 截取后的图片路径
     */
    public static List<String> makeScreenCut(String videoRealPath) {

        List<String> imageList = new ArrayList<>();
        int index = videoRealPath.lastIndexOf(".");
        String imageRealName = videoRealPath.substring(0, index);
        System.out.println("视频截图开始...");

        for (int i = 0; i < 5; i++) {
            String imageName = imageRealName + "-" + i + ".jpg";
            List<String> commend = new ArrayList<>();
            commend.add("ffmpeg");
            commend.add("-i");
            commend.add(videoRealPath);
            commend.add("-y");
            commend.add("-f");
            commend.add("image2");
            commend.add("-ss");
            commend.add(String.valueOf(i == 0 ? 1 : (i + 1) * 5));
            commend.add("-t");
            commend.add("0.001");
            commend.add(imageName);
            imageList.add(imageName);

            try {
                ProcessBuilder builder = new ProcessBuilder();
                builder.command(commend);
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] bytes = new byte[1024];
                while (in.read(bytes) != -1) {
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("视频截图失败！");
            }

        }
        return imageList;
    }


    /**
     * 雄迈下载视频回调获取故障编号
     *
     * @param mp4Path 文件路径
     * @return 故障编号
     */
    public static String videoGetFaultId(String mp4Path) {
        int index = mp4Path.lastIndexOf(".");
        int firstIndex = mp4Path.lastIndexOf("-");
        log.info("mp4Path：{}", mp4Path);
        return mp4Path.substring(firstIndex + 1, index);
    }


    /**
     * 获取故障编号
     *
     * @param imagePath 文件路径
     * @return 故障编号
     */
    public static String imageGetFaultId(String imagePath) {
        int firstIndex = imagePath.lastIndexOf("-");
        return imagePath.substring(firstIndex + 1);
    }


    /**
     * 根据h264文件路径 生成MP4文件路径
     */
    private static String generateMp4PathByH264Path(String h264Path) {
        int index = h264Path.lastIndexOf(".");
        return h264Path.substring(0, index) + ".mp4";
    }


    /**
     * 根据文件名获取图片列表
     *
     * @param fileName 文件名
     * @param faultId  故障id
     * @return 图片列表
     */
    public static List<String> getImageFileList(String fileName, String faultId) {
        // 获取文件夹路径
        int index = fileName.lastIndexOf("/");
        fileName = fileName.substring(0, index);

        List<String> resultList = new ArrayList<>();
        // get the folder list
        File[] array = new File(fileName).listFiles();
        if (array == null) {
            return null;
        }
        for (File file : array) {
            if (file.isFile()) {
                String name = file.getName();
                String pattern = ".*" + faultId + ".*";
                if (Pattern.matches(pattern, name)) {
                    resultList.add(file.getPath());
                }
            }
        }
        return resultList;
    }

}
