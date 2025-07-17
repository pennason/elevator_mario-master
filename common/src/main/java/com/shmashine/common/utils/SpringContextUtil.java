// Copyright (C) 2023 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.utils;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/6/21 17:10
 * @since v1.0
 */

@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static final String ENV_PROD = "prod";

    /**
     * spring的应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 初始化时将应用上下文设置进applicationContext
     *
     * @param applicationContext 应用上下文
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据bean名称获取某个bean对象
     *
     * @param name bean名称
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据bean的class获取某个bean对象
     *
     * @param beanClass
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> beanClass) throws BeansException {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 获取spring.profiles.active 激活的环境列表， 这里可能会存在多个
     *
     * @return 激活环境列表
     */

    public static List<String> getProfiles() {
        return List.of(getApplicationContext().getEnvironment().getActiveProfiles());
    }

    /**
     * 是否为线上环境
     *
     * @return
     */
    public static boolean isProdEnv() {
        return List.of(getApplicationContext().getEnvironment().getActiveProfiles()).contains(ENV_PROD);
    }


}