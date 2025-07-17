package com.shmashine.common.constants;

import java.util.ArrayList;

/**
 * @Author LLF
 * @date 2020/4/14 9:52
 * 描述：
 */
public class SecurityContants {
    // 签名
    public static final String SIGN = "SHMASHINE2020DFDF";

    // 过期时间，单位毫秒
    public static final long EXPIRESAT = 7200000L;

    // 无需认证的接口
    public static final ArrayList<String> OPENURLS = new ArrayList<String>() {{
        add("/api/login");
    }};

}
