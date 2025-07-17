package com.shmashine.sender.platform.city.shanghai;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.shmashine.common.constants.RedisConstants;
import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;
import com.shmashine.sender.log.SenderLog;
import com.shmashine.sender.redis.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Slf4j
@Component
public class YidianHttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private RedisUtils redisUtils;

    //     生产环境
    private static final String BASE_URL = "http://www.smartelevator.net";
    private static final String USERNAME = "sys_shmx";
    private static final String USER_PASSWORD = "i*esa-t0013C";

    // 测试环境
    //    private static final String BASE_URL = "http://test.smartelevator.net";
    //    private static final String name = "sys_shmx";
    //    private static final String password = "123456";

    private static final String DEFAULT_TOKEN_URL = BASE_URL + "/iot/api/v1/login";

    private final ExecutorService yidianSendExecutor = new ShmashineThreadPoolExecutor(256, 1024,
            8L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024), ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("Send"), "YidianHttpUtil");

    /**
     * 获取token
     */
    private synchronized String getToken() {

        String token = redisUtils.get(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN);

        if (org.springframework.util.StringUtils.hasText(token)) {
            return token;
        }

        log.info("messageId: dispatch_step_yidian token start");
        // 请求头 设置格式为：application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 请求体 用户名/密码
        Map<String, String> bodyMap = new LinkedHashMap();
        bodyMap.put("username", USERNAME);
        bodyMap.put("password", USER_PASSWORD);
        String body = JSON.toJSONString(bodyMap);

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        TokenResponse tokenResponse = restTemplate.postForObject(DEFAULT_TOKEN_URL, request, TokenResponse.class);
        if (tokenResponse != null && tokenResponse.getStatus() == 200) {
            TokenResponse.TokenResult tokenResult = tokenResponse.getResult();
            if (tokenResult != null) {
                token = tokenResult.getToken();
                long expirationTime = tokenResult.getExpirationTime();
                redisUtils.set(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN,
                        token, (expirationTime - System.currentTimeMillis()));
            }
        }
        log.info("messageId: dispatch_step_yidian token end");
        return token;
    }

    public synchronized String reflushToken() {
        redisUtils.del(RedisConstants.SH_YIDIAN_PLATFORM_TOKEN);
        return getToken();
    }

    /**
     * 统一推送入口 便于统计计数
     */
    @SenderLog(groupId = "yidian")
    public PostResponse send(@NotNull String registerNumber, @NotNull String topic, @NotNull String url,
                             @NotNull String body) throws Exception {
        log.info("push data to yidian bizSetData: registerNumber: {}, topic: {}, url: {}, body: {}", registerNumber,
                topic, url, body);
        Future<PostResponse> submit = yidianSendExecutor.submit(() -> post(url, body, registerNumber));
        return submit.get();
    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public PostResponse post(String url, String body, String registerNumber) {

        if (StringUtils.isBlank(url) || StringUtils.isBlank(body)) {
            return null;
        }
        url = BASE_URL + url;
        // 请求头 设置格式为：application/json
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", getToken());

        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        PostResponse response;
        try {
            response = restTemplate.postForObject(url, request, PostResponse.class);
        } catch (ResourceAccessException e) {
            log.error("HTTP请求推送仪电异常-url:{} - body:{} -registerNumber:{} - error:{}",
                    url, body, registerNumber, e.getMessage());
            PostResponse postResponse = new PostResponse();
            postResponse.setMessage("HTTP请求推送仪电异常-" + e.getMessage());
            postResponse.setStatus(500);
            return postResponse;
        }
        if (response.getStatus() == 401) {
            // 刷新token
            reflushToken();
            //getToken();
            response = post(url, body, registerNumber);
        }
        // post成功日志
        log.info("messageId:[{}],dispatch_step_yidian end . post: url[{}] body[{}] success response: [{}]",
                registerNumber, url, body, JSON.toJSONString(response));
        return response;
    }

    public Map<String, String> getHeadMap() {
        Map<String, String> headmap = new LinkedHashMap();
        headmap.put("Authorization", getToken());
        headmap.put("Content-Type", "application/json");
        return headmap;
    }

    @Recover
    public PostResponse recover(Exception e) {
        // todo 记失败日志到数据库
        PostResponse response = new PostResponse();
        response.setMessage(e.getMessage());
        return response;
    }
}

