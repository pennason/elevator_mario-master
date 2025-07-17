// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.cameratysl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/8/21 10:39
 * @since v1.0
 */

@EnableScheduling
@SpringBootApplication
public class CameraTyslApplication {
    public static void main(String[] args) {

        SpringApplication.run(CameraTyslApplication.class, args);
        //限制javacv堆外内存大小，防止内存泄露问题
        System.setProperty("org.bytedeco.javacpp.maxphysicalbytes", "3G");
        System.setProperty("org.bytedeco.javacpp.maxbytes", "3G");
        System.out.println(
                "     _                         _     _            \n"
                        + "    | |                       | |   (_)           \n"
                        + " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n"
                        + "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n"
                        + "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n"
                        + "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
