// Copyright (C) 2022 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.sender.builder;

import java.util.function.Consumer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * 默认说明
 *
 * @author 'Xue Chen - (chenxue4076@163.com)'
 * @version v1.0  -  2023/01/03 9:00
 * @since v1.0
 */

public class CounterBuilder {
    private final MeterRegistry meterRegistry;

    private Counter.Builder builder;

    private Consumer<Counter.Builder> consumer;

    public CounterBuilder(MeterRegistry meterRegistry, String name, Consumer<Counter.Builder> consumer) {
        this.builder = Counter.builder(name);
        this.meterRegistry = meterRegistry;
        this.consumer = consumer;
    }

    public Counter build() {
        consumer.accept(builder);
        return builder.register(meterRegistry);

    }
}