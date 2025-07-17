package com.shmashine.api.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * @PackageName org.sulotion.config.aop
 * @ClassName LogRecordAspect
 * @Date 2020/3/12 1:20
 * @Author Liulifu
 * Version v1.0
 * @description 对controller层的每一个方法进行参数打印
 */
@Aspect
@Configuration
@Slf4j
public class LogRecordAspect {
    // 定义切点Pointcut
    @Pointcut("execution(* com.shmashine.api.controller.*.*Controller.*(..))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String url = request.getRequestURL().toString();
        String user = request.getHeader("user_id");
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("\n" + "用户 {} 请求, url: {}, method: {}, uri: {}," + "\n", user, url, method, uri);
        // result  拦截方法的返回值
        Object result = pjp.proceed();
        //log.info("请求结束，返回数据: " + "\n" + JSONObject.toJSONString(result, true) + "\n");
        log.info("请求结束，返回数据: {}", result == null ? null : result.toString());
        return result;
    }
}
