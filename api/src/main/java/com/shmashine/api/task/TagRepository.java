package com.shmashine.api.task;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import cn.hutool.http.HttpRequest;

import com.shmashine.api.config.tag.TagProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * gitlab 打标签任务
 *
 * @Author: jiangheng
 * @Version: 1.0.0
 * @Date: 2024/7/9 10:09
 * @Since: 1.0.0
 */
@Slf4j
@Component
@Profile("prod")
public class TagRepository {

    /**
     * gitlab 打标签接口URL
     */
    private static final String GITLAB_TAGS_REPOSITORY =
            "http://gitlab.shmashine.com:8384/api/v4/projects/82/repository/tags";

    /**
     * gitlab API访问令牌
     */
    private static final String PRIVATE_TOKEN = "glpat-YBXGeAzn9et8zQxqD29s";

    @PostConstruct
    public void initialize() {

        Map<String, Object> reqMap = Map.of("tag_name", TagProperties.TAG_NAME,
                "message", StringUtils.collectionToDelimitedString(TagProperties.CONTENT, "\n"),
                "ref", "release");

        String resp = HttpRequest.post(GITLAB_TAGS_REPOSITORY)
                .header("PRIVATE-TOKEN", PRIVATE_TOKEN)
                .form(reqMap).timeout(6000).execute().body();

        log.info("请求gitlab打标签成功 - req：{} - resp：{}", reqMap, resp);
    }

}
