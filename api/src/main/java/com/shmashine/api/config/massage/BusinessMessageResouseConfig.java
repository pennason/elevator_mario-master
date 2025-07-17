package com.shmashine.api.config.massage;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @PackageName org.sulotion.config.massage
 * @ClassName BusinessMessageConfig
 * @Date 2020/3/11 14:38
 * @Author Liulifu
 * Version v1.0
 * @description 业务消息底层封装（将消息从配置文件中加载到内存）
 */
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
