package com.shmashine.commonbigscreen.aop;

import org.springframework.stereotype.Component;

import cn.dev33.satoken.stp.StpUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign拦截器, 在feign请求发出之前，加入一些操作
 *
 * @author jiangheng
 * @version v1.0.0 - 2024/4/2 16:42
 * @since v1.0.0
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 为 Feign 的 RCP调用 添加请求头
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(StpUtil.getTokenName(), StpUtil.getTokenValueNotCut());
    }

}
