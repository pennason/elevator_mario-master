// Copyright (C) 2024 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2024/10/29 10:50
 * @since v1.0
 */

public class ShmashineThreadFactory implements ThreadFactory {
    private final String name;

    public ShmashineThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        // 返回一个 虚拟线程
        /*return Thread.ofVirtual()
                .name(name, 0)
                .start(r);*/
        return ofVirtual(name).newThread(r);
    }


    /**
     * 默认线程工厂  Executors.defaultThreadFactory()
     *
     * @return 默认线程工厂
     */
    public static ThreadFactory of() {
        return Executors.defaultThreadFactory();
    }

    /**
     * 虚拟线程工厂, 带数据库请求的不要使用此线程，否则无法正常调用
     *
     * @return 虚拟线程工厂
     */
    public static ThreadFactory ofVirtual() {
        // 虚拟线程有问题，待找原因
        return new ShmashineThreadFactory("shmashine-virtual-thread-");
    }

    /**
     * 虚拟线程工厂, 带数据库请求的不要使用此线程，否则无法正常调用
     *
     * @param name 线程名称
     * @return 虚拟线程工厂
     */
    public static ThreadFactory ofVirtual(String name) {
        // 虚拟线程有问题，待找原因
        return new ShmashineThreadFactory(name);
    }
}
