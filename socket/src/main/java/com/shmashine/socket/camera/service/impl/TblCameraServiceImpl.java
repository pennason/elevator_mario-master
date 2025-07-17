package com.shmashine.socket.camera.service.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shmashine.common.utils.CameraUtils;
import com.shmashine.common.utils.TimeUtils;
import com.shmashine.socket.camera.dao.TblCameraDao;
import com.shmashine.socket.camera.entity.TblCamera;
import com.shmashine.socket.camera.service.TblCameraServiceI;
import com.shmashine.socket.redis.RedisService;

/**
 * 摄像头
 *
 * @author little.li
 */
@Service
public class TblCameraServiceImpl implements TblCameraServiceI {

    @Resource(type = TblCameraDao.class)
    private TblCameraDao tblCameraDao;

    @Resource(type = RedisService.class)
    private RedisService redisService;


    @Autowired
    private RedisTemplate redisTemplate;

    public static final String XM_NUMBER_OF_CALLS_CAMERSERVER = "XM_NUMBER_OF_CALLS_CAMERSERVER";

    @Override
    public String getRtmpUrlByElevatorCode(String elevatorCode) {
        return tblCameraDao.getRtmpUrlByElevatorCode(elevatorCode);
    }

    @Override
    public String getHlsUrlByElevatorCode(String elevatorCode) {
        return tblCameraDao.getRtmpUrlByElevatorCode(elevatorCode);
    }

    @Override
    public String getCurrentImagePathByElevatorCode(String elevatorCode) {
        TblCamera camera = tblCameraDao.getByElevatorCode(elevatorCode);
        if (camera == null) {
            System.out.printf("%s --- %s --- 困人 --- 视频流不存在\n", TimeUtils.nowTime(), elevatorCode);
            return "";
        }
        String path = "/shmashine-deploy/java-oreo/socket/image/" + TimeUtils.getTenBitTimestamp() + ".jpg";
        //String path = "D:\\Desktop\\新建文件夹\\" + TimeUtils.getTenBitTimestamp() + ".jpg"; // 本地TEST
        if (camera.getICameraType() == 2) { // 雄迈摄像头
            //调用-雄迈摄像-头次数累加
            //redisTemplate.opsForHash().increment(XM_NUMBER_OF_CALLS_CAMERSERVER, camera.getVElevatorCode(), 1);

            String hlsUrl = CameraUtils.getXiongMaiHls(camera.getVCloudNumber(), elevatorCode);

            System.out.printf("%s --- %s --- 困人 --- Baidu截取图片\n存储路径: %s \nsourceUrl: %s\n",
                    TimeUtils.nowTime(), elevatorCode, path, hlsUrl);
            // 重试三次
            /*int flag = 3;
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(hlsUrl);
                    ff.start();
                    doExecuteFrame(ff.grabImage(), path);
                    ff.stop();
                    break;
                } catch (FrameGrabber.Exception | InterruptedException e) {
                    System.out.printf("%s --- %s --- 困人 --- 打开流失败...重试%s...\n",
                            TimeUtils.nowTime(), elevatorCode, flag);
                    flag--;
                }
                if (flag == 0) {
                    System.out.printf("%s --- %s --- 困人 --- 重试3次失败...\n", TimeUtils.nowTime(), elevatorCode);
                    return "";
                }
            }*/
        }
        if (StringUtils.isNotEmpty(camera.getVRtmpUrl())) {
            // rtmp流处理
            String rtmpUrl = camera.getVRtmpUrl();
            System.out.printf("%s --- %s --- 困人 --- Baidu截取图片\n存储路径: %s \nsourceUrl: %s\n",
                    TimeUtils.nowTime(), elevatorCode, path, rtmpUrl);
            // 截取视频图片
            String command = "ffmpeg -i " + rtmpUrl + " -vframes 1 -y -f image2 -t 0.001 " + path;
            try {
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                System.out.printf("%s --- %s --- 困人 --- Baidu识别中...\n", TimeUtils.nowTime(), elevatorCode);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
        }
        File file = new File(path);
        if (file.exists()) {
            return path;
        }
        return "";
    }


    /**
     * javacv 截取图片
     */
    /*public static void doExecuteFrame(Frame frame, String fileName) {
        if (null == frame || null == frame.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        String imageMat = "jpg";
        BufferedImage bi = converter.getBufferedImage(frame);
        File output = new File(fileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}
