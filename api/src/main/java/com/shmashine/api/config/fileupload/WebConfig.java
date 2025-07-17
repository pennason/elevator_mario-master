package com.shmashine.api.config.fileupload;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @PackgeName: com.shmashine.api.config.fileupload
 * @ClassName: WebConfig
 * @Date: 2020/6/2016:05
 * @Author: LiuLiFu
 * @Description: 文件上传临时路径
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/**").addResourceLocations("file:/temp/");
    }
}
