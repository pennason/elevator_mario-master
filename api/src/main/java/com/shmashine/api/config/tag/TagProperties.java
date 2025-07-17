package com.shmashine.api.config.tag;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 版本标签配置文件
 *
 * @author jiangheng
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tag")
public class TagProperties {

    /**
     * 标签名-版本号
     */
    private String tagName;

    /**
     * 版本更新内容
     */
    private List<String> content;

    public static String TAG_NAME;

    public static List<String> CONTENT;

    @PostConstruct
    private void setProps() {
        TAG_NAME = this.tagName;
        CONTENT = this.content;
    }

}
