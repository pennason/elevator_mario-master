package com.shmashine.hkCameraForTY;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 启动类
 * @author jiangheng
 * @version 1.0 - 2021/6/7 9:38
 */
@MapperScan("com.shmashine.hkCameraForTY.dao")
@EnableScheduling
@SpringBootApplication
public class TYCameraApplication {

    public static void main(String[] args) {
        SpringApplication.run(TYCameraApplication.class);

    }

}
