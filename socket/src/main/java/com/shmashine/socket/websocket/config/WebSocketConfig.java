package com.shmashine.socket.websocket.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.stp.StpUtil;

/**
 * 使用@ServerEndpoint创立websocket endpoint
 * 首先要注入 ServerEndpointExporter，
 *
 * @author little.li
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 自定义注册 [Sa-Token 全局过滤器]
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()

                // 指定 [拦截路由]
                // .addInclude("")
                //指定 [放行路由]
                .addExclude("/ws/**")
                .addExclude("/cube/sendMessage")

                // 认证函数: 每次请求执行
                .setAuth(obj -> {

                    // 已登录情况下才允许握手
                    StpUtil.checkLogin();

                    SaManager.getLog().info("----- 用户[{}]请求path=[{}] 提交Params=[{}]",
                            StpUtil.getLoginId(""), SaHolder.getRequest().getRequestPath(),
                            SaHolder.getRequest().getParamMap());

                })

                // 异常处理函数：每次认证函数发生异常时执行此函数
                .setError(e -> {
                    SaManager.getLog().error("---------- sa全局异常，e：{} ", Arrays.toString(e.getStackTrace()));
                    return e.getMessage();
                })

                // 前置函数：在每次认证函数之前执行（BeforeAuth 不受 includeList 与 excludeList 的限制，所有请求都会进入）
                .setBeforeAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            // 服务器名称
                            .setServer("sa-server")
                            // 是否可以在iframe显示视图： DENY=不可以 | SAMEORIGIN=同域下可以 | ALLOW-FROM uri=指定域名下可以
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            // 是否启用浏览器默认XSS防护： 0=禁用 | 1=启用 | 1; mode=block 启用, 并在检查到XSS攻击时，停止渲染页面
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            // 禁用浏览器内容嗅探
                            .setHeader("X-Content-Type-Options", "nosniff");
                });
    }

}
