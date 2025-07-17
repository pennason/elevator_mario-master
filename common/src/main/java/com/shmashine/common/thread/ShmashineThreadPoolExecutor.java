// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/5/20 9:54
 * @since v1.0
 */

@Slf4j
public class ShmashineThreadPoolExecutor extends ThreadPoolExecutor {
    /**
     * 类名
     */
    private final String className;
    /**
     * 线程池name
     */
    private final String executorName;

    public ShmashineThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                       Integer workQueueSize, String className, String executorName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(workQueueSize),
                ShmashineThreadFactory.of(), // 使用虚拟线程
                new DiscardOldestPolicy());
        this.className = className;
        this.executorName = executorName;
    }

    public ShmashineThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                       BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                       PersistentRejectedExecutionHandler rejectExecutionHandler,
                                       String className) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectExecutionHandler);
        this.className = className;
        this.executorName = rejectExecutionHandler.getThreadName();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        //这个是excute提交的时候
        if (t != null) {
            if (t.getCause() instanceof ShmashineException) {
                log.error("{}-{}-线程处理异常，errorMsg: {}", className, executorName, t.getMessage());
            } else {
                log.error("{}-{}-线程处理异常，errorMsg: {}", className, executorName, ExceptionUtils.getStackTrace(t));
            }
        }
        //如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
        if (r instanceof FutureTask) {
            try {
                var future = (Future<?>) r;
                //get获取异常
                future.get();
            } catch (Exception e) {
                if (e.getCause() instanceof ShmashineException) {
                    log.error("{}-{}-线程处理异常，errorMsg: {}", className, executorName, e.getMessage());
                } else {
                    log.error("{}-{}-线程处理异常，errorMsg: {}", className, executorName, ExceptionUtils.getStackTrace(e));
                }

            }
        }
    }
}
