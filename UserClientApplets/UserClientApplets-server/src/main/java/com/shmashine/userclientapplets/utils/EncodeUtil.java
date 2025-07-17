package com.shmashine.userclientapplets.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * 密码加密工具类
 */
@Slf4j
public class EncodeUtil {

    // 密码加密
    public static String cryptPasswordEncoder(String content) {
        log.info("========== BCryptPasswordEncoderTest start==========");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String head = "{bcrypt}";
        String encode = encoder.encode(content);
        String result = head + encode;
        log.info("========== BCryptPasswordEncoderTest end==========");
        return result;
    }

    // 比较明文是否和加密字段明文相同
    public static boolean checkEncoderContent(String content, String encode) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean checkFlag = encoder.matches(content, encode.replace("{bcrypt}", ""));
        return checkFlag;
    }

}