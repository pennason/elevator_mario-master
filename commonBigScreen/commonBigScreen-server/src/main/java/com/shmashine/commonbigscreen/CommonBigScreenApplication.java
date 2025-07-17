package com.shmashine.commonbigscreen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author jiangheng
 * @version V1.0 - 2022/3/3 15:48
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.shmashine.commonbigscreen.client"})
@MapperScan("com.shmashine.commonbigscreen.dao")
@ComponentScan({"com.shmashine.common", "com.shmashine.commonbigscreen"})
public class CommonBigScreenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonBigScreenApplication.class, args);
        System.out.println(
                "     _                         _     _            \n"
                        + "    | |                       | |   (_)           \n"
                        + " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n"
                        + "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n"
                        + "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n"
                        + "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
