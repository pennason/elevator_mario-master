package com.shmashine.common.utils;

/**
 * 正则表达式 工具
 *
 * @author chenx
 */
public class RegexUtils {

    /**
     * 判度电话号码格式
     *
     * @param num 数字
     * @return 是否为电话号码
     */
    public static boolean matchPhoneNum(String num) {
        if (num.isEmpty()) {
            return false;
        }
        if (num.length() != 11) {
            return false;
        }
        return num.matches("1[1345789]\\d{9}");
    }
}
