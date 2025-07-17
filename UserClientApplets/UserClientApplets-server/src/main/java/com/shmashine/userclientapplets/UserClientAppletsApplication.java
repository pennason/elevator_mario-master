package com.shmashine.userclientapplets;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/2/8 11:47
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.shmashine.userclientapplets.client", "com.shmashine.satoken.client"})
@MapperScan("com.shmashine.userclientapplets.dao")
public class UserClientAppletsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserClientAppletsApplication.class, args);
        System.out.println(
                "     _                         _     _            \n"
                        + "    | |                       | |   (_)           \n"
                        + " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n"
                        + "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n"
                        + "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n"
                        + "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
