package com.shmashine.haierCamera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/18 14:38
 */
@MapperScan("com.shmashine.haierCamera.dao")
@SpringBootApplication
public class HaierCameraApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaierCameraApplication.class, args);
        System.out.println(
                "     _                         _     _            \n" +
                        "    | |                       | |   (_)           \n" +
                        " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n" +
                        "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n" +
                        "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n" +
                        "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
