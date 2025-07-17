// Copyright (C) 2022 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.builder;

import java.util.function.Consumer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/01/03 9:00
 * @since v1.0
 */

public class TimerBuilder {

    private final MeterRegistry meterRegistry;

    private Timer.Builder builder;

    private Consumer<Timer.Builder> consumer;

    public TimerBuilder(MeterRegistry meterRegistry, String name, Consumer<Timer.Builder> consumer) {
        this.builder = Timer.builder(name);
        this.meterRegistry = meterRegistry;
        this.consumer = consumer;
    }

    public Timer build() {
        this.consumer.accept(builder);
        return builder.register(meterRegistry);
    }
}