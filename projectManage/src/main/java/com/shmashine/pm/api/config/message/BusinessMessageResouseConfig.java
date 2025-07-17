package com.shmashine.pm.api.config.message;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BusinessMessageResouseConfig implements ApplicationListener<ApplicationStartedEvent> {

    private static final String FILE_PATH = "message/message.properties";

    public static Map<String, String> msgPropertiesMap;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        this.loadAllProperties(this.FILE_PATH);
    }

    public void loadAllProperties(String filePath) {
        log.info("---------------------加载业务消息文件到内存");
        try {
            Properties props = PropertiesLoaderUtils.loadAllProperties(filePath);
            msgPropertiesMap = new LinkedHashMap<String, String>();

            for (Object key : props.keySet()) {
                String keyStr = key.toString();
                try {
                    msgPropertiesMap.put(keyStr, props.getProperty(keyStr));
                } catch (Exception e) {
                    log.error("加载Properties配置发生异常！", e.fillInStackTrace());
                }
            }
        } catch (IOException e) {
            log.error("加载Properties配置发生异常！", e.fillInStackTrace());
        }
    }
}
