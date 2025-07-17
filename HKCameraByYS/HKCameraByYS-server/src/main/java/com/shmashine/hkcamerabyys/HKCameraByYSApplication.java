package com.shmashine.hkcamerabyys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author jiangheng
 * @version v1.0.0 - 2021/11/5 15:08
 */
@SpringBootApplication
@MapperScan("com.shmashine.hkcamerabyys.dao")
public class HKCameraByYSApplication {

    public static void main(String[] args) {
        SpringApplication.run(HKCameraByYSApplication.class, args);
        System.out.println(
                "     _                         _     _            \n"
                        + "    | |                       | |   (_)           \n"
                        + " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n"
                        + "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n"
                        + "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n"
                        + "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
