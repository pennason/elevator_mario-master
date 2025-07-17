package com.shmashine.hikYunMou.test;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.hikYunMou.handle.HiKPlatformHandle;
import com.shmashine.hikYunMou.tasks.FileDownloadTask;

/**
 * @author  jiangheng
 * @version 2023/3/27 14:12
 * @description: com.shmashine.hikYunMou.test
 */
@SpringBootTest
public class HikvYunMouPlatformTest {

    @Resource
    private HiKPlatformHandle hiKPlatformHandle;

    @Resource
    private FileDownloadTask fileDownloadTask;

    /**
     * 下载图片测试
     */
    @Test
    public void downloadPictureFileTest() {
        hiKPlatformHandle.downloadPictureFile("MXCS123123", "123", "12", "L18014792");
    }

    /**
     * 下载视频测试
     */
    @Test
    public void downloadFaultVideoFileTest() {
        hiKPlatformHandle.downloadFaultVideoFile("L18014792", "MXCS123123", "123",
                "12", "2023-03-27 15:50:00");

    }

    /**
     * 下载视频测试
     */
    @Test
    public void downloadTaskTest() {
        fileDownloadTask.downloadTask();

    }

}
