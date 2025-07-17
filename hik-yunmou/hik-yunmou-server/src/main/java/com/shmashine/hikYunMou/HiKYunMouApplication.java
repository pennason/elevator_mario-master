package com.shmashine.hikYunMou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author  jiangheng
 * @version 2023/3/23 14:55
 * @description: com.shmashine.hikYunMou
 */
@EnableScheduling
@SpringBootApplication
public class HiKYunMouApplication {

    public static void main(String[] args) {

        SpringApplication.run(HiKYunMouApplication.class, args);
        System.out.println(
                "     _                         _     _            \n" +
                        "    | |                       | |   (_)           \n" +
                        " ___| |__  _ __ ___   __ _ ___| |__  _ _ __   ___ \n" +
                        "/ __| '_ \\| '_ ` _ \\ / _` / __| '_ \\| | '_ \\ / _ \\\n" +
                        "\\__ \\ | | | | | | | | (_| \\__ \\ | | | | | | |  __/\n" +
                        "|___/_| |_|_| |_| |_|\\__,_|___/_| |_|_|_| |_|\\___|");
    }
}
