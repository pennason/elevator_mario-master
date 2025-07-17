package com.shmashine.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.util.StringUtils;


/**
 * ip工具类
 *
 * @author little.li
 */
public class IpUtils {

    public static final String ENVIRONMENT_HOST_IP = "HOST_IP_ADDRESS";
    public static final String ENVIRONMENT_HOST_PORT = "HOST_IP_PORT";

    /**
     * 获取本机ip
     *
     * @return 本机ip
     */
    public static String getLocalIP() {
        try {
            var ipEnv = System.getenv(ENVIRONMENT_HOST_IP);
            if (StringUtils.hasText(ipEnv)) {
                return ipEnv;
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalPort() {
        var portEnv = System.getenv(ENVIRONMENT_HOST_PORT);
        if (StringUtils.hasText(portEnv)) {
            return portEnv;
        }
        return null;
    }


}
