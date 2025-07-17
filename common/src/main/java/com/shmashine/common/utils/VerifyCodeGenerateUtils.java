package com.shmashine.common.utils;

import java.util.Random;

/**
 * 验证码 工具
 *
 * @author chenx
 */

public class VerifyCodeGenerateUtils {
    /**
     * 登陆验证码长度
     */
    private static final int LOGIN_CODE_LENGTH = 4;

    /**
     * 生成随机数
     *
     * @return 结果
     */
    public static String fetchLoginCode() {
        int idx = LOGIN_CODE_LENGTH;

        StringBuffer buffer = new StringBuffer();

        Random r = new Random();

        do {
            buffer.append(r.nextInt(10));
            idx--;
        } while (!(idx == 0));

        return buffer.toString();
    }
}
