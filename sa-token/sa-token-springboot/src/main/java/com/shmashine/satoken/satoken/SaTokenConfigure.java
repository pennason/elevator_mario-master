//package com.pj.satoken;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//
//
/// **
// * [Sa-Token 权限认证] 配置类
// *
// * @author click33
// */
//@Configuration
//public class SaTokenConfigure implements WebMvcConfigurer {
//
//    /**
//     * 注册 Sa-Token 拦截器打开注解鉴权功能
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SaInterceptor(handle -> {
//            SaRouter.match("/user/**", r -> StpUtil.checkRole("ROLE0000000000000001"));
//        })).addPathPatterns("/**");
//    }
//
//}
