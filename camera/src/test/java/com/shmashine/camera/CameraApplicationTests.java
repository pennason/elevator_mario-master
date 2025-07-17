package com.shmashine.camera;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.common.utils.OSSUtil;

@SpringBootTest
class CameraApplicationTests {

    @Test
    void contextLoads() {
        // 上传OSS
        String fileUrl = OSSUtil.saveFaultMP4("D:\\Videos\\MX3732_01_20210807083800.mp4");
        System.out.println("fileUrl = " + fileUrl);
    }

}
