package com.shmashine.sender.platform.city.shanghai;

import java.util.Base64;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.sender.log.SenderLog;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Component
public class SonJiangHttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    private static Logger log = LoggerFactory.getLogger("sonJiangDianXinSendLogger");

    //     生产环境
    private static final String BASE_URL = "https://icity.sh.189.cn:8881";

    /**
     * 统一推送入口 便于统计计数
     */
    @SenderLog(groupId = "sonjiang_dianxin")
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public String send(@NotNull String registerNumber, @NotNull String topic, @NotNull String url,
                       @NotNull String body) throws Exception {
        return post(url, body);
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public String post(String url, String body) throws Exception {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(body)) {
            return null;
        }
        url = BASE_URL + url;
        // 请求头 设置格式为：application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString("haier:85@abZ#*@sd#08R".getBytes()));
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject(url, request, String.class);

        // post成功日志
        log.info("post: url[{}] body[{}] success !  response: [{}]", url, body, JSON.toJSONString(response));
        return response;
    }

}

