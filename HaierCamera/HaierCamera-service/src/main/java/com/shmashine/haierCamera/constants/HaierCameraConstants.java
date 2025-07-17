package com.shmashine.haierCamera.constants;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author jiangheng
 * @version 1.0
 * @date 2021/10/19 13:51
 */
@Component
public class HaierCameraConstants {

    @Value("${haierCamera.url.token}")
    private String tokenUrl;

    @Value("${haierCamera.url.realTimeVod}")
    private String realTimeVodUrl;

    public static String TOKEN_URL;

    public static String REALTIMEVOD_URl;

    @PostConstruct
    private void setProps() {
        TOKEN_URL = this.tokenUrl;
        REALTIMEVOD_URl = this.realTimeVodUrl;
    }
}
