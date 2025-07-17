package com.shmashine.common.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认说明
 *
 * @author chenx
 */
@Slf4j
public class PersistentRejectedExecutionHandler implements RejectedExecutionHandler {

    @Getter
    private final String threadName;
    /**
     * 拒绝策略，是否淘汰最早的记录
     */
    private final boolean discardOldestPolicy;

    public PersistentRejectedExecutionHandler(String threadName, boolean discardOldestPolicy) {
        this.threadName = threadName;
        this.discardOldestPolicy = discardOldestPolicy;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //log.info("Task rejected " + threadName + ": " + r.toString() + ", activeCount:" + executor.getActiveCount()
        //        + ", taskCount:" + executor.getTaskCount() + ", largestPoolSize:" + executor.getLargestPoolSize());
        // 这里可以添加更多行为，例如记录日志、发送通知等
        var msg = String.format("Thread pool is EXHAUSTED!"
                        + " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d),"
                        + " Task: %d (completed: %d), status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)!",
                threadName, executor.getPoolSize(), executor.getActiveCount(), executor.getCorePoolSize(),
                executor.getMaximumPoolSize(), executor.getLargestPoolSize(),
                executor.getTaskCount(), executor.getCompletedTaskCount(), executor.isShutdown(),
                executor.isTerminated(), executor.isTerminating()
        );
        log.warn(msg);
        // 丢弃最老的一个请求
        if (discardOldestPolicy) {
            if (!executor.isShutdown()) {
                executor.getQueue().poll();
                executor.execute(r);
            }
        }
    }

    public static PersistentRejectedExecutionHandler of(String threadName) {
        return new PersistentRejectedExecutionHandler(threadName, false);
    }

    public static PersistentRejectedExecutionHandler of(String threadName, boolean discardOldestPolicy) {
        return new PersistentRejectedExecutionHandler(threadName, discardOldestPolicy);
    }
}