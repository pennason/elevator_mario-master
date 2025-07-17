// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.hkcamerabyys.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.shmashine.common.utils.FileUtil;
import com.shmashine.common.utils.OssInternalUtils;


/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/3/2 16:41
 * @since v1.0
 */

@SpringBootTest
public class RemoteFileToOssTest {

    @Test
    public void testRemoteFileToOss() {
        var file = "http://oss-mashine.oss-cn-qingdao.aliyuncs.com/Oreo_Project/fault/"
                + "2023-06-13/8356634190473396224.jpg";

        var ossFileName = "test.jpg";
        OssInternalUtils.setOSS(FileUtil.getBytesByRemotePath(file), ossFileName);
        System.out.println(OssInternalUtils.OSS_URL + ossFileName);
    }
}
