//package com.shmashine.api.config.auth;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//
/// **
// * 自定义拦截需要排除默认拦截配置 @SpringBootApplication(exclude = {SaTokenConfigure.class })
// *
// * @author jiangheng
// * @version v1.0.0 - 2024/3/20 16:18
// * @since v1.0.0
// */
//@Configuration
//public class TokenConfigure implements WebMvcConfigurer {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册 Sa-Token 拦截器打开注解鉴权功能
//        registry.addInterceptor(new SaInterceptor(handle -> {
//            // 登录校验 -- 拦截所有路由，并排除/v1/getToken 用于开放登录
//            SaRouter.match("/**", "/v1/getToken", r -> StpUtil.checkLogin());
//        })).addPathPatterns("/**");
//    }
//
//}
//
