package com.shmashine.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeUtil {

    // 密码加密
    public static String BCryptPasswordEncoder(String content) {
        System.out.println("========== BCryptPasswordEncoderTest start==========");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String head = "{bcrypt}";
        String encode = encoder.encode(content);
        String result = head + encode;
        System.out.println("========== BCryptPasswordEncoderTest end==========");
        return result;
    }

    // 比较明文是否和加密字段明文相同
    public static boolean CheckEncoderContent(String content, String encode) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean checkFlag = encoder.matches(content, encode.replace("{bcrypt}", ""));
        return checkFlag;
    }

    public static void main(String[] args) {
        String s = EncodeUtil.BCryptPasswordEncoder("123456");
        System.out.println(s);
        System.out.println(EncodeUtil.CheckEncoderContent("654321", s));
    }

}