package com.shmashine.sender.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.shmashine.common.thread.PersistentRejectedExecutionHandler;
import com.shmashine.common.thread.ShmashineThreadFactory;
import com.shmashine.common.thread.ShmashineThreadPoolExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/04/26 17:29
 * @since v1.0
 */

@Aspect
@Configuration
@Slf4j
public class SenderLogAspect {

    @Autowired
    private SenderCounterServer senderCounterServer;

    private final ExecutorService executorService = new ShmashineThreadPoolExecutor(64, 256,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2000),
            ShmashineThreadFactory.of(),
            PersistentRejectedExecutionHandler.of("executorService"), "senderLog");


    @Around("@annotation(senderLog)")
    public Object around(ProceedingJoinPoint point, SenderLog senderLog) throws Throwable {
        Object[] args = point.getArgs();
        String registerNumber = (String) args[0];
        String topic = (String) args[1];
        if (registerNumber != null) {
            executorService.execute(() -> {
                senderCounterServer.publish(senderLog.groupId(), registerNumber, topic);
            });
        }
        return point.proceed();
    }
}
